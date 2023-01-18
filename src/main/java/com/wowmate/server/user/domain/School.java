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
public class School extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "school_id")
    private Long id;

    private String name;

    private String email_domain;

}
