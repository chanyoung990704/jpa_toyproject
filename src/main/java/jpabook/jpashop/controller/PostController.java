package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Comment;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.service.CommentService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.PostService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;

    public PostController(PostService postService, MemberService memberService, CommentService commentService) {
        this.postService = postService;
        this.memberService = memberService;
        this.commentService = commentService;
    }


    @GetMapping("/createPost")
    public String createPost() {
        return "post/createPost";
    }


    @PostMapping("/createPost")
    public String savePost(Authentication authentication, @RequestParam("title") String title, @RequestParam("content") String content) {
        Member member = memberService.findByName(authentication.getName());

        Post post = new Post();
        post.set_Member(member);
        post.setPostDate(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);

        postService.save(post);

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
        List<Comment> comments = commentService.getCommentsByPostId(postID);

        model.addAttribute("post", findPost);
        model.addAttribute("comments", comments);
        return "post/post";

    }


    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId, @RequestParam String commentText, Authentication authentication) {
        Post post = postService.findById(postId);
        Member member = memberService.findByName(authentication.getName());

        Comment comment = new Comment();
        comment.setContent(commentText);
        comment.set_Post(post);
        comment.set_Member(member);


        commentService.addComment(comment);

        return "redirect:/posts/" + postId;
    }

}
