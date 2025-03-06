package com.musa3team.devout.domain.member.entity;

import com.musa3team.devout.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "member",
        uniqueConstraints = {@UniqueConstraint(name="UK_email_role", columnNames = {"email", "member_role"})}
)
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    public Member(String name, String email, String password, String address, String phoneNumber, String memberRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.memberRole = MemberRole.valueOf(memberRole);

    }

    public void modifyName(String name) {
        this.name = name;
    }

    public void modifyAddress(String address) {
        this.address = address;
    }

    public void modifyPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

    public void deleteMember(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

}
