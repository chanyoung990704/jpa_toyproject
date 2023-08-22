package jpabook.jpashop.api;

import jpabook.jpashop.api.dto.SimpleQueryDto;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


//DTO로 반환해야 API조회 가능해짐
//아마 멤버에서 비밀번호를 조회할 때 조회가 안되는 문제 발생??
@RestController
public class OrderSimpleApiController {


    /*
    xToOne 괸계에서 성능최적화
     */

    private final OrderRepository orderRepository;


    public OrderSimpleApiController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> allOrderSearch = orderRepository.findAllOrderSearch(new OrderSearch());
        return allOrderSearch;

    }

    //DTO 반환
    // LAZY 로딩 1 + N 문제 발생
    // 연관된 테이블 조회하기 위한 쿼리 N번 발생
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {

        List<Order> orders = orderRepository.findAllOrderSearch(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    // 1 + N 문제 해결 방안
    // fetch join
    // 엔티티와 연관된 엔티티 한 번에 조회 (쿼리 1번으로)
    // *****4번 보다 비교적 사용 추천*******
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return collect;

    }

    // DTO를 활용해 쿼리르 작성해 select 문이 짧아지는 장점
    @GetMapping("/api/v4/simple-orders")
    public List<SimpleQueryDto> ordersV4() {

        return orderRepository.findOrderDtos();

    }



    @Data
    static class SimpleOrderDto{
        private Long id;
        private String username;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order o) {
            id = o.getId();
            username = o.getMember().getUsername();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
        }
    }
}
