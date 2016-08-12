package com.tanjinc.tmediaplayer;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by tanjincheng on 16/8/12.
 */
public class MainApplication extends Application {

        @Override public void onCreate() {
            super.onCreate();
            LeakCanary.install(this);
        }
}
