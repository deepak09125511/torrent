package com.torrent.torrentApplication.peerDownloader;

import com.torrent.torrentApplication.tracker.dto.SwarmResponseDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrackerClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String TRACKER_URL =
            "http://localhost:8080/api";

    public SwarmResponseDTO getSwarm(String shareCode) {
        String url = TRACKER_URL + "/swarm/" + shareCode;

        return restTemplate.getForObject(url, SwarmResponseDTO.class);
    }

}
