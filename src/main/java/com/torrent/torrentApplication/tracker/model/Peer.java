package com.torrent.torrentApplication.tracker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="peer")
public class Peer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long peer_id;

    @Column(unique = true,name = "user_id")
    private Long userId;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name="port")
    private Integer port;

    @Column(name="status")
    private String status;

    @Column(name = "last_seen")
    private LocalDateTime lastSeen;
}
