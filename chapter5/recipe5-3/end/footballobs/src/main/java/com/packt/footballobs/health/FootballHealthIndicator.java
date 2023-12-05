package com.packt.footballobs.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import com.packt.footballobs.services.TradingService;


@Component
public class FootballHealthIndicator implements HealthIndicator {

    private TradingService tradingService;

    public FootballHealthIndicator(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @Override
    public Health health() {
        if (tradingService.getPendingOrders() > 90) {
            return Health.status(Status.DOWN).withDetail("Error Code", "Pending orders is greater than 90").build();
        } else {
            return Health.status(Status.UP).build();
        }
    }

}
