package com.tanjinc.tmediaplayer.player;

/**
 * Created by tanjincheng on 16/4/16.
 */
public class VideoUtils {

    public static String length2time(long length) {
        length /= 1000L;
        long minute = length / 60L;
        long hour = minute / 60L;
        long second = length % 60L;
        minute %= 60L;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
}
