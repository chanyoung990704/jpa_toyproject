package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    private String content;

    // 연관관계
    public void set_Member(Member member) {
        this.member = member;
        member.getComments().add(this);
    }

    public void set_Post(Post post) {
        this.post = post;
        post.getComments().add(this);
    }


}
