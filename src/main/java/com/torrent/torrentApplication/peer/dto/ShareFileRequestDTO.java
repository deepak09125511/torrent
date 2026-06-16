package com.torrent.torrentApplication.peer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareFileRequestDTO {
    private long userId;
    private String filePath;
}
