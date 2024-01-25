package com.packt.footballobs.controller;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.packt.footballobs.service.FileLoader;
import com.packt.footballobs.service.TradingService;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;

@RestController
@RequestMapping("/football")
public class FootballController {

    private FileLoader fileLoader;
    private TradingService tradingService;
    private final ObservationRegistry observationRegistry;

    private static final Logger logger = LoggerFactory.getLogger(FootballController.class);
    private static Random random = new Random();

    public FootballController(FileLoader fileLoader, TradingService tradingService,
            ObservationRegistry observationRegistry) {
        this.fileLoader = fileLoader;
        this.tradingService = tradingService;
        this.observationRegistry = observationRegistry;
    }

    @GetMapping
    public List<String> getTeams() {
        return fileLoader.getTeams();
    }

    @PostMapping
    public int tradeCards(@RequestBody int orders) {
        return tradingService.tradeCards(orders);
    }

    @GetMapping("ranking/{player}")
    public int getRanking(@PathVariable String player) {
        logger.info("Preparing ranking for player {}", player);
        if (random.nextInt(100) > 94) {
            throw new RuntimeException("It's not possible to get the ranking for player " + player
                    + " at this moment. Please try again later.");
        }
        Observation collectObservation = Observation.createNotStarted("collect", observationRegistry);
        collectObservation.lowCardinalityKeyValue("player", player);
        collectObservation.observe(() -> {
            try {
                logger.info("Simulate a data collection for player {}", player);
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Observation processObservation = Observation.createNotStarted("process", observationRegistry);
        processObservation.lowCardinalityKeyValue("player", player);
        processObservation.observe(() -> {
            try {
                logger.info("Simulate a data processing for player {}", player);
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        return random.nextInt(1000);
    }

}
