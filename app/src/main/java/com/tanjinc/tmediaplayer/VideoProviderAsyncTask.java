package com.tanjinc.tmediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.widget.ProgressBar;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class VideoProviderAsyncTask extends AsyncTask<VideoData, VideoData, ArrayList<VideoData>> {

    private Context mContext;
    private ArrayList<VideoData> mVideoList = new ArrayList<>();
    private LocalVideoFragment mFragmentUI;

    VideoProviderAsyncTask(Context context, LocalVideoFragment fragment) {
        mContext = context;
        mFragmentUI = fragment;
    }

    @Override
    protected ArrayList<VideoData> doInBackground(VideoData... params) {

        ContentResolver contentResolver = mContext.getContentResolver();

        String[] projection = new String[]{
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
        };

        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null,MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            cursor.moveToFirst();
            int fileNum = cursor.getCount();
            while (cursor.moveToNext()) {
                VideoData videoData = new VideoData();
                videoData.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));


                publishProgress(videoData);
                mVideoList.add(videoData);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return mVideoList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(VideoData... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<VideoData> videoDatas) {
        super.onPostExecute(videoDatas);
        mFragmentUI.notifyDataChange(videoDatas);
    }
}
