package com.grocery.backend.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @GetMapping
    public ResponseEntity<?> getCart(Authentication auth) {
        String email = (String) auth.getPrincipal();
        Cart cart = cartRepository.findByUserEmail(email).orElseGet(() -> {
            Cart c = Cart.builder().userEmail(email).build();
            return cartRepository.save(c);
        });
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(Authentication auth, @RequestBody @Valid ModifyItemRequest req) {
        String email = (String) auth.getPrincipal();
        Cart cart = cartRepository.findByUserEmail(email).orElseGet(() -> cartRepository.save(Cart.builder().userEmail(email).build()));
        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(req.getProductId()))
                .findFirst()
                .orElseGet(() -> {
                    CartItem ci = CartItem.builder().productId(req.getProductId()).quantity(0).build();
                    cart.getItems().add(ci);
                    return ci;
                });
        item.setQuantity(item.getQuantity() + req.getQuantity());
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> removeFromCart(Authentication auth, @RequestBody @Valid ModifyItemRequest req) {
        String email = (String) auth.getPrincipal();
        Cart cart = cartRepository.findByUserEmail(email).orElseGet(() -> cartRepository.save(Cart.builder().userEmail(email).build()));
        cart.getItems().removeIf(i -> i.getProductId().equals(req.getProductId()));
        cartRepository.save(cart);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<?> clearCart(Authentication auth) {
        String email = (String) auth.getPrincipal();
        Cart cart = cartRepository.findByUserEmail(email).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ModifyItemRequest {
        @NotBlank
        private String productId;
        @Min(1)
        private int quantity;
    }
}

