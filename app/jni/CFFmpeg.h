#ifdef __cplusplus
extern "C" {
#endif

#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include "libavdevice/avdevice.h"

#ifdef __cplusplus
} // endof extern "C"
#endif

class CFFmpeg {
    public:
     CFFmpeg();
     ~CFFmpeg();
     int openFile(char* format, char* fname);

     private:
        AVFormatContext *pFormatCtx;
        int videoIndex;
        int audioIndex;
        AVCodecContext *pVideoCodecCtx;
        AVCodecContext *pAudioCodecCtx;
        AVCodec *pVideoCodec;
        AVCodec *pAudioCodec;

        AVFrame *pVideoFrame;
        AVFrame *pAudioFrame;

};
