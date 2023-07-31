package jpabook.jpashop.controller.dto;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderDTO {


    private Long memberId;

    private int count;

    @NotEmpty(message = "상품이 선택되어야 합니다")
    private List<Long> itemIds;


    List<Member> memberList;

    List<Item> itemList;


}
