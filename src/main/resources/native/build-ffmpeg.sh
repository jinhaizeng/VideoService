#!/bin/bash

# FFmpeg版本
FFMPEG_VERSION="4.4.4"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(cd "$SCRIPT_DIR/../../../.." && pwd)"

# 创建构建目录
mkdir -p "$SCRIPT_DIR/ffmpeg-build"
cd "$SCRIPT_DIR/ffmpeg-build"

# 下载FFmpeg源码
if [ ! -f "ffmpeg-${FFMPEG_VERSION}.tar.bz2" ]; then
    curl -O "https://ffmpeg.org/releases/ffmpeg-${FFMPEG_VERSION}.tar.bz2"
fi

# 解压源码
tar xjf "ffmpeg-${FFMPEG_VERSION}.tar.bz2"
cd "ffmpeg-${FFMPEG_VERSION}"

# 配置FFmpeg构建
./configure \
    --prefix="$(pwd)/build" \
    --enable-shared \
    --enable-pic \
    --disable-static \
    --disable-doc \
    --disable-programs \
    --disable-avdevice \
    --disable-postproc \
    --disable-avfilter \
    --disable-network \
    --disable-everything \
    --enable-protocol=file \
    --enable-demuxer=mov,mp4,matroska \
    --enable-decoder=h264,aac \
    --enable-encoder=h264,aac \
    --enable-muxer=mp4

# 编译
make -j4
make install

# 创建lib目录并复制动态库
LIB_DIR="$PROJECT_ROOT/lib"
mkdir -p "$LIB_DIR"

# 复制所有需要的动态库
cp build/lib/libavcodec.dylib "$LIB_DIR/"
cp build/lib/libavformat.dylib "$LIB_DIR/"
cp build/lib/libavutil.dylib "$LIB_DIR/"
cp build/lib/libswresample.dylib "$LIB_DIR/"

echo "FFmpeg compilation completed successfully!"
echo "Dynamic libraries have been copied to: $LIB_DIR"
