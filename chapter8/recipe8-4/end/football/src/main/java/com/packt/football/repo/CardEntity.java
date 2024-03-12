package com.packt.football.repo;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Optional;

@Table(name = "cards")
public class CardEntity {
    @Id
    private Long id;

    private Optional<Long> albumId;

    private Long playerId;

    private Long ownerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Optional<Long> getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Optional<Long> albumId) {
        this.albumId = albumId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
