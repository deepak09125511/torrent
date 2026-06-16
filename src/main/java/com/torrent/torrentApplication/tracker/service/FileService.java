package com.torrent.torrentApplication.tracker.service;

import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class FileService {
    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository){
        this.fileRepository = fileRepository;
    }

    public FileMetadata RegisterFile(FileMetadata file){
        file.setShareCode(
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        return fileRepository.save(file);
    }
    public FileMetadata getFileByShareCode(String shareCode) {
        return fileRepository.findByShareCode(shareCode);
    }
}
