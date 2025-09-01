package com.grocery.backend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findByNameContainingIgnoreCase(String q, Pageable pageable);
    Page<Product> findByCategoryId(String categoryId, Pageable pageable);
    List<Product> findByIsNew(boolean isNew);
    List<Product> findByIsTopSeller(boolean isTopSeller);
    List<Product> findByDiscountPriceNotNull();
}

