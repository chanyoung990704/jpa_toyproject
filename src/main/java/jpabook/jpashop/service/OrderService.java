package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    public OrderService(OrderRepository orderRepository, MemberRepository memberRepository, ItemRepository itemRepository) {
        this.orderRepository = orderRepository;
        this.memberRepository = memberRepository;
        this.itemRepository = itemRepository;
    }


    public Long order(Long memberId, int cnt, Long... itemIds) {
        Member findMember = memberRepository.findOne(memberId);
        Delivery delivery = new Delivery();
        delivery.setAddress(findMember.getAddress());

        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for (Long itemId : itemIds) {
            Item findItem = itemRepository.findOne(itemId);
            orderItems.add(OrderItem.createOrderItem(findItem, findItem.getPrice(), cnt));

        }

        Order order = Order.createOrder(findMember, delivery, orderItems.toArray(new OrderItem[0]));

        orderRepository.save(order);

        return order.getId();


    }


    public void cancel(Long orderId) {

        Order order = orderRepository.findOne(orderId);
        order.cancel();

    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }









}
