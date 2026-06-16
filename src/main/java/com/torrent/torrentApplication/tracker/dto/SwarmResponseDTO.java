package com.torrent.torrentApplication.tracker.dto;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
@Getter
@Setter
public
class SwarmResponseDTO {
    private Long fileId;
    private String fileName;
    private String shareCode;
    private List<PeerSwarmDTO> peers;
}
