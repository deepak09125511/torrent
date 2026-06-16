package com.torrent.torrentApplication.tracker.service;

import com.torrent.torrentApplication.tracker.dto.PeerSwarmDTO;
import com.torrent.torrentApplication.tracker.dto.SwarmResponseDTO;
import com.torrent.torrentApplication.tracker.repository.SwarmRepository;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SwarmService {
    private final SwarmRepository swarmRepository;

    public SwarmService(SwarmRepository swarmRepository) {
        this.swarmRepository = swarmRepository;
    }

    public SwarmResponseDTO getSwarmByShareCode(String shareCode) {

        List<Object[]> rows = swarmRepository.getSwarmByShareCode(shareCode);

        if (rows.isEmpty()) {
            throw new RuntimeException("No file found for shareCode: "+shareCode);
        }

        SwarmResponseDTO response = new SwarmResponseDTO();

        Map<Long, PeerSwarmDTO> peerMap = new HashMap<>();

        for (Object[] row : rows) {

            Long fileId = ((Number) row[0]).longValue();
            String fileName = (String) row[1];
            String shareCodeValue = (String) row[2];

            Long peerId = ((Number) row[3]).longValue();
            String ip = (String) row[4];
            Integer port = ((Number) row[5]).intValue();
            Integer pieceIndex = ((Number) row[6]).intValue();


            response.setFileId(fileId);
            response.setFileName(fileName);
            response.setShareCode(shareCodeValue);

            PeerSwarmDTO peerDto = peerMap.get(peerId);

            if (peerDto == null) {
                peerDto = new PeerSwarmDTO();

                peerDto.setPeerId(peerId);
                peerDto.setIp(ip);
                peerDto.setPort(port);
                peerDto.setPieces(new ArrayList<>());

                peerMap.put(peerId, peerDto);
            }

            peerDto.getPieces().add(pieceIndex);
        }

        response.setPeers(new ArrayList<>(peerMap.values()));

        return response;
    }
}
