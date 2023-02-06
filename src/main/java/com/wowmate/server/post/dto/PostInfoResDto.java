package com.wowmate.server.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PostInfoResDto {
    private Long postId;
    private String postTitle;
    private String categoryName;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private int postLikeNumber;
    private String schoolName;

    private String postMember;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

}