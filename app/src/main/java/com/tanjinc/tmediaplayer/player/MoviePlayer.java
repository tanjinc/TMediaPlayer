package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tanjinc.tmediaplayer.utils.VideoUtils;

/**
 * Created by tanjincheng on 16/2/20.
 *
 */
public class MoviePlayer extends FrameLayout {

    private static final String TAG = "MoviePlayer";
    private Activity mActivity;


    private IVideoView mVideoView;
    private MovieController mController;
    private Handler mMainHandler;

        /**
         * 获取Player Surfaceview中mediaplayer实现方式不同
         * @param context   context
         * @param type      类型：EXOPlayer，MediaPlayer
         * @return
         */
    public IVideoView getPlayer(Context context, int type) {
        if (type == VideoUtils.PLAYER_TYPE_EXO) {
            return new ExoVideoView(context, mMainHandler);
        } else if (type == VideoUtils.PLAYER_TYPE_DEF){
            return new MyVideoView(context);
        } else {
            return new MyVideoView(context);
        }
    }

    public MoviePlayer(Context context) {
        super(context);
//        mActivity = (Activity)context;
        mMainHandler = new Handler();
        setBackgroundColor(Color.BLACK);
        // 创建 VideoView

        mVideoView = getPlayer(context, VideoUtils.getPlayerType(getContext()));
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView((View) mVideoView, layoutParams);

        // 创建 Controller
        mController = new MovieController(context, mVideoView);
        addView(mController, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mVideoView.setCompletelistener(new IVideoView.CompleteListener() {
            @Override
            public void onCompletion() {
//                mActivity.finish();
            }
        });

        mVideoView.setErrorListener(new IVideoView.ErrorListener() {
            @Override
            public boolean onError(int errNo, String msg) {
                Log.e(TAG, "video onError() called with: " + "errNo = [" + errNo + "], msg = [" + msg + "]");
                return false;
            }
        });
    }

    public void setUrl(Uri uri) {
        mVideoView.setUri(uri);
        mController.setTitle(mVideoView.getTitle());
        mController.showController();
    }

    public void suspend() {
        mController.pause();
        mVideoView.release();
    }

    public void onPause() {
        mController.pause();
        mVideoView.pause();
    }

    public void onResume() {
        start();
    }

    public void start() {
        mVideoView.start();
        mController.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mActivity.finish();
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
