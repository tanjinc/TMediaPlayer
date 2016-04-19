package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaMetadata;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by tanjinc on 16-2-22.
 */
public class MyVideoView extends SurfaceView implements IVideoView{


    // all possible internal states
    private static final int STATE_ERROR              = -1;
    private static final int STATE_IDLE               = 0;
    private static final int STATE_PREPARING          = 1;
    private static final int STATE_PREPARED           = 2;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_PAUSED             = 4;
    private static final int STATE_PLAYBACK_COMPLETED = 5;
    private static final String TAG = "MyVideoView";

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
    private CompleteListener mCompleteListener;
    private ErrorListener mErrorLst;

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
        Log.d(TAG, "video initVideoView() called with: " + "");
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


    @Override
    public void start() {
        if (isInPlaybackState()) {
            mMediaPlayer.start();
            mCurrentState = STATE_PLAYING;
        }
        mTargetState = STATE_PLAYING;
    }

    @Override
    public void release() {
        mMediaPlayer.release();
        mTargetState = STATE_IDLE;
    }

    @Override
    public void pause() {
        mMediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer != null && (isInPlaybackState())? mMediaPlayer.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer != null && (isInPlaybackState())? mMediaPlayer.getCurrentPosition() : 0;
    }

    @Override
    public void seekTo(int pos) {
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return (isInPlaybackState()) && mMediaPlayer.isPlaying();
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

    @Override
    public void setCompletelistener(CompleteListener listener) {
        mCompleteListener = listener;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        mErrorLst = errorListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);

        if (mVideoHeight > 0 && mVideoWidth > 0) {
            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
                width = widthSpecSize;
                height = heightSpecSize;
                // mVideoWidth/mVideoHeight  > width/height
                if (mVideoWidth * height > mVideoHeight * width) {
                    //too wide
                    height = mVideoHeight * width / mVideoWidth;

                } else if (mVideoWidth * height < mVideoHeight * width){
                    //too tall
                    width = mVideoWidth * height / mVideoHeight;
                }

            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize;
                height = width * mVideoHeight / mVideoWidth;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize;
                }
            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize;
                width = height * mVideoWidth / mVideoHeight;
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize;
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth;
                height = mVideoHeight;
                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize;
                    width = height * mVideoWidth / mVideoHeight;
                }
                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize;
                    height = width * mVideoHeight / mVideoWidth;
                }
            }

        } else {

        }
        setMeasuredDimension(width, height);
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


    private MediaPlayer.OnCompletionListener mOncompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mCompleteListener != null) {
                mCompleteListener.onCompletion();
            }
        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            if (mErrorLst != null) {
                mErrorLst.onError(what, "error");
            }
            return false;
        }
    };
    @Override
    public void setUri(Uri uri) {
        mUri = uri;
        openVideo();
        requestLayout();
        invalidate();
    }
    @Override
    public String getTitle() {
        String path = Uri.decode(mUri.getPath());
        return path != null ? path.substring(TextUtils.lastIndexOf(path, '/') + 1) : "";
    }
}
