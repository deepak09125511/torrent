package com.torrent.torrentApplication.tracker.controller;

import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/register")
    public ResponseEntity<FileMetadata>
    registerFile(@RequestBody FileMetadata file) {

        return ResponseEntity.ok(
                fileService.RegisterFile(file)
        );
    }

    @GetMapping("/{shareCode}")
    public ResponseEntity<FileMetadata>
    getFile(@PathVariable String shareCode) {
        FileMetadata file = fileService.getFileByShareCode(shareCode);
        return ResponseEntity.ok(file);
    }
}