package jpabook.jpashop.repository;

import com.querydsl.core.Query;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QOrder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class OrderRepository {

    @PersistenceContext
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public OrderRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }


    public void save(Order order) {
        em.persist(order);

    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    public List<Order> findAll(OrderSearch orderSearch) {

        QMember member = QMember.member;
        QOrder order = QOrder.order;
        BooleanExpression predicate = order.status.eq(orderSearch.getOrderStatus());

        if (orderSearch.getMemberName() != null && !orderSearch.getMemberName().isEmpty()) {
            predicate = predicate.and(order.member.name.eq(orderSearch.getMemberName()));
        }

        return queryFactory
                .selectFrom(order)
                .join(order.member, member)
                .where(predicate)
                .fetch();

    }


}
