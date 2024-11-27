package com.sh.project_JPA.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardFileDTO {
    private BoardDTO boardDTO;
    private List<FileDTO> fileDTOList;
}
