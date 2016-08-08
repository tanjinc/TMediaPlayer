package com.tanjinc.tmediaplayer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by tanjincheng on 16/4/16.
 */
public class VideoUtils {
    private static final String TAG = "VideoUtils";
    public static final int PLAYER_TYPE_EXO = 0;
    public static final int PLAYER_TYPE_DEF = 1;

    public enum PlayState {
        STATE_ERROR ,
        STATE_IDLE ,
        STATE_PREPARING,
        STATE_PREPARED,
        STATE_PLAYING,
        STATE_PAUSED ,
        STATE_PLAYBACK_COMPLETED;
    }

    public static String length2time(long length) {
        length /= 1000L;
        long minute = length / 60L;
        long hour = minute / 60L;
        long second = length % 60L;
        minute %= 60L;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public static void setPlayerType(Context context, int type) {
        Log.d(TAG, "video setPlayerType() called with:  type = [" + type + "]");
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanjinc", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("video_player_type", type);
        editor.apply();
    }

    public static int getPlayerType(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanjinc", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("video_player_type", 1);
    }
}
