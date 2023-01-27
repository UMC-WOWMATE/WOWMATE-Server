package com.wowmate.server.post.controller;

import com.wowmate.server.comment.dto.CommentDto;
import com.wowmate.server.post.domain.Category;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.repository.CategoryRepository;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.dto.PostClickDto;
import com.wowmate.server.post.service.PostService;
import com.wowmate.server.post.dto.PostInfoDto;
import com.wowmate.server.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

import static com.wowmate.server.response.ResponseStatus.*;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 전체 조회
    @GetMapping("/posts")
    public Response<List<PostInfoDto>, Object> getAllPostList(){
        List<PostInfoDto> postInfoDtoList;
        try {
            postInfoDtoList = postService.getAllPostList();    //Service에 구현한 함수 사용해서 정보 가져오기
            return new Response<>(postInfoDtoList);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }


    }

    @GetMapping("/posts/search/{postTitle}")
    public Response<List<PostInfoDto>, Object> getPostListByTitle(@PathVariable String postTitle) {
        List<PostInfoDto> postInfoDtoList;
        try {
            postInfoDtoList = postService.getPostListByTitle(postTitle);    //Service에 구현한 함수 사용해서 정보 가져오기
            if(postInfoDtoList.isEmpty())
                throw new BaseException(NO_RELATED_POST);
            return new Response<>(postInfoDtoList);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }

    }

    @GetMapping("/posts/{postId}")
    public Response<PostClickDto,List<CommentDto>> getPostClick(@PathVariable Long postId) {
        PostClickDto postClickDto;
        List<CommentDto> commentDtoList;
        try {
            postClickDto = postService.getPostClick(postId);
            commentDtoList = postService.getCommentList(postId);
            return new Response<>(postClickDto,commentDtoList);
        }
        catch(BaseException e){
            return new Response<>(e.getResponseStatus());
        }
    }

}

