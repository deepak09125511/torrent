package com.torrent.torrentApplication.tracker.repository;

import com.torrent.torrentApplication.tracker.model.Peer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PeerRepository extends JpaRepository<Peer,Long> {
    Optional<Peer> findByUserId(Long userId);
}
