package com.torrent.torrentApplication.peerDownloader;

import com.torrent.torrentApplication.peerDownloader.service.PieceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/piece")
public class PieceController {

    private final PieceService pieceService;

    public PieceController(PieceService pieceService) {
        this.pieceService = pieceService;
    }

    @GetMapping("/{fileId}/{pieceIndex}")
    public ResponseEntity<byte[]> getPiece(
            @PathVariable Long fileId,
            @PathVariable Integer pieceIndex) {

        byte[] data = pieceService.getPiece(fileId, pieceIndex);

        return ResponseEntity.ok(data);
    }
}
