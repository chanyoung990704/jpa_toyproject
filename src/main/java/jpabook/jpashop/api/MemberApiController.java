package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController // API 사용시 requestBody 구조 사용가능
public class MemberApiController {


    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }


    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member){

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }


    // 별도 DTO만들어서 API 사용
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setUsername(request.getUsername());
        Long id = memberService.join(member);

        return new CreateMemberResponse(id);

    }

    @PutMapping("/api/v2/members/{id}") // PutMapping : 리소스 업데이트 시 사용,
    public UpdateMemberResponse updateMemberV2(@PathVariable(name = "id")Long id, @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getUsername());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getUsername());

    }


    @Data
    static class UpdateMemberRequest{
        private String username;
    }

    @Data
    static class UpdateMemberResponse{
        private Long id;
        private String username;

        public UpdateMemberResponse(Long id, String username) {
            this.id = id;
            this.username = username;
        }

    }



    @Data
    static class CreateMemberResponse{
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }


    @Data
    static class CreateMemberRequest{
        private String username;
    }

}
