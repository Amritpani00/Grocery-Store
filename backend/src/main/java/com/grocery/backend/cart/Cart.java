package com.grocery.backend.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("carts")
public class Cart {
    @Id
    private String id;
    private String userEmail;
    @Builder.Default
    private List<CartItem> items = new ArrayList<>();
}

