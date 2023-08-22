package jpabook.jpashop.repository.member;

import jpabook.jpashop.domain.Member;

public interface MemberRepositoryCustom {

     Member findMemberByName(String username);


}
