package jpabook.jpashop.controller;

import jpabook.jpashop.controller.dto.BookDTO;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ItemController {

    private final ItemService itemService;


    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @GetMapping("/items/new")
    public String createForm(Model model) {

        model.addAttribute("itemDTO", new BookDTO());
        return "items/createItemForm";

    }


    @PostMapping("/items/new")
    public String saveForm(BookDTO bookDTO) {

        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setPrice(bookDTO.getPrice());
        book.setStockQuantity(bookDTO.getStockQuantity());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());

        itemService.saveItem(book);

        return "redirect:/";


    }


    @GetMapping("/items")
    public String list(Model model) {
        model.addAttribute("items", itemService.findItems());
        return "items/itemList";
    }


    @GetMapping("/items/{id}/edit")
    public String edit(Model model, @PathVariable(name = "id") Long id) {


        Book item = (Book) itemService.findOne(id);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(item.getId());
        bookDTO.setName(item.getName());
        bookDTO.setIsbn(item.getIsbn());
        bookDTO.setPrice(item.getPrice());
        bookDTO.setAuthor(item.getAuthor());
        bookDTO.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", bookDTO);
        return "items/updateItemForm";

    }


    @PostMapping("/items/{id}/edit")
    public String update(@ModelAttribute("form") BookDTO bookDTO, @PathVariable Long id) {

        itemService.updateBook(id, bookDTO.getName(), bookDTO.getPrice(), bookDTO.getStockQuantity(), bookDTO.getAuthor()
                , bookDTO.getIsbn());
        return "redirect:/items";
    }
}
