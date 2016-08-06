package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tanjinc.tmediaplayer.R;

/**
 * Created by tanjinc on 16-8-4.
 */
public class VideoPlayerService extends Service {


    private static final String TAG = "VideoPlayerService" ;
    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private boolean sIsFloatWindow = true;
    private static Uri mUri;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: ");
        super.onCreate();

        mRoot = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.player_root, null);

        initFloatWindow();
        mPlayer = new MoviePlayer(getApplicationContext());
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
        mPlayer.setUrl(mUri);
    }


    public static void startServices(Activity activity, Intent intent) {
        Log.d(TAG, "startServices: ");
        mUri = intent.getData();
        Intent mIntent = new Intent(activity, VideoPlayerService.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startService(mIntent);
    }

    private void initFloatWindow() {
        Log.d(TAG, "initFloatWindow: ");
        WindowManager windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED;
        wmParams.gravity = Gravity.START | Gravity.TOP;
//        wmParams.width = screenW;
//        wmParams.height = screenH;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.alpha = 1.0f;
        wmParams.token = mRoot.getApplicationWindowToken();

        mRoot.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        windowManager.addView(mRoot, wmParams);
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
}
