package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class LocalVideoAdapter extends BaseAdapter {

    private ArrayList<VideoData> mVideoList;
    private Context mContext;


    public LocalVideoAdapter(Context context, ArrayList<VideoData> videolist) {
        mContext = context;
        mVideoList = videolist;
    }

    public void getVideoList(ArrayList list) {
        mVideoList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mVideoList != null) {

            return mVideoList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        VideoData item = null;
        if (mVideoList != null) {
            item = mVideoList.get(position);
        }
        return item;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_layout, null);
            viewHolder.displayName = (TextView) convertView.findViewById(R.id.display_name);
            viewHolder.path = (TextView) convertView.findViewById(R.id.path);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        VideoData videoData = (VideoData) getItem(position);
        viewHolder.displayName.setText(videoData.getName());
        viewHolder.path.setText(videoData.getPath());
        viewHolder.thumbnail.setImageBitmap(videoData.getThumbnail());

        return convertView;
    }

    private class ViewHolder {
        TextView displayName;
        TextView path;
        ImageView thumbnail;
    }
}
