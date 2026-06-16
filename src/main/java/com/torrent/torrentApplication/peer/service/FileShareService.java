package com.torrent.torrentApplication.peer.service;

import com.torrent.torrentApplication.tracker.repository.PeerRepository;
import com.torrent.torrentApplication.tracker.dto.PieceAnnouncementRequest;
import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.model.Peer;
import com.torrent.torrentApplication.tracker.service.FileService;
import com.torrent.torrentApplication.tracker.service.PeerPieceService;
import org.springframework.stereotype.Service;

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

    public FileShareService(FileSplitterService fileSplitterService,FileService fileService,
                            PeerRepository peerRepository,
                            PeerPieceService peerPieceService){
        this.fileSplitterService = fileSplitterService;
        this.fileService = fileService;
        this.peerRepository = peerRepository;
        this.peerPieceService = peerPieceService;
    }
    public void shareFile(Long userId,String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {
                throw new RuntimeException("File not found");
            }

            String fileName = file.getName();
            long fileSize = file.length();

            System.out.println(fileName);
            System.out.println(fileSize);

            int pieceCount = fileSplitterService.splitFile(file);
            FileMetadata fileMetadata = new FileMetadata();
            fileMetadata.setFileName(fileName);
            fileMetadata.setFileSize(fileSize);
            fileMetadata.setPieceSize(524288); // if your field is Long
            fileMetadata.setTotalPieces(pieceCount);
            fileMetadata.setUploaderUserId(userId);
            FileMetadata registeredFile = fileService.RegisterFile(fileMetadata);
            Peer peer = peerRepository
                    .findByUserId(userId)
                    .orElseThrow(() ->
                            new RuntimeException("Peer not registered"));
            Long peerId = peer.getPeer_id();
            Long fileId = registeredFile.getFile_id();
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
