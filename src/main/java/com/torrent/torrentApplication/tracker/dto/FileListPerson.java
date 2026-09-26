package com.torrent.torrentApplication.tracker.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
public class FileListPerson {
    private Long fileId;
    private String fileName;
    private Long fileSize;
    private String shareCode;
    private Integer totalNoOfPieces;
    private Integer pieceSize;
}
