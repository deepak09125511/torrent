package com.torrent.torrentApplication.tracker.service;

import com.torrent.torrentApplication.tracker.model.Peer;
import com.torrent.torrentApplication.tracker.repository.PeerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service

public class PeerService {

    private final PeerRepository peerRepository;

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    public Peer registerPeer(Peer peer) {

        peer.setStatus("ONLINE");
        peer.setLastSeen(LocalDateTime.now());
        return peerRepository.save(peer);
    }

    public Peer getPeer(Long peerId) {
        return peerRepository.findById(peerId).orElse(null);
    }
}