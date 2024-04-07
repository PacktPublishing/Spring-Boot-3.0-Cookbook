package com.packt.football.domain;

import java.util.List;

public class TradingUser {
    private User user;
    private List<Card> cards;
    private List<Album> albums;
    public TradingUser(User user2, List<Card> cards, List<Album> albums) {
        this.user = user2;
        this.cards = cards;
        this.albums = albums;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

}
