package com.tanjinc.tmediaplayer.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by tanjincheng on 16/8/12.
 */
public class TimeAndPowerView extends LinearLayout{
    private static final String TAG = "TimeAndPowerView";

    private TextView mTimeText;
    private BatteryView mBatteryView;

    final Handler mHandler = new Handler();
    private Runnable mRunnable;

    public TimeAndPowerView(Context context) {
        super(context);
        init(context);
    }

    public TimeAndPowerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TimeAndPowerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mTimeText = new TextView(context);
        mTimeText.setTextColor(Color.WHITE);
        mTimeText.setTextSize(10);

        mBatteryView = new BatteryView(context);

        setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        addView(mTimeText, layoutParams);

        addView(mBatteryView, layoutParams);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTimeText.setText(getTime());
                mHandler.postDelayed(this, 1000);
            }
        };

    }


    private String getTime() {
        String ret =  DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
        return ret;
    }

    private void star() {
        mHandler.post(mRunnable);
    }

    private void stop() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        Log.d(TAG, "video onVisibilityChanged() " + visibility);
        if (visibility == VISIBLE) {
            star();
        } else {
            stop();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop();
    }
}

