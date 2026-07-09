package com.torrent.torrentApplication.peer.service;

import com.torrent.torrentApplication.tracker.repository.FileRepository;
import com.torrent.torrentApplication.tracker.repository.PeerRepository;
import com.torrent.torrentApplication.tracker.dto.PieceAnnouncementRequest;
import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.model.Peer;
import com.torrent.torrentApplication.tracker.service.FileService;
import com.torrent.torrentApplication.tracker.service.PeerPieceService;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.UUID;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class FileShareService {
    private final FileSplitterService fileSplitterService;
    private final FileService fileService;
    private final PeerRepository peerRepository;
    private final PeerPieceService peerPieceService;
    private  final FileRepository fileRepository;

    public FileShareService(FileSplitterService fileSplitterService,FileService fileService,
                            PeerRepository peerRepository,
                            PeerPieceService peerPieceService,
                            FileRepository fileRepository){
        this.fileSplitterService = fileSplitterService;
        this.fileService = fileService;
        this.peerRepository = peerRepository;
        this.peerPieceService = peerPieceService;
        this.fileRepository = fileRepository;
    }
    private String calculateHash(File file) {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            FileInputStream fis =
                    new FileInputStream(file);

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }

            fis.close();

            byte[] hashBytes = digest.digest();

            StringBuilder sb = new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hash", e);
        }
    }
    public void shareFile(Long userId, String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {
                throw new RuntimeException("File not found");
            }

            String fileName = file.getName();
            long fileSize = file.length();
            String fileHash = calculateHash(file);

            System.out.println(fileName);
            System.out.println(fileSize);

            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setFileName(fileName);
            fileMetadata.setFileSize(fileSize);
            fileMetadata.setPieceSize(524288);
            fileMetadata.setUploaderUserId(userId);
            fileMetadata.setFileHash(fileHash);

            FileMetadata registeredFile =
                    fileService.RegisterFile(fileMetadata);

            Long fileId = registeredFile.getFile_id();

            int pieceCount =
                    fileSplitterService.splitFile(file, fileId);

            registeredFile.setTotalPieces(pieceCount);

            fileService.save(registeredFile);

            Peer peer = peerRepository
                    .findByUserId(userId)
                    .orElseThrow(() ->
                            new RuntimeException("Peer not registered"));

            Long peerId = peer.getPeer_id();

            String shareCode = registeredFile.getShareCode();

            List<Integer> pieceIndexes = new ArrayList<>();

            for (int i = 0; i < pieceCount; i++) {
                pieceIndexes.add(i);
            }

            PieceAnnouncementRequest request =
                    new PieceAnnouncementRequest();

            request.setFileId(fileId);
            request.setPeerId(peerId);
            request.setPieceIndexes(pieceIndexes);

            peerPieceService.announcePieces(request);

            System.out.println("File ID: " + fileId);
            System.out.println("Peer ID: " + peerId);
            System.out.println("Share Code: " + shareCode);
            System.out.println("Pieces created: " + pieceCount);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to split file", e);
        }
    }
}
