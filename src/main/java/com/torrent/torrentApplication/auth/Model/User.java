package com.torrent.torrentApplication.auth.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long user_id;

    @Column(name="username")
    private String userName;
    @Column(name = "email")
    private String email;
    @Column(name="password_hash")
    private String passwordHash;
    LocalDateTime created_at = LocalDateTime.now();

}

