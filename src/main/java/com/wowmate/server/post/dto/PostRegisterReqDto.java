package com.wowmate.server.post.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
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

    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
}