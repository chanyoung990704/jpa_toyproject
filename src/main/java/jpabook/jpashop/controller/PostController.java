package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.PostService;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    public PostController(PostService postService, MemberService memberService) {
        this.postService = postService;
        this.memberService = memberService;
    }


    @GetMapping("/createPost")
    public String createPost() {
        return "post/createPost";
    }


    @PostMapping("/createPost")
    public String savePost(Authentication authentication, @RequestParam("title") String title, @RequestParam("content") String content) {
        Member member = memberService.findByName(authentication.getName());
        postService.post(member.getId(), title, content);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String postList(Model model) {

        model.addAttribute("posts", postService.findAllListDTO());
        return "post/postList";
    }


    @GetMapping("/posts/{postId}")
    public String showPost(@PathVariable("postId") Long postID, Model model){

        Post findPost = postService.findById(postID);
        model.addAttribute("post", findPost);
        return "post/post";

    }

}
