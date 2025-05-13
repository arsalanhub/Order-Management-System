package com.OMS.ProductService.repositories;

import com.OMS.ProductService.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.*;

import java.util.*;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByAvailableQuantityGreaterThan(int quantity);
}
