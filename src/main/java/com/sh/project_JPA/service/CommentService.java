package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.CommentDTO;
import com.sh.project_JPA.entity.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {

    default Comment convertDtoToEntity(CommentDTO commentDTO){

        return Comment.builder()
                .cno(commentDTO.getCno())
                .bno(commentDTO.getBno())
                .writer(commentDTO.getWriter())
                .content(commentDTO.getContent())
                .build();
    }

    default CommentDTO convertEntityToDto(Comment comment){

        return CommentDTO.builder()
                .cno(comment.getCno())
                .bno(comment.getBno())
                .writer(comment.getWriter())
                .content(comment.getContent())
                .regAt(comment.getRegAt())
                .modAt(comment.getModAt())
                .build();
    }



    Long post(CommentDTO commentDTO);

    Page<CommentDTO> getList(long bno, int page);

    Long modify(CommentDTO commentDTO);

    long delete(Long cno);
}
