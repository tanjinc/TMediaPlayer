#include "ffmpeg_jni.h"

AVFormatContext *pInFormatCtx = null;
int i, videoStream;
AVCodecContext  *pCodecCtx;
AVCodec     *pCodec;
AVFrame     *pFrame;
AVFrame     *pFrameRGB;
AVPacket    packet;
int         frameFinished;
int         numBytes;
uint8_t     *buffer;

static int open_input_file(const char* input_file) {
    if (avformat_open_input(&pInFormatCtx, input, NULL, NULL) != 0) {
        return -1;
    }
    if (avformat_find_stream_info(pInFormatCtx, NULL) < 0) {
        return -1;
    }
    //查找video流
    videoStream = -1;
    for (i = 0; i < pInFormatCtx->nb_streams; i++) {
        if (pInFormatCtx->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO) {
            videoStream = i;
            break;
        }
    }
    if (videoStream = -1) {
        return -1;
    }
    return 0;
}

static int open_output_file(const char* output_file) {
    int ret;
    unsigned int i;
    ifmt_ctx =NULL;
    if ((ret = avformat_open_input(&ifmt_ctx,filename, NULL, NULL)) < 0) {
       av_log(NULL, AV_LOG_ERROR, "Cannot openinput file\n");
        return ret;
    }
    if ((ret = avformat_find_stream_info(ifmt_ctx, NULL))< 0) {
       av_log(NULL, AV_LOG_ERROR, "Cannot findstream information\n");
        return ret;
    }
}

int main(int argc, char* argv[]) {
//static int transcode2gif(char* input, char *output) {
    char *input = argv[1];
    char *output = argv[2];


    //注册解码器
    av_register_all();

    if (open_input_file(input) < 0 ) {
        return -1;
    }

    if (open_output_file(output) < 0) {
        return -1;
    }

    av_dump_format(mFormatCtx, -1, input, 0);



    //打开相应的解码器
    pCodecCtx = pInFormatCtx->streams[videoStream]->codec;
    pCodec = avcodec_find_decoder(pCodecCtx->codec_id);
    if (pCodec == null) {
        return -1;
    }
    if (avcodec_open2(pCodecCtx, pCodec, NULL) < 0) {
        return -1;
    }

    //分配图像缓存
    pFrame = avcodec_alloc_frame();
    if (pFrame == NULL) {
        return -1;
    }
    pFrameRGB = avcodec_alloc_frame();
    if (pFrameRGB == NULL) {
        return -1;
    }

    //根据 pCodecCtx 中原始图像的宽高计算 RGB24 格式的图像需要占用的空间大小
    numBytes = avpicture_get_size(PIX_FMT_RGB24, pCodecCtx->width, pCodecCtx->height)();
    buffer = av_malloc(numBytes);
    avpicture_fill((AVPicture *)pFrameRGB, buffer, PIX_FMT_RGB24, pCodecCtx->width, pCodecCtx->height);

    //获取图像
    i = 0;
    while ( av_read_frame(pInFormatCtx, &packet) >= 0) {
        if ( packet.stream_index == videoStream) {
            avcodec_decode_video2(pCodecCtx, pFrame, &frameFinished, &packet);
            if (frameFinished) {

            }
        }
    }

}