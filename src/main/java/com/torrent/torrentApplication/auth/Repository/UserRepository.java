package com.torrent.torrentApplication.auth.Repository;
import com.torrent.torrentApplication.auth.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findOptionalByEmail(String email);
}