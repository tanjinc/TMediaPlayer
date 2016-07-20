package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.data.VideoData;
import com.tanjinc.tmediaplayer.utils.ImageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tanjinc on 16-7-15.
 */
public class DoubanMovieFragment extends Fragment implements VideoContract.View {

    @BindView(R.id.online_video_layout)
    RecyclerView mRecyclerView;
    private VideoContract.Presenter mPresenter;

    private OnlineVideoAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_douban_layout, container, false);
        ButterKnife.bind(this, view);
        mAdapter = new OnlineVideoAdapter(getContext());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        ButterKnife.setDebug(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showVideoList(ArrayList<VideoData> videoList) {
        mAdapter.notifyDataChange(videoList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        mPresenter = presenter;
    }


    class OnlineVideoAdapter extends RecyclerView.Adapter<OnlineVideoAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<VideoData> mVideoDatas;

        public OnlineVideoAdapter(Context context) {
            mContext = context;
        }

        public void notifyDataChange(ArrayList<VideoData> videoDatas) {
            mVideoDatas = videoDatas;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.online_view_item_layout, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (mVideoDatas != null && position < mVideoDatas.size()) {
                VideoData videoData = mVideoDatas.get(position);
                ImageUtil.loadLoalImage(mContext, videoData.getThumbPath(), 200, 200, holder.mVideoImage, 10);
                holder.mVideoTitle.setText(videoData.getName());
            }
        }

        @Override
        public int getItemCount() {
            return mVideoDatas != null ? mVideoDatas.size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.video_image)
            ImageView mVideoImage;
            @BindView(R.id.video_title)
            TextView mVideoTitle;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
