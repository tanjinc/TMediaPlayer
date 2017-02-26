#ifndef FFMPEG_JNI_H
#define FFMPEG_JNI_H

#ifdef ANDROID
#include <jni.h>
#include <android/log.h>
#define LOG "FFmpeg_jni"
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG, ##__VA_ARGS__)
#define LOGI(...)  __android_log_print(ANDROID_LOG_INFO,  LOG, ##__VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_INFO,  LOG, ##__VA_ARGS__)
#else
#define LOGE(format, ...)  printf("(>_<) " format "\n", ##__VA_ARGS__)
#define LOGI(format, ...)  printf("(^_^) " format "\n", ##__VA_ARGS__)
#endif

#endif