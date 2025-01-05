#!/bin/bash

# FFmpeg版本
FFMPEG_VERSION="4.4.4"

# 创建构建目录
mkdir -p ffmpeg-build
cd ffmpeg-build

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
    --enable-gpl \
    --enable-version3 \
    --enable-nonfree \
    --enable-postproc \
    --enable-avfilter \
    --enable-pthreads \
    --disable-debug \
    --disable-doc

# 编译
make -j$(nproc)
make install

# 复制库文件到项目的lib目录
mkdir -p ../../../../lib
cp build/lib/* ../../../../lib/

echo "FFmpeg compilation completed successfully!"
