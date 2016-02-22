package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by tanjinc on 16-2-22.
 */
public class MyVideoView extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mSFHolder;
    private MediaPlayer mMediaPlayer;
    private Uri mUri;

    public MyVideoView(Context context) {
        super(context);
        initVideoView();
    }

    private void initVideoView() {
        getHolder().addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();
    }
    public void setUri(Uri uri) {
        mUri = uri;
        openVideo();
    }

    public void start() {
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
    }

    private void openVideo() {
        if (mUri == null || mSFHolder == null) {
            return;
        }
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDisplay(mSFHolder);
            mMediaPlayer.setDataSource(mUri.getPath());
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSFHolder = holder;
        openVideo();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
