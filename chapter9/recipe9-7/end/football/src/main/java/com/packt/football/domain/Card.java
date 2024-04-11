package com.packt.football.domain;

public class Card {
    private Integer id;
    private Integer ownerId;
    private Integer albumId;
    private Player player;
    
    public Card(Integer id, Integer ownerId, Integer albumId, Player player) {
        this.id = id;
        this.ownerId = ownerId;
        this.albumId = albumId;
        this.player = player;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }
    public Integer getAlbumId() {
        return albumId;
    }
    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }
    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

}
