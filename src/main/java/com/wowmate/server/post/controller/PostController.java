package com.wowmate.server.post.controller;

import com.wowmate.server.comment.dto.*;
import com.wowmate.server.post.dto.*;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.service.PostService;
import com.wowmate.server.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.wowmate.server.response.ResponseStatus.*;


@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //게시글 전체 조회
    @GetMapping("/posts")
    public Response<List<PostInfoResDto>, Object> getAllPostList(){
        List<PostInfoResDto> postInfoResDtoList;
        try {
            postInfoResDtoList = postService.getAllPostList();    //Service에 구현한 함수 사용해서 정보 가져오기
            return new Response<>(postInfoResDtoList);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }


    }

    @GetMapping("/posts/search")
    public Response<List<PostInfoResDto>, Object> getPostListByTitle(@RequestBody PostTitleDto postTitleDto) {
        List<PostInfoResDto> postInfoResDtoList;
        try {
            postInfoResDtoList = postService.getPostListByTitle(postTitleDto.getPostTitle());    //Service에 구현한 함수 사용해서 정보 가져오기
            if(postInfoResDtoList.isEmpty())
                throw new BaseException(NO_RELATED_POST);
            return new Response<>(postInfoResDtoList);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }

    }

    @GetMapping("/posts/info")
    public Response<PostClickResDto,List<CommentInfoResDto>> getPostClick(@RequestBody PostIdDto postIdDto) {
        PostClickResDto postClickResDto;
        List<CommentInfoResDto> commentInfoResDtoList;
        try {
            postClickResDto = postService.getPostClick(postIdDto.getPostId());
            commentInfoResDtoList = postService.getCommentList(postIdDto.getPostId());
            return new Response<>(postClickResDto, commentInfoResDtoList);
        }
        catch(BaseException e){
            return new Response<>(e.getResponseStatus());
        }
    }

    @GetMapping("/posts/category")
    public Response<List<PostInfoResDto>, Object> getAllPostListByCategory(@RequestBody CategoryNameDto categoryNameDto) {
        List<PostInfoResDto> postInfoResDtoList;
        try {
            postInfoResDtoList = postService.getAllPostListByCategory(categoryNameDto.getCategoryName());
            return new Response<>(postInfoResDtoList);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts")
    public Response<PostRegisterResDto, Object> registerPost(@RequestBody PostRegisterReqDto postRegisterReqDto) {
        try {
            if(postRegisterReqDto.getPostTitle()==null){
                throw new BaseException(NO_TITLE);
            }
            if(postRegisterReqDto.getCategoryName()==null){
                throw new BaseException(NO_CATEGORY);
            }
            if(postRegisterReqDto.getPostContext()==null){
                throw new BaseException(NO_CONTEXT);
            }
            PostRegisterResDto postRegisterResDto = postService.registerPost(postRegisterReqDto);
            return new Response<>(postRegisterResDto);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts/comments")
    public Response<CommentRegisterResDto,Object> registerComment(@RequestBody CommentRegisterReqDto commentRegisterReqDto){

        try {

            //각종 예외처리

            CommentRegisterResDto commentRegisterResDto = postService.registerComment(commentRegisterReqDto);
            return new Response<>(commentRegisterResDto);
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        }

    }

    @PostMapping("/posts/commentReplies")
    public Response<CommentReplyRegisterResDto,Object> registerCommentReply(@RequestBody CommentReplyRegisterReqDto commentReplyRegisterReqDto) {
        try{

            CommentReplyRegisterResDto commentReplyRegisterResDto =postService.registerCommentReply(commentReplyRegisterReqDto);
            return new Response<>(commentReplyRegisterResDto);
        }
        catch (BaseException e){
            return new Response<>(e.getResponseStatus());
        }


    }
}