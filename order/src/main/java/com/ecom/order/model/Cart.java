package com.ecom.order.model;

import com.ecom.order.exception.InsufficientQuantityException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();

    public Cart(Long userId) {
        this.userId = userId;
    }

    public void addItem(Long productId, int quantity, BigDecimal price, int availableStock) {

        CartItem item = findItem(productId);

        if (item != null) {
            int newQty = item.getQuantity() + quantity;
            if (newQty > availableStock) {
                throw new InsufficientQuantityException("Insufficient Quantity");
            }
            item.updateQuantity(newQty);
        } else {
            if (quantity > availableStock) {
                throw new InsufficientQuantityException("Insufficient Quantity");
            }
            items.add(new CartItem(this, productId, quantity, price));
        }

        recalculateTotal();
    }

    public void removeItem(Long productId) {
        CartItem item = findItem(productId);
        if (item == null) {
            throw new IllegalArgumentException("Item not found in cart");
        }
        items.remove(item);
        recalculateTotal();
    }

    public void clear() {
        items.clear();
        totalAmount = BigDecimal.ZERO;
    }

    private CartItem findItem(Long productId) {
        return items.stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private void recalculateTotal() {
        totalAmount = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
