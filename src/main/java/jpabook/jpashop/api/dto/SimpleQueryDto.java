package jpabook.jpashop.api.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimpleQueryDto {

        private Long id;
        private String username;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleQueryDto(Order o) {
            id = o.getId();
            username = o.getMember().getUsername();
            orderDate = o.getOrderDate();
            orderStatus = o.getStatus();
            address = o.getDelivery().getAddress();
        }

    public SimpleQueryDto() {
    }

    public SimpleQueryDto(Long id, String username, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.id = id;
        this.username = username;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
    }
}

