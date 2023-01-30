package com.wowmate.server.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@AllArgsConstructor
@Getter
public class CommentInfoResDto {
    private String commentContext;
    private int likeNumber;
    private String createdDate;
    List<CommentReplyInfoResDto> commentReplyDtoList;
}