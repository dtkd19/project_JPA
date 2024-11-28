package com.sh.project_JPA.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuthDTO {
    private long id;
    private String email;
    private String auth;
}
