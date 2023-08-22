package jpabook.jpashop.repository.order;

import jpabook.jpashop.api.dto.SimpleQueryDto;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderSearch;

import java.util.List;

public interface OrderRepositoryCustom {


    List<Order> findAllOrderSearch(OrderSearch orderSearch);

    List<Order> findAllWithMemberDelivery();

    List<SimpleQueryDto> findOrderDtos();

    List<Order> findAllWithItem();

    List<Order> findAllWithMemberDelivery(int offset, int limit);
}
