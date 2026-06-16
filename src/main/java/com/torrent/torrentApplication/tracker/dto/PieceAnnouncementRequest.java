package com.torrent.torrentApplication.tracker.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class PieceAnnouncementRequest {
    private Long fileId;

    private Long peerId;

    private List<Integer> pieceIndexes;
}
