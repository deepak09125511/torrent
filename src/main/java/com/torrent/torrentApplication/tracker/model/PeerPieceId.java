package com.torrent.torrentApplication.tracker.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class PeerPieceId implements Serializable {

    private Long fileId;

    private Long peerId;

    private Integer pieceIndex;
}