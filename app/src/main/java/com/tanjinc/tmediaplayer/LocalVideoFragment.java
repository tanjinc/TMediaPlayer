package com.tanjinc.tmediaplayer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
//import android.app.ListFragment;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.util.Log;
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
import com.tanjinc.tmediaplayer.player.VideoPlayActivity2;
import com.tanjinc.tmediaplayer.utils.ImageUtil;
import com.tanjinc.tmediaplayer.utils.SDCardHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class LocalVideoFragment extends ListFragment implements VideoContract.View{

    private static final String TAG = "LocalVideoFragment";
    private LocalVideoAdapter mAdapter;
    private Context mContext;
    private ListView mListView;
    private ArrayList<VideoData> mVideoList = new ArrayList<>();

    private VideoContract.Presenter mPresenter;

    private boolean mIsFloatPlay = true;    //是否悬浮播放


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

        view.setBackground(null);
        setListAdapter(mAdapter);
        return view;
    }

    private Uri uri;
    private Handler handler = new Handler();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = getListView();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startVideoPlayerActivity(Uri.parse(mVideoList.get
                        (position).getPath()));

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                uri = Uri.parse(mVideoList.get(position).getPath());
//                intent.setDataAndType(uri, "video/*");
//                intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                intent.setClassName("com.meizu.media.video", ".player.ui.VideoWindowActivity");
//                startActivity(intent);

//                SDCardHelper.createInstance(mContext);
//                SDCardHelper.getInstance().getMountPointList(mContext);
//                SDCardHelper.MountPoint  a = SDCardHelper.getInstance().getSDCardMountPoint();
//                boolean b = SDCardHelper.getInstance().isMounted();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.start();
        }
    }

    private void startVideoPlayerActivity(Uri uri) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), mIsFloatPlay ? VideoPlayActivity.class : VideoPlayActivity2.class);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void addTest(ArrayList list) {
        VideoData testData = new VideoData();
        testData.setName("online test");
        String video1 = "http://14.152.58.21/sohu/s26h23eab6/xdispatch/sohu.vod.qingcdn.com/51/116/UdKGIuSjQIO8dynrybyS1E.mp4?key=Oj9qeHJQrFaAJBNG7KTR6SUruM1ykdFX&n=1&a=1804&cip=61.145.229.245&pt=1&rb=1&prod=flash";
        String video = "http://183.6.194.18/sohu/s26h23eab6/xdispatch/sohu.vod.qingcdn.com/24/73/Pne6aF6wQHCgXW8lvnBfpB.mp4?key=7UMc5iMBRK6n9FHPQRFM4jo5bOvldKFc&n=1&a=1804&cip=113.74.78.243&pt=1&rb=1&prod=flash";
        testData.setPath(video);
        list.add(testData);


        VideoData testData2 = new VideoData();
        testData2.setName("online test2");
        String video2 = "http://ivod.itv.wo.com.cn/m2/1001/007/130/1001007130_bq.mp4";
        testData2.setPath(video2);
        list.add(testData2);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showVideoList(ArrayList<VideoData> videoList) {
        mVideoList = videoList;
        addTest(mVideoList);
        mAdapter.setVideoList(mVideoList);
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

        private int mHeight;
        private int mWidth;

        public LocalVideoAdapter(Context context, ArrayList<VideoData> videolist) {
            mContext = context;
            mVideoList = videolist;
            mHeight= (int) mContext.getResources().getDimension(R.dimen.video_thumbnail_height);
            mWidth = (int) mContext.getResources().getDimension(R.dimen.video_thumbnail_width);
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
//            viewHolder.thumbnail.setImageBitmap(videoData.getThumbnail());


            ImageUtil.loadLoalImage(mContext, videoData.thumbPath, mHeight, mWidth, viewHolder.thumbnail, 10);

            return convertView;
        }

        private class ViewHolder {
            TextView displayName;
            TextView path;
            ImageView thumbnail;
        }
    }
}
