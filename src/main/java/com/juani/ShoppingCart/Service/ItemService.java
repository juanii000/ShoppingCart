package com.juani.ShoppingCart.Service;

import com.juani.ShoppingCart.Model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Item findById(Long id) {
        return itemRepository.getReferenceById(id);
    }

    public void save(Item item) {
        itemRepository.save(item);
    }

    public void delete(Item item) {
        itemRepository.delete(item);
    }

    public Page<Item> getItemsByCartId(Long cartId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return itemRepository.findByCartId(cartId, pageable);
    }
}
