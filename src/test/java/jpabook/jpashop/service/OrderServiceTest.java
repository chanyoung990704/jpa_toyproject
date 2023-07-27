package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    @Test
    public void 상품주문() {

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);


        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCnt = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        Order one = orderRepository.findOne(orderId);

        Assertions.assertEquals(OrderStatus.ORDER, one.getStatus(), "실제 주문은 ORDER");
        Assertions.assertEquals(1, one.getOrderItems().size(), "주문 상품 수량일치");
        Assertions.assertEquals(10000 * 2, one.getTotalPrice(), "주문 가격은 가격 * 수량");
        Assertions.assertEquals(8, book.getStockQuantity(), "주문하면 재고 줄어듦");



    }


    @Test
    public void 주문취소() {

        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);


        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCnt = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        Order one = orderRepository.findOne(orderId);

        one.cancel();

        Assertions.assertEquals(10, book.getStockQuantity(), "주문 취소시 상품 수량 온전해야");


    }


    @Test
    public void 상품주문_재고수량초과() {


        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);


        Book book = new Book();
        book.setName("시골 JPA");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        int orderCnt = 11;

        try {
            Long orderId = orderService.order(member.getId(), book.getId(), orderCnt);

        } catch (NotEnoughStockException e) {
            return;
        }

        fail("예외 발생해야함");




    }






}