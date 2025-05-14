package com.OMS.ProductService.services;

import com.OMS.ProductService.entities.Order;
import com.OMS.ProductService.entities.Product;
import com.OMS.ProductService.repositories.OrderRepository;
import com.OMS.ProductService.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailableQuantityGreaterThan(0);
    }
}

