package com.torrent.torrentApplication.peer.controller;

import com.torrent.torrentApplication.peer.dto.ShareFileRequestDTO;
import com.torrent.torrentApplication.peer.service.FileShareService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/peer")
public class PeerFileController {

    private final FileShareService fileShareService;

    public PeerFileController(FileShareService fileShareService) {
        this.fileShareService = fileShareService;
    }

    @PostMapping("/share")
    public ResponseEntity<String> shareFile(
            @RequestBody ShareFileRequestDTO request)
            throws IOException {

        fileShareService.shareFile(
                request.getUserId(),
                request.getFilePath());

        return ResponseEntity.ok("File shared successfully");
    }
}
