package jpabook.jpashop.repository.member;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final EntityManager em;

    public MemberRepositoryImpl(EntityManager em) {
        this.em = em;
    }



    @Override
    public Member findMemberByName(String username) {
        return em.createQuery("select m from Member m where m.username = :name", Member.class)
                .setParameter("name", username)
                .getSingleResult();
    }
}
