package com.tanjinc.tmediaplayer;

import com.tanjinc.tmediaplayer.data.VideoData;
import com.tanjinc.tmediaplayer.data.VideoRepository;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/4/28.
 */
public class LocalVideoPresenter implements VideoContract.Presenter{
    private VideoContract.View      mView;
    private VideoRepository mRepository;

    private boolean mFirstLoad = true;

    /**
     *
     * @param view              MVP--> V
     * @param videoRespository  MVP--> M
     */
    public LocalVideoPresenter(VideoContract.View view, VideoRepository videoRespository) {
        if (view != null) {
            mView = view;
            mView.setPresenter(this);
        }
        mRepository = videoRespository;
    }

    @Override
    public void start() {
        if (mFirstLoad) {
            loadVideo();
        }
        mFirstLoad = false;
    }

    @Override
    public void loadVideo() {
        mRepository.getVideoList(new VideoRepository.LoadVideoCallback() {
            @Override
            public void onVideoLoaded(ArrayList<VideoData> videos) {
                mView.showVideoList(videos);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void completeLoad() {

    }
}
