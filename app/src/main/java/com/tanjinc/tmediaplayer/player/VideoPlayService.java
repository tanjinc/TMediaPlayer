package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.KeyboardUtil;

/**
 * Created by tanjinc on 16-8-4.
 */
public class VideoPlayService extends Service implements IPlayerServiceHelp{


    private static final String TAG = "VideoPlayerService" ;
    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private static Uri mUri;

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;

    private static IPlayerServiceHelp sPlayerServiceHelp;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();

        sPlayerServiceHelp = this;

        if (mRoot == null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mRoot = new FrameLayout(this);
            mRoot.setLayoutParams(params);
            mRoot.setBackground(null);
            mRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
        initFloatWindow();
        mPlayer = new MoviePlayer(getApplicationContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
        mPlayer.setUrl(mUri);

        KeyboardUtil.addSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
    }

    private KeyboardUtil.OnSoftKeyboardChangeListener mSoftKeyboardChangeListener = new KeyboardUtil.OnSoftKeyboardChangeListener() {
        @Override
        public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
            setEnableInput(visible);
        }
    };


    public static IPlayerServiceHelp getPlayerServiceHelp() {
        return sPlayerServiceHelp;
    }

    public static void startServices(Activity activity, Intent intent) {
        Log.d(TAG, "startServices: ");
        mUri = intent.getData();
        Intent mIntent = new Intent(activity, VideoPlayService.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startService(mIntent);
    }

    private void initFloatWindow() {
        Log.d(TAG, "initFloatWindow: ");
        mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        mWmParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWmParams.format = PixelFormat.RGBA_8888;
        mWmParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWmParams.gravity = Gravity.START | Gravity.TOP;
        mWmParams.x = 0;
        mWmParams.y = 0;
        mWmParams.alpha = 1.0f;
        mWmParams.token = mRoot.getApplicationWindowToken();
        mWindowManager.addView(mRoot, mWmParams);
    }

    public void setEnableInput(boolean enable) {
        if (enable) {
            mWmParams.flags = mWmParams.flags & ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            mWmParams.flags = mWmParams.flags | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        mWindowManager.updateViewLayout(mRoot, mWmParams);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        mPlayer.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        KeyboardUtil.removeSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "video onPause()");
        mPlayer.onPause();
        mRoot.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "video onResume()");
        mPlayer.onResume();
        mRoot.setVisibility(View.VISIBLE);
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void stop() {
        mPlayer.suspend();
        mWindowManager.removeView(mRoot);
        stopSelf();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_BACK:
                stop();
                break;
        }
        return false;
    }
}
