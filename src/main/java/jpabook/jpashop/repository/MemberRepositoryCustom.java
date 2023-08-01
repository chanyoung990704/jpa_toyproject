package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;

public interface MemberRepositoryCustom {

     Member findMemberByName(String username);


}
