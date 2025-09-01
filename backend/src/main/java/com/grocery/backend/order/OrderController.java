package com.grocery.backend.order;

import com.grocery.backend.cart.Cart;
import com.grocery.backend.cart.CartRepository;
import com.grocery.backend.product.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, CartRepository cartRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<?> list(Authentication auth) {
        String email = (String) auth.getPrincipal();
        return ResponseEntity.ok(orderRepository.findByUserEmailOrderByCreatedAtDesc(email));
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication auth, @RequestBody @Valid CheckoutRequest req) {
        String email = (String) auth.getPrincipal();
        Cart cart = cartRepository.findByUserEmail(email).orElse(null);
        if (cart == null || cart.getItems().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Cart is empty"));
        }
        BigDecimal total = cart.getItems().stream().map(i ->
                productRepository.findById(i.getProductId()).map(p -> p.getPrice().multiply(BigDecimal.valueOf(i.getQuantity()))).orElse(BigDecimal.ZERO)
        ).reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userEmail(email)
                .items(cart.getItems())
                .total(total)
                .status("PAID")
                .createdAt(Instant.now())
                .build();
        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);
        return ResponseEntity.ok(order);
    }

    @Data
    public static class CheckoutRequest {
        @NotBlank
        private String address;
        @NotBlank
        private String paymentMethod; // mock
    }
}

