package com.wowmate.server.user.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
public class University {

    @Id
    @Column(name = "univ_id")
    private Long id;

    private String name;

    private String email_domain;

}
