package com.tanjinc.tmediaplayer.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.LeftMenuAdapter;
import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.AnimaUtils;
import com.tanjinc.tmediaplayer.utils.ScreenUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/4/23.
 */
public class PlayerMenuWidget extends BaseWidget{
    private static final String TAG = "PlayerMenuWidget";
    private static final int HIDE_AUTO = 1;
    private static final int HIDE_AUTO_TIME = 5000;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private PlayerMenuAdapter mMenuAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private boolean mIsHorizion;
    private ArrayList<PlayerMenuData> mMenuDataArrayList = new ArrayList<>();


    public enum MENU_ITEM_TYPE {
        MENU_PLAY,
        MENU_SCREEN,
        MENU_AUDIO_TRACK,
        MENU_SUBTITLE_TRACK,
        MENU_DNLA
    }
    public static class PlayerMenuData {
        MENU_ITEM_TYPE mMenuType;
        boolean isTitle = false;
        boolean isSelected = false;
        String mMenuItem;

        public PlayerMenuData(MENU_ITEM_TYPE type, boolean isSelected, String menuItem) {
            this.mMenuType = type;
            this.isTitle = false;
            this.isSelected = isSelected;
            this.mMenuItem = menuItem;
        }

        public PlayerMenuData(MENU_ITEM_TYPE type, String title) {
            this.mMenuType = type;
            this.isTitle = true;
            this.isSelected = false;
            this.mMenuItem = title;
        }
    }

    public PlayerMenuWidget(Context context) {
        super(context);
        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;
        mRecyclerView = new RecyclerView(context);
        mMenuAdapter = new PlayerMenuAdapter(context);
        mMenuAdapter.setOnItemClickListener(new PlayerMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mMenuDataArrayList != null && position < mMenuDataArrayList.size()) {
                    PlayerMenuData item = mMenuDataArrayList.get(position);
                    if (!item.isSelected) {
                        clearSelectSate(item.mMenuType);
                        item.isSelected = true;
                        mMenuAdapter.setData(mMenuDataArrayList);
                    }
                    int childIndex = position - getMenuTypeIndex(item.mMenuType);
                    Log.e(TAG, "video onItemClick: childIndex=" + childIndex);
                    switch (item.mMenuType) {
                        case MENU_PLAY:
                            Log.e(TAG, "video onItemClick: MENU_PLAY");
                            break;
                        case MENU_SCREEN:
                            Log.e(TAG, "video onItemClick: MENU_SCREEN");
                            break;
                        case MENU_AUDIO_TRACK:
                            Log.e(TAG, "video onItemClick: MENU_AUDIO_TRACK");
                            break;
                        case MENU_SUBTITLE_TRACK:
                            Log.e(TAG, "video onItemClick: MENU_SUBTITLE_TRACK");
                            break;
                        case MENU_DNLA:
                            Log.e(TAG, "video onItemClick: MENU_DNLA");
                            break;
                        default:
                            break;
                    }
                    mHandler.removeMessages(HIDE_AUTO);
                    mHandler.sendEmptyMessageDelayed(HIDE_AUTO, HIDE_AUTO_TIME);
                }
            }
        });
        mRecyclerView.setAdapter(mMenuAdapter);
        mLinearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.player_menu_widget_bg));
        setVisibility(GONE);

        setChildView(mRecyclerView);
    }

    private int getMenuTypeIndex(MENU_ITEM_TYPE type) {
        if (mMenuDataArrayList != null && mMenuDataArrayList.size() > 0) {
            for (int i = 0 ; i < mMenuDataArrayList.size() ; i++) {
                if (mMenuDataArrayList.get(i).mMenuType == type && mMenuDataArrayList.get(i).isTitle) {
                    return i;
                }
            }
        }
        return 0;
    }

    private void clearSelectSate(MENU_ITEM_TYPE type) {
        if (mMenuDataArrayList != null && mMenuDataArrayList.size() > 0) {
            for (int i = 0 ; i < mMenuDataArrayList.size() ; i++) {
                if (mMenuDataArrayList.get(i).mMenuType == type) {
                    mMenuDataArrayList.get(i).isSelected = false;
                }
            }
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HIDE_AUTO:
                    hide();
                    break;
                default:
                    break;
            }
        }
    };
    public void setMenuData(ArrayList<PlayerMenuData> list) {
        mMenuDataArrayList = list;
        mMenuAdapter.setData(mMenuDataArrayList);
    }

    @Override
    public void resetLayout(boolean isHorizion) {
        mIsHorizion = isHorizion;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
        if (isHorizion) {
            layoutParams.width = ScreenUtil.getScreenWidth(mContext) / 2;
            layoutParams.height = LayoutParams.MATCH_PARENT;
            layoutParams.topMargin = 0;
            layoutParams.rightMargin = 0;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            setAnimaType(FROM.RIGHT);

        } else {
            layoutParams.height = ScreenUtil.getScreenHeight(mContext) / 2;
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.leftMargin = 0;
            layoutParams.bottomMargin = 0;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            setAnimaType(FROM.BOTTOM);
        }
        mRecyclerView.setLayoutParams(layoutParams);
    }
}
