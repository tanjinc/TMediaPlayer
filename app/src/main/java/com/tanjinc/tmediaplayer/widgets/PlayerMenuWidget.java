package com.tanjinc.tmediaplayer.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.tanjinc.tmediaplayer.LeftMenuAdapter;
import com.tanjinc.tmediaplayer.R;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/4/23.
 */
public class PlayerMenuWidget extends FrameLayout implements IWidget{
    private static final String TAG = "PlayerMenuWidget";

    private Context mContext;
    private RecyclerView mRecyclerView;
    private PlayerMenuAdapter mMenuAdapter;

    private int mScreenWidth;
    private int mScreenHeight;
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

    public PlayerMenuWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public PlayerMenuWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        mContext = context;
        mRecyclerView = new RecyclerView(context);
        addView(mRecyclerView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

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
                }
            }
        });
        mRecyclerView.setAdapter(mMenuAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mRecyclerView.setBackgroundColor(getResources().getColor(R.color.player_menu_widget_bg));
        initWindowSize();
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

    public void setMenuData(ArrayList<PlayerMenuData> list) {
        mMenuDataArrayList = list;
        mMenuAdapter.setData(mMenuDataArrayList);
    }

    private void initWindowSize() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
    }

    @Override
    public void showWithAnim(boolean hasAnim) {
        if (hasAnim) {
            ObjectAnimator transAnim = null;
            Log.e(TAG, "show ()=" + getTop() +" "+getBottom()+" "+getRight()+ " "+getLeft());
            if (mIsHorizion) {
                setTranslationY(0);
                transAnim = ObjectAnimator.ofFloat(this, "translationX", getWidth(), 0);
            } else {
                setTranslationX(0);
                transAnim = ObjectAnimator.ofFloat(this, "translationY", getHeight(), 0);
            }
            transAnim.setDuration(350);
            transAnim.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.33f, 1f));
            transAnim.start();
            setVisibility(VISIBLE);
        } else {
            setVisibility(VISIBLE);
        }
    }

    @Override
    public void hideWithAnim(boolean haveAnim) {
        if (haveAnim) {
            ObjectAnimator transAnim = null;
            if (mIsHorizion) {
                clearAnimation();
                transAnim = ObjectAnimator.ofFloat(this, "translationX", 0, getWidth());
            } else {
                transAnim = ObjectAnimator.ofFloat(this, "translationY", 0, getHeight());
            }
            transAnim.setDuration(300);
            transAnim.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.33f, 1f));

            transAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    setVisibility(GONE);
                    Log.d(TAG, "hide end ()=" + getTop() + " " + getBottom() + " " + getRight() + " " + getLeft());
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            transAnim.start();
        } else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void resetLayout(boolean isHorizion) {
        mIsHorizion = isHorizion;
        initWindowSize();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        if (isHorizion) {
            layoutParams.width = mScreenWidth / 2;
            layoutParams.height = LayoutParams.MATCH_PARENT;
            layoutParams.rightMargin = 0;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            layoutParams.height = mScreenHeight / 2;
            layoutParams.width = LayoutParams.MATCH_PARENT;
            layoutParams.bottomMargin = 0;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        }
        setLayoutParams(layoutParams);
    }
}
