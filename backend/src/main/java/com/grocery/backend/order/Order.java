package com.grocery.backend.order;

import com.grocery.backend.cart.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("orders")
public class Order {
    @Id
    private String id;
    private String userEmail;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal total;
    private String status; // CREATED, PAID, OUT_FOR_DELIVERY, DELIVERED
    private Instant createdAt;
}

