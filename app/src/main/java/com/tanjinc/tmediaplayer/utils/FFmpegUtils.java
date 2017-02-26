package com.tanjinc.tmediaplayer.utils;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by tanjinc on 17-2-23.
 */
public class FFmpegUtils {
    private static final String TAG = "FFmpegUtils";

    private static FFmpegUtils sInstance;
    private OnCompleteListener mOnCompleteListener;

    public interface OnCompleteListener {
        void onComplete();
    }

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

    public void setOnCompleteListener(OnCompleteListener listener) {
        mOnCompleteListener = listener;
    }


    public void video2Gif(String videoName, String gifName, int duration, String startTime) {
        String cmdline = "ffmpeg -t " + duration + " -ss " + startTime + " -i " + videoName + " " + gifName;
        performFFmpeg(cmdline);
    }

    public int performFFmpeg(String cmdLine) {
        new FFmpegAsyncTask(cmdLine).execute();
        return 0;
    }

    public void showInfo(String log) {
        Log.d(TAG, "video showInfo() " + log);
    }

    public String getVideoFormatInfo() {
        return avformatinfo();
    }

    class FFmpegAsyncTask extends AsyncTask{
        int argc = 0;
        String[] argv=null;

        FFmpegAsyncTask(String cmdline) {
            argv = cmdline.split(" ");
            argc = argv.length;
            Log.d(TAG, "video : argc= " + argc + " cmd= \n" + cmdline);

        }

        @Override
        protected Void doInBackground(Object[] params) {
            Log.d(TAG, "video doInBackground() ");
            Log.d(TAG, "video : argc= " + argc + " cmd= \n" + argv.toString());
            FFmpegUtils.getInstance().ffmpegcore(argc,argv);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            if (mOnCompleteListener != null) {
                mOnCompleteListener.onComplete();
            }
        }

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
