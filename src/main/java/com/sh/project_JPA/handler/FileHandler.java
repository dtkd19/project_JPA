package com.sh.project_JPA.handler;


import com.sh.project_JPA.dto.FileDTO;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class FileHandler {
    private final String UP_DIR = "D:\\_myProject\\_java\\_fileUpload\\";

    public List<FileDTO> uploadFiles (MultipartFile[] files){
    List<FileDTO> flist = new ArrayList<>();
    LocalDate date = LocalDate.now();
    // 2024-11-15  => 2024\\11\\15
    String today = date.toString().replace("-", File.separator);
    //  D:\_myProject\_java\_fileUpload\2024\11\15
    File folders = new File(UP_DIR, today);

    // mkdir = 1개의 폴더만 // mkdirs = 여러개
        if(!folders.exists()){
            folders.mkdirs();
        }

        for(MultipartFile file : files){
            FileDTO fileDTO = new FileDTO();
            fileDTO.setSaveDir(today);
            fileDTO.setFileSize(file.getSize());

            String originalFileName = file.getOriginalFilename();
            String onlyFileName = originalFileName.substring(
                    originalFileName.lastIndexOf(File.separator)+1
            );
            fileDTO.setFileName(onlyFileName);

            UUID uuid = UUID.randomUUID();
            log.info(">>> uuid TOstring > {} ", uuid.toString());
            fileDTO.setUuid(uuid.toString());


            String fullFileName = uuid.toString()+"_"+onlyFileName;
            String thumbFileName = uuid.toString()+"_th_"+onlyFileName;
            log.info(">>> fullFileName > {} ", fullFileName);


            File storeFile = new File(folders, fullFileName);

            try{
                file.transferTo(storeFile); // 실제 파일의 값을 저장 File 객체에 기록
                String fileUrl = "http://localhost:8088/upload/" + today + "/" + fullFileName;
                fileDTO.setFileUrl(fileUrl); // FileDTO에 URL 설정
                log.info(">>> fileUrl > {} ", fileUrl);

                if(isImageFile(storeFile)){
                    log.info(">>> storeFile > {}" , storeFile);
                    fileDTO.setFileType(1);
                    File thumbnail = new File(folders, thumbFileName);
                    Thumbnails.of(storeFile).size(100,100).toFile(thumbnail);
                } else{
                    fileDTO.setFileType(0);
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            flist.add(fileDTO);
        }
        return flist;
    }
    private boolean isImageFile(File file) throws IOException {
        String mimeType = new Tika().detect(file);
        return mimeType.startsWith("image");
    }
}
