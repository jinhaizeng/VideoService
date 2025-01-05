package com.videosplitter.ffmpeg;

import com.sun.jna.*;
import com.sun.jna.ptr.PointerByReference;

import java.util.Arrays;
import java.util.List;

public interface FFmpegNative extends Library {
    // FFmpeg基础结构体
    class AVFormatContext extends Structure {
        public static class ByReference extends AVFormatContext implements Structure.ByReference {}
        public static class ByValue extends AVFormatContext implements Structure.ByValue {}

        public long duration;           // 视频时长（微秒）
        public int nb_streams;         // 流的数量
        public Pointer streams;         // 流数组
        public String filename;         // 文件名
        public long bit_rate;          // 比特率

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("duration", "nb_streams", "streams", "filename", "bit_rate");
        }
    }

    class AVStream extends Structure {
        public static class ByReference extends AVStream implements Structure.ByReference {}
        public static class ByValue extends AVStream implements Structure.ByValue {}

        public int index;              // 流索引
        public Pointer codecpar;       // 编解码器参数
        public Pointer time_base;      // 时间基准

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("index", "codecpar", "time_base");
        }
    }

    class AVCodecContext extends Structure {
        public static class ByReference extends AVCodecContext implements Structure.ByReference {}
        public static class ByValue extends AVCodecContext implements Structure.ByValue {}

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("dummy"); // 实际使用时需要添加所有字段
        }
    }

    // FFmpeg API函数
    Pointer avformat_alloc_context();
    int avformat_open_input(PointerByReference ctx, String url, Pointer fmt, Pointer options);
    void avformat_close_input(PointerByReference ctx);
    int avformat_find_stream_info(Pointer ctx, Pointer options);
    int av_read_frame(Pointer ctx, Pointer pkt);
    int av_seek_frame(Pointer ctx, int stream_index, long timestamp, int flags);
    void av_packet_unref(Pointer pkt);
}
