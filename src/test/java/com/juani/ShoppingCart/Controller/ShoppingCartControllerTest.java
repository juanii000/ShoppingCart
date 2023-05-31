package com.juani.ShoppingCart.Controller;

import com.juani.ShoppingCart.Model.Cart;
import com.juani.ShoppingCart.Model.Item;
import com.juani.ShoppingCart.Service.CartService;
import com.juani.ShoppingCart.Service.ItemService;
import com.juani.ShoppingCart.Service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShoppingCartControllerTest {
    @Mock
    private ProductService productService;

    @Mock
    private CartService cartService;

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ShoppingCartController shoppingCartController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        when(cartService.createCart()).thenReturn(cart);

        ResponseEntity<String> response = shoppingCartController.createCart();

        assertEquals(cart.getId().toString(), response.getBody());
    }

    @Test
    public void testAddToCart() {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1);
        item.setQuantity(2);
        item.setCart(cart);
        when(cartService.getCartById(1L)).thenReturn(cart);
        when(productService.getProduct(1)).thenReturn(item);

        ResponseEntity<String> response = shoppingCartController.addToCart(1, 2, 1L);

        verify(itemService, times(10)).save(item);
        verify(cartService, times(1)).updateCart(cart);
        assertEquals("Product added to the cart.", response.getBody());
    }

    @Test
    public void testRemoveFromCart() {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1);
        item.setQuantity(2);
        item.setCart(cart);
        cart.addItem(item);
        when(cartService.getCartById(1L)).thenReturn(cart);
        when(itemService.findById(1L)).thenReturn(item);

        ResponseEntity<String> response = shoppingCartController.removeFromCart(1L, 1L);

        verify(itemService, times(1)).delete(item);
        verify(cartService, times(1)).updateCart(cart);
        assertEquals("Product removed from the cart.", response.getBody());
    }

    @Test
    public void testClearCart() {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1);
        item.setQuantity(2);
        item.setCart(cart);
        cart.addItem(item);
        when(cartService.getCartById(1L)).thenReturn(cart);

        ResponseEntity<String> response = shoppingCartController.clearCart(1L);

        verify(itemService, times(1)).delete(item);
        assertEquals("Cart cleared.", response.getBody());
    }

    @Test
    public void testGetItemsByCartId() {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1);
        item.setQuantity(2);
        item.setCart(cart);
        cart.addItem(item);
        Page<Item> itemsPage = new PageImpl<>(Collections.singletonList(item));
        when(itemService.getItemsByCartId(1L, 0, 10)).thenReturn(itemsPage);

        ResponseEntity<Page<Item>> response = shoppingCartController.getItemsByCartId(1L, 0, 10);

        assertEquals(itemsPage, response.getBody());
    }

    @Test
    public void testGetTotalAmount() {
        Cart cart = new Cart();
        Item item = new Item();
        item.setId(1);
        item.setQuantity(2);
        item.setCart(cart);
        cart.addItem(item);
        when(cartService.getCartById(1L)).thenReturn(cart);

        ResponseEntity<Double> response = shoppingCartController.getTotalAmount(1L);

        assertEquals(cart.getTotalAmount(), response.getBody());
    }
}