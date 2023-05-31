package com.juani.ShoppingCart.Service;

import com.juani.ShoppingCart.Model.Cart;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart() {
        // Create a new cart entity
        Cart cart = new Cart();
        // Save the cart in the database
        return cartRepository.save(cart);
    }

    public Cart getCartById(Long id) {
        return cartRepository.getReferenceById(id);
    }

    public void updateCart(Cart cart) {
        cartRepository.save(cart);
    }
}
