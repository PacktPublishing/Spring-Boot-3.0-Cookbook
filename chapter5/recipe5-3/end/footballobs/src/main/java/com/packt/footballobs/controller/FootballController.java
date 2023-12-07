package com.packt.footballobs.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballobs.services.FileLoader;
import com.packt.footballobs.services.TradingService;



@RestController
@RequestMapping("/football")
public class FootballController {

    private FileLoader fileLoader;
    private TradingService tradingService;

    public FootballController(FileLoader fileLoader, TradingService tradingService){
        this.fileLoader = fileLoader;
        this.tradingService = tradingService;
    }

    @GetMapping
    public List<String> getTeams(){
        return fileLoader.getTeams();
    }
    

    @PostMapping
    public int tradeCards(@RequestBody int orders) {
        return tradingService.tradeCards(orders);
    }
    
}
