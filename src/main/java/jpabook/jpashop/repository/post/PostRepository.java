package jpabook.jpashop.repository.post;

import jpabook.jpashop.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {



}
