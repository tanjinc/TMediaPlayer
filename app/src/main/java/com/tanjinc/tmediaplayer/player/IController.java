package com.tanjinc.tmediaplayer.player;

/**
 * Created by tanjincheng on 16/3/30.
 */
public interface IController {
    void showController();
    void hideController();

    void setTitle(String title);
    void pause();
    void start();
    void seek(int seekTo);
    void replay();
    void stop();
}
