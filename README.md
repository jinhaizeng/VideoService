# Video Splitter

这是一个基于Spring Boot的视频分割工具，集成了FFmpeg源码，可以将长视频自动切分成指定时长的短视频片段。本项目直接包含FFmpeg源码，无需系统安装FFmpeg。

## 前置要求

1. Java 11 或更高版本
2. Maven
3. GCC编译器和开发工具
4. Make工具

## 编译FFmpeg

项目使用了自定义编译的FFmpeg库。在运行项目之前，需要先编译FFmpeg：

```bash
# 给构建脚本添加执行权限
chmod +x src/main/resources/native/build-ffmpeg.sh

# 运行构建脚本
cd src/main/resources/native
./build-ffmpeg.sh
```

编译完成后，FFmpeg的库文件会自动复制到项目的resources目录中。

## 项目配置

项目使用JNA (Java Native Access) 直接调用FFmpeg的原生库，不需要额外的系统配置。

## 运行项目

1. 克隆项目到本地
2. 编译FFmpeg（按照上述步骤）
3. 运行项目：
   ```bash
   mvn spring-boot:run
   ```

## API使用说明

### 分割视频

**POST** `/api/videos/split`

使用multipart/form-data格式发送请求，包含以下参数：
- `file`: 要分割的视频文件
- `segmentDuration`: 每个片段的时长（分钟）

示例请求：
```bash
curl -X POST \
  http://localhost:8080/api/videos/split \
  -F "file=@/path/to/your/video.mp4" \
  -F "segmentDuration=5"
```

响应：
```json
[
  "output/video_uuid/video_part1.mp4",
  "output/video_uuid/video_part2.mp4",
  ...
]
```

## 自定义FFmpeg功能

由于项目直接集成了FFmpeg源码，你可以通过以下方式自定义FFmpeg功能：

1. 修改 `src/main/resources/native/build-ffmpeg.sh` 中的编译选项
2. 在 `com.videosplitter.ffmpeg.FFmpegNative` 中添加需要的FFmpeg API接口
3. 在 `com.videosplitter.ffmpeg.FFmpegWrapper` 中实现新的功能

## 注意事项

1. 默认支持最大2GB的文件上传（可在application.yml中修改）
2. 视频文件会被临时保存在`uploads`目录
3. 分割后的视频片段会保存在`output`目录
4. 建议定期清理临时文件
5. 首次运行前必须编译FFmpeg
