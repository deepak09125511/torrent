package com.torrent.torrentApplication.peerDownloader;

import com.torrent.torrentApplication.peerDownloader.service.DownloadManager;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/download")
public class DownloadController {

    private final DownloadManager downloadManager;

    public DownloadController(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    @PostMapping("/{shareCode}")
    public String download(
            @PathVariable String shareCode) {

        downloadManager.download(shareCode);

        return "Download completed";
    }
}

