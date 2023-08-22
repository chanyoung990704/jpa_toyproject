package jpabook.jpashop.repository.order;

import jpabook.jpashop.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {




}
