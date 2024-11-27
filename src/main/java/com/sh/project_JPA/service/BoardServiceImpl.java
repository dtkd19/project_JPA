package com.sh.project_JPA.service;

import com.sh.project_JPA.dto.BoardDTO;
import com.sh.project_JPA.dto.BoardFileDTO;
import com.sh.project_JPA.dto.FileDTO;
import com.sh.project_JPA.entity.Board;
import com.sh.project_JPA.entity.File;
import com.sh.project_JPA.handler.FileHandler;
import com.sh.project_JPA.repository.BoardRepository;
import com.sh.project_JPA.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    @Autowired
    private FileHandler fileHandler;

    @Override
    public long register(BoardDTO boardDTO) {

        Board board = new Board();
        board.setTitle(boardDTO.getTitle());
        board.setContent(boardDTO.getContent());
        board.setWriter(boardDTO.getWriter());

        return boardRepository.save(board).getBno();
    }

    @Override
    public Page<BoardDTO> getList(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo,10,Sort.by("bno").descending());

        Page<Board> list = boardRepository.findAll(pageable);

        Page<BoardDTO> boardDTOList = list.map(b -> convertEntityToDto(b));
        return boardDTOList;
    }

//    @Override
//    public List<BoardDTO> getList() {
//        List<Board> list = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "bno"));
//        List<BoardDTO> boardDTOList = list.stream().map(b -> convertEntityToDto(b)).toList();
//
//        return boardDTOList;
//    }

    @Override
    public String fileUpload(List<FileDTO> flist) {
        if(!flist.isEmpty())
            for(FileDTO fileDTO : flist){
                if(!fileRepository.existsByUuid(fileDTO.getUuid())){
                    File file = new File();
                    file.setFileName(fileDTO.getFileName());
                    file.setUuid(fileDTO.getUuid());
                    file.setSaveDir(fileDTO.getSaveDir());
                    file.setBno(fileDTO.getBno());
                    file.setFileSize(fileDTO.getFileSize());
                    file.setFileType(fileDTO.getFileType());
                    file.setFileUrl(fileDTO.getFileUrl());
                    fileRepository.save(file);
                }
            }

        return "uploadSuccess";
    }

    @Override
    public void setBno(long bno) {

       List<File> list = fileRepository.findAllByBno(0);
       log.info(list.toString());

        if(!list.isEmpty()){
            List<FileDTO> DtoList = list.stream().map(f -> convertFileTOFileDTO(f)).toList();
               for(FileDTO fileDTO : DtoList){
                   fileDTO.setBno(bno);
               }

               list = DtoList.stream().map(f -> convertFileDTOTOFile(f)).toList();
               log.info(list.toString());

               for(File file : list){
                 log.info(">>> file > {}", file);
                 fileRepository.save(file);
               }

        }
    }

    @Override
    public BoardFileDTO getDetail(Long bno) {

        Optional<Board> optional = boardRepository.findById(bno);

        if(optional.isPresent()){
            BoardDTO boardDTO = convertEntityToDto(optional.get());
            List<File> fileList = fileRepository.findByBno(bno);
            List<FileDTO> fileDTOList = fileList.stream().map(f -> convertFileTOFileDTO(f)).toList();
            BoardFileDTO boardFileDTO = new BoardFileDTO(boardDTO, fileDTOList);
            log.info(">>> boardFileDto>> {}", boardFileDTO);
            return boardFileDTO;
        }

        return null;
    }

    @Override
    public String getUpdateContent(long bno) {
        Optional<Board> optional = boardRepository.findById(bno);

        if(optional.isPresent()){
            BoardDTO boardDTO = convertEntityToDto(optional.get());

            return  boardDTO.getContent();
        }

        return null;
    }

    @Transactional
    @Override
    public void deleteFileFromDB(String uuid) {
        fileRepository.deleteByUuid(uuid);
    }
}
