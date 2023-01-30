package com.wowmate.server.comment.dto;

import lombok.Getter;

@Getter
public class CommentRegisterReqDto {

    private Long userId;
    private Long postId;
    private String commentContext;
}