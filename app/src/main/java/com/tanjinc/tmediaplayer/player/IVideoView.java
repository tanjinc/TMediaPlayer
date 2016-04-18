package com.tanjinc.tmediaplayer.player;

import android.net.Uri;

/**
 * Created by tanjincheng on 16/4/15.
 * 类似 MediaController接口
 */
public interface IVideoView {
    void    start();
    void    pause();
    int     getDuration();
    int     getCurrentPosition();
    void    seekTo(int pos);
    boolean isPlaying();
    int     getBufferPercentage();
    boolean canPause();
    boolean canSeekBackward();
    boolean canSeekForward();
    int     getAudioSessionId();
    public void release();
    public void setUri(Uri uri);
    public String getTitle();
}
