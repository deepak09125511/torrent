package com.torrent.torrentApplication.peerDownloader.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class PieceService {

    private static final String STORAGE_PATH = "peer_storage/";

    public byte[] getPiece(Long fileId, Integer pieceIndex) {
        try {
            Path path = Paths.get(
                    STORAGE_PATH,
                    "file_" + fileId,
                    "piece_" + pieceIndex
            );

            return Files.readAllBytes(path);

        } catch (IOException e) {
            throw new RuntimeException("Piece not found", e);
        }
    }
}
