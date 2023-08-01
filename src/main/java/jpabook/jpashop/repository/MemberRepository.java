package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> , MemberRepositoryCustom{

    List<Member> findMembersByUsername(String username);

    Optional<Member> findByUsername(String userName);



}
