package com.torrent.torrentApplication.tracker.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="peer_pieces")
public class PeerPiece {
    @EmbeddedId
    private PeerPieceId id;
}
