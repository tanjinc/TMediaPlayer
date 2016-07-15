package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.constant.WBConstants;
import com.tanjinc.tmediaplayer.R;

/**
 * Created by tanjincheng on 16/2/21.
 */
public class VideoPlayActivity extends AppCompatActivity{
    private static final String TAG = "VideoPlayActivity";

    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_root);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(Window.FEATURE_ACTION_BAR_OVERLAY); //导致ANR
        mRoot = (FrameLayout) findViewById(R.id.root);
        mPlayer = new MoviePlayer(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
        mPlayer.setUrl(getDataFromIntent(getIntent()));
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
        mPlayer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayer.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "video onKeyDown() called with: " + "keyCode = [" + keyCode + "], event = [" + event + "]");
        return mPlayer != null && mPlayer.onKeyDown(keyCode, event);
    }

}
