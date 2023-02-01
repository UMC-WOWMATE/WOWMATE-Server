package com.wowmate.server.chatroom.dto;

import com.wowmate.server.chatroom.domain.Chatroom;
import com.wowmate.server.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetChatroomListDto {

    private String postTitle;

    private String lastMessage;

    private String lastMessageDate;

//    private String opponentUserImg;


    // 이미지 포함 생성 메서드(날짜 가공 로직) 쓸 필요 있나?
//    public static GetChatroomListDto createGetChatroomListDto
//            (String postTitle, String lastMessage, LocalDateTime lastMessageDate, String opponentUserImg) {
//
//        GetChatroomListDto getChatroomListDto = new GetChatroomListDto();
//        getChatroomListDto.setPostTitle(postTitle);
//        getChatroomListDto.setLastMessage(lastMessage);
//        getChatroomListDto.setOpponentUserImg(opponentUserImg);
//
//        if(lastMessageDate.getYear() < LocalDateTime.now().getYear()) {
//            getChatroomListDto.setLastMessageDate(lastMessageDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")));
//            return getChatroomListDto;
//        }
//
//        if(lastMessageDate.getDayOfYear() < lastMessageDate.getDayOfYear()) {
//            getChatroomListDto.setLastMessageDate(lastMessageDate.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
//            return getChatroomListDto;
//        }
//
//        getChatroomListDto.setLastMessageDate(lastMessageDate.format(DateTimeFormatter.ofPattern("MM월 dd일")));
//        return getChatroomListDto;
//
//    }

}
