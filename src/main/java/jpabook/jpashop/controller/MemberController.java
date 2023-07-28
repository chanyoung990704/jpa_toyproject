package jpabook.jpashop.controller;

import jpabook.jpashop.controller.dto.MemberDTO;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;


    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @GetMapping("/members/new")
    public String createForm(Model model) {

        model.addAttribute("memberDTO", new MemberDTO());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String saveForm(@Valid MemberDTO memberDTO, BindingResult result) {

        if (result.hasErrors()) {
            return "/members/createMemberForm";
        }


        Address address = new Address(memberDTO.getCity(), memberDTO.getStreet(), memberDTO.getZipcode());
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";

    }

    @GetMapping("/members")
    public String list(Model model) {
        model.addAttribute("memberList", memberService.findMembers());
        return "members/memberList";
    }
}
