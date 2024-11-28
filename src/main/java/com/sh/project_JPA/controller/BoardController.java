package com.sh.project_JPA.controller;


import com.sh.project_JPA.dto.BoardDTO;
import com.sh.project_JPA.dto.BoardFileDTO;
import com.sh.project_JPA.dto.FileDTO;
import com.sh.project_JPA.dto.PagingVO;
import com.sh.project_JPA.entity.File;
import com.sh.project_JPA.handler.FileDeleteHandler;
import com.sh.project_JPA.handler.FileHandler;
import com.sh.project_JPA.handler.ImageHandler;
import com.sh.project_JPA.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board/*")
@Slf4j
public class BoardController {

    @Autowired
    private ImageHandler imageHandler;

    private final BoardService boardService;
    private final FileDeleteHandler fileDeleteHandler;

    @Autowired
    private FileHandler fileHandler;

    @GetMapping("/register")
    public void register(){}


    @PostMapping("/register")
    public String register(@ModelAttribute BoardDTO boardDTO){
        log.info("boardDTO >>> {} ", boardDTO);

        long bno = boardService.register(boardDTO);

        boardService.setBno(bno);

        return (bno > 0) ? "/index" : "redirect:/board/register";
    }


    @PostMapping("/multiFileUpload")
    public ResponseEntity<List<FileDTO>> multiFileUpload(@RequestParam("files")MultipartFile[] files){

        List<FileDTO> flist = null;

        if( files != null && files[0].getSize() > 0){
            flist = fileHandler.uploadFiles(files);
            log.info(">>>> flist >> {} " , flist.toString());
            String msg = boardService.fileUpload(flist);
        }

        return ResponseEntity.ok(flist);
    }




    @GetMapping("/list")
    public void list(Model model, @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo){
        log.info(">>>> page No1 >> {} " , pageNo);
        pageNo = (pageNo == 0  ? 0  : pageNo - 1);
        log.info(">>>> page No2 >> {} " , pageNo);

        Page<BoardDTO> list = boardService.getList(pageNo);

        PagingVO pgvo = new PagingVO(list, pageNo);

        model.addAttribute("list", list);
        model.addAttribute("pgvo",pgvo);

    }

    @GetMapping({"/detail","/modify"})
    public void detail(Model model, @RequestParam("bno") Long bno){

        BoardFileDTO boardFileDTO = boardService.getDetail(bno);

        model.addAttribute("boardFileDTO", boardFileDTO);

    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){
        // 수정된 컨텐츠 가져와서 이미지 url만 빼기
        List<String> existingImages = imageHandler.extractImageUrls(boardDTO.getContent());

        // 기존 이미지 목록과 비교하여 삭제된 이미지들을 추출
        List<String> updatedImages = imageHandler.extractImageUrls(boardService.getUpdateContent(boardDTO.getBno())); // 서버에서 파일에 저장돼있는 정보를 가져옴
        log.info(">>> updateImages >> {}" , updatedImages);
        List<String> imagesToDelete = new ArrayList<>(updatedImages);
        log.info(">>> imagesToDelete1 >> {} " , imagesToDelete);

        imagesToDelete.removeAll(existingImages);

        log.info(">>> imagesToDelete2 >> {} " , imagesToDelete);

        for (String imageUrl : imagesToDelete) {
            log.info(">>> imageUrl >> {}" , imageUrl);
            String uuid = imageHandler.extractUuidFromUrl(imageUrl);  // 이미지 URL에서 UUID 추출
            log.info(">>> uuid >>> {}" , uuid);
            if (uuid != null) {
                String imageFileName = imageHandler.extractImageFileName(imageUrl);
                log.info(">>> imageFileName ?? > {}" , imageFileName);
                String saveDir = imageHandler.extractSaveDir(imageUrl);
                log.info(">>> saveDir ?? > {}", saveDir);
                // 실제파일 삭제
                fileDeleteHandler.deleteFile(saveDir,uuid,imageFileName);
                // DB에서 파일 정보 삭제
                boardService.deleteFileFromDB(uuid);
            }
        }

        String updatedContent = imageHandler.removeImageUrlsFromContent(boardDTO.getContent(), imagesToDelete);

        log.info("updateContent >>>> {}" , updatedContent);

        boardDTO.setContent(updatedContent);

        log.info("boardDTO.getUpdateContent >>>> {}" , boardDTO.getContent());

        boardService.updateBoardContent(boardDTO);

        return  "redirect:/board/detail?bno=" + boardDTO.getBno();
    }


    @GetMapping("/delete")
    public String delete(@RequestParam("bno") Long bno){

        boardService.delete(bno);

        return "redirect:/board/list";
    }



}
