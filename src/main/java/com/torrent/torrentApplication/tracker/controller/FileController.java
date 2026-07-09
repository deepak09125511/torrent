package com.torrent.torrentApplication.tracker.controller;

import com.torrent.torrentApplication.tracker.dto.FileListPerson;
import com.torrent.torrentApplication.tracker.model.FileMetadata;
import com.torrent.torrentApplication.tracker.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FileListPerson>> getListFile(@PathVariable Long userId){
        List<FileListPerson> fileList = fileService.getFileByUserId(userId);
        return ResponseEntity.ok(fileList);
    }
}