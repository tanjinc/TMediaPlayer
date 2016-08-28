package com.tanjinc.tmediaplayer.widgets;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tanjinc.tmediaplayer.utils.AnimaUtils;
import com.tanjinc.tmediaplayer.utils.ScreenUtil;

/**
 * Created by tanjincheng on 16/8/28.
 */
public abstract class BaseWidget extends RelativeLayout{

    private FROM mFrom = FROM.BOTTOM;
    private boolean mIsHorizion;
    private LayoutParams mLayoutParams;

    private OnDismissListener mOnDismissListener;
    private View mChildView;

    public enum FROM {
        UP, BOTTOM, LEFT, RIGHT
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public BaseWidget(Context context) {
        super(context);
    }


    public void setAnimaType(FROM fromType) {
        mFrom = fromType;
    }

    public void setChildView(View childView) {
        mChildView = childView;
        mLayoutParams= new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        addView(mChildView, mLayoutParams);
    }

    public void setChildView(int resource) {
        View view = LayoutInflater.from(getContext()).inflate(resource, null);
        if (view != null) {
            mChildView = view;
            RelativeLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            addView(mChildView);
        }
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mOnDismissListener = listener;
    }
    /**
     * 现实控件
     */
    public void show() {
        AnimaUtils.setMask(this, true);
        int height = mChildView.getHeight();
        int width = mChildView.getWidth();
        ObjectAnimator transAnim = null;
        switch (mFrom) {
            case UP:
                mChildView.setTranslationX(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationY", 0, height);
                break;
            case BOTTOM:
                mChildView.setTranslationX(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationY", height, 0);
                break;
            case LEFT:
                mChildView.setTranslationY(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationX", 0, width);
                break;
            case RIGHT:
                mChildView.setTranslationY(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationX", width, 0);
                break;
            default:
                break;
        }
        transAnim.setDuration(350);
        transAnim.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.33f, 1f));
        transAnim.start();
        mChildView.setVisibility(VISIBLE);
        setVisibility(VISIBLE);
    }

    /**
     * 隐藏控件
     */
    public void hide() {
        AnimaUtils.setMask(this, false);
        int height = mChildView.getHeight();
        int width = mChildView.getWidth();
        ObjectAnimator transAnim = null;
        switch (mFrom) {
            case UP:
                mChildView.setTranslationX(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationY", height, 0);
                break;
            case BOTTOM:
                mChildView.setTranslationX(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationY", 0, height);
                break;
            case LEFT:
                mChildView.setTranslationY(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationX", width, 0);
                break;
            case RIGHT:
                mChildView.setTranslationY(0);
                transAnim = ObjectAnimator.ofFloat(mChildView, "translationX", 0, width);
                break;
            default:
                break;
        }
        transAnim.setDuration(300);
        transAnim.setInterpolator(PathInterpolatorCompat.create(0.33f, 0f, 0.33f, 1f));
        transAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                setVisibility(GONE);
            }

        });
        transAnim.start();
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    /**
     * 根据是否横竖屏重新布局
     * @param isHorizion    是否横屏
     */
    public void resetLayout(boolean isHorizion) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            hide();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
