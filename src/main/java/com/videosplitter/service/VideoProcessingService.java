package com.videosplitter.service;

import com.videosplitter.ffmpeg.FFmpegWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VideoProcessingService {

    private final FFmpegWrapper ffmpegWrapper;

    public List<String> splitVideo(String inputPath, int segmentDurationMinutes, String outputDirPath) throws Exception {
        // 创建输出目录
        Files.createDirectories(Paths.get(outputDirPath));

        // 获取视频总时长（秒）
        long totalDuration = ffmpegWrapper.getVideoDuration(inputPath);
        long segmentDurationSeconds = segmentDurationMinutes * 60L;
        List<String> outputFiles = new ArrayList<>();

        // 分割视频
        for (long startTime = 0; startTime < totalDuration; startTime += segmentDurationSeconds) {
            String outputFileName = String.format("%s/segment_%d.mp4", outputDirPath, startTime / segmentDurationSeconds + 1);
            long duration = Math.min(segmentDurationSeconds, totalDuration - startTime);
            
            ffmpegWrapper.splitVideo(inputPath, outputFileName, startTime, duration);
            outputFiles.add(outputFileName);
        }

        return outputFiles;
    }
}
