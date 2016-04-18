package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.Util;

import javax.security.auth.login.LoginException;

/**
 * Created by tanjincheng on 16/4/15.
 */
public class ExoVideoView extends SurfaceView implements IVideoView, SurfaceHolder.Callback{
    private static final String TAG = "ExoVideoView";

    private ExoPlayer mExoPlayer;
    private MediaCodecVideoTrackRenderer mVideoTrackRenderer;
    private MediaCodecAudioTrackRenderer mAudioTrackRenderer;

    private SurfaceHolder mSFHolder;

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 265;

    private Context context;
    private Uri mUri;

    public ExoVideoView(Context context) {
        super(context);
        this.context = context;
        mSFHolder = getHolder();
    }


    private void initView(Uri uri) {
        mExoPlayer = ExoPlayer.Factory.newInstance(2);

        String userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DataSource dataSource = new DefaultUriDataSource(context, null, userAgent);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                uri, dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);

        mVideoTrackRenderer = new MediaCodecVideoTrackRenderer(
                context, sampleSource, MediaCodecSelector.DEFAULT, MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);
        mAudioTrackRenderer = new MediaCodecAudioTrackRenderer(
                sampleSource, MediaCodecSelector.DEFAULT);
        mExoPlayer.prepare(mVideoTrackRenderer, mAudioTrackRenderer);
        mExoPlayer.sendMessage(mVideoTrackRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSFHolder.getSurface());

        mExoPlayer.addListener(new ExoPlayer.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                Log.d(TAG, "video onPlayerStateChanged() called with: " + "playWhenReady = [" + playWhenReady + "], playbackState = [" + playbackState + "]");
            }

            @Override
            public void onPlayWhenReadyCommitted() {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.e(TAG, "video onPlayerError: " + error);
            }
        });
    }

    @Override
    public void start() {
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void release() {
        mExoPlayer.release();
    }

    @Override
    public void pause() {
        mExoPlayer.setPlayWhenReady(false);
    }

    @Override
    public int getDuration() {
        return (int) mExoPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return (int) mExoPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        mExoPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return mExoPlayer.getPlayWhenReady();
    }

    @Override
    public int getBufferPercentage() {
        return mExoPlayer.getBufferedPercentage();
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
    public void surfaceCreated(SurfaceHolder holder) {
        mSFHolder = holder;
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mExoPlayer.blockingSendMessage(mVideoTrackRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, null);
        mExoPlayer.release();
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = getDefaultSize(mVideoWidth, widthMeasureSpec);
//        int height = getDefaultSize(mVideoHeight, heightMeasureSpec);
//
//        if (mVideoHeight > 0 && mVideoWidth > 0) {
//            int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//            int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//            int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//            int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == MeasureSpec.EXACTLY) {
//                width = widthSpecSize;
//                height = heightSpecSize;
//                // mVideoWidth/mVideoHeight  > width/height
//                if (mVideoWidth * height > mVideoHeight * width) {
//                    //too wide
//                    height = mVideoHeight * width / mVideoWidth;
//
//                } else if (mVideoWidth * height < mVideoHeight * width){
//                    //too tall
//                    width = mVideoWidth * height / mVideoHeight;
//                }
//
//            } else if (widthSpecMode == MeasureSpec.EXACTLY) {
//                // only the width is fixed, adjust the height to match aspect ratio if possible
//                width = widthSpecSize;
//                height = width * mVideoHeight / mVideoWidth;
//                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
//                    // couldn't match aspect ratio within the constraints
//                    height = heightSpecSize;
//                }
//            } else if (heightSpecMode == MeasureSpec.EXACTLY) {
//                // only the height is fixed, adjust the width to match aspect ratio if possible
//                height = heightSpecSize;
//                width = height * mVideoWidth / mVideoHeight;
//                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
//                    // couldn't match aspect ratio within the constraints
//                    width = widthSpecSize;
//                }
//            } else {
//                // neither the width nor the height are fixed, try to use actual video size
//                width = mVideoWidth;
//                height = mVideoHeight;
//                if (heightSpecMode == MeasureSpec.AT_MOST && height > heightSpecSize) {
//                    // too tall, decrease both width and height
//                    height = heightSpecSize;
//                    width = height * mVideoWidth / mVideoHeight;
//                }
//                if (widthSpecMode == MeasureSpec.AT_MOST && width > widthSpecSize) {
//                    // too wide, decrease both width and height
//                    width = widthSpecSize;
//                    height = width * mVideoHeight / mVideoWidth;
//                }
//            }
//
//        } else {
//
//        }
//        setMeasuredDimension(width, height);
//    }

    @Override
    public void setUri(Uri uri) {
        mUri = uri;
        initView(mUri);
    }

    @Override
    public String getTitle() {
        String path = Uri.decode(mUri.getPath());
        return path != null ? path.substring(TextUtils.lastIndexOf(path, '/') + 1) : "";
    }
}
