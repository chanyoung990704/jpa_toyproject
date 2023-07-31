package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long join(Member member) {

        validateDuplicateMember(member);

        memberRepository.save(member);

        return member.getId();

    }


    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findOne(Long id) {
        return memberRepository.findById(id).get();
    }



    private void validateDuplicateMember(Member member) {

        String curName = member.getName();
        List<Member> nameList = memberRepository.findMembersByName(curName);

        if (!nameList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }





    }


}
