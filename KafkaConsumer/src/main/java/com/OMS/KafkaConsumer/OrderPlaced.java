package com.OMS.KafkaConsumer;

import com.OMS.ProductService.entities.Order;
import com.OMS.ProductService.entities.Product;
import com.OMS.ProductService.repositories.OrderRepository;
import com.OMS.ProductService.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class OrderPlaced {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @KafkaListener(topics = "OrderPlaced", groupId = "order-management-group")
    public void consume(Map<String, Object> eventPayload) {
        System.out.println("Consumed event: OrderPlaced");
        System.out.println(eventPayload);

        try {
            // 1. Extract order information
            UUID orderId = UUID.fromString(eventPayload.get("orderId").toString());
            List<Map<String, Object>> items = (List<Map<String, Object>>) eventPayload.get("items");

            // 2. Check inventory availability
            checkInventoryAvailability(items);

            // 3. Deduct quantities
            deductQuantities(items);

            // 4. Update order status to "PROCESSED"
            updateOrderStatus(orderId, "PROCESSED");

            System.out.println("Inventory updated and order processed successfully");

        } catch (Exception e) {
            System.err.println("Error processing order: " + e.getMessage());
            // You might want to update order status to "FAILED" here
            if (eventPayload.containsKey("orderId")) {
                updateOrderStatus(UUID.fromString(eventPayload.get("orderId").toString()), "FAILED");
            }
            throw e; // This will trigger Kafka retry mechanism if configured
        }
    }

    private void checkInventoryAvailability(List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            UUID productId = UUID.fromString(item.get("productId").toString());
            int requestedQuantity = (int) item.get("quantity");

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            if (product.getAvailableQuantity() < requestedQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + productId +
                        ". Available: " + product.getAvailableQuantity() +
                        ", Requested: " + requestedQuantity);
            }
        }
    }

    private void deductQuantities(List<Map<String, Object>> items) {
        for (Map<String, Object> item : items) {
            UUID productId = UUID.fromString(item.get("productId").toString());
            int quantityToDeduct = (int) item.get("quantity");

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

            product.setAvailableQuantity(product.getAvailableQuantity() - quantityToDeduct);
            productRepository.save(product);
        }
    }

    private void updateOrderStatus(UUID orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        order.setStatus(status);
        orderRepository.save(order);
    }
}
