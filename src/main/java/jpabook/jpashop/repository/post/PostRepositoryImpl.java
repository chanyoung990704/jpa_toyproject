package jpabook.jpashop.repository.post;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpabook.jpashop.domain.QMember;
import jpabook.jpashop.domain.QPost;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public PostRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public List<PostListDTO> findAllListDTO() {
        QPost post =  QPost.post;
        QMember member = QMember.member;

        JPAQuery<PostListDTO> query = queryFactory.select(new QPostListDTO(post.id, post.member.username, post.title, post.postDate))
                .from(post)
                .join(post.member, member);

        return query.fetch();


    }
}
