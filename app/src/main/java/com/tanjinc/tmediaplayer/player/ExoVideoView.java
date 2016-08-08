package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecTrackRenderer;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.audio.AudioTrack;
import com.google.android.exoplayer.dash.mpd.MediaPresentationDescription;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.ManifestFetcher;
import com.google.android.exoplayer.util.Util;
import com.tanjinc.tmediaplayer.utils.VideoUtils;

/**
 * Created by tanjincheng on 16/4/15.
 */
public class ExoVideoView extends SurfaceView implements IVideoView, SurfaceHolder.Callback,
        ExoPlayer.Listener, MediaCodecVideoTrackRenderer.EventListener, MediaCodecAudioTrackRenderer.EventListener{
    private static final String TAG = "ExoVideoView";

    private ExoPlayer mExoPlayer;
    private MediaCodecVideoTrackRenderer mVideoTrackRenderer;
    private MediaCodecAudioTrackRenderer mAudioTrackRenderer;

    private SurfaceHolder mSFHolder;

    private static final int BUFFER_SEGMENT_COUNT = 265;

    private static final int BUFFER_SEGMENT_SIZE = 1024 * 1024;
    private static final int VIDEO_BUFFER_SEGMENTS = 200;
    private static final int AUDIO_BUFFER_SEGMENTS = 54;
    private static final int TEXT_BUFFER_SEGMENTS = 2;
    private static final int LIVE_EDGE_LATENCY_MS = 30000;

    private Context context;
    private Uri mUri;
    private int mVideoWidth;
    private int mVideoHeight;
    private int mSurfaceWidth;
    private int mSurfaceHeight;

    private VideoUtils.PlayState mCurrentState = VideoUtils.PlayState.STATE_IDLE;
    private VideoUtils.PlayState mTargetState = VideoUtils.PlayState.STATE_IDLE;

    // listener
    private CompleteListener mCompleteLst;
    private ErrorListener mErrorLst;
    private Handler mHandler;
    private String userAgent ;//= Util.getUserAgent(context, "ExoPlayerDemo");

    private  ManifestFetcher<MediaPresentationDescription> manifestFetcher;


    public ExoVideoView(Context context, Handler mainLooper) {
        super(context);
        this.context = context;
        mHandler = mainLooper;
        userAgent = Util.getUserAgent(context, "ExoPlayerDemo");
        mSFHolder = getHolder();
    }


    public void onRenderers(MediaCodecVideoTrackRenderer videoTrackRenderer, MediaCodecAudioTrackRenderer audioTrackRenderer) {
        mVideoTrackRenderer = videoTrackRenderer;
        mAudioTrackRenderer = audioTrackRenderer;
        mExoPlayer.prepare(mVideoTrackRenderer, mAudioTrackRenderer);
        mExoPlayer.sendMessage(mVideoTrackRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSFHolder.getSurface());

    }

    public Handler getMainHandler() {
        return mHandler;
    }

    private void initView(Uri uri) {
        mExoPlayer = ExoPlayer.Factory.newInstance(2);


        Allocator allocator = new DefaultAllocator(BUFFER_SEGMENT_SIZE);
        DataSource dataSource = new DefaultUriDataSource(context, null, userAgent);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource(
                uri, dataSource, allocator, BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);

        // 尽管google不建议使用FrameworkSampleSource，但它是使用原生MediaExtracor，能够解析FLV文件。
        // 而google推荐的ExtractorSampleSource 却不能播放FLV，并且也没有计划推出新的Extractor支持flv
//        FrameworkSampleSource sampleSource = new FrameworkSampleSource(context, uri, null);
        mVideoTrackRenderer = new MediaCodecVideoTrackRenderer(context, sampleSource, MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT, 0, mHandler, this, 100);
        mAudioTrackRenderer = new MediaCodecAudioTrackRenderer(
                sampleSource, MediaCodecSelector.DEFAULT);

        mExoPlayer.prepare(mVideoTrackRenderer, mAudioTrackRenderer);
        mExoPlayer.sendMessage(mVideoTrackRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, mSFHolder.getSurface());

        DashRendererBuilder dashRendererBuilder = new DashRendererBuilder(context, userAgent, uri.getPath());
        dashRendererBuilder.buildRenderers(this);
        mExoPlayer.addListener(this);
        mExoPlayer.setPlayWhenReady(true);
    }

    private boolean isInPlaybackState() {
        return (mExoPlayer != null &&
                mCurrentState != VideoUtils.PlayState.STATE_ERROR &&
                mCurrentState != VideoUtils.PlayState.STATE_IDLE &&
                mCurrentState != VideoUtils.PlayState.STATE_PREPARING);
    }

    @Override
    public void start() {
        if (isInPlaybackState()) {
            Log.d(TAG, "video start()");
            mExoPlayer.setPlayWhenReady(true);
            mCurrentState = VideoUtils.PlayState.STATE_PLAYING;
        }
        mTargetState = VideoUtils.PlayState.STATE_PLAYING;
    }

    @Override
    public void release() {
        Log.d(TAG, "video release()");
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
    public long getBufferedPosition() {
        return mExoPlayer.getBufferedPosition();
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
    public void setCompletelistener(CompleteListener listener) {
        mCompleteLst = listener;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        mErrorLst = errorListener;
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSFHolder = holder;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "video surfaceChanged() called with: " + "holder = [" + holder + "], format = [" + format + "], width = [" + width + "], height = [" + height + "]");
        mSurfaceHeight = height;
        mSurfaceWidth = width;
        if (mVideoHeight == mSurfaceHeight && mVideoWidth == mSurfaceWidth) {
            start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mExoPlayer.blockingSendMessage(mVideoTrackRenderer, MediaCodecVideoTrackRenderer.MSG_SET_SURFACE, null);
        mExoPlayer.release();
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

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d(TAG, "video onPlayerStateChanged() called with: " + "playWhenReady = [" + playWhenReady + "], playbackState = [" + playbackState + "]");

        if (playbackState == ExoPlayer.STATE_READY) {
            start();
        }
        if (playbackState == ExoPlayer.STATE_ENDED) {
            if (mCompleteLst != null) {
                mCompleteLst.onCompletion();
            }
        }
    }

    @Override
    public void onPlayWhenReadyCommitted() {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (mErrorLst != null) {
            Log.e(TAG, "video onPlayerError: " + error );
            mErrorLst.onError(0, error.toString());
        }
    }

    @Override
    public void onDroppedFrames(int count, long elapsed) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "video onVideoSizeChanged: width:" + width + " height:" + height );
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoHeight > 0 && mVideoWidth > 0) {
            getHolder().setFixedSize(mVideoWidth, mVideoHeight);
        }
    }

    @Override
    public void onDrawnToSurface(Surface surface) {

    }

    @Override
    public void onDecoderInitializationError(MediaCodecTrackRenderer.DecoderInitializationException e) {

    }

    @Override
    public void onCryptoError(MediaCodec.CryptoException e) {

    }

    @Override
    public void onDecoderInitialized(String decoderName, long elapsedRealtimeMs, long initializationDurationMs) {

    }

    @Override
    public void onAudioTrackInitializationError(AudioTrack.InitializationException e) {

    }

    @Override
    public void onAudioTrackWriteError(AudioTrack.WriteException e) {

    }

    @Override
    public void onAudioTrackUnderrun(int bufferSize, long bufferSizeMs, long elapsedSinceLastFeedMs) {

    }
}
