package com.OMS.ProductService.services;

import com.OMS.ProductService.requests.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.UUID;

@Service
public class DexClient {

    @Autowired
    private RestTemplate restTemplate;

    public void pushToDex(Map<String, Object> payload, String eventName) {
        String url = "http://DEX-SERVICE/events"; // Placeholder URL

        Map<String, Object> eventRequest = new HashMap<>();
        eventRequest.put("eventName", eventName);
        eventRequest.put("payload", payload);

        try {
            restTemplate.postForEntity(url, eventRequest, Void.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to call Dex service: " + e.getMessage());
        }
    }
}

