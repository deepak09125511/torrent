package com.torrent.torrentApplication.peerDownloader;

import com.torrent.torrentApplication.peerDownloader.service.DownloadManager;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/download")
public class DownloadController {

    private final DownloadManager downloadManager;

    public DownloadController(DownloadManager downloadManager) {

        this.downloadManager = downloadManager;
    }

    @PostMapping("/{shareCode}")
    public ResponseEntity<Long> download(
            @PathVariable String shareCode) {

        Long fileId = downloadManager.download(shareCode);

        return ResponseEntity.ok(fileId);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable Long fileId) {

        try {

            Resource resource =
                    downloadManager.getDownloadedFile(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"final.mp4\""
                    )
                    .body(resource);

        } catch (IOException e) {

            return ResponseEntity.notFound().build();
        }
    }
}

