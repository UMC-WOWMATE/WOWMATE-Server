package server.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import server.comment.domain.Comment;
import server.comment.dto.CommentDto;
import server.comment.repository.CommentRepository;
import server.post.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    public List<CommentDto> showComments(Long postId) {

        List<Comment> comments = commentRepository.findByPostId(postId);

        List<CommentDto> dtos = new ArrayList<CommentDto>();
        for (int i = 0; i < comments.size(); i++) {
            Comment comment  = comments.get(i);
            CommentDto dto = CommentDto.createCommentDto(comment);
            dtos.add(dto);
        }
        return dtos;
    }


}
