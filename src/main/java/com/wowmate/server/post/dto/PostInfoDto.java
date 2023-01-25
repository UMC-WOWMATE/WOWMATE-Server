package com.wowmate.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@Getter
public class PostInfoDto {
    private String postTitle;
    private String categoryName;
    private String postTag1;
    private String postTag2;
    private String postTag3;
    private String postTag4;
    private String postTag5;
    private int postLikeNumber;
    private Long schoolId; //user와 school 매핑안되어있음..???탁균이형한테 물어보기

    private String createdBy;

}
