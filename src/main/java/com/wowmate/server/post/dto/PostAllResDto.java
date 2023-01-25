package com.wowmate.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@AllArgsConstructor
public class PostAllResDto {
    private List<PostInfoDto> postInfoDtos;

    //Response 추가해야됨
}
