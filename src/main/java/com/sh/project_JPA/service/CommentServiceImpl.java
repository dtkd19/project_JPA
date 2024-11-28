package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.CommentDTO;
import com.sh.project_JPA.entity.Comment;
import com.sh.project_JPA.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
       private final CommentRepository commentRepository;


    @Override
    public Long post(CommentDTO commentDTO) {
        return commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
    }

    @Override
    public Page<CommentDTO> getList(long bno, int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by("cno").descending());

        Page<Comment> list = commentRepository.findByBno(bno, pageable);

        Page<CommentDTO> dtoPageList = list.map(c -> convertEntityToDto(c));

        return dtoPageList;

    }

    @Override
    public Long modify(CommentDTO commentDTO) {
        Optional<Comment> optional = commentRepository.findById(commentDTO.getCno());

        if(optional.isPresent()){
            Comment comment = optional.get();
            comment.setContent(commentDTO.getContent());
            comment.setModAt(LocalDateTime.now());
            return commentRepository.save(comment).getCno();
        }
        return null;
    }

    @Override
    public long delete(Long cno) {
        commentRepository.deleteById(cno);
        Optional<Comment> optionalComment = commentRepository.findById(cno);

        return optionalComment.map(Comment::getCno).orElse(0L);
    }


}
