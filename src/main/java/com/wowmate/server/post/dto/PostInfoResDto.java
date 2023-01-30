package com.wowmate.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostInfoResDto {
    private String postTitle;
    private String categoryName;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private int postLikeNumber;
    private String schoolName;

    private String createdBy;

}