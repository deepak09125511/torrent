package com.torrent.torrentApplication.tracker.repository;

import com.torrent.torrentApplication.tracker.model.PeerPiece;
import com.torrent.torrentApplication.tracker.model.PeerPieceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeerPieceRepository
        extends JpaRepository<PeerPiece, PeerPieceId> {

}