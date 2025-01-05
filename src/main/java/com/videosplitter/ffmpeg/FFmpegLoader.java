package com.videosplitter.ffmpeg;

import com.sun.jna.Native;
import java.io.File;

public class FFmpegLoader {
    private static final String LIB_PATH = new File("lib").getAbsolutePath();
    private static FFmpegNative INSTANCE;

    static {
        try {
            // 设置库搜索路径
            System.setProperty("jna.library.path", LIB_PATH);
            
            // 加载所有必需的FFmpeg库
            Native.load(LIB_PATH + "/libavutil.dylib", FFmpegNative.class);
            Native.load(LIB_PATH + "/libswresample.dylib", FFmpegNative.class);
            INSTANCE = Native.load(LIB_PATH + "/libavcodec.dylib", FFmpegNative.class);
            Native.load(LIB_PATH + "/libavformat.dylib", FFmpegNative.class);
        } catch (UnsatisfiedLinkError e) {
            String error = String.format("Failed to load FFmpeg libraries from %s. Error: %s", LIB_PATH, e.getMessage());
            throw new RuntimeException(error, e);
        }
    }

    public static FFmpegNative getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("FFmpeg libraries not properly initialized");
        }
        return INSTANCE;
    }
}
