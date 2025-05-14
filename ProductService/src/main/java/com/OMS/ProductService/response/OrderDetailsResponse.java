package com.OMS.ProductService.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetailsResponse {
    private UUID orderId;
    private LocalDateTime orderDate;
    private String status;
    private List<OrderItemResponse> items;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public static class OrderItemResponse {
        private UUID productId;
        private int quantity;
    }
}

