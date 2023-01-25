package com.wowmate.server.post.controller;

import com.wowmate.server.config.BaseException;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.post.service.PostService;
import com.wowmate.server.post.dto.PostInfoDto;
import com.wowmate.server.config.Response;
import com.wowmate.server.config.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static com.wowmate.server.config.ResponseStatus.NO_RELATED_POST;
import static com.wowmate.server.config.ResponseStatus.SUCCESS_NO_POST;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 전체 조회
    //Object는 선언할 때만 필요한 NULL 값
    @GetMapping("/posts")
    public Response<List<PostInfoDto>, Object> getPostList(){
        List<PostInfoDto> postInfoDtoList;
        postInfoDtoList = postService.getPostList();    //Service에 구현한 함수 사용해서 정보 가져오기
        if(postInfoDtoList.isEmpty())
            return new Response<>(SUCCESS_NO_POST);         //실패 했을 때의 객체가 생성됨
        return new Response<>(postInfoDtoList);     //성공 했을 때의 객체가 생성됨
    }

    @GetMapping("/posts/search")
    public Response<List<PostInfoDto>, Object> getPostListByTitle(@PathVariable String postTitle) {
        List<PostInfoDto> postInfoDtoList;
        postInfoDtoList = postService.getPostListByTitle(postTitle);
        if(postInfoDtoList.isEmpty())
            return new Response<>(NO_RELATED_POST);     //실패시 객체 생성
        return new Response<>(postInfoDtoList);         //성공 했을 때의 객체가 생성됨
    }

}

