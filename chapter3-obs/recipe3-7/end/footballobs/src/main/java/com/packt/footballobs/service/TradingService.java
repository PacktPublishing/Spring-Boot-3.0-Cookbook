package com.packt.footballobs.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;


@Service
public class TradingService {

    private ApplicationEventPublisher applicationEventPublisher;
    private static final Logger logger = LoggerFactory.getLogger(TradingService.class);

    public TradingService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public int getPendingOrders() {
        logger.debug("Ensuring that pending orders can be calculated");        
        Random random = new Random();
        int pendingOrders = random.nextInt(100);
        logger.info(pendingOrders + " pending orders found");
        return pendingOrders;
    }

    public int tradeCards(int orders) {
        if (getPendingOrders() > 90) {
            logger.warn("There are more than 90 orders, this can cause the system to crash");
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("There are more than 90 pending orders"), LivenessState.BROKEN);
        } else {
            logger.debug("There are more less than 90 orders, can manage it");
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("working fine"), LivenessState.CORRECT);
        }
        return orders;
    }
}
