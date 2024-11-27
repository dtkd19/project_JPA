package com.sh.project_JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class File extends Timebase{
    @Id
    private String uuid;
    @Column(name = "save_dir", nullable = false)
    private String saveDir;
    @Column(name= "file_name" , nullable = false)
    private String fileName;
    @Column(name = "file_type",nullable = false, columnDefinition = "integer default 0")
    private int fileType;
    private long bno;
    @Column(name = "file_size")
    private long fileSize;
    @Column(name= "file_Url")
    private String fileUrl;

}
