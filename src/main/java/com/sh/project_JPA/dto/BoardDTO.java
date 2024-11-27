package com.sh.project_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BoardDTO {
    private long bno;
    private String title;
    private String writer;
    private String content;
    private LocalDateTime regAt;
    private LocalDateTime modAt;
}
