package com.juani.ShoppingCart.Service;

import com.juani.ShoppingCart.Model.Item;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ProductService {
    private final WebClient webClient;

    public ProductService() {
        this.webClient = WebClient.create();
    }
    public Item getProduct(int id) {
        return webClient.get()
                .uri("https://fakestoreapi.com/products/" + id)
                .retrieve()
                .bodyToMono(Item.class)
                .block();
    }
}
