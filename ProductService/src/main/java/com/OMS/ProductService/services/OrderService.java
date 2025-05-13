package com.OMS.ProductService.services;

import com.OMS.ProductService.entities.Order;
import com.OMS.ProductService.entities.OrderItem;
import com.OMS.ProductService.entities.Product;
import com.OMS.ProductService.repositories.OrderRepository;
import com.OMS.ProductService.repositories.ProductRepository;
import com.OMS.ProductService.requests.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private DexClient dexClient;

    public void placeOrder(OrderRequest request) {
        List<OrderRequest.Item> items = request.getItems();

        // 1. Basic sanity check
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one product.");
        }

        List<UUID> productIds = new ArrayList<>();
        for (OrderRequest.Item item : items) {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("Quantity must be greater than zero.");
            }
            productIds.add(item.getProductId());
        }

        // 2. Fetch products
        List<Product> foundProducts = productRepository.findAllById(productIds);
        if (foundProducts.size() != productIds.size()) {
            throw new IllegalArgumentException("One or more products do not exist.");
        }

        // 3. Validate each requested product's quantity
        for (OrderRequest.Item item : items) {
            UUID requestedProductId = item.getProductId();
            int requestedQty = item.getQuantity();

            boolean validQty = false;
            for (Product product : foundProducts) {
                if (product.getId().equals(requestedProductId)) {
                    if (product.getAvailableQuantity() >= requestedQty) {
                        validQty = true;
                        break;
                    } else {
                        throw new IllegalArgumentException("Insufficient stock for product: " + product.getName());
                    }
                }
            }

            if (!validQty) {
                throw new IllegalArgumentException("Product not found during quantity check.");
            }
        }

        // 4. Save Order in PENDING state
        Order order = new Order();
        order.setUserId(request.getUserId());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.Item item : items) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // 5. Trigger Dex Event
        Map<String, Object> payload = new HashMap<>();
        payload.put("orderId", order.getId());
        payload.put("items", items);

        dexClient.pushToDex(payload, "OrderPlaced");
    }
}


