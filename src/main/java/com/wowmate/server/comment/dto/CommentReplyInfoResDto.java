package com.wowmate.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentReplyInfoResDto {
    private String commentReplyContext;
    private int likeNumber;
    private String createdTime;
}