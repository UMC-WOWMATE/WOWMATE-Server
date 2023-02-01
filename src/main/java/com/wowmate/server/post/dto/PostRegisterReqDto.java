package com.wowmate.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PostRegisterReqDto {

    private String postTitle;
    private String categoryName;
    private int postMember;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;
    private String postContext;

}