package com.videosplitter.controller;

import com.videosplitter.service.VideoProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoProcessingService videoProcessingService;
    private final String UPLOAD_DIR = "data/uploads";
    private final String OUTPUT_DIR = "data/output";

    @PostMapping("/split")
    public ResponseEntity<?> splitVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("segmentDuration") int segmentDurationMinutes) {
        try {
            // 创建上传和输出目录
            createDirectories();

            // 保存上传的文件
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path uploadPath = Paths.get(UPLOAD_DIR, uniqueFileName);
            Files.write(uploadPath, file.getBytes());

            // 处理视频
            String outputDirPath = Paths.get(OUTPUT_DIR, uniqueFileName.substring(0, uniqueFileName.lastIndexOf("."))).toString();
            List<String> outputFiles = videoProcessingService.splitVideo(
                    uploadPath.toString(),
                    segmentDurationMinutes,
                    outputDirPath
            );

            return ResponseEntity.ok(outputFiles);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing video: " + e.getMessage());
        }
    }

    private void createDirectories() throws Exception {
        Files.createDirectories(Paths.get(UPLOAD_DIR));
        Files.createDirectories(Paths.get(OUTPUT_DIR));
    }
}
