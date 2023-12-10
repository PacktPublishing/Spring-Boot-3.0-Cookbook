package com.packt.footballobs.service;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;

import java.util.Random;

@Service
public class TradingService {

    private ApplicationEventPublisher applicationEventPublisher;
    private MeterRegistry meterRegistry;
    private Counter ordersTradedCounter;
    
    public TradingService(ApplicationEventPublisher applicationEventPublisher, MeterRegistry meterRegistry) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.meterRegistry = meterRegistry;
        this.ordersTradedCounter = meterRegistry.counter("orders_traded");
        this.meterRegistry.gauge("pending_orders", this, TradingService::getPendingOrders);
    }

    public int getPendingOrders() {
        Random random = new Random();
        return random.nextInt(100);
    }

    public int tradeCards(int orders) {
        if (getPendingOrders() > 90) {
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("There are more than 90 pending orders"), LivenessState.BROKEN);
        } else {
            AvailabilityChangeEvent.publish(applicationEventPublisher,
                    new Exception("working fine"), LivenessState.CORRECT);
        }
        ordersTradedCounter.increment(orders);
        return orders;
    }
}
