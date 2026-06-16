package com.torrent.torrentApplication.tracker.service;

import com.torrent.torrentApplication.tracker.dto.PieceAnnouncementRequest;
import com.torrent.torrentApplication.tracker.model.PeerPiece;
import com.torrent.torrentApplication.tracker.model.PeerPieceId;
import com.torrent.torrentApplication.tracker.repository.PeerPieceRepository;
import org.springframework.stereotype.Service;

@Service
public class PeerPieceService {

    private final PeerPieceRepository peerPieceRepository;

    public PeerPieceService(PeerPieceRepository peerPieceRepository) {
        this.peerPieceRepository = peerPieceRepository;
    }

    public void announcePieces(PieceAnnouncementRequest request) {

        for (Integer pieceIndex : request.getPieceIndexes()) {

            PeerPieceId id = new PeerPieceId();
            id.setFileId(request.getFileId());
            id.setPeerId(request.getPeerId());
            id.setPieceIndex(pieceIndex);

            PeerPiece peerPiece = new PeerPiece();
            peerPiece.setId(id);

            peerPieceRepository.save(peerPiece);
        }
    }
}
