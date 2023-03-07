package com.wowmate.server.user.controller;

import com.wowmate.server.chatroom.domain.UserChatroom;
import com.wowmate.server.chatroom.repository.UserChatroomRepository;
import com.wowmate.server.comment.domain.Comment;
import com.wowmate.server.comment.domain.CommentReply;
import com.wowmate.server.comment.repository.CommentReplyRepository;
import com.wowmate.server.comment.repository.CommentRepository;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.PostRegisterResDto;
import com.wowmate.server.post.repository.PostRepository;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.Response;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.dto.AuthCodeDto;
import com.wowmate.server.user.dto.SignUpRequestDto;
import com.wowmate.server.user.dto.SignUpResultDto;
import com.wowmate.server.user.dto.UpdatePasswordDto;
import com.wowmate.server.user.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.wowmate.server.response.ResponseStatus.NOT_EXIST_COMMENT;
import static com.wowmate.server.response.ResponseStatus.NOT_EXIST_COMMENTREPLY;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final EmailService emailService;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final UserChatroomRepository userChatroomRepository;

    @PostMapping("/emailConfirm")
    public Response<AuthCodeDto, Object> emailConfirm(@RequestParam String email) throws Exception {

        log.info("[emailConfirm] 이메일 인증을 시도하고 있습니다. Email : {}", email);

        AuthCodeDto authCodeDto = new AuthCodeDto();
        authCodeDto.setCode(emailService.sendSimpleMessage(email));

        return new Response<>(authCodeDto);
    }

    @PostMapping(value = "/postAccusation/{postId}")
    public Response<Object, Object> PostAccusation(@PathVariable Long postId, @RequestParam String reason, @AuthenticationPrincipal User user) {

        try {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new BaseException(ResponseStatus.NOT_EXIST_POST));
            String category = "<게시글 신고>";
            String ID = "게시글 ID: " + postId;
            String content = "신고 사유: " + reason;

            emailService.sendAccusationMessage(user.getEmail(), post.getUser().getEmail(), category, ID, content );
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Response<>(ResponseStatus.SUCCESS);
    }

    @PostMapping(value = "/commentAccusation/{postId}/{commentId}")
    public Response<Object, Object> CommentAccusation(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String reason, @AuthenticationPrincipal User user) {

        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENT));

            String category = "<댓글 신고>";
            String ID = "댓글 ID: " + commentId + " (in " + postId +"번 게시글)";
            String content = "신고 사유: " + reason;

            emailService.sendAccusationMessage(user.getEmail(), comment.getUser().getEmail(), category, ID, content );
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Response<>(ResponseStatus.SUCCESS);
    }

    @PostMapping(value = "/commentReplyAccusation/{postId}/{commentReplyId}")
    public Response<Object, Object> CommentReplyAccusation(@PathVariable Long postId, @PathVariable Long commentReplyId, @RequestParam String reason, @AuthenticationPrincipal User user) {

        try {
            CommentReply commentReply = commentReplyRepository.findById(commentReplyId)
                    .orElseThrow(() -> new BaseException(NOT_EXIST_COMMENTREPLY));

            String category = "<대댓글 신고>";
            String ID = "대댓글 ID: " + commentReplyId + " (in " + postId +"번 게시글)";
            String content = "신고 사유: " + reason;

            emailService.sendAccusationMessage(user.getEmail(), commentReply.getUser().getEmail(), category, ID, content);
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Response<>(ResponseStatus.SUCCESS);
    }

    @PostMapping(value = "/chatAccusation/{roomUuid}")
    public Response<Object, Object> ChatAccusation(@PathVariable("roomUuid") String roomUuid, @RequestParam String opppnentEmail, @RequestParam String reason, @AuthenticationPrincipal User user) {

        try {
            UserChatroom chatroom = userChatroomRepository.findByUuidAndEmail(roomUuid, user.getEmail())
                    .orElseThrow(() -> new BaseException(ResponseStatus.NO_CHATROOM));

            String category = "<채팅방 신고>";
            String ID = "채팅방 ID: " + roomUuid;
            String content = "신고 사유: " + reason;

            emailService.sendAccusationMessage(user.getEmail(), opppnentEmail, category, ID, content);
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new Response<>(ResponseStatus.SUCCESS);
    }
}
