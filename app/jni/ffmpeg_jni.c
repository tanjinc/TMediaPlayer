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

//jstring char2jstring(JNIEnv* env, const char* pat)
//{
//    jclass strClass = env->FindClass("Ljava/lang/String;");
//    jmethodID ctorID = env->GetMethodID(strClass, "<init>", "([BLjava/lang/String;)V");
//    jbyteArray bytes = env->NewByteArray(strlen(pat));
//    env->SetByteArrayRegion(bytes, 0, strlen(pat), (jbyte*)pat);
//    jstring encoding = env->NewStringUTF("utf-8");
//    return (jstring)env->NewObject(strClass, ctorID, bytes, encoding);
//}

//jstring Java_com_example_hellojni_HelloJni_stringFromJNI( JNIEnv* env, jobject thiz )
//{
//    char info[10000] = { 0 };
//    sprintf(info, "%s\n", avcodec_configuration());
//    return (*env)->NewStringUTF(env, info);
//}

//void native_show_info(JNIEnv* env, char* info)
//{
//    /* 声明局部变量 */
//    jobject zoomimage;
//    jmethodID jId;
//    jclass jffmpegUtils;
//
//    /* 查找指定名称类 */
//    jffmpegUtils = (*env)->FindClass(env, "com/tanjinc/TMediaPlayer/utils/FFmpegUtils");
//
//    /*
//        获取方法id
//        (Ljava/lang/String)V表示此方法参数为String一个参数。返回值为void
//    */
//    jId = (*env)->GetMethodID(env, jffmpegUtils, "showInfo", "(Ljava/lang/String)V");
//    (*env)->CallVoidMethod(env,jId, char2jstring(env, pat));
//}

///home/tanjinc/tanjinc/secondWord/TMediaPlayer/app/src/main/java/com/tanjinc/tmediaplayer/FFmpegFragment.java
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
