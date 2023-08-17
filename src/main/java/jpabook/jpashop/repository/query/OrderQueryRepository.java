package jpabook.jpashop.repository.query;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.QDelivery;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import jpabook.jpashop.domain.QOrderItem;
import jpabook.jpashop.domain.item.QItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public OrderQueryRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public List<OrderQueryDto> findOrderQueryDtos() {

        List<OrderQueryDto> order = findOrder();
        order
                .forEach(o -> {
                    List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
                    o.setOrderItems(orderItems);
                });


        return order;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        QOrderItem orderItem = QOrderItem.orderItem;
        QItem item = QItem.item;
        QOrder order = QOrder.order;

        JPAQuery<OrderItemQueryDto> query = queryFactory.select(new QOrderItemQueryDto(orderItem.order.id, orderItem.item.name, orderItem.orderPrice, orderItem.count))
                .from(orderItem)
                .join(orderItem.order, order)
                .join(orderItem.item, item)
                .where(order.id.eq(orderId));


        return  query.fetch();

    }


    public List<OrderQueryDto> findOrder() {
        QMember member = QMember.member;
        QDelivery delivery = QDelivery.delivery;
        QOrder order = QOrder.order;

        JPAQuery<OrderQueryDto> query = queryFactory.select(new QOrderQueryDto(order.id, order.member.username, order.orderDate, order.status, order.delivery.address))
                .from(order)
                .join(order.member, member)
                .join(order.delivery, delivery);

        return query.fetch();

    }


    public List<OrderQueryDto> findAllByDto_opt() {

        List<OrderQueryDto> orders = findOrder();
        List<Long> result = orders.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        QOrderItem orderItem = QOrderItem.orderItem;
        QItem item = QItem.item;
        QOrder order = QOrder.order;

        JPAQuery<OrderItemQueryDto> query = queryFactory.select(new QOrderItemQueryDto(orderItem.order.id, orderItem.item.name, orderItem.orderPrice, orderItem.count))
                .from(orderItem)
                .join(orderItem.order, order)
                .join(orderItem.item, item)
                .where(orderItem.order.id.in(result));

        Map<Long, List<OrderItemQueryDto>> orderItemMap = query.stream()
                .collect(Collectors.groupingBy(oi -> oi.getOrderId()));


        orders.stream()
                .forEach(orderQueryDto -> orderQueryDto.setOrderItems(orderItemMap.get(orderQueryDto.getOrderId())));


        return orders;


    }

    public List<OrderFlatDto> findAllByDto_flat() {

        QOrder order = QOrder.order;
        QMember member = QMember.member;
        QDelivery delivery = QDelivery.delivery;
        QItem item = QItem.item;
        QOrderItem orderItem = QOrderItem.orderItem;

        JPAQuery<OrderFlatDto> query = queryFactory.select(new QOrderFlatDto(order.id, order.member.username, order.orderDate, order.status,
                        order.delivery.address, orderItem.item.name, orderItem.orderPrice, orderItem.count))
                .from(order)
                .join(order.member, member)
                .join(order.delivery, delivery)
                .join(order.orderItems, orderItem)
                .join(orderItem.item, item);


        return query.fetch();

    }
}
