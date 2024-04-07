package com.packt.football.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.football.domain.Album;
import com.packt.football.domain.Card;
import com.packt.football.domain.TradingUser;
import com.packt.football.service.AlbumsService;

@RequestMapping("/albums")
@RestController
public class AlbumsController {

    private AlbumsService albumsService;

    public AlbumsController(AlbumsService albumsService) {
        this.albumsService = albumsService;
    }

    @PostMapping("/{userId}")
    public Album buyAlbum(@PathVariable Integer userId, @RequestBody String title) {
        return albumsService.buyAlbum(userId, title);
    }

    @PostMapping("/{userId}/cards")
    public List<Card> buyCards(@PathVariable Integer userId, @RequestBody Integer count) {
        return albumsService.buyCards(userId, count);
    }

    @GetMapping("/users/{userId}")
    public Optional<TradingUser> getUserWithCardsAndAlbums(@PathVariable Integer userId) {
        return albumsService.getUserWithCardsAndAlbums(userId);
    }

    @PostMapping("/{albumId}/cards/{cardId}")
    public Card addCardToAlbum(@PathVariable Integer cardId, @PathVariable Integer albumId) {
        return albumsService.addCardToAlbum(cardId, albumId);
    }

    @PostMapping("/{userId}/transfer/{cardId}")
    public Optional<Card> transferCard(@PathVariable Integer cardId, @PathVariable Integer userId) {
        return albumsService.transferCard(cardId, userId);
    }

    @PostMapping("/{userId}/auto")
    public List<Card> autoAssign(@PathVariable Integer userId) {
        return albumsService.useAllCardAvailable(userId);
    }

    @PostMapping("/trade/{userId1}/{userId2}")
    public List<Card> tradeCardsBetweenUsers(@PathVariable Integer userId1, @PathVariable Integer userId2) {
        return albumsService.tradeAllCards(userId1, userId2);
    }

    

}
