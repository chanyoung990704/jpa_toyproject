package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;


    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }


    public void saveItem(Item item) {
        itemRepository.save(item);

    }


    @Transactional(readOnly = true)
    public List<Item> findItems() {
        return itemRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Item findOne(Long id) {
        return itemRepository.findOne(id);
    }


    public void updateBook(Long itemId, String name, int price, int stockQuantity, String author, String isbn) {
        Book one = (Book) itemRepository.findOne(itemId);
        one.setName(name);
        one.setPrice(price);
        one.setStockQuantity(stockQuantity);
        one.setAuthor(author);
        one.setIsbn(isbn);


    }



}
