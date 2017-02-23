package com.tanjinc.tmediaplayer.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.ScreenUtil;

/**
 * Created by tanjincheng on 16/5/14.
 */
public class ShareWidget extends BaseWidget implements View.OnClickListener{

    private static final String TAG = "ShareWidget";
    private Context mContext;
    private boolean mIsHorizion;
    private TextView mWeiboBtn;

    private WBShareView mWBShareView;
    private WXShareView mWXShareView;
    private WXShareView mWXSceneTimelineView;
    private ImageView mShareView;
    private LinearLayout mRootView;


    public ShareWidget(Context context) {
        super(context);
        mContext = context;
        initView();
        setVisibility(INVISIBLE);
    }


    public void initView() {
        mRootView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.share_widget_layout,null);
        mWBShareView = (WBShareView) mRootView.findViewById(R.id.weibo_view);
        mWBShareView.initView((Activity) mContext);
        mWBShareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWBShareView.share();
            }
        });

        mWXShareView = (WXShareView) mRootView.findViewById(R.id.wx_view);
        mWXShareView.initView((Activity) mContext);
        mWXShareView.setFocusable(true);
        mWXShareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWXShareView.share(true);
            }
        });

        mWXSceneTimelineView = (WXShareView) mRootView.findViewById(R.id.wx_timeline);
        mWXSceneTimelineView.initView((Activity) mContext);
        mWXSceneTimelineView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWXSceneTimelineView.share(false);
            }
        });

        mShareView = (ImageView) mRootView.findViewById(R.id.share_other);
        mShareView.setOnClickListener(this);

        setChildView(mRootView);
        Log.d(TAG, "video init: " + mRootView.getWidth() + "/ " + mRootView.getHeight());
    }

    @Override
    public void resetLayout(boolean isHorizion) {
        mIsHorizion = isHorizion;
        RelativeLayout.LayoutParams layoutParams = (LayoutParams) mRootView.getLayoutParams();
        if (isHorizion) {
            layoutParams.width = 600;
            layoutParams.height = LayoutParams.MATCH_PARENT;
            mRootView.setOrientation(LinearLayout.VERTICAL);
            setAnimaType(FROM.CENTER);
        } else {
            layoutParams.height = 250;
            layoutParams.width = LayoutParams.MATCH_PARENT;
            mRootView.setOrientation(LinearLayout.HORIZONTAL);
            setAnimaType(FROM.UP);
        }
        mRootView.setLayoutParams(layoutParams);
        Log.d(TAG, "video resetLayout: " + mRootView.getWidth() + "/ " + mRootView.getHeight());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        mWBShareView.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_other:

                break;

        }
    }
}
