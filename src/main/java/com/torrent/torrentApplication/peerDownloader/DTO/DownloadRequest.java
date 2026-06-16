package com.torrent.torrentApplication.peerDownloader.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DownloadRequest {
    private String shareCode;
    private Long requesterPeerId;
}