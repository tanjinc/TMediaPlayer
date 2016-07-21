package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class DoubanMovieFragment extends Fragment implements VideoContract.View, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "DoubanMovieFragment";
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private VideoContract.Presenter mPresenter;

    private OnlineVideoAdapter mAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private int lastVisibleItem;

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
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.loadMoreVideo();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
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

    @Override
    public void onRefresh() {
        mPresenter.loadVideo();
        mSwipeRefreshLayout.setRefreshing(false);
    }


    class OnlineVideoAdapter extends RecyclerView.Adapter<OnlineVideoAdapter.ViewHolder> {

        private Context mContext;
        private ArrayList<VideoData> mVideoDatas;
        private int TYPE_FOOTER = 100;
        private int TYPE_ITEM = 101;

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
                return new ViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (holder.mViewType == TYPE_ITEM) {
                if (mVideoDatas != null && position < mVideoDatas.size()) {
                    VideoData videoData = mVideoDatas.get(position);
                    ImageUtil.loadLoalImage(mContext, videoData.getThumbPath(), 200, 200, holder.mVideoImage, 10);
                    holder.mVideoTitle.setText(videoData.getName());
                }
            } else {
                holder.mVideoTitle.setText("上拉加载更多...");
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (position +1 == getItemCount()) {
                return TYPE_FOOTER;
            } else {
                return TYPE_ITEM;
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

            public int mViewType;

            ViewHolder(View view, int viewType) {
                super(view);
                ButterKnife.bind(this, view);
                mViewType = viewType;
            }
        }
    }
}
