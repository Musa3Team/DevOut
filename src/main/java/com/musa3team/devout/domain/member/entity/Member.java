package com.musa3team.devout.domain.member.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "member")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private String memberRole;
    private LocalDateTime deletedAt;
}
