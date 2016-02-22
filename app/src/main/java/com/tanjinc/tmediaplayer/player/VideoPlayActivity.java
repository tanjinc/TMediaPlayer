package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tanjinc.tmediaplayer.R;

/**
 * Created by tanjincheng on 16/2/21.
 */
public class VideoPlayActivity extends Activity {
    private static final String TAG = "VideoPlayActivity";

    private MoviePlayer mPlayer;
    private FrameLayout mRoot;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_root);
        mRoot = (FrameLayout) findViewById(R.id.root);
        mUri = getDataFromIntent(getIntent());
        initPlayer();

        mPlayer.setUrl(mUri);

    }


    private void initPlayer() {
        mPlayer = new MoviePlayer(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mRoot.addView(mPlayer, lp);
    }

    private Uri getDataFromIntent(Intent intent) {
        Log.d(TAG, "video mUri = " + mUri);
        return intent.getData();
    }
}
