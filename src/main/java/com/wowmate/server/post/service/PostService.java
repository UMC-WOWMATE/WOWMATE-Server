package com.wowmate.server.post.service;

import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.comment.domain.CommentReply;
import com.wowmate.server.comment.dto.*;
import com.wowmate.server.comment.repository.CommentReplyRepository;
import com.wowmate.server.comment.repository.CommentRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.*;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.wowmate.server.response.ResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
@CrossOrigin
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
            if(p.getMember() == 0) {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), "무관",
                        p.getCreatedDate()));
            }
            else {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), Integer.toString(p.getMember()),
                        p.getCreatedDate()));
            }
        }
        return postInfoResDtoList;                     //반환
    }
    //게시글 제목 검색
    public List<PostInfoResDto> getPostListByTitle(String postTitle) throws BaseException {
        if(postTitle.isBlank())
            throw new BaseException(NO_TITLE);
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();  //반환할 List를 생성
        postList = postRepository.findByTitleContaining(postTitle);
        if(postList.isEmpty())
            throw new BaseException(NO_RELATED_POST);
        for (Post p : postList) {
            if(p.getMember() == 0) {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), "무관",
                        p.getCreatedDate()));
            }
            else {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), Integer.toString(p.getMember()),
                        p.getCreatedDate()));
            }
        }
        return postInfoResDtoList;                     //반환
    }
    //게시글 단일 조회
    public PostClickResDto getPostClick(Long postId, User user) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        PostClickResDto postClickResDto;
        if(post.getUser().getId().equals(user.getId()))
        {
            postClickResDto = new PostClickResDto(
                    true,
                    post.getId(),
                    post.getTitle(),
                    post.getCategoryName(),
                    post.getTag1(),
                    post.getTag2(),
                    post.getTag3(),
                    post.getTag4(),
                    post.getTag5(),
                    post.getLikeNumber(),
                    post.getContext(),
                    post.getCreatedDate());
        }
        else {
            postClickResDto = new PostClickResDto(
                    false,
                    post.getId(),
                    post.getTitle(),
                    post.getCategoryName(),
                    post.getTag1(),
                    post.getTag2(),
                    post.getTag3(),
                    post.getTag4(),
                    post.getTag5(),
                    post.getLikeNumber(),
                    post.getContext(),
                    post.getCreatedDate());
        }
        return postClickResDto;
    }
    //게시글 단일 조회 - 댓글
    public List<CommentInfoResDto> getCommentList(Long postId) throws BaseException {
        List<Comment> commentList;
        List<CommentInfoResDto> commentInfoResDtoList = new ArrayList<>();
        commentList = commentRepository.findAllByPostId(postId);
        for (Comment c : commentList) {
            List<CommentReply> commentReplyList = commentReplyRepository.findAllByCommentId(c.getId());
            List<CommentReplyInfoResDto> commentReplyInfoResDtoList =new ArrayList<>();
            for(CommentReply r: commentReplyList){
                commentReplyInfoResDtoList.add(new CommentReplyInfoResDto(
                        r.getId(),
                        r.getContext(),
                        r.getCommentReplyLikeNumber(),
                        r.getCreatedDate()));
            }
            commentInfoResDtoList.add(new CommentInfoResDto(
                    c.getId(),
                    c.getContext(),
                    c.getCommentLikeNumber(),
                    c.getCreatedDate(),
                    commentReplyInfoResDtoList));
        }
        return commentInfoResDtoList;
    }
    //내가 쓴 게시글 조회
    public List<PostInfoResDto> getAllPostListByUser(User user) throws BaseException {
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();
        postList = postRepository.findAll();
        if(postList.isEmpty())
            throw new BaseException(SUCCESS_NO_POST);
        for (Post p : postList) {
            if(p.getUser().getId().equals(user.getId())) {
                if (p.getMember() == 0) {
                    postInfoResDtoList.add(new PostInfoResDto(
                            p.getId(), p.getTitle(),
                            p.getCategoryName(),
                            p.getTag1(), p.getTag2(),
                            p.getTag3(), p.getTag4(),
                            p.getTag5(), p.getLikeNumber(),
                            p.getUser().getUniv(), "무관",
                            p.getCreatedDate()));
                } else {
                    postInfoResDtoList.add(new PostInfoResDto(
                            p.getId(), p.getTitle(),
                            p.getCategoryName(),
                            p.getTag1(), p.getTag2(),
                            p.getTag3(), p.getTag4(),
                            p.getTag5(), p.getLikeNumber(),
                            p.getUser().getUniv(), Integer.toString(p.getMember()),
                            p.getCreatedDate()));
                }
            }
        }
        return postInfoResDtoList;
    }
    //카테고리별 게시글 조회
    public List<PostInfoResDto> getAllPostListByCategory(String categoryName) throws BaseException {
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();

        postList = postRepository.findByCategoryName(categoryName);
        if(postList.isEmpty())
            throw new BaseException(NO_RELATED_POST);
        for (Post p : postList) {
            if(p.getMember() == 0) {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), "무관",
                        p.getCreatedDate()));
            }
            else {
                postInfoResDtoList.add(new PostInfoResDto(
                        p.getId(), p.getTitle(),
                        p.getCategoryName(),
                        p.getTag1(), p.getTag2(),
                        p.getTag3(), p.getTag4(),
                        p.getTag5(), p.getLikeNumber(),
                        p.getUser().getUniv(), Integer.toString(p.getMember()),
                        p.getCreatedDate()));
            }
        }
        return postInfoResDtoList;
    }
    //게시글 등록
    public PostRegisterResDto registerPost(PostRegisterReqDto postRegisterReqDto, User currentUser) throws BaseException {
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        if(postRegisterReqDto.getPostTitle().isBlank()){
            throw new BaseException(NO_TITLE);
        }
        if(postRegisterReqDto.getCategoryName().isBlank()){
            throw new BaseException(NO_CATEGORY);
        }
        if(postRegisterReqDto.getPostContext().isBlank()){
            throw new BaseException(NO_CONTEXT);
        }

        Post post = postRepository.save(new Post(
                user,
                postRegisterReqDto.getPostTitle(),
                postRegisterReqDto.getPostContext(),
                postRegisterReqDto.getCategoryName(),
                postRegisterReqDto.getTag1(),
                postRegisterReqDto.getTag2(),
                postRegisterReqDto.getTag3(),
                postRegisterReqDto.getTag4(),
                postRegisterReqDto.getTag5(),
                postRegisterReqDto.getPostMember()));
        PostRegisterResDto postRegisterResDto = new PostRegisterResDto(post.getId());
        return postRegisterResDto;
    }
    //게시글 삭제
    public void deletePost(Long postId, User currentUser) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        if(!post.getUser().getId().equals(user.getId())){
            throw new BaseException(NOT_WRITER);
        }
        postRepository.deleteById(postId);

        throw new BaseException(SUCCESS);
    }
    //댓글 등록
    public CommentRegisterResDto registerComment(CommentRegisterReqDto commentRegisterReqDto,Long postId,User currentUser) throws BaseException{
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        if(commentRegisterReqDto.getCommentContext().isBlank()) {
            throw new BaseException(NO_CONTEXT);
        }

        Comment comment = new Comment(post,user,commentRegisterReqDto.getCommentContext(),0);
        commentRepository.save(comment);

        CommentRegisterResDto commentRegisterResDto = new CommentRegisterResDto(comment.getId());

        return commentRegisterResDto;
    }
    //댓글 삭제
    public void deleteComment(Long postId,Long commentId, User currentUser) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

        if(!comment.getUser().getId().equals(user.getId())){
            throw new BaseException(NOT_WRITER);
        }
        if(comment.getPost().getId() != postId) {
            throw new BaseException(NO_RELATED_COMMENT);
        }

        commentRepository.deleteById(commentId);
        throw new BaseException(SUCCESS);
    }
    //대댓글 등록
    public CommentReplyRegisterResDto registerCommentReply(CommentReplyRegisterReqDto commentReplyRegisterReqDto,Long commentId,User currentUser) throws BaseException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));
        if(commentReplyRegisterReqDto.getCommentReplyContext().isBlank()) {
            throw new BaseException(NO_CONTEXT);
        }

        CommentReply commentReply = new CommentReply(comment,user, commentReplyRegisterReqDto.getCommentReplyContext(), 0);
        comment.getCommentReplyList().add(commentReply);

        commentReplyRepository.save(commentReply);

        CommentReplyRegisterResDto commentReplyRegisterResDto = new CommentReplyRegisterResDto(commentReply.getId());
        return commentReplyRegisterResDto;
    }
    //대댓글 삭제
    public void deleteCommentReply(Long commentId,Long commentReplyId,User currentUser) throws BaseException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENTREPLY));
        User user = userRepository.findByEmail(currentUser.getUsername())
                .orElseThrow(() -> new BaseException(NOT_FOUND_USER));

        if(!commentReply.getUser().getId().equals(user.getId())){
            throw new BaseException(NOT_WRITER);
        }
        if(commentReply.getComment().getId() != commentId) {
            throw new BaseException(NO_RELATED_COMMENTREPLY);
        }
        commentReplyRepository.deleteById(commentReplyId);
        throw new BaseException(SUCCESS);
    }
    //게시글 좋아요
    public void registerPostLike(Long postId) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        int like = post.getLikeNumber();
        post.setLikeNumber(++like);

    }
    //게시글 좋아요 삭제
    public void deletePostLike(Long postId) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        int like = post.getLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        post.setLikeNumber(--like);
    }
    //댓글 좋아요
    public void registerCommentLike(Long postId, Long commentId) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        int like = comment.getCommentLikeNumber();
        comment.setCommentLikeNumber(++like);
    }
    //댓글 좋아요 취소
    public void deleteCommentLike(Long postId, Long commentId) throws BaseException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        int like = comment.getCommentLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        comment.setCommentLikeNumber(--like);
    }
    //대댓글 좋아요
    public void registerCommentReplyLike(Long commentId, Long commentReplyId) throws BaseException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENTREPLY));
        int like = commentReply.getCommentReplyLikeNumber();
        commentReply.setCommentReplyLikeNumber(++like);
    }
    //대댓글 좋아요 취소
    public void deleteCommentReplyLike(Long commentId, Long commentReplyId) throws BaseException {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));
        CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENTREPLY));
        int like = commentReply.getCommentReplyLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        commentReply.setCommentReplyLikeNumber(--like);
    }
    //게시글 작성자인지 확인(게시글 ...)
    public void isPostWriter(Long postId, Long id) throws BaseException {
        if(postRepository.findById(postId).get().getUser().getId().equals(id))
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }
    //게시글 작성자인지 확인(댓글 ...)
    public void isCommentWriter(Long commentId, Long id) throws BaseException {
        if(commentRepository.findById(commentId).get().getUser().getId().equals(id))
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }
    //게시글 작성자인지 확인(대댓글 ...)
    public void isCommentReplyWriter(Long commentReplyId, Long id) throws BaseException {
        if(commentReplyRepository.findById(commentReplyId).get().getUser().getId().equals(id))
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }


}