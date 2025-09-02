package com.grocery.backend.config;

import com.grocery.backend.product.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataSeeder {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final boolean enabled;

    public DataSeeder(CategoryRepository categoryRepository,
                      ProductRepository productRepository,
                      BannerRepository bannerRepository,
                      @Value("${app.seed:false}") boolean enabled) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.bannerRepository = bannerRepository;
        this.enabled = enabled;
    }

    @PostConstruct
    public void seed() {
        if (!enabled) return;
        if (categoryRepository.count() == 0) {
            Category fruits = categoryRepository.save(Category.builder().name("Fruits").slug("fruits").build());
            Category vegetables = categoryRepository.save(Category.builder().name("Vegetables").slug("vegetables").build());
            Category dairy = categoryRepository.save(Category.builder().name("Dairy").slug("dairy").build());
            Category bakery = categoryRepository.save(Category.builder().name("Bakery").slug("bakery").build());
            Category grocery = categoryRepository.save(Category.builder().name("Grocery").slug("grocery").build());

            productRepository.saveAll(List.of(
                    Product.builder().name("Rice").description("Basmati rice 1kg")
                            .price(new BigDecimal("9.99")).categoryId(grocery.getId()).imageUrl("/images/rice.jpg").inStock(true).build(),
                    Product.builder().name("Pasta").description("Penne pasta 500g")
                            .price(new BigDecimal("2.49")).categoryId(grocery.getId()).imageUrl("/images/pasta.jpg").inStock(true).build(),
                    Product.builder().name("Olive Oil").description("Extra virgin olive oil 500ml")
                            .price(new BigDecimal("7.99")).categoryId(grocery.getId()).imageUrl("/images/olive-oil.jpg").inStock(true).build(),
                    Product.builder().name("Lentils").description("Red lentils 1kg")
                            .price(new BigDecimal("4.99")).categoryId(grocery.getId()).imageUrl("/images/lentils.jpg").inStock(true).build(),
                    Product.builder().name("Flour").description("All-purpose flour 1kg")
                            .price(new BigDecimal("3.49")).categoryId(grocery.getId()).imageUrl("/images/flour.jpg").inStock(true).build(),
                    Product.builder().name("Ghee").description("Pure cow ghee 500g")
                            .price(new BigDecimal("12.99")).categoryId(grocery.getId()).imageUrl("/images/ghee.jpg").inStock(true).build(),
                    Product.builder().name("Sugar").description("Refined sugar 1kg")
                            .price(new BigDecimal("2.99")).categoryId(grocery.getId()).imageUrl("/images/sugar.jpg").inStock(true).build(),
                    Product.builder().name("Besan").description("Gram flour 1kg")
                            .price(new BigDecimal("4.49")).categoryId(grocery.getId()).imageUrl("/images/besan.jpg").inStock(true).build(),
                    Product.builder().name("Apple").description("Fresh red apples")
                            .price(new BigDecimal("2.49")).categoryId(fruits.getId()).imageUrl("/images/apple.jpg").inStock(true)
                            .isTopSeller(true).build(),
                    Product.builder().name("Banana").description("Sweet bananas")
                            .price(new BigDecimal("1.29")).categoryId(fruits.getId()).imageUrl("/images/banana.jpg").inStock(true)
                            .isNew(true).build(),
                    Product.builder().name("Orange").description("Juicy oranges")
                            .price(new BigDecimal("3.99")).discountPrice(new BigDecimal("3.49"))
                            .categoryId(fruits.getId()).imageUrl("/images/orange.jpg").inStock(true).build(),
                    Product.builder().name("Milk").description("Whole milk 1L")
                            .price(new BigDecimal("0.99")).categoryId(dairy.getId()).imageUrl("/images/milk.jpg").inStock(true).build(),
                    Product.builder().name("Cheese").description("Cheddar cheese")
                            .price(new BigDecimal("5.99")).categoryId(dairy.getId()).imageUrl("/images/cheese.jpg").inStock(true).build(),
                    Product.builder().name("Yogurt").description("Greek yogurt")
                            .price(new BigDecimal("2.99")).categoryId(dairy.getId()).imageUrl("/images/yogurt.jpg").inStock(true).build(),
                    Product.builder().name("Tomato").description("Juicy tomatoes")
                            .price(new BigDecimal("1.99")).discountPrice(new BigDecimal("1.49"))
                            .categoryId(vegetables.getId()).imageUrl("/images/tomato.jpg").inStock(true).build(),
                    Product.builder().name("Carrot").description("Fresh carrots")
                            .price(new BigDecimal("1.49")).categoryId(vegetables.getId()).imageUrl("/images/carrot.jpg").inStock(true).build(),
                    Product.builder().name("Broccoli").description("Fresh broccoli")
                            .price(new BigDecimal("2.99")).categoryId(vegetables.getId()).imageUrl("/images/broccoli.jpg").inStock(true).build(),
                    Product.builder().name("Bread").description("Whole wheat bread")
                            .price(new BigDecimal("3.49")).categoryId(bakery.getId()).imageUrl("/images/bread.jpg").inStock(true).build(),
                    Product.builder().name("Croissant").description("Buttery croissant")
                            .price(new BigDecimal("2.99")).discountPrice(new BigDecimal("2.49"))
                            .categoryId(bakery.getId()).imageUrl("/images/croissant.jpg").inStock(true).build(),
                    Product.builder().name("Muffin").description("Blueberry muffin")
                            .price(new BigDecimal("1.99")).categoryId(bakery.getId()).imageUrl("/images/muffin.jpg").inStock(true)
                            .isNew(true).build()
            ));
        }
        if (bannerRepository.count() == 0) {
            bannerRepository.saveAll(List.of(
                    Banner.builder().title("Fresh groceries delivered to your door").subtitle("Browse products, add to cart, checkout and track your delivery in real time.").imageUrl("/images/banner1.jpg").link("/products").build(),
                    Banner.builder().title("Festival Season Offer").subtitle("Get 10% off on selected items.").imageUrl("/images/banner2.jpg").link("/products/discounted").build(),
                    Banner.builder().title("10% Discount on all vegetables").subtitle("Fresh from the farm.").imageUrl("/images/banner3.jpg").link("/products?categoryId=vegetables").build(),
                    Banner.builder().title("Diwali Season Special").subtitle("Get 20% off on all sweets.").imageUrl("/images/banner4.jpg").link("/products").build()
            ));
        }
    }
}

