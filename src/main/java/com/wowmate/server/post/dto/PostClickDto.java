package com.wowmate.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostClickDto {
    private String postTitle;
    private String categoryName;
    private String postTag1;
    private String postTag2;
    private String postTag3;
    private String postTag4;
    private String postTag5;
    private int postLikeNumber;
    private String postContext;

    private String createdBy;
}