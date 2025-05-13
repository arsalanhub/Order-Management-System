package com.OMS.ProductService.services;

import com.OMS.ProductService.entities.Product;
import com.OMS.ProductService.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableQuantityGreaterThan(0);
    }
}

