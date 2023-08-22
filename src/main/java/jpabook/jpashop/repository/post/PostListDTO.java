package jpabook.jpashop.repository.post;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostListDTO {

    private Long postID;
    private String name;
    private String title;
    private LocalDateTime date;


    @QueryProjection
    public PostListDTO(Long postID, String name, String title, LocalDateTime date) {
        this.postID = postID;
        this.name = name;
        this.title = title;
        this.date = date;
    }
}
