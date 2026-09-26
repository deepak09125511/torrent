package com.torrent.torrentApplication.peerDownloader.service;
import java.io.FileNotFoundException;
import java.util.concurrent.ConcurrentHashMap;
import com.torrent.torrentApplication.peerDownloader.TrackerClient;
import com.torrent.torrentApplication.tracker.dto.PeerSwarmDTO;
import com.torrent.torrentApplication.tracker.dto.SwarmResponseDTO;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.*;

@Service
public class DownloadManager {

    private final ConcurrentHashMap<String, PeerStats> peerStats =
            new ConcurrentHashMap<>();

    private final RestTemplate restTemplate;
    private final TrackerClient trackerClient;

    private final ExecutorService executor =
            Executors.newFixedThreadPool(8);

    public DownloadManager(TrackerClient trackerClient) {
        this.restTemplate = new RestTemplate();
        this.trackerClient = trackerClient;
    }

    private PeerStats getPeerStats(PeerSwarmDTO peer) {

        String key = peer.getIp() + ":" + peer.getPort();

        return peerStats.computeIfAbsent(
                key,
                k -> new PeerStats()
        );
    }

    public Long download(String shareCode) {

        SwarmResponseDTO swarm = trackerClient.getSwarm(shareCode);

        Long fileId = swarm.getFileId();
        List<PeerSwarmDTO> peers = swarm.getPeers();

        int totalPieces = peers.stream()
                .flatMap(p -> p.getPieces().stream())
                .max(Integer::compareTo)
                .orElse(0) + 1;

        Map<Integer, List<PeerSwarmDTO>> pieceToPeers =
                buildMap(peers);

        List<Future<?>> futures = new ArrayList<>();

        for (int i = 0; i < totalPieces; i++) {

            final int pieceIndex = i;

            futures.add(
                    executor.submit(() ->
                            downloadPieceWithRetry(
                                    fileId,
                                    pieceIndex,
                                    pieceToPeers
                            )
                    )
            );
        }

        for (Future<?> future : futures) {

            try {
                future.get();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        try {

            mergeFile(fileId, totalPieces);

        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        // IMPORTANT
        return fileId;
    }

    private Map<Integer, List<PeerSwarmDTO>> buildMap(List<PeerSwarmDTO> peers) {

        Map<Integer, List<PeerSwarmDTO>> map = new HashMap<>();

        for (PeerSwarmDTO peer : peers) {
            for (Integer piece : peer.getPieces()) {

                map.computeIfAbsent(piece, k -> new ArrayList<>())
                        .add(peer);
            }
        }

        return map;
    }

    private void downloadPieceWithRetry(
            Long fileId,
            int pieceIndex,
            Map<Integer, List<PeerSwarmDTO>> pieceToPeers) {

        List<PeerSwarmDTO> originalPeers = pieceToPeers.get(pieceIndex);

        if (originalPeers == null || originalPeers.isEmpty()) {
            throw new RuntimeException("No peer available for piece " + pieceIndex);
        }

        // Make a copy before sorting
        List<PeerSwarmDTO> peers = new ArrayList<>(originalPeers);

        // Highest score first
        peers.sort((p1, p2) ->
                Double.compare(
                        getPeerStats(p2).getScore(),
                        getPeerStats(p1).getScore()
                )
        );

        for (PeerSwarmDTO peer : peers) {

            try {

                long start = System.currentTimeMillis();

                byte[] data =
                        downloadFromPeer(peer, fileId, pieceIndex);

                long responseTime =
                        System.currentTimeMillis() - start;

                getPeerStats(peer)
                        .recordSuccess(responseTime);

                System.out.println(
                        "Downloaded piece " + pieceIndex +
                                " from " +
                                peer.getIp() + ":" + peer.getPort() +
                                " | Score = " +
                                String.format("%.2f", getPeerStats(peer).getScore())
                );

                savePiece(fileId, pieceIndex, data);

                return;

            } catch (Exception e) {

                getPeerStats(peer)
                        .recordFailure();

                System.out.println(
                        "Failed to download piece " + pieceIndex +
                                " from " +
                                peer.getIp() + ":" + peer.getPort() +
                                " | Score = " +
                                String.format("%.2f", getPeerStats(peer).getScore())
                );
                e.printStackTrace();
            }
        }

        throw new RuntimeException(
                "Failed to download piece " + pieceIndex + " from all available peers."
        );
    }

    private byte[] downloadFromPeer(
            PeerSwarmDTO peer,
            Long fileId,
            int index) {

        String url = "http://" + peer.getIp()
                + ":" + peer.getPort()
                + "/api/piece/"
                + fileId + "/" + index;

        return restTemplate.getForObject(url, byte[].class);
    }

    private void savePiece(Long fileId, int index, byte[] data)
            throws IOException {

        Path path = Paths.get(
                "download_storage",
                "file_" + fileId
        );

        Files.createDirectories(path);

        Files.write(
                path.resolve("piece_" + index),
                data
        );
    }

    private void mergeFile(Long fileId, int totalPieces)
            throws IOException {

        Path output =
                Paths.get(
                        "download_storage/file_" + fileId + "/final.mp4"
                );

        try (OutputStream os = Files.newOutputStream(output)) {

            for (int i = 0; i < totalPieces; i++) {

                Path piece =
                        Paths.get(
                                "download_storage/file_" + fileId,
                                "piece_" + i
                        );

                Files.copy(piece, os);
            }
        }
    }

    public Resource getDownloadedFile(Long fileId) throws IOException {

        Path filePath = Paths.get(
                "download_storage",
                "file_" + fileId,
                "final.mp4"
        );

        if (!Files.exists(filePath)) {
            throw new FileNotFoundException(
                    "Downloaded file not found for fileId: " + fileId
            );
        }

        return new FileSystemResource(filePath);
    }
}