package com.tanjinc.tmediaplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
    private LocalVideoFragment mFragment;

    VideoProviderAsyncTask(Context context, LocalVideoFragment fragment) {
        mContext = context;
        mFragment = fragment;
    }

    @Override
    protected ArrayList<VideoData> doInBackground(VideoData... params) {

        ContentResolver contentResolver = mContext.getContentResolver();

        String[] projection = new String[]{
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Thumbnails.DATA
        };

        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection,
                null, null,MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor != null) {
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                VideoData videoData = new VideoData();
                videoData.setName(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE)));
                videoData.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA)));

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inDither = false;
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id,  MediaStore.Images.Thumbnails.MICRO_KIND, options);
                videoData.setThumbnail(bitmap);

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
        mFragment.getVideoList(videoDatas);
    }
}
