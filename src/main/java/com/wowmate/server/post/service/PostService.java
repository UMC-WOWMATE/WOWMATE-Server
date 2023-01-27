package com.wowmate.server.post.service;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.comment.domain.CommentReply;
import com.wowmate.server.comment.dto.CommentDto;
import com.wowmate.server.comment.dto.CommentReplyDto;
import com.wowmate.server.comment.repository.CommentReplyRepository;
import com.wowmate.server.comment.repository.CommentRepository;
import com.wowmate.server.post.domain.Category;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.PostClickDto;
import com.wowmate.server.post.dto.PostInfoDto;
import com.wowmate.server.post.repository.CategoryRepository;
import com.wowmate.server.response.Response;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wowmate.server.response.ResponseStatus.NO_RELATED_POST;
import static com.wowmate.server.response.ResponseStatus.SUCCESS_NO_POST;
import static com.wowmate.server.response.ResponseStatus.NO_COMMENT;


@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;

    //게시글 전체 조회
    public List<PostInfoDto> getAllPostList() throws BaseException {

        List<Post> postList;
        List<PostInfoDto> postInfoDtoList = new ArrayList<>();  //반환할 List를 생성

        postList = postRepository.findAll();
        if(postList.isEmpty())
            throw new BaseException(SUCCESS_NO_POST);
        for (Post p : postList) {
            postInfoDtoList.add(new PostInfoDto(
                            p.getTitle(),
                            p.getCategory().getName(),
                            p.getTag1(),
                            p.getTag2(),
                            p.getTag3(),
                            p.getTag4(),
                            p.getTag5(),
                            p.getLikeNumber(),
                            p.getUser().getSchool_id(),
                            p.getCreatedBy()
                    )
            );
        }
        return postInfoDtoList;                     //반환
    }

    //게시글 제목 검색
    public List<PostInfoDto> getPostListByTitle(String postTitle) throws BaseException {
        List<Post> postList;
        List<PostInfoDto> postInfoDtoList = new ArrayList<>();  //반환할 List를 생성
        postList = postRepository.findByTitleContaining(postTitle);
        if(postList.isEmpty())
            throw new BaseException(NO_RELATED_POST);
        for (Post p : postList) {
            postInfoDtoList.add(new PostInfoDto(
                    p.getTitle(),
                    p.getCategory().getName(),
                    p.getTag1(),
                    p.getTag2(),
                    p.getTag3(),
                    p.getTag4(),
                    p.getTag5(),
                    p.getLikeNumber(),
                    p.getUser().getSchool_id(),
                    p.getCreatedBy())
            );
        }
        return postInfoDtoList;                     //반환
    }


    public PostClickDto getPostClick(Long postId) throws BaseException {
        Optional<Post> post;
        PostClickDto postClickDto;
        try{
            post = postRepository.findById(postId);
            postClickDto = new PostClickDto(
                    post.get().getTitle(),
                    post.get().getCategory().getName(),
                    post.get().getTag1(),
                    post.get().getTag2(),
                    post.get().getTag3(),
                    post.get().getTag4(),
                    post.get().getTag5(),
                    post.get().getLikeNumber(),
                    post.get().getContext(),
                    post.get().getCreatedBy()
            );
        }
        catch(Exception e) {
            throw new BaseException(NO_RELATED_POST);
        }
        return postClickDto;
    }

    public List<CommentDto> getCommentList(Long postId) throws BaseException {
        List<Comment> commentList;
        List<CommentDto> commentDtoList = new ArrayList<>();
        try{
            commentList = commentRepository.findAllByPostId(postId);
            for (Comment c : commentList) {
                List<CommentReply> commentReplyList = commentReplyRepository.findAllByCommentId(c.getId());
                List<CommentReplyDto> commentReplyDtoList=new ArrayList<>();
                for(CommentReply r: commentReplyList){
                    commentReplyDtoList.add(new CommentReplyDto(
                            r.getContent(),
                            r.getCommentReplyLikeNumber(),
                            r.getCreatedBy()));
                }
                commentDtoList.add(new CommentDto(
                                c.getContent(),
                                c.getCommentLikeNumber(),
                                c.getCreatedBy(),
                                commentReplyDtoList
                        )
                );
            }
        }
        catch(Exception e) {
            throw new BaseException(NO_COMMENT);
        }
        return commentDtoList;
    }

}