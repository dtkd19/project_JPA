package com.sh.project_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Comment extends Timebase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;
    @Column(nullable = false)
    private Long bno;
    @Column(nullable = false)
    private String writer;
    @Column(length = 2000,nullable = false)
    private String content;
}
