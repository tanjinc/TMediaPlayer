package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import com.tanjinc.tmediaplayer.R;

/**
 * Created by tanjincheng on 16/2/21.
 */
public class VideoPlayActivity extends Activity {
    private static final String TAG = "VideoPlayActivity";

    private Uri mUri;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Log.d(TAG, "video onCreate");
        setContentView(R.layout.player_root);
        getDataFromIntent(getIntent());
    }

    private void getDataFromIntent(Intent intent) {
        mUri = intent.getData();
        Log.d(TAG, "video mUri = " + mUri);
    }
}
