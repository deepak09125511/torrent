package com.torrent.torrentApplication.peerDownloader;

import com.torrent.torrentApplication.tracker.dto.SwarmResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class TrackerClient {
    private final RestTemplate restTemplate = new RestTemplate();

    private static final String TRACKER_URL =
            "http://localhost:8080/api";

    public SwarmResponseDTO getSwarm(String shareCode) {

        String url = TRACKER_URL + "/swarm/" + shareCode;

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = attributes.getRequest();

        String authHeader = request.getHeader("Authorization");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<SwarmResponseDTO> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        SwarmResponseDTO.class
                );

        return response.getBody();
    }

}
