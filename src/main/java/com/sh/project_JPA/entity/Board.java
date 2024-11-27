package com.sh.project_JPA.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Board extends Timebase{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bno;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String writer;
    @Column(length = 3000, nullable = false)
    private String content;

}
