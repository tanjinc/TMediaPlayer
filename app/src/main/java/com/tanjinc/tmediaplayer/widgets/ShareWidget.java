package com.tanjinc.tmediaplayer.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.R;

import java.util.zip.Inflater;

/**
 * Created by tanjincheng on 16/5/14.
 */
public class ShareWidget extends FrameLayout implements IWidget {

    private static final String TAG = "ShareWidget";
    private int mScreenWidth;
    private int mScreenHeight;
    private Context mContext;
    private boolean mIsHorizion;
    private TextView mWeiboBtn;
    private LinearLayout mRoot;

    private WeiboShareView mWeiboShareView;


    public ShareWidget(Context context) {
        super(context);
        mContext = context;
        initView();
        setVisibility(INVISIBLE);
    }


    public void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.share_widget_layout,this);
        mRoot = (LinearLayout) findViewById(R.id.share_item_container);

        mWeiboShareView = (WeiboShareView) findViewById(R.id.weibo_view);
        mWeiboShareView.initView((Activity) mContext);
        mWeiboShareView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeiboShareView.share();
            }
        });
    }

    @Override
    public void showWithAnim(boolean anim) {
        if (anim) {
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
    public void hideWithAnim(boolean anim) {
        if (anim) {
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initWindowSize() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
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
