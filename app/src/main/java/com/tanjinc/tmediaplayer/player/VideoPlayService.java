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
import com.tanjinc.tmediaplayer.utils.WindowUtil;

/**
 * Created by tanjinc on 16-8-4.
 */
public class VideoPlayService extends Service implements IPlayerServiceHelp{


    private static final String TAG = "VideoPlayerService" ;
    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private static Uri mUri;

    private static IPlayerServiceHelp sPlayerServiceHelp;

    private KeyboardUtil.OnSoftKeyboardChangeListener mSoftKeyboardChangeListener;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();
        sPlayerServiceHelp = this;
        initView();
        initRegister();
    }

    private void initView() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot = new FrameLayout(this);
        mRoot.setLayoutParams(params);
        mRoot.setBackground(null);
        mRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mPlayer = new MoviePlayer(getApplicationContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
        mPlayer.setUrl(mUri);

        WindowUtil.getInstance().initWindow(getApplicationContext(),mRoot);
    }

    private void initRegister() {
        mSoftKeyboardChangeListener = new KeyboardUtil.OnSoftKeyboardChangeListener() {
            @Override
            public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
                WindowUtil.getInstance().setEnableInput(visible);
            }
        };
        KeyboardUtil.addSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
    }

    private void unRegister() {
        KeyboardUtil.removeSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
    }



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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        WindowUtil.getInstance().showWindow();
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
        unRegister();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "video onPause()");
        mPlayer.onPause();
        WindowUtil.getInstance().hideWindow();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "video onResume()");
        mPlayer.onResume();
        WindowUtil.getInstance().showWindow();
    }

    @Override
    public void start() {
        mPlayer.start();
    }

    @Override
    public void stop() {
        Log.d(TAG, "video stop: ");
        mPlayer.suspend();
        WindowUtil.getInstance().hideWindow();
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
