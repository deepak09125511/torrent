package com.torrent.torrentApplication.peer.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileSplitterService {
    private static final int PIECE_SIZE = 512 * 1024;

    public int splitFile(File file,Long file_id) throws IOException {

        String fileName = file.getName();

        Path outputDir = Paths.get("peer-storage", "file_" + file_id);

        Files.createDirectories(outputDir);

        int pieceIndex = 0;

        try (BufferedInputStream input =
                     new BufferedInputStream(
                             new FileInputStream(file))) {

            byte[] buffer = new byte[PIECE_SIZE];

            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {

                File pieceFile =
                        outputDir.resolve(
                                        "piece_" + pieceIndex + ".bin")
                                .toFile();

                try (FileOutputStream output =
                             new FileOutputStream(pieceFile)) {

                    output.write(buffer, 0, bytesRead);
                }

                pieceIndex++;
            }
        }

        return pieceIndex;
    }
}
