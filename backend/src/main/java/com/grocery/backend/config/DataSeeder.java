package com.grocery.backend.config;

import com.grocery.backend.product.Category;
import com.grocery.backend.product.CategoryRepository;
import com.grocery.backend.product.Product;
import com.grocery.backend.product.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final boolean enabled;

    public DataSeeder(CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      @Value("${app.seed:false}") boolean enabled) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.enabled = enabled;
    }

    @PostConstruct
    public void seed() {
        if (!enabled) return;
        if (categoryRepository.count() == 0) {
            Category fruits = categoryRepository.save(Category.builder().name("Fruits").slug("fruits").build());
            Category vegetables = categoryRepository.save(Category.builder().name("Vegetables").slug("vegetables").build());
            Category dairy = categoryRepository.save(Category.builder().name("Dairy").slug("dairy").build());

            productRepository.saveAll(List.of(
                    Product.builder().name("Apple").description("Fresh red apples")
                            .price(new BigDecimal("2.49")).categoryId(fruits.getId()).imageUrl("/images/apple.jpg").inStock(true)
                            .isTopSeller(true).build(),
                    Product.builder().name("Banana").description("Sweet bananas")
                            .price(new BigDecimal("1.29")).categoryId(fruits.getId()).imageUrl("/images/banana.jpg").inStock(true)
                            .isNew(true).build(),
                    Product.builder().name("Milk").description("Whole milk 1L")
                            .price(new BigDecimal("0.99")).categoryId(dairy.getId()).imageUrl("/images/milk.jpg").inStock(true).build(),
                    Product.builder().name("Tomato").description("Juicy tomatoes")
                            .price(new BigDecimal("1.99")).discountPrice(new BigDecimal("1.49"))
                            .categoryId(vegetables.getId()).imageUrl("/images/tomato.jpg").inStock(true).build()
            ));
        }
    }
}

