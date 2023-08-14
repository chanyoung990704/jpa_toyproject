package jpabook.jpashop.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.api.dto.SimpleQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.QDelivery;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Order> findAllOrderSearch(OrderSearch orderSearch) {

        QMember member = QMember.member;
        QOrder order = QOrder.order;

        JPAQuery<Order> query = queryFactory.selectFrom(order)
                .join(order.member, member);

        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query.where(member.username.containsIgnoreCase(orderSearch.getMemberName()));
        }

        if (orderSearch.getOrderStatus() != null) {
            query.where(order.status.eq(orderSearch.getOrderStatus()));
        }

        return query.fetch();





    }

    @Override
    public List<Order> findAllWithMemberDelivery() {

        QMember member = QMember.member;
        QOrder order = QOrder.order;
        QDelivery delivery = QDelivery.delivery;

        JPAQuery<Order> query = queryFactory.selectFrom(order)
                .join(order.member, member).fetchJoin()
                .join(order.delivery, delivery).fetchJoin();


        return query.fetch();

    }

    @Override
    public List<SimpleQueryDto> findOrderDtos() {
        QMember member = QMember.member;
        QOrder order = QOrder.order;
        QDelivery delivery = QDelivery.delivery;

        JPAQuery<SimpleQueryDto> query = queryFactory.select(Projections.bean(SimpleQueryDto.class, order.id, order.member.username, order.orderDate,
                order.status, order.delivery.address))
                .from(order)
                .join(order.member, member)
                .join(order.delivery, delivery);


        return query.fetch();

    }


}
