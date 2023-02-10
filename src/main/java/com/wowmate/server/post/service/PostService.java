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
                        p.getUser().getUniv(), "무관",
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
                        p.getUser().getUniv(), "무관",
                        p.getCreatedDate()));
            }
        }
        return postInfoResDtoList;                     //반환
    }
    //게시글 단일 조회
    public PostClickResDto getPostClick(Long postId, User user) throws BaseException {
        Optional<Post> post;
        PostClickResDto postClickResDto;

        try{
            post = postRepository.findById(postId);
            if(post.get().getUser().getId()==user.getId())
            {
                postClickResDto = new PostClickResDto(
                        true,
                        post.get().getId(),
                        post.get().getTitle(),
                        post.get().getCategoryName(),
                        post.get().getTag1(),
                        post.get().getTag2(),
                        post.get().getTag3(),
                        post.get().getTag4(),
                        post.get().getTag5(),
                        post.get().getLikeNumber(),
                        post.get().getContext(),
                        post.get().getCreatedDate());
            }
            else {
                postClickResDto = new PostClickResDto(
                        false,
                        post.get().getId(),
                        post.get().getTitle(),
                        post.get().getCategoryName(),
                        post.get().getTag1(),
                        post.get().getTag2(),
                        post.get().getTag3(),
                        post.get().getTag4(),
                        post.get().getTag5(),
                        post.get().getLikeNumber(),
                        post.get().getContext(),
                        post.get().getCreatedDate());
            }

        }
        catch(Exception e) {
            throw new BaseException(NO_RELATED_POST);
        }
        return postClickResDto;
    }
    //게시글 단일 조회 - 댓글
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
        }
        catch(Exception e) {
            throw new BaseException(NO_COMMENT);
        }
        return commentInfoResDtoList;
    }
    //내가 쓴 게시글 조회
    public List<PostInfoResDto> getAllPostListByUser(User user) throws BaseException {
        List<Post> postList;
        List<PostInfoResDto> postInfoResDtoList = new ArrayList<>();  //반환할 List를 생성
        postList = postRepository.findAll();
        if(postList.isEmpty())
            throw new BaseException(SUCCESS_NO_POST);
        for (Post p : postList) {
            if(p.getUser().getId() == user.getId()) {
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
                                    p.getUser().getUniv(), "무관",
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
                                p.getUser().getUniv(), "무관",
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
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findByEmail(currentUser.getUsername());
        if(!post.get().getUser().getId().equals(user.get().getId())){
            throw new BaseException(NOT_WRITER);
        }
        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        postRepository.deleteById(postId);

        throw new BaseException(SUCCESS);
    }
    //댓글 등록
    public CommentRegisterResDto registerComment(CommentRegisterReqDto commentRegisterReqDto,Long postId,User currentUser) throws BaseException{
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(currentUser.getId());

        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        if(commentRegisterReqDto.getCommentContext().isBlank()) {
            throw new BaseException(NO_CONTEXT);
        }

        Comment comment = new Comment(post.get(),user.get(),commentRegisterReqDto.getCommentContext(),0);
        commentRepository.save(comment);

        CommentRegisterResDto commentRegisterResDto = new CommentRegisterResDto(comment.getId());

        return commentRegisterResDto;
    }
    //댓글 삭제
    public void deleteComment(Long postId,Long commentId, User currentUser) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<User> user = userRepository.findByEmail(currentUser.getUsername());

        if(!comment.get().getUser().getId().equals(user.get().getId())){
            throw new BaseException(NOT_WRITER);
        }
        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        if(comment.get().getPost().getId() != postId) {
            throw new BaseException(NO_RELATED_COMMENT);
        }

        commentRepository.deleteById(commentId);
        throw new BaseException(SUCCESS);
    }
    //대댓글 등록
    public CommentReplyRegisterResDto registerCommentReply(CommentReplyRegisterReqDto commentReplyRegisterReqDto,Long commentId,User currentUser) throws BaseException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<User> user =userRepository.findById(currentUser.getId());

        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        if(commentReplyRegisterReqDto.getCommentReplyContext().isBlank()) {
            throw new BaseException(NO_CONTEXT);
        }

        CommentReply commentReply = new CommentReply(comment.get(),user.get(), commentReplyRegisterReqDto.getCommentReplyContext(), 0);
        comment.get().getCommentReplyList().add(commentReply);

        commentReplyRepository.save(commentReply);

        CommentReplyRegisterResDto commentReplyRegisterResDto = new CommentReplyRegisterResDto(commentReply.getId());
        return commentReplyRegisterResDto;
    }
    //대댓글 삭제
    public void deleteCommentReply(Long commentId,Long commentReplyId,User currentUser) throws BaseException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<CommentReply> commentReply = commentReplyRepository.findById(commentReplyId);
        Optional<User> user = userRepository.findByEmail(currentUser.getUsername());

        if(!commentReply.get().getUser().getId().equals(user.get().getId())){
            throw new BaseException(NOT_WRITER);
        }
        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        if(commentReply.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENTREPLY);
        }
        if(commentReply.get().getComment().getId() != commentId) {
            throw new BaseException(NO_RELATED_COMMENTREPLY);
        }
        commentReplyRepository.deleteById(commentReplyId);
        throw new BaseException(SUCCESS);
    }
    //게시글 좋아요
    public void registerPostLike(Long postId) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        int like = post.get().getLikeNumber();
        post.get().setLikeNumber(++like);

    }
    //게시글 좋아요 삭제
    public void deletePostLike(Long postId) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        int like = post.get().getLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        post.get().setLikeNumber(--like);
    }
    //댓글 좋아요
    public void registerCommentLike(Long postId, Long commentId) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        Optional<Comment> comment = commentRepository.findById(commentId);

        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        int like = comment.get().getCommentLikeNumber();
        comment.get().setCommentLikeNumber(++like);
    }
    //댓글 좋아요 취소
    public void deleteCommentLike(Long postId, Long commentId) throws BaseException {
        Optional<Post> post = postRepository.findById(postId);
        Optional<Comment> comment = commentRepository.findById(commentId);

        if(post.isEmpty()) {
            throw new BaseException(NOT_EXIST_POST);
        }
        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        int like = comment.get().getCommentLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        comment.get().setCommentLikeNumber(--like);
    }
    //대댓글 좋아요
    public void registerCommentReplyLike(Long commentId, Long commentReplyId) throws BaseException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<CommentReply> commentReply = commentReplyRepository.findById(commentReplyId);

        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        if(commentReply.isEmpty()) {
            throw new BaseException((NOT_EXIST_COMMENTREPLY));
        }
        int like = commentReply.get().getCommentReplyLikeNumber();
        commentReply.get().setCommentReplyLikeNumber(++like);
    }
    //대댓글 좋아요 취소
    public void deleteCommentReplyLike(Long commentId, Long commentReplyId) throws BaseException {
        Optional<Comment> comment = commentRepository.findById(commentId);
        Optional<CommentReply> commentReply = commentReplyRepository.findById(commentReplyId);

        if(comment.isEmpty()) {
            throw new BaseException(NOT_EXIST_COMMENT);
        }
        if(commentReply.isEmpty()) {
            throw new BaseException((NOT_EXIST_COMMENTREPLY));
        }
        int like = commentReply.get().getCommentReplyLikeNumber();
        if(like <= 0) {
            throw new BaseException(NO_LIKE_NUMBER);
        }
        commentReply.get().setCommentReplyLikeNumber(--like);
    }
    //게시글 작성자인지 확인(게시글 ...)
    public void isPostWriter(Long postId, Long id) throws BaseException {
        if(postRepository.findById(postId).get().getUser().getId()==id)
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }
    //게시글 작성자인지 확인(댓글 ...)
    public void isCommentWriter(Long commentId, Long id) throws BaseException {
        if(commentRepository.findById(commentId).get().getUser().getId()==id)
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }
    //게시글 작성자인지 확인(대댓글 ...)
    public void isCommentReplyWriter(Long commentReplyId, Long id) throws BaseException {
        if(commentReplyRepository.findById(commentReplyId).get().getUser().getId()==id)
            throw new BaseException(SUCCESS);
        else
            throw new BaseException(NOT_WRITER);
    }


}