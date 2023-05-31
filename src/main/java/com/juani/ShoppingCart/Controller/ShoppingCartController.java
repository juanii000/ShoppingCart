package com.juani.ShoppingCart.Controller;

import com.juani.ShoppingCart.Model.Cart;
import com.juani.ShoppingCart.Model.Item;
import com.juani.ShoppingCart.Service.CartService;
import com.juani.ShoppingCart.Service.ItemService;
import com.juani.ShoppingCart.Service.ProductService;
import com.juani.ShoppingCart.Utils.InternalServerErrorException;
import com.juani.ShoppingCart.Utils.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ProductService productService;
    private final CartService cartService;
    private final ItemService itemService;

    public ShoppingCartController(ProductService productService, CartService cartService, ItemService itemService) {
        this.productService = productService;
        this.cartService = cartService;
        this.itemService = itemService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createCart() {
        try {
            String cartId = cartService.createCart().getId().toString();
            return ResponseEntity.ok(cartId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot create cart");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam int id, @RequestParam int quantity, @RequestParam Long cartId) {

        Cart cart = cartService.getCartById(cartId);
        Item item = productService.getProduct(id);
        if (item == null) {
            throw new NotFoundException("Item not found");
        }
        item.setQuantity(quantity);
        item.setCart(cart);
        try {
            cart.addItem(item);
            itemService.save(item);
            cartService.updateCart(cart);
            return ResponseEntity.ok("Product added to the cart.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot add product to cart");
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long itemId, @RequestParam Long cartId) {
        try {
            Cart cart = cartService.getCartById(cartId);
            Item item = itemService.findById(itemId);
            cart.removeItem(item.getId());
            itemService.delete(item);
            cartService.updateCart(cart);
            return ResponseEntity.ok("Product removed from the cart.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot remove product from cart");
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(@RequestParam Long cartId) {
        try {
            cartService.getCartById(cartId).getItems().forEach(itemService::delete);
            return ResponseEntity.ok("Cart cleared.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot clear cart");
        }
    }

    @GetMapping("/{cartId}/items")
    public ResponseEntity<Page<Item>> getItemsByCartId(
            @PathVariable Long cartId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Page<Item> itemsPage = itemService.getItemsByCartId(cartId, pageNumber, pageSize);
            return ResponseEntity.ok(itemsPage);
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot get items");
        }
    }

    @GetMapping("/{cartId}/total")
    public ResponseEntity<Double> getTotalAmount(@PathVariable Long cartId) {
        try {
            return ResponseEntity.ok(cartService.getCartById(cartId).getTotalAmount());
        } catch (Exception e) {
            throw new InternalServerErrorException("Cannot get total amount");
        }
    }

}
