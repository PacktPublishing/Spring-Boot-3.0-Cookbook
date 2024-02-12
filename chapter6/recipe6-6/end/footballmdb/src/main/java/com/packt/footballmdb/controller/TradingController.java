package com.packt.footballmdb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballmdb.model.TradeRequest;
import com.packt.footballmdb.service.TradingService;

@RequestMapping("/trading")
@RestController
public class TradingController {

    private TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @PostMapping("{cardId}")
    public ResponseEntity<String> Trade(@PathVariable String cardId, @RequestBody TradeRequest tradeRequest) {
        if (tradingService.exchangeCard(cardId, tradeRequest.newOwnerId(), tradeRequest.price())) {
            return ResponseEntity.ok("Card exchanged");
        } else {
            return ResponseEntity.status(409).body("Someone achieved the card before you");
        }
    }

}
