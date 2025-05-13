package com.OMS.ProductService.requests;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    private UUID userId;

    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private UUID productId;
        private int quantity;
    }
}