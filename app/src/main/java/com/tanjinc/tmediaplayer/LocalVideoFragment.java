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
    private ListView mListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mContext = getActivity();
//        mProvider = new VideoProviderAsyncTask(mContext, this);
//        mAdapter = new LocalVideoAdapter(mContext, mVideoList);
//
//        mProvider.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        View view = inflater.inflate(R.layout.fragment_container, container, false);

//        mListView = (ListView) view.findViewById(R.id.list_view);
//        mListView.setAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void notifyDataChange(ArrayList videolist) {
//        mVideoList = videolist;
//        mAdapter.notifyDataSetChanged();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
