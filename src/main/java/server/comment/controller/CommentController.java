package server.comment.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import server.comment.dto.CommentDto;
import server.comment.service.CommentService;

import java.util.List;

@RestController
public class CommentController {
    private CommentService commentService;

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentDto>> showComments(@PathVariable Long postId){
        List<CommentDto> dtos = commentService.showComments(postId);
        return ResponseEntity.ok().body(dtos);
    }


}
