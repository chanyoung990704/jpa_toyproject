package jpabook.jpashop.controller;

import jpabook.jpashop.controller.dto.OrderDTO;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    public OrderController(OrderService orderService, MemberService memberService, ItemService itemService) {
        this.orderService = orderService;
        this.memberService = memberService;
        this.itemService = itemService;
    }


    @GetMapping("/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setMemberList(members);
        orderDTO.setItemList(items);


        model.addAttribute("orderDto", orderDTO);

        return "order/orderForm";

    }


    @PostMapping("/order")
    public String order(@Valid OrderDTO orderDto, BindingResult result) {

        if (result.hasErrors()) {
            return "redirect:/order";
        }


        orderService.order(orderDto.getMemberId(), orderDto.getCount(), orderDto.getItemIds().toArray(new Long[0]));
        return "redirect:/orders";


    }


    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {

        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);

        return "order/orderList";


    }


    @PostMapping("/orders/{id}/cancel")
    public String orderCancel(@PathVariable(name = "id") Long orderId)
    {
        orderService.cancel(orderId);
        return "redirect:/";
    }


}
