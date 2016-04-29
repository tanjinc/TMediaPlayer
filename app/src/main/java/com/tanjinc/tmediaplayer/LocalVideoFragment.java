package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.data.VideoData;
import com.tanjinc.tmediaplayer.player.VideoPlayActivity;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class LocalVideoFragment extends ListFragment implements VideoContract.View{

    private LocalVideoAdapter mAdapter;
    private Context mContext;
    private ListView mListView;
    private ArrayList<VideoData> mVideoList = new ArrayList<>();

    private VideoContract.Presenter mPresenter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mAdapter = new LocalVideoAdapter(mContext, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //onCreateView must only return the view that represent the fragment
        View view = inflater.inflate(R.layout.fragment_container, container, false);

        setListAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startVideoPlayerActivity(Uri.parse(mVideoList.get(position).getPath()));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    private void startVideoPlayerActivity(Uri uri) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    private void addTest(ArrayList list) {
        VideoData testData = new VideoData();
        testData.setName("online test");
        String video1 = "http://14.152.58.21/sohu/s26h23eab6/xdispatch/sohu.vod.qingcdn.com/51/116/UdKGIuSjQIO8dynrybyS1E.mp4?key=Oj9qeHJQrFaAJBNG7KTR6SUruM1ykdFX&n=1&a=1804&cip=61.145.229.245&pt=1&rb=1&prod=flash";
        String video = "http://183.6.194.18/sohu/s26h23eab6/xdispatch/sohu.vod.qingcdn.com/24/73/Pne6aF6wQHCgXW8lvnBfpB.mp4?key=7UMc5iMBRK6n9FHPQRFM4jo5bOvldKFc&n=1&a=1804&cip=113.74.78.243&pt=1&rb=1&prod=flash";
        testData.setPath(video);
        list.add(0, testData);


        VideoData testData2 = new VideoData();
        testData2.setName("online test2");
        String video2 = "http://ivod.itv.wo.com.cn/m2/1001/007/130/1001007130_bq.mp4";
        testData2.setPath(video2);
        list.add(1, testData2);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showVideoList(ArrayList<VideoData> videoList) {
        addTest(videoList);
        mVideoList = videoList;
        mAdapter.setVideoList(videoList);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        // show loading
    }

    @Override
    public void setPresenter(VideoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private class LocalVideoAdapter extends BaseAdapter {

        private ArrayList<VideoData> mVideoList;
        private Context mContext;


        public LocalVideoAdapter(Context context, ArrayList<VideoData> videolist) {
            mContext = context;
            mVideoList = videolist;
        }

        public void setVideoList(ArrayList list) {
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
//        viewHolder.path.setText("fadf");
            viewHolder.thumbnail.setImageBitmap(videoData.getThumbnail());

            return convertView;
        }

        private class ViewHolder {
            TextView displayName;
            TextView path;
            ImageView thumbnail;
        }
    }
}
