package com.packt.footballobs.services;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class TradingService {
    public int getPendingOrders() {
        Random random = new Random();
        return random.nextInt(100);
    }
}
