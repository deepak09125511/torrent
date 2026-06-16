package com.torrent.torrentApplication.tracker.controller;

import com.torrent.torrentApplication.tracker.model.Peer;
import com.torrent.torrentApplication.tracker.service.PeerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/peers")
public class PeerController {

    private final PeerService peerService;

    public PeerController(PeerService peerService) {
        this.peerService = peerService;
    }

    @PostMapping("/register")
    public ResponseEntity<Peer> registerPeer(
            @RequestBody Peer peer) {

        return ResponseEntity.ok(
                peerService.registerPeer(peer)
        );
    }

    @GetMapping("/{peerId}")
    public ResponseEntity<Peer> getPeer(
            @PathVariable Long peerId) {

        Peer peer = peerService.getPeer(peerId);

        if (peer == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(peer);
    }
}
