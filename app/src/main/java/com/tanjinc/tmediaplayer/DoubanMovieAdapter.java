package com.tanjinc.tmediaplayer;

import android.content.Context;
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
 * Created by tanjinc on 16-7-22.
 */
class DoubanMovieAdapter extends RecyclerView.Adapter<DoubanMovieAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<VideoData> mVideoDatas;
    private int TYPE_FOOTER = 100;
    private int TYPE_ITEM = 101;
    private OnItemCLickListener mOnClickListener;

    public DoubanMovieAdapter(Context context) {
        mContext = context;
    }

    public void notifyDataChange(ArrayList<VideoData> videoDatas) {
        mVideoDatas = videoDatas;
        notifyDataSetChanged();
    }

    public static interface OnItemCLickListener {
        void onItemClick(int postion);
        boolean onLongClick(int postion);
    }

    public void setOnItemClickListener(OnItemCLickListener onClickListener) {
        mOnClickListener = onClickListener;
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
                holder.mPostion = position;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        @BindView(R.id.video_image)
        ImageView mVideoImage;
        @BindView(R.id.video_title)
        TextView mVideoTitle;

        public int mViewType;
        public int mPostion;

        ViewHolder(View view, int viewType) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            mViewType = viewType;
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(mPostion);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mOnClickListener != null) {
               return mOnClickListener.onLongClick(mPostion);
            }
            return false;
        }
    }
}
