package com.torrent.torrentApplication.tracker.dto;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PeerSwarmDTO {
    private Long peerId;
    private String ip;
    private Integer port;
    private List<Integer> pieces;
}
