package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderFlatDto;
import jpabook.jpashop.repository.query.OrderQueryDto;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderApiController {


    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    public OrderApiController(OrderRepository orderRepository, OrderQueryRepository orderQueryRepository) {
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
    }


    //Lazy로딩이기 떄문에 get 강제 초기화를 해줘야 함
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllOrderSearch(new OrderSearch());
        for (Order order : orders) {
            order.getMember().getUsername();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }
        }


        return orders;

    }


    @GetMapping("/api/v2/orders")
    public List<OrdersDto> ordersV2() {
        List<Order> orders = orderRepository.findAllOrderSearch(new OrderSearch());
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());

        return collect;


    }


    //컬렉션 페치 조인
    // 연관된 것들 fetchjoin 해준다. -> N번 만큼의 중복 발생 가능성
    //                     => 쿼리 문에서 distinct를 사용해 해결 가능
    // 단점 : 페이징 불가능해짐
    @GetMapping("/api/v3/orders")
    public List<OrdersDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    //컬렉션 페치 조인 해결법
    // ToOne관계는 페치 조인 사용
    // ToMany관계는 페치 조인 사용 x
    // ToMany관계를 거쳐 ToOne 관계를 갖는 경우에도 페치 조인 사용 x
    // batch_size를 이용해 한번에 DB 조회해서 사용
    @GetMapping("/api/v3.1/orders")
    public List<OrdersDto> ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                         @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());

        return collect;
    }


    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {

        return orderQueryRepository.findOrderQueryDtos();
    }


    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_opt();
    }

    @GetMapping("/api/v6/orders")
    public List<OrderFlatDto> ordersV6() {
        return orderQueryRepository.findAllByDto_flat();
    }


    @Data
    static class OrdersDto {


        public OrdersDto(Order o) {
            this.orderId = o.getId();
            this.orderDate = o.getOrderDate();
            this.orderStatus = o.getStatus();
            this.name = o.getMember().getUsername();
            this.address = o.getDelivery().getAddress();
            this.orderItems = o.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());


        }

        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        private List<OrderItemDto> orderItems;

    }

    @Data
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();

        }
    }

}
