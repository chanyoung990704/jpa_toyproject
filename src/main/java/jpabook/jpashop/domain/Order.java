package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "orders")
public class Order {

    protected Order() {

    }

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
//////////////////////////기본 set/////////////////////////////////////////








//////////////////////연관관계 set//////////////////////////////////
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);

    }

    public void setOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);

    }


    public void setDelivery(Delivery delivery) {
        delivery.setOrder(this);
        this.delivery = delivery;
    }

/////////////////////////////////////////////////////////////////////////////////////////
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.setOrderItem(orderItem);
        }

        order.status = OrderStatus.ORDER;
        order.orderDate = LocalDateTime.now();

        return order;

        
    }

///////////////////////////////////////////////////////////////////////////////////////


    public void cancel() {

        if (this.delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송이 완료된 상품입니다");
        }

        this.status = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {

            orderItem.cancel();

        }


    }


    public int getTotalPrice() {

        int totalPrice = 0;

        for (OrderItem orderItem : orderItems) {

            totalPrice += orderItem.getTotalPrice();

        }

        return totalPrice;

    }




}
