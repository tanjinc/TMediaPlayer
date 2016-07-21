package com.tanjinc.tmediaplayer.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/4/27.
 */
public interface VideoRepository {

    interface LoadVideoCallback {
        void onVideoLoaded(ArrayList<VideoData> videos);
        void onDataNotAvailable();
    }

    void getVideoList(LoadVideoCallback callback);

    void getMoreVideo(LoadVideoCallback callback);

}
