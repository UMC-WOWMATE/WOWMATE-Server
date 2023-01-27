package com.wowmate.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class CommentDto {
    private String commentContent;
    private int likeNumber;
    private String createdDate;
    List<CommentReplyDto> commentReplyDtoList;
}
