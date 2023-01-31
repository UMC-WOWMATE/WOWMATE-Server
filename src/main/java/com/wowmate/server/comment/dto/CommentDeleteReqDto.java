package com.wowmate.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDeleteReqDto {
    private Long userId;
    private Long commentId;
}