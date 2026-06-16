package com.torrent.torrentApplication.tracker.controller;

import com.torrent.torrentApplication.tracker.dto.SwarmResponseDTO;
import com.torrent.torrentApplication.tracker.service.SwarmService;
import jakarta.persistence.EmbeddedId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/swarm")
public class SwarmController {
    private final SwarmService swarmService;

    public SwarmController(SwarmService swarmService) {
        this.swarmService = swarmService;
    }

    @GetMapping("/{shareCode}")
    public ResponseEntity<SwarmResponseDTO> getSwarm(
            @PathVariable String shareCode) {

        SwarmResponseDTO response =
                swarmService.getSwarmByShareCode(shareCode);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}
