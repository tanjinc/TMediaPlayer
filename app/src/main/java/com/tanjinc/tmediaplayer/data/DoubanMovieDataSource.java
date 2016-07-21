package com.tanjinc.tmediaplayer.data;

import android.content.Context;
import android.util.Log;

import com.tanjinc.tmediaplayer.data.entity.DoubanMovieEntity;
import com.tanjinc.tmediaplayer.data.entity.Subject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tanjinc on 16-7-15.
 */
public class DoubanMovieDataSource implements VideoRepository {

    private static final String BASE_URL = "https://api.douban.com/v2/movie/";
    private static final String TAG = "DoubanMovieDataSource";

    private ArrayList<VideoData> mVideoData = new ArrayList<>();

    private Retrofit mRetrofit;
    private DoubanMovieService mDoubanMovieService;

    private int mLoadStart = 0;
    private int mLoadCount = 10;

    public DoubanMovieDataSource(Context context) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mDoubanMovieService = mRetrofit.create(DoubanMovieService.class);
    }

    @Override
    public void getVideoList(final LoadVideoCallback callback) {

        if (mVideoData != null && mVideoData.size() != 0) {
            return;
        }
        Call<DoubanMovieEntity> repos = mDoubanMovieService.getTopMovie(mLoadStart, mLoadCount);
        repos.enqueue(new Callback<DoubanMovieEntity>() {
            @Override
            public void onResponse(Call<DoubanMovieEntity> call, Response<DoubanMovieEntity> response) {
                DoubanMovieEntity data = response.body();
                for (Subject subject: data.getSubjects()) {
                    VideoData videoData = new VideoData();
                    videoData.setName(subject.getTitle());
                    videoData.setThumbPath((subject.getImages().getMedium()));
                    videoData.setPath(subject.getOriginal_title());
                    mVideoData.add(videoData);
                }
                callback.onVideoLoaded(mVideoData);
            }

            @Override
            public void onFailure(Call<DoubanMovieEntity> call, Throwable t) {
                Log.e(TAG, "失败: ", t);
            }
        });
    }

    @Override
    public void getMoreVideo(final LoadVideoCallback callback) {
        mLoadStart += mLoadCount;
        mDoubanMovieService.getTopMovie(mLoadStart, mLoadCount).enqueue(new Callback<DoubanMovieEntity>() {
            @Override
            public void onResponse(Call<DoubanMovieEntity> call, Response<DoubanMovieEntity> response) {
                DoubanMovieEntity data = response.body();
                for (Subject subject: data.getSubjects()) {
                    VideoData videoData = new VideoData();
                    videoData.setName(subject.getTitle());
                    videoData.setThumbPath((subject.getImages().getMedium()));
                    videoData.setPath(subject.getOriginal_title());
                    mVideoData.add(videoData);
                }
                callback.onVideoLoaded(mVideoData);
            }

            @Override
            public void onFailure(Call<DoubanMovieEntity> call, Throwable t) {
                Log.e(TAG, "失败: ", t);
            }
        });
    }
}
