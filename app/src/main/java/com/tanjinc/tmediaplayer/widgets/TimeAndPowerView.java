package com.tanjinc.tmediaplayer.widgets;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by tanjincheng on 16/8/12.
 */
public class TimeAndPowerView extends LinearLayout{
    private static final String TAG = "TimeAndPowerView";

    private TextView mTimeText;
    private TextView mPowerView;

    private boolean mIsCharging = false;

    final Handler mHandler = new Handler();
    private Runnable mRunnable;

    private Context mContext;



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

        mContext = context;
        mTimeText = new TextView(context);
        mPowerView = new TextView(context);

        setOrientation(VERTICAL);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        addView(mTimeText, layoutParams);

        addView(mPowerView, layoutParams);

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mTimeText.setText(getTime());
                mHandler.postDelayed(this, 1000);
            }
        };

        mContext.registerReceiver(mPowerConnectionReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    private BroadcastReceiver mPowerConnectionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            mIsCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

            float batteryPct = level / (float)scale;
            Log.d(TAG, "video onReceive: " + batteryPct);
            mPowerView.setText(batteryPct * 100 + "%");
        }
    };


    private String getTime() {
        String ret =  DateUtils.formatDateTime(mContext, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME);
        Log.d(TAG, "video getTime: " + ret);
        return ret;
    }

    public void star() {
        mHandler.post(mRunnable);
    }

    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }

    public void destory() {
        mContext.unregisterReceiver(mPowerConnectionReceiver);
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDetachedFromWindow() {
        destory();
        super.onDetachedFromWindow();
    }
}

