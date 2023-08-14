package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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


    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    //DTO로 감싸서 JSON을 반환해야 함. 엔티티를 그대로 노출하면 안된다.
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> collect = members.stream()
                .map(m -> new MemberDto(m.getUsername()))
                .collect(Collectors.toList());

        return new Result(collect, collect.size());

    }


    @Data
    static class MemberDto {
        private String username;

        public MemberDto(String username) {
            this.username = username;
        }
    }

    @Data

    static class Result<T> {

        private T data;

        private int count;

        public Result(T data) {
            this.data = data;
        }

        public Result(int count) {
            this.count = count;
        }

        public Result(T data, int count) {
            this.data = data;
            this.count = count;
        }
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
