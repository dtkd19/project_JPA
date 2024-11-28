package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.BoardDTO;
import com.sh.project_JPA.dto.BoardFileDTO;
import com.sh.project_JPA.dto.FileDTO;
import com.sh.project_JPA.entity.Board;
import com.sh.project_JPA.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    /* 컨버트 */
    default Board convertDtoToEntity(BoardDTO boardDTO){
        return Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .writer(boardDTO.getWriter())
                .content(boardDTO.getContent())
                .build();
    }

    default  BoardDTO convertEntityToDto(Board board){
        return BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .writer(board.getWriter())
                .content(board.getContent())
                .regAt(board.getRegAt())
                .modAt(board.getModAt())
                .build();
    }

    default File convertFileDTOTOFile(FileDTO fileDTO){
        return File.builder()
                .bno(fileDTO.getBno())
                .uuid(fileDTO.getUuid())
                .fileName(fileDTO.getFileName())
                .fileSize(fileDTO.getFileSize())
                .fileType(fileDTO.getFileType())
                .fileUrl(fileDTO.getFileUrl())
                .saveDir(fileDTO.getSaveDir())
                .build();
    }

    default FileDTO convertFileTOFileDTO(File file){
        return FileDTO.builder()
                .bno(file.getBno())
                .uuid(file.getUuid())
                .fileUrl(file.getFileUrl())
                .fileName(file.getFileName())
                .fileSize(file.getFileSize())
                .fileType(file.getFileType())
                .regAt(file.getRegAt())
                .modAt(file.getModAt())
                .saveDir(file.getSaveDir())
                .build();
    }





    long register(BoardDTO boardDTO);

    Page<BoardDTO> getList(int pageNo);

    String fileUpload(List<FileDTO> flist);

    void setBno(long bno);

    BoardFileDTO getDetail(Long bno);

    String getUpdateContent(long bno);

    void deleteFileFromDB(String uuid);

    void updateBoardContent(BoardDTO boardDTO);

    void delete(Long bno);
}
