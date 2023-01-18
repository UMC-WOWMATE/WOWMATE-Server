package com.wowmate.server.user.domain;

import com.wowmate.server.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private Long school_id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long phoneNumber;

    private int age;

    @Enumerated(EnumType.STRING)
    private Gender gender;

//    이미지 논의 필요
//    @Lob
//    private String image; --> ??


}
