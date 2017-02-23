package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
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

    public static final String ACTION_CLOSE_SELF = "com.tanjinc.tmediaplayer.action_close_self";
    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private Uri mUri;
    private boolean sIsFloatWindow = true;
    private boolean mIsSwithchToFloat = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_root);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN );

        VideoPlayService.startServices(this, getIntent());

        KeyboardUtil.observeSoftKeyboard(this);

        IntentFilter intentFilter = new IntentFilter(ACTION_CLOSE_SELF);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_CLOSE_SELF:
                    mIsSwithchToFloat = true;
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        if (VideoPlayService.getPlayerServiceHelp() != null && !mIsSwithchToFloat) {
            VideoPlayService.getPlayerServiceHelp().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (VideoPlayService.getPlayerServiceHelp() != null && !mIsSwithchToFloat) {
            VideoPlayService.getPlayerServiceHelp().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        KeyboardUtil.unObserveSoftKeyboard(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "video onKeyDown() called with: " + "keyCode = [" + keyCode + "], event = [" + event + "]");
        if (VideoPlayService.getPlayerServiceHelp() != null) {
            VideoPlayService.getPlayerServiceHelp().onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }
}
