package com.torrent.torrentApplication.tracker.controller;

import com.torrent.torrentApplication.tracker.dto.PieceAnnouncementRequest;
import com.torrent.torrentApplication.tracker.service.PeerPieceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pieces")
public class PeerPieceController {

    private final PeerPieceService peerPieceService;

    public PeerPieceController(PeerPieceService peerPieceService) {
        this.peerPieceService = peerPieceService;
    }

    @PostMapping("/announce")
    public ResponseEntity<String> announcePieces(
            @RequestBody PieceAnnouncementRequest request) {

        peerPieceService.announcePieces(request);

        return ResponseEntity.ok("Pieces announced successfully");
    }
}
