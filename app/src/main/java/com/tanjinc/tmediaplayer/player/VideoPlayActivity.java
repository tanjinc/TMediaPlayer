package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.KeyboardUtil;
import com.tanjinc.tmediaplayer.utils.ScreenUtil;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/21.
 */
public class VideoPlayActivity extends AppCompatActivity{
    private static final String TAG = "VideoPlayActivity";

    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private Uri mUri;
    private boolean sIsFloatWindow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_root);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        mRoot = (FrameLayout) findViewById(R.id.root);
//        mRoot = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.player_root, null);
//
//        if (sIsFloatWindow) {
//            initFloatWindow();
//        } else {
//            setContentView(R.layout.player_root);
//        }
//
//        mPlayer = new MoviePlayer(this);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        mRoot.addView(mPlayer, lp);
//        mPlayer.setUrl(getDataFromIntent(getIntent()));


//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        Window window = getWindow();
//        if (window != null) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
        VideoPlayerService.startServices(this, getIntent());
//        finish();

        KeyboardUtil.observeSoftKeyboard(this);
    }

    private Uri getDataFromIntent(Intent intent) {
        mUri = intent.getData();
        Log.d(TAG, "video mUri = " + mUri);
        return mUri;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
//        mPlayer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        mPlayer.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyboardUtil.unObserveSoftKeyboard(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mPlayer != null && mPlayer.onKeyDown(keyCode, event);
    }


}
