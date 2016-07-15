package com.tanjinc.tmediaplayer.data;

import com.tanjinc.tmediaplayer.data.entity.DoubanMovieEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by tanjinc on 16-7-15.
 */
public interface DoubanMovieService {
    @GET("top250")
    Call<DoubanMovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);

    @GET("top250")
    Call<DoubanMovieEntity> getTopMovie();
}
