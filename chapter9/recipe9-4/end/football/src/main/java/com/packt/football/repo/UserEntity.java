package com.packt.football.repo;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "users")
@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    // @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<CardEntity> ownedCards;

    // @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<AlbumEntity> ownedAlbums;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CardEntity> getOwnedCards() {
        return ownedCards;
    }

    public void setOwnedCards(List<CardEntity> ownedCards) {
        this.ownedCards = ownedCards;
    }

    public Set<AlbumEntity> getOwnedAlbums() {
        return ownedAlbums;
    }

    public void setOwnedAlbums(Set<AlbumEntity> ownedAlbums) {
        this.ownedAlbums = ownedAlbums;
    }
    
}
