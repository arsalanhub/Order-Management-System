package com.OMS.ProductService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, Map<String, Object> payload) {
        kafkaTemplate.send(topic, payload);
    }
}