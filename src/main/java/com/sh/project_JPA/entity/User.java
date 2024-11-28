package com.sh.project_JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends Timebase {
    @Id
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String pwd;
    @Column(name = "nick_name", nullable = false)
    private String nickName;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;
}
