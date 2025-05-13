package com.OMS.ProductService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DexClient {
    @Autowired
    private KafkaProducerService kafkaProducerService;

    public void pushToDex(Map<String, Object> payload, String eventName) {
        kafkaProducerService.sendEvent(eventName, payload);
    }
}