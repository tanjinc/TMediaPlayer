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
import android.widget.ListView;

import com.tanjinc.tmediaplayer.player.VideoPlayActivity;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class LocalVideoFragment extends ListFragment implements IVideoProviderUI{

    private LocalVideoAdapter mAdapter;
    private VideoProviderAsyncTask mProvider;
    private Context mContext;
    private ArrayList<VideoData> mVideoList;
    private ListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mAdapter = new LocalVideoAdapter(mContext, null);
        mProvider = new VideoProviderAsyncTask(mContext, this);

        mProvider.execute();
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

    private void startVideoPlayerActivity(Uri uri) {
        Intent intent = new Intent(getActivity(), VideoPlayActivity.class);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getActivity().startActivity(intent);
    }

    @Override
    public void setVideoList(ArrayList videoList) {
        mVideoList = videoList;
        mAdapter.setVideoList(mVideoList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
