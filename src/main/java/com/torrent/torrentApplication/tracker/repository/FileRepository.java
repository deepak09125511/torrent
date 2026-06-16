package com.torrent.torrentApplication.tracker.repository;

import com.torrent.torrentApplication.tracker.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository
        extends JpaRepository<FileMetadata, Long> {

    FileMetadata findByShareCode(String share_code);
}
