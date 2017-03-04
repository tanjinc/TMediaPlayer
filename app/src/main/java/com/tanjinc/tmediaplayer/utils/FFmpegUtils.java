package com.tanjinc.tmediaplayer.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * Created by tanjinc on 17-2-23.
 */
public class FFmpegUtils {
    private static final String TAG = "FFmpegUtils";

//    private static FFmpegUtils sInstance;
    private static OnCompleteListener mOnCompleteListener;

    public interface OnCompleteListener {
        void onComplete();
    }

//    private FFmpegUtils() {}
//
//    public static FFmpegUtils getInstance() {
//        if (sInstance == null) {
//            synchronized (FFmpegUtils.class) {
//                if (sInstance == null) {
//                    sInstance = new FFmpegUtils();
//                }
//            }
//        }
//        return sInstance;
//    }

    public static void setOnCompleteListener(OnCompleteListener listener) {
        WeakReference<OnCompleteListener> listenerWeakReference=new WeakReference<OnCompleteListener>(listener);
        mOnCompleteListener = listenerWeakReference.get();
    }


    public static void video2Gif(String input, String output, String scale, String biteRate, String startTime, int duration) {
        StringBuilder cmdString = new StringBuilder();
        cmdString.append("ffmpeg");
        cmdString.append(" -ss "+ startTime);
        cmdString.append(" -t " + duration);
        cmdString.append(" -r " + 10);
        cmdString.append(" -i " + input);
        cmdString.append(" -s " + scale);
        cmdString.append(" -b:v " + biteRate);   //设置码率
        cmdString.append(" " + output);
        new FFmpegAsyncTask(cmdString.toString()).execute();
    }
    public static int performFFmpeg(String cmdLine) {
        new FFmpegAsyncTask(cmdLine).execute();
        return 0;
    }

    public void showInfo(String log) {
        Log.d(TAG, "video showInfo() " + log);
    }

    public String getVideoFormatInfo() {
        return avformatinfo();
    }

    static class FFmpegAsyncTask extends AsyncTask{
        int argc = 0;
        String[] argv=null;
        String in, out;

        FFmpegAsyncTask(String cmdline) {
            argv = cmdline.split(" ");
            argc = argv.length;
            Log.d(TAG, "video : argc= " + argc + " cmd= \n" + cmdline);
        }

        @Override
        protected Void doInBackground(Object[] params) {
            Log.d(TAG, "video doInBackground() ");
            if (argc > 0) {
                FFmpegUtils.ffmpegcore(argc, argv);
            }
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
    private static native int ffmpegcore(int argc, String[] argv);
    private native int transcodeJNI(String input, String output);
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
