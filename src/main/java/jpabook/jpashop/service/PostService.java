package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.member.MemberRepository;
import jpabook.jpashop.repository.post.PostListDTO;
import jpabook.jpashop.repository.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PostService {


    private final PostRepository postRepository;
    private final MemberRepository memberRepository;


    public PostService(PostRepository postRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }


    public void post(Long memberId, String title, String content) {

        Member findMember = memberRepository.findById(memberId).get();
        Post post = new Post();
        post.setMembers(findMember);
        post.setPostDate(LocalDateTime.now());
        post.setTitle(title);
        post.setContent(content);

        postRepository.save(post);

    }


    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public List<PostListDTO> findAllListDTO() {
        return postRepository.findAllListDTO();
    }

    public Post findById(Long id) {
        return postRepository.findById(id).get();
    }






}
