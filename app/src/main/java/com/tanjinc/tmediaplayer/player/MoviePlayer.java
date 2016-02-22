package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by tanjincheng on 16/2/20.
 */
public class MoviePlayer extends FrameLayout {

    private Uri mVideoUri;
    private Context mContext;
    private MyVideoView mVideoView;

    public MoviePlayer(Context context) {
        super(context);
        mContext = context;
        mVideoView = new MyVideoView(mContext);
        mVideoView.setVisibility(VISIBLE);
        addView(mVideoView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setUrl(Uri uri) {
        mVideoView.setVisibility(VISIBLE);
        mVideoView.setUri(uri);
    }
}
