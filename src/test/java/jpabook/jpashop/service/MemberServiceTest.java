package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback()
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;


    @Test
    public void 회원가입() throws Exception {

        Member member = new Member();
        member.setName("park");

        Long join = memberService.join(member);

        Assertions.assertThat(member.getId()).isEqualTo(join);

    }

    @Test
    public void 중복확인() throws Exception {

        Member member = new Member();
        Member member1 = new Member();

        member.setName("park1");
        member1.setName("park1");

        memberService.join(member);

        try {
            memberService.join(member1);
        } catch (IllegalStateException e) {
            return;
        }

        fail("예외 발생 필요");



    }

}