package com.sh.project_JPA.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ImageHandler {
    public List<String> extractImageUrls(String content) {
        List<String> imageUrls = new ArrayList<>();

        log.info(content);

        if (content == null || content.isEmpty()) {
            log.warn("Content is null or empty. No image URLs to extract.");
            return imageUrls;  // 빈 리스트 반환
        }

        String regex = "<img[^>]+src=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        while (matcher.find()) {
            imageUrls.add(matcher.group(1));  // 이미지 URL만 추출
        }

        log.info(" >> imageUrls>> {]" , imageUrls);

        return imageUrls;
    }
    public String extractUuidFromUrl(String fileUrl) {

        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;  // URL이 null이거나 비어있으면 UUID 추출 불가
        }

        // 예: http://localhost:8088/upload/2024/11/27/UUID_filename.jpg
        String regex = ".*(/([a-f0-9\\-]+)_.*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fileUrl);

        if (matcher.find()) {
            return matcher.group(2);  // UUID 부분만 반환
        }
        return null;  // UUID가 없으면 null 반환
    }

}
