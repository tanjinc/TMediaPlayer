package com.tanjinc.tmediaplayer;

import com.tanjinc.tmediaplayer.data.VideoData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanjincheng on 16/4/28.
 * This specifies the contract between the view and the presenter.
 */
public interface VideoContract {

    interface View {
        void showVideoList(ArrayList<VideoData> videoList);

        void setLoadingIndicator(boolean active);   //Loading

        void setPresenter(Presenter presenter);

    }

    interface Presenter {
        void loadVideo();

        void loadMoreVideo();

        void completeLoad();

        void start();
    }
}
