package com.torrent.torrentApplication.tracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="files")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long file_id;

    @Column(unique = true,name = "share_code")
    private String shareCode;

    @Column(unique = true,name = "file_hash")
    private String fileHash;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "piece_size")
    private Integer pieceSize;

    @Column(name = "total_pieces")
    private Integer totalPieces;

    @Column(name = "uploader_user_id")
    private Long uploaderUserId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
