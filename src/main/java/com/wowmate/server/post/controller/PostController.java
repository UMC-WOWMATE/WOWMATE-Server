package com.wowmate.server.post.controller;

import com.wowmate.server.comment.dto.*;
import com.wowmate.server.post.dto.*;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.service.PostService;
import com.wowmate.server.response.Response;
import com.wowmate.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Response<List<PostInfoResDto>, Object> getPostListByTitle(@RequestParam("title") String postTitle) {
        List<PostInfoResDto> postInfoResDtoList;
        try {
            postInfoResDtoList = postService.getPostListByTitle(postTitle);    //Service에 구현한 함수 사용해서 정보 가져오기
            if(postInfoResDtoList.isEmpty())
                throw new BaseException(NO_RELATED_POST);
            return new Response<>(postInfoResDtoList);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }

    }

    @GetMapping("/posts/{postId}")
    public Response<PostClickResDto,List<CommentInfoResDto>> getPostClick(@PathVariable Long postId) {
        PostClickResDto postClickResDto;
        List<CommentInfoResDto> commentInfoResDtoList;
        try {
            postClickResDto = postService.getPostClick(postId);
            commentInfoResDtoList = postService.getCommentList(postId);
            return new Response<>(postClickResDto, commentInfoResDtoList);
        }
        catch(BaseException e){
            return new Response<>(e.getResponseStatus());
        }
    }

    @GetMapping("/posts/category")
    public Response<List<PostInfoResDto>, Object> getAllPostListByCategory(@RequestParam("name") String categoryName) {
        List<PostInfoResDto> postInfoResDtoList;
        try {
            postInfoResDtoList = postService.getAllPostListByCategory(categoryName);
            return new Response<>(postInfoResDtoList);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts")
    public Response<PostRegisterResDto, Object> registerPost(@RequestBody PostRegisterReqDto postRegisterReqDto,@AuthenticationPrincipal User user) {

        try {

            PostRegisterResDto postRegisterResDto = postService.registerPost(postRegisterReqDto,user);
            return new Response<>(postRegisterResDto);
        }
        catch(BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts/{postId}/comments")
    public Response<CommentRegisterResDto,Object> registerComment(@PathVariable Long postId, @RequestBody CommentRegisterReqDto commentRegisterReqDto, @AuthenticationPrincipal User user){

        try {

            //각종 예외처리

            CommentRegisterResDto commentRegisterResDto = postService.registerComment(commentRegisterReqDto,postId,user);
            return new Response<>(commentRegisterResDto);
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        }

    }

    @PostMapping("/comments/{commentId}/commentReplies")
    public Response<CommentReplyRegisterResDto,Object> registerCommentReply(@PathVariable Long commentId,@RequestBody CommentReplyRegisterReqDto commentReplyRegisterReqDto,@AuthenticationPrincipal User user) {
        try {

            CommentReplyRegisterResDto commentReplyRegisterResDto =postService.registerCommentReply(commentReplyRegisterReqDto,commentId,user);
            return new Response<>(commentReplyRegisterResDto);
        }
        catch (BaseException e){
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/comments/{commentId}/{commentReplyId}")
    public Response<Object, Object> deleteCommentReply(@PathVariable Long commentId, @PathVariable Long commentReplyId,@AuthenticationPrincipal User user) {
        try {
            postService.deleteCommentReply(commentId,commentReplyId,user);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/posts/{postId}/{commentId}")
    public Response<Object, Object> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,@AuthenticationPrincipal User user) {
        try {
            postService.deleteComment(postId,commentId,user);
            return new Response<>(SUCCESS);}
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/posts/{postId}")
    public Response<Object, Object> deletePost(@PathVariable Long postId,@AuthenticationPrincipal User user) {
        try {
            postService.deletePost(postId,user);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts/{postId}/like")
    public Response<Object, Object> registerPostLike(@PathVariable Long postId) {
        try {
            postService.registerPostLike(postId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/posts/{postId}/like")
    public Response<Object, Object> deletePostLike(@PathVariable Long postId) {
        try {
            postService.deletePostLike(postId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/posts/{postId}/{commentId}/like")
    public Response<Object, Object> registerCommentLike(@PathVariable Long postId, @PathVariable Long commentId) {
        try {
            postService.registerCommentLike(postId, commentId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/posts/{postId}/{commentId}/like")
    public Response<Object, Object> deleteCommentLike(@PathVariable Long postId, @PathVariable Long commentId) {
        try {
            postService.deleteCommentLike(postId, commentId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping("/comments/{commentId}/{commentReplyId}/like")
    public Response<Object, Object> registerCommentReplyLike(@PathVariable Long commentId, @PathVariable Long commentReplyId) {
        try {
            postService.registerCommentReplyLike(commentId, commentReplyId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @DeleteMapping("/comments/{commentId}/{commentReplyId}/like")
    public Response<Object, Object> deleteCommentReplyLike(@PathVariable Long commentId, @PathVariable Long commentReplyId) {
        try {
            postService.deleteCommentReplyLike(commentId, commentReplyId);
            return new Response<>(SUCCESS);
        }
        catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }
}