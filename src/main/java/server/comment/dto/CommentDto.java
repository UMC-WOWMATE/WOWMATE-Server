package server.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import server.comment.domain.Comment;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommentDto {
    private Long id; //comment id;
    private Long userId;//or nickname
    private Long postId;
    
    private int likeNumber;

    private String content;

    public static CommentDto createCommentDto(Comment comment) {

        return new CommentDto(
                comment.getId(),
                comment.getUser().getId(),
                comment.getPost().getId(),
                comment.getLikeNumber(),
                comment.getContent()
        );
    }
}
