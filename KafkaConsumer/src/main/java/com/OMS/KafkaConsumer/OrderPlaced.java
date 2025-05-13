package com.OMS.KafkaConsumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderPlaced {
    @KafkaListener(topics = "OrderPlaced", groupId = "order-management-group")
    public void consume(Map<String, Object> eventPayload) {
        System.out.println("Consumed event: inventory-reserve-requested");
        System.out.println(eventPayload);
    }
}
