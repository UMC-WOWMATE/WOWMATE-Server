package com.wowmate.server.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wowmate.server.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    private String email;

    private String password;

    private String univ;

    private String phoneNumber;

    /*서버에서 클라이언트측으로 LocalDateTime 을 넘겨줄 떄 이를 String 타입으로 변환해줌 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    /* 클라이언트에서 String 타입으로 날짜/시간을 서버측으로 넘겨주면 이를 LocalDateTime 으로 바꿔줌 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;

    private Gender gender;

    private String role;


}
