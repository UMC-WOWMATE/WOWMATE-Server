package com.wowmate.server.post.service;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.comment.domain.CommentReply;
import com.wowmate.server.comment.dto.*;
import com.wowmate.server.comment.repository.CommentReplyRepository;
import com.wowmate.server.comment.repository.CommentRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.*;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.swing.text.html.Option;
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
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final UserRepository userRepository;

    //게시글 전체 조회
    public List<PostInfoResDto> getAllPostList() throws BaseException {

        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();  //반환할 List를 생성

        postList = postRepository.findAll();
        if(postList.isEmpty())
            throw new BaseException(SUCCESS_NO_POST);
        for (Post p : postList) {
            postInfoResDtoList.add(new PostInfoResDto(
                            p.getTitle(),
                            p.getCategoryName(),
                            p.getTag1(),
                            p.getTag2(),
                            p.getTag3(),
                            p.getTag4(),
                            p.getTag5(),
                            p.getLikeNumber(),
                            p.getUser().getUniv(),
                            p.getCreatedBy()
                    )
            );
        }
        return postInfoResDtoList;                     //반환
    }

    //게시글 제목 검색
    public List<PostInfoResDto> getPostListByTitle(String postTitle) throws BaseException {
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();  //반환할 List를 생성
        postList = postRepository.findByTitleContaining(postTitle);
        if(postList.isEmpty())
            throw new BaseException(NO_RELATED_POST);
        for (Post p : postList) {
            postInfoResDtoList.add(new PostInfoResDto(
                    p.getTitle(),
                    p.getCategoryName(),
                    p.getTag1(),
                    p.getTag2(),
                    p.getTag3(),
                    p.getTag4(),
                    p.getTag5(),
                    p.getLikeNumber(),
                    p.getUser().getUniv(),
                    p.getCreatedBy())
            );
        }
        return postInfoResDtoList;                     //반환
    }


    public PostClickResDto getPostClick(Long postId) throws BaseException {
        Optional<Post> post;
        PostClickResDto postClickResDto;
        try{
            post = postRepository.findById(postId);
            postClickResDto = new PostClickResDto(
                    post.get().getTitle(),
                    post.get().getCategoryName(),
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
        return postClickResDto;
    }

    public List<CommentInfoResDto> getCommentList(Long postId) throws BaseException {
        List<Comment> commentList;
        List<CommentInfoResDto> commentInfoResDtoList = new ArrayList<>();
        try{
            commentList = commentRepository.findAllByPostId(postId);
            for (Comment c : commentList) {
                List<CommentReply> commentReplyList = commentReplyRepository.findAllByCommentId(c.getId());
                List<CommentReplyInfoResDto> commentReplyInfoResDtoList =new ArrayList<>();
                for(CommentReply r: commentReplyList){
                    commentReplyInfoResDtoList.add(new CommentReplyInfoResDto(
                            r.getContext(),
                            r.getCommentReplyLikeNumber(),
                            r.getCreatedBy()));
                }
                commentInfoResDtoList.add(new CommentInfoResDto(
                                c.getContent(),
                                c.getCommentLikeNumber(),
                                c.getCreatedBy(),
                                commentReplyInfoResDtoList
                        )
                );
            }
        }
        catch(Exception e) {
            throw new BaseException(NO_COMMENT);
        }
        return commentInfoResDtoList;
    }


    public List<PostInfoResDto> getAllPostListByCategory(String categoryName) throws BaseException {
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();

        postList = postRepository.findByCategoryName(categoryName);
        if(postList.isEmpty())
            throw new BaseException(NO_RELATED_POST);
        for (Post p : postList) {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(),
                        p.getTag2(),
                        p.getTag3(),
                        p.getTag4(),
                        p.getTag5(),
                        p.getLikeNumber(),
                        p.getUser().getUniv(),
                        p.getCreatedBy())
                );

        }
        return postInfoResDtoList;
    }

    public PostRegisterResDto registerPost(PostRegisterReqDto postRegisterReqDto) throws BaseException {

        Optional<User> user = userRepository.findById(postRegisterReqDto.getUserId());

        Post post = postRepository.save(new Post(
                user.get(),
                postRegisterReqDto.getPostTitle(),
                postRegisterReqDto.getPostContext(),
                postRegisterReqDto.getCategoryName(),
                postRegisterReqDto.getTag1(),
                postRegisterReqDto.getTag2(),
                postRegisterReqDto.getTag3(),
                postRegisterReqDto.getTag4(),
                postRegisterReqDto.getTag5())
        );
        PostRegisterResDto postRegisterResDto = new PostRegisterResDto(post.getId());
        return postRegisterResDto;

    }

    public CommentRegisterResDto registerComment(CommentRegisterReqDto commentRegisterReqDto) throws BaseException{
        Optional<Post> post = postRepository.findById(commentRegisterReqDto.getPostId());
        Optional<User> user = userRepository.findById(commentRegisterReqDto.getUserId());
        Comment comment = new Comment(post.get(),user.get(),commentRegisterReqDto.getCommentContext(),0);
        post.get().getCommentList().add(comment);

        commentRepository.save(comment);

        CommentRegisterResDto commentRegisterResDto=new CommentRegisterResDto(comment.getId());

        return commentRegisterResDto;
    }

    public CommentReplyRegisterResDto registerCommentReply(CommentReplyRegisterReqDto commentReplyRegisterReqDto) throws BaseException {

        Optional<Comment> comment = commentRepository.findById(commentReplyRegisterReqDto.getCommentId());
        Optional<User> user =userRepository.findById(commentReplyRegisterReqDto.getUserId());

        CommentReply commentReply = new CommentReply(comment.get(),user.get(), commentReplyRegisterReqDto.getCommentReplyContext(), 0);
        comment.get().getCommentReplyList().add(commentReply);

        commentReplyRepository.save(commentReply);

        CommentReplyRegisterResDto commentReplyRegisterResDto = new CommentReplyRegisterResDto(commentReply.getId());
        return commentReplyRegisterResDto;
    }
}