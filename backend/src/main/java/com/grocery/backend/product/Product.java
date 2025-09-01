package com.grocery.backend.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("products")
public class Product {
    @Id
    private String id;
    @Indexed
    private String name;
    private String description;
    private String imageUrl;
    private String categoryId;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private boolean isNew;
    private boolean isTopSeller;
    private boolean inStock;
}

