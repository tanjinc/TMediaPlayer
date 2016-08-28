package com.tanjinc.tmediaplayer.player;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.KeyboardUtil;

/**
 * Created by tanjincheng on 16/8/8.
 */
public class VideoPlayActivity2 extends AppCompatActivity {
    private static final String TAG = "VideoPlayActivity";

    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_root);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mRoot = (FrameLayout) findViewById(R.id.root);
        mPlayer = new MoviePlayer(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
        mPlayer.setUrl(getDataFromIntent(getIntent()));

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
        mPlayer.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mPlayer.onResume();
        super.onResume();
    }

    @Override
    protected void onStart() {
        mPlayer.start();
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayer.suspend();
        KeyboardUtil.unObserveSoftKeyboard(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
