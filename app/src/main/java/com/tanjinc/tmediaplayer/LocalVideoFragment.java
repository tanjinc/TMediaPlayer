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
        addTest();
        mAdapter.setVideoList(mVideoList);
    }

    private void addTest() {
        VideoData testData = new VideoData();
        testData.setName("test");
        String video = "https://r12---sn-a5m7ln76.c.youtube.com/videoplayback?id=bf5bb2419360daf1&itag=135&source=youtube&requiressl=yes&ratebypass=yes&mime=video/mp4&gir=yes&clen=15973445&lmt=1434104793587130&dur=135.502&signature=5A8E9D4CD81ECBDA6669958AC5765298F75B007D.43FE2EA2EEF80A6B188F64092158C11EB770EBAF&key=cms1&ip=45.78.17.131&ipbits=0&expire=2147483647&sparams=clen,dur,expire,gir,id,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,nh,pl,ratebypass,requiressl,source&mm=30&mn=sn-a5m7ln76&ms=nxu&mt=1461213226&mv=u&nh=IgpwcjAyLmxheDAyKgkxMjcuMC4wLjE&pl=24";
        testData.setPath(video);
        mVideoList.add(0, testData);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
