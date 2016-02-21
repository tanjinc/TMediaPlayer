package com.tanjinc.tmediaplayer;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class LocalVideoFragment extends ListFragment {

    private LocalVideoAdapter mAdapter;
    private VideoProviderAsyncTask mProvider;
    private Context mContext;
    private ArrayList<VideoData> mVideoList;


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

        View view = inflater.inflate(R.layout.fragment_container, container, false);

        setListAdapter(mAdapter);
        return view;
    }

    public void getVideoList(ArrayList videoList) {
        mAdapter.getVideoList(videoList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
