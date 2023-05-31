package com.juani.ShoppingCart.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Cart {
    @OneToMany(mappedBy = "cart")
    private final List<Item> items;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }

    public void updateItemQuantity(int itemId, int quantity) {
        for (Item item : items) {
            if (item.getId() == (itemId)) {
                item.setQuantity(quantity);
                break;
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void removeItem(int itemId) {
        items.removeIf(item -> item.getId() == (itemId));
    }

    public double getTotalAmount() {
        return items.stream()
                .mapToDouble(Item::getTotalPrice)
                .sum();
    }
}
