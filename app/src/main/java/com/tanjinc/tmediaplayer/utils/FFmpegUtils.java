package com.tanjinc.tmediaplayer.utils;

import android.util.Log;

/**
 * Created by tanjinc on 17-2-23.
 */
public class FFmpegUtils {
    private static final String TAG = "FFmpegUtils";

    private static FFmpegUtils sInstance;

    private FFmpegUtils() {}

    public static FFmpegUtils getInstance() {
        if (sInstance == null) {
            synchronized (FFmpegUtils.class) {
                if (sInstance == null) {
                    sInstance = new FFmpegUtils();
                }
            }
        }
        return sInstance;
    }

    public int performFFmpeg(int argc, String[] argv) {
        return ffmpegcore(argc, argv);
    }

    public void showInfo(String log) {
        Log.d(TAG, "video showInfo() " + log);
    }

    public String getVideoFormatInfo() {
        return avformatinfo();
    }

    //JNI
    private native String urlprotocolinfo();
    private native int ffmpegcore(int argc, String[] argv);
    private native String avformatinfo();
    private native String avcodecinfo();
    private native String avfilterinfo();
    private native String configurationinfo();

    static{
        System.loadLibrary("avutil-54");
        System.loadLibrary("swresample-1");
        System.loadLibrary("avcodec-56");
        System.loadLibrary("avformat-56");
        System.loadLibrary("swscale-3");
        System.loadLibrary("postproc-53");
        System.loadLibrary("avfilter-5");
        System.loadLibrary("avdevice-56");
        System.loadLibrary("ffmpeg_util");
    }
}
