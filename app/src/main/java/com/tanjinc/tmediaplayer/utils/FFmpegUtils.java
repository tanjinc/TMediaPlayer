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
//    String cmdline = "ffmpeg -t 5 -ss 00:00:00 -r 10 -i http://v3.365yg.com/75808efd66db168fcf57eff436597c58/58b8e362/video/m/220be835fc4d4834879867ade7dd10a7feb114422500001ebab5f76e71/ -s 320x240 -b:v 1500k /sdcard/1.gif";

    public void transcode(String input, String output, String scale, int startTime, int duration) {
        String cmdline = "ffmpeg" +
                " -ss " + startTime +
                " -t " + duration +
                " -r " + 10 +
                " -i " + input +
                " -s " + scale +
                " " +output;
        Log.d(TAG, "video transcode() cmdline=" + cmdline);
        new FFmpegAsyncTask(cmdline).execute();
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
        String in, out;

        FFmpegAsyncTask(String cmdline) {
            argv = cmdline.split(" ");
            argc = argv.length;
            Log.d(TAG, "video : argc= " + argc + " cmd= \n" + cmdline);
        }
        FFmpegAsyncTask(String in, String out) {
            this.in = in;
            this.out = out;
        }

        @Override
        protected Void doInBackground(Object[] params) {
            Log.d(TAG, "video doInBackground() ");
            if (argc > 0) {
                Log.d(TAG, "video : argc= " + argc + " cmd= \n" + argv.toString());
                FFmpegUtils.getInstance().ffmpegcore(argc, argv);
            } else {
                FFmpegUtils.getInstance().transcodeJNI(in, out);
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
    private native int ffmpegcore(int argc, String[] argv);
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
