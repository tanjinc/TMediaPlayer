package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaMetadata;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by tanjinc on 16-2-22.
 */
public class MyVideoView extends SurfaceView implements MediaController.MediaPlayerControl{


    // all possible internal states
    private static final int STATE_ERROR              = -1;
    private static final int STATE_IDLE               = 0;
    private static final int STATE_PREPARING          = 1;
    private static final int STATE_PREPARED           = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED             = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.

    private int mCurrentState = STATE_IDLE;     //当前VideoView的状态
    private int mTargetState = STATE_IDLE;      //将要前往的状态

    private Uri mUri;
    private Context mContext;
    private MediaPlayer mMediaPlayer = null;

    private SurfaceHolder mSFHolder;

    private int mSurfaceHeight;
    private int mSurfaceWidth;
    private int mVideoHeight;
    private int mVideoWidth;

    public MyVideoView(Context context) {
        super(context);
        mContext = context;
        initVideoView();
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initVideoView();
    }


    private void initVideoView() {
        mVideoHeight = 0;
        mVideoWidth = 0;
        getHolder().addCallback(mSHCallback);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }

    private SurfaceHolder.Callback mSHCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            mSFHolder = holder;
            openVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mVideoHeight = height;
            mVideoWidth = width;
            boolean isValidState = (mTargetState == STATE_PLAYING);
            boolean isValidSize = (mVideoHeight == height && mVideoWidth == width);
            if (mMediaPlayer != null && isValidSize && isValidState) {
                mMediaPlayer.start();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mSFHolder = null;
            release(true);
        }
    };



    public void setVideoPath(String path){
        setVideoURI(Uri.parse(path));
    }

    public void setVideoURI(Uri uri) {
        mUri = uri;
        openVideo();
        requestLayout();
        invalidate();
    }

    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }

    private void openVideo() {
        if (mUri == null || mSFHolder == null) {
            return;
        }
        release(true);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setOnPreparedListener(mOnPrepareListener);
            mMediaPlayer.setDisplay(mSFHolder);
            mMediaPlayer.setDataSource(mUri.getPath());
            mMediaPlayer.prepareAsync();
            mCurrentState = STATE_PREPARING;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void release(boolean cleartargetstate) {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private MediaPlayer.OnPreparedListener mOnPrepareListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mVideoWidth = mp.getVideoWidth();
            mVideoHeight = mp.getVideoHeight();
            mCurrentState = STATE_PREPARED;
            if (mTargetState == STATE_PLAYING) {
                start();
            }

        }
    };

}
