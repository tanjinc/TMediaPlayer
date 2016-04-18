package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by tanjincheng on 16/2/20.
 *
 */
public class MoviePlayer extends FrameLayout {

    private Context mContext;

    private IVideoView mVideoView;
    private MovieController mController;

    private static class PlayerFactory {
        public enum PlayerType {
            EXOPLAYER, MEDIAPLAYER
        }

        /**
         * 获取Player Surfaceview中mediaplayer实现方式不同
         * @param context   context
         * @param type      类型：EXOPlayer，MediaPlayer
         * @return
         */
        public static IVideoView getPlayer(Context context, PlayerType type) {
            if (type == PlayerType.EXOPLAYER) {
                return new ExoVideoView(context);
            } else if (type == PlayerType.MEDIAPLAYER){
                return new MyVideoView(context);
            } else {
                return new MyVideoView(context);
            }
        }
    }

    public MoviePlayer(Context context) {
        super(context);
        mContext = context;
        setBackgroundColor(Color.BLACK);
        // 创建 VideoView
        mVideoView = PlayerFactory.getPlayer(context, PlayerFactory.PlayerType.EXOPLAYER);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        addView((View) mVideoView, layoutParams);

        // 创建 Controller
        mController = new MovieController(context, mVideoView);
        addView(mController, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setUrl(Uri uri) {
        mVideoView.setUri(uri);
//        mVideoView.start();

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
        mVideoView.start();
        mController.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
