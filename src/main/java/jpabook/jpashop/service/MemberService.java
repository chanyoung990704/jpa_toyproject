package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MemberService  {

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


    @Transactional(readOnly = true)
    public Member findByName(String name) {

        return memberRepository.findMemberByName(name);


    }


    public Optional<Member> findOne(String userId) {
        return memberRepository.findByUsername(userId);
    }



    private void validateDuplicateMember(Member member) {

        String curName = member.getUsername();
        List<Member> nameList = memberRepository.findMembersByUsername(curName);

        if (!nameList.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원");
        }





    }


    public void update(Long id, String username) {
        Optional<Member> byId = memberRepository.findById(id);
        byId.get().setUsername(username);
    }


}
