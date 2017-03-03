/**
 * ��򵥵Ļ���FFmpeg��ת����-��׿
 * Simplest FFmpeg Android Transcoder
 *
 * ������  Lei Xiaohua
 * leixiaohua1020@126.com
 * ��С��  Ma Xiaoyu
 * maxiaoyucuc@163.com
 * �й���ý��ѧ/���ֵ��Ӽ���
 * Communication University of China / Digital TV Technology
 * http://blog.csdn.net/leixiaohua1020
 *
 *
 * �������ǰ�׿ƽ̨�µ�ת����������ֲ��ffmpeg.c�����й��ߡ�
 *
 * This software is a Transcoder in Android.
 * It is transplanted from ffmpeg.c command line tools.
 *
 */

#include <string.h>
#include <jni.h>
#include <ffmpeg.h>
#include "CFFmpeg.h"
#include <ffmpeg_jni.h>

int ffmpegmain(int argc, char **argv);
int transcode(char* inputfile, char* outoutfile);

//Output FFmpeg's av_log()
void custom_log(void *ptr, int level, const char* fmt, va_list vl){

	//To TXT file

/*
	FILE *fp=fopen("/storage/emulated/0/av_log.txt","a+");
	if(fp){
		vfprintf(fp,fmt,vl);
		fflush(fp);
		fclose(fp);
	}

*/
	//To Logcat
	__android_log_print(ANDROID_LOG_ERROR, "FFmpeg_jni", fmt, vl);
	//LOGE(fmt, vl);
}


JNIEXPORT jint JNICALL Java_com_tanjinc_tmediaplayer_utils_FFmpegUtils_transcodeJNI( JNIEnv * env, jobject thiz, jobject inputfile, jobject outoutfile)
{

  //FFmpeg av_log() callback
  av_log_set_callback(custom_log);
  char *inputfileName = (*env)->GetStringUTFChars(env,inputfile,0);
  char *outFileName = (*env)->GetStringUTFChars(env,outoutfile,0);

  transcode(inputfileName,outFileName);
  return 0;

}

JNIEXPORT jint JNICALL Java_com_tanjinc_tmediaplayer_utils_FFmpegUtils_ffmpegcore( JNIEnv * env, jobject thiz, jint cmdnum, jobjectArray cmdline)
{

  //FFmpeg av_log() callback
  av_log_set_callback(custom_log);

  int argc=cmdnum;
  char** argv=(char**)malloc(sizeof(char*)*argc);

  int i=0;
  for(i=0;i<argc;i++)
  {
    jstring string=(*env)->GetObjectArrayElement(env,cmdline,i);
    const char* tmp=(*env)->GetStringUTFChars(env,string,0);
    argv[i]=(char*)malloc(sizeof(char)*1024);
    strcpy(argv[i],tmp);
  }

  ffmpegmain(argc,argv);

  for(i=0;i<argc;i++){
    free(argv[i]);
  }
  free(argv);
  return 0;

}
