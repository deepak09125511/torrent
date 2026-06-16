package com.torrent.torrentApplication.tracker.repository;

import com.torrent.torrentApplication.tracker.model.PeerPiece;
import com.torrent.torrentApplication.tracker.model.PeerPieceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SwarmRepository extends JpaRepository<PeerPiece, PeerPieceId> {
    @Query(value = """
        SELECT
            f.file_id,
            f.file_name,
            f.share_code,
            p.peer_id,
            p.ip_address,
            p.port,
            pp.piece_index
        FROM peer_pieces pp
        JOIN peer p
            ON pp.peer_id = p.peer_id
        JOIN files f
            ON pp.file_id = f.file_id
        WHERE f.share_code = :shareCode
        """, nativeQuery = true)
    List<Object[]> getSwarmByShareCode(
            @Param("shareCode") String shareCode);
}

