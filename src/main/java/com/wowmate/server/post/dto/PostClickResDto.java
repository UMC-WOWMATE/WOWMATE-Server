package com.wowmate.server.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostClickResDto {
    private Boolean isPostUser;
    private Long postId;
    private String postTitle;
    private String categoryName;
    private String postTag1;
    private String postTag2;
    private String postTag3;
    private String postTag4;
    private String postTag5;
    private int postLikeNumber;
    private String postContext;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd' 'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
}