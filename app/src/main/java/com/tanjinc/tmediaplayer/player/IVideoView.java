package com.tanjinc.tmediaplayer.player;

import android.net.Uri;

/**
 * Created by tanjincheng on 16/4/15.
 * 类似 MediaController接口
 */
public interface IVideoView {

    public interface CompleteListener {
        void onCompletion();
    }

    interface ErrorListener {
        boolean onError(int errNo, String msg);
    }

    void    start();
    void    pause();
    int     getDuration();
    int     getVideoWidth();
    int     getVideoHeight();
    int     getCurrentPosition();
    long    getBufferedPosition();
    void    seekTo(int pos);
    boolean isPlaying();
    int     getBufferPercentage();
    boolean canPause();
    boolean canSeekBackward();
    boolean canSeekForward();
    int     getAudioSessionId();
    public void setCompletelistener(CompleteListener listener);
    public void setErrorListener(ErrorListener errorListener);
    public void release();
    public void setUri(Uri uri);
    Uri getUri();
    public String getTitle();
}
