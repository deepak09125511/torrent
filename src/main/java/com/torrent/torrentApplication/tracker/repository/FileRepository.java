package com.torrent.torrentApplication.tracker.repository;

import com.torrent.torrentApplication.tracker.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository
        extends JpaRepository<FileMetadata, Long> {

    FileMetadata findByShareCode(String share_code);
    Optional<FileMetadata> findByFileHash(String fileHash);
    List<FileMetadata> findByUploaderUserId(Long userId);
}
