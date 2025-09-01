package com.grocery.backend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/categories")
    public ResponseEntity<?> listCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String categoryId
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> result;
        if (q != null && !q.isBlank()) {
            result = productRepository.findByNameContainingIgnoreCase(q, pageable);
        } else if (categoryId != null && !categoryId.isBlank()) {
            result = productRepository.findByCategoryId(categoryId, pageable);
        } else {
            result = productRepository.findAll(pageable);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/products/new")
    public ResponseEntity<List<Product>> getNewProducts() {
        return ResponseEntity.ok(productRepository.findByIsNew(true));
    }

    @GetMapping("/products/top-seller")
    public ResponseEntity<List<Product>> getTopSellerProducts() {
        return ResponseEntity.ok(productRepository.findByIsTopSeller(true));
    }

    @GetMapping("/products/discounted")
    public ResponseEntity<List<Product>> getDiscountedProducts() {
        return ResponseEntity.ok(productRepository.findByDiscountPriceNotNull());
    }
}

