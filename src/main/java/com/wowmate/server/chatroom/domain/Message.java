package com.wowmate.server.chatroom.domain;

import com.wowmate.server.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Message extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "message_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private Chatroom chatroom;

    private Long sendUserId;

    private String content;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

}
