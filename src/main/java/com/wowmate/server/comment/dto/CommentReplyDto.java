package com.wowmate.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentReplyDto {
    private String commentContext;
    private int likeNumber;
    private String createdTime;
}
