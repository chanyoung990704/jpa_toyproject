package jpabook.jpashop.repository.member;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

    List<Member> findMembersByUsername(String username);

    Optional<Member> findByUsername(String userName);



}
