package com.packt.footballobs.service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.observation.annotation.Observed;

@Observed(name = "football.auction")
@Service
public class AuctionService {
    private Map<String, String> bids = new ConcurrentHashMap<>();
    private Counter bidReceivedCounter;
    private Timer bidDuration;
    Random random = new Random();

    public AuctionService(MeterRegistry meterRegistry) {
        meterRegistry.gauge("football.bids.pending", bids, Map::size);
        this.bidReceivedCounter = meterRegistry.counter("football.bids.received");
        this.bidDuration = meterRegistry.timer("football.bids.duration");
    }

    public void addBid(String player, String bid) {
        bidDuration.record(() -> {
            bids.put(bid, player);
            bidReceivedCounter.increment();
            try {
                // simulate a long running operation
                Thread.sleep(random.nextInt(20));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bids.remove(bid);
        });
    }

    public void addBidAOP(String player, String bid) {
        bids.put(bid, player);
        try {
            // simulate a long running operation
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        bids.remove(bid);
    }

}
