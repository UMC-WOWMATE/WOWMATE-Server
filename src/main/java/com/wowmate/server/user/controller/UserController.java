package com.wowmate.server.user.controller;

import com.wowmate.server.comment.dto.CommentRegisterResDto;
import com.wowmate.server.post.domain.Post;
import com.wowmate.server.post.dto.PostInfoResDto;
import com.wowmate.server.response.BaseException;
import com.wowmate.server.response.Response;
import com.wowmate.server.response.ResponseStatus;
import com.wowmate.server.user.domain.University;
import com.wowmate.server.user.domain.User;
import com.wowmate.server.user.dto.*;
import com.wowmate.server.user.repository.UniversityRepository;
import com.wowmate.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.wowmate.server.response.ResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
//  private final UniversityRepository universityRepository;
	
    @GetMapping(value = "/")
    public Response<Object, Object> init(){
	    return new Response<>(ResponseStatus.SUCCESS);
    }
	
    @PostMapping(value = "/sign-in")
    public SignInResultDto signIn(@RequestBody SignInRequestDto signInRequestDto) throws RuntimeException, BaseException {

        log.info("[signIn] 로그인을 시도하고 있습니다. Email : {}, pw : ****", signInRequestDto.getEmail());
        SignInResultDto signInResultDto = userService.signIn(signInRequestDto);

        if (signInResultDto.getCode() == 0) {
            log.info("[signIn] 정상적으로 로그인되었습니다. Email : {}, token : {}", signInRequestDto.getEmail(), signInResultDto.getToken());
        }

        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(@RequestBody SignUpRequestDto signUpRequestDto){

        log.info("[signUp] 회원가입을 수행합니다. Email : {}, password : ****, 학교 : {}, 전화번호 : {}, 생년월일 : {}, 성별 : {}, 권한 : {}"
                ,signUpRequestDto.getEmail(), signUpRequestDto.getUniv(), signUpRequestDto.getPhoneNumber()
                ,signUpRequestDto.getBirth(), signUpRequestDto.getGender(), signUpRequestDto.getRole());

        SignUpResultDto signUpResultDto = userService.signUp(signUpRequestDto);

        log.info("[signUp] 회원가입을 완료했습니다. Email : {}", signUpRequestDto.getEmail());
        return signUpResultDto;
    }

//    @GetMapping(value = "/univ")
//    public List<University> getUnivList() {
//            log.info("학교별 도메인 출력");
//            List<University> UnivList = universityRepository.findAll();
//            return UnivList;
//    }

    @GetMapping(value = "/mypage")
    public Response<UserInfoDto,Object> getUserInfo(@AuthenticationPrincipal User user) {

        UserInfoDto userInfoDto = userService.getUserInfo(user);

        return new Response<>(userInfoDto);
    }

    @GetMapping(value = "/logout")
    public Response<Object, Object> logout() {

        return new Response<>(ResponseStatus.SUCCESS);
    }

    @GetMapping(value = "/secession")
    public Response<Object, Object> secession(@AuthenticationPrincipal User user) {
        try {
            userService.deleteUser(user);
            return new Response<>(SUCCESS);
        } catch (BaseException e) {
            return new Response<>(e.getResponseStatus());
        }
    }

    @PostMapping(value = "/updatePassword")
    public Response<Object, Object> updatePassword(@RequestBody UpdatePasswordDto updatePasswordDto) {

        try {
            userService.updatePassword(updatePasswordDto);
            return new Response<>(ResponseStatus.SUCCESS);
        }
        catch   (BaseException e){
            return new Response<>(e.getResponseStatus());
        }
    }
//    @GetMapping(value = "/myposts")
//    public int getMyPosts(@AuthenticationPrincipal User user) {
//        log.info("내가 쓴 글 목록 반환 {}");
//        List<Post> MyPosts = user.getPostList();
//        List<PostInfoResDto> MyPostsInfo = new ArrayList<>();
//        for (Post p : MyPosts) {
//            MyPostsInfo.add(new PostInfoResDto(
//                            p.getTitle(),
//                            p.getCategoryName(),
//                            p.getTag1(),
//                            p.getTag2(),
//                            p.getTag3(),
//                            p.getTag4(),
//                            p.getTag5(),
//                            p.getLikeNumber(),
//                            p.getUser().getUniv(),
//                            p.getMember(),
//                            p.getCreatedBy()
//                    )
//            );
//        }
//        return MyPostsInfo;
//    }
    @GetMapping(value = "/sign-api/exception")
    public void exceptionResponse() throws RuntimeException {
        throw new RuntimeException("접근이 금지되었습니다.");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String, String>> exceptionHandler(RuntimeException e) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("isSuccess", "false");
		map.put("message", "이메일/비밀번호를 확인해주세요");
        map.put("code", "401");
        return new ResponseEntity<>(map, headers, httpStatus);
    }
}
