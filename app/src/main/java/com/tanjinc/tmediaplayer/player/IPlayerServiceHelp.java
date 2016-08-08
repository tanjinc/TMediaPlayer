package com.tanjinc.tmediaplayer.player;

import android.view.KeyEvent;

/**
 * Created by tanjincheng on 16/8/8.
 */
interface IPlayerServiceHelp {
    void onPause();
    void onResume();
    void start();
    void stop();

    boolean onKeyDown(int keyCode, KeyEvent event);
}
