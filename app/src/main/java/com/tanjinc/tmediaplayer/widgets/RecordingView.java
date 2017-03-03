package com.tanjinc.tmediaplayer.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.tanjinc.tmediaplayer.R;

/**
 * Created by tanjinc on 17-3-3.
 */
public class RecordingView extends View implements View.OnClickListener{
    private static final String TAG = "RecordingView";
    private Paint mPaint;
    private int mRadius;
    private int mRoundColor;
    private int mRoundWidth;
    private int mProgress;
    private int mProgressColor;
    private int max = 10000;

    private boolean isLongPress;
    private boolean isMove;
    private final static int MSG_GET_GIME = 0;
    private final static int TIME_DELAY = 100; //500ms

    public interface OnStateChangeListener {
        void onBegin();
        void onFinish(int duration);
    }
    private OnStateChangeListener mStateChangeListener;

    public RecordingView(Context context) {
        this(context, null);
    }

    public RecordingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecordingView);
        mRoundColor = typedArray.getColor(R.styleable.RecordingView_roundColor, Color.BLUE);
        mRoundWidth = typedArray.getDimensionPixelSize(R.styleable.RecordingView_roundWidth, 15);
        mProgressColor = typedArray.getColor(R.styleable.RecordingView_roundProgressColor, Color.RED);
        typedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        int center = getWidth() / 2;
        int radius = (center - mRoundWidth /2 );
        mPaint.setColor(mRoundColor);
        mPaint.setStyle(Paint.Style.STROKE);    //空心
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle(center, center, radius, mPaint);

        //画进度
        mPaint.setStrokeWidth(mRoundWidth);
        mPaint.setColor(mProgressColor);

        RectF oval = new RectF(center-radius, center - radius, center + radius, center + radius);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(oval, 0, 360 * mProgress / max, false,mPaint);

        String timeStr = Integer.toString(mProgress / 1000);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(24);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(timeStr, center, center, mPaint);
    }

    public synchronized int getMax() {
        return max;
    }

    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    public void setOnStateChangeListener(OnStateChangeListener listner) {
        mStateChangeListener = listner;
    }

    /**
     * 获取进度.需要同步
     * @return
     */
    public synchronized int getProgress() {
        return mProgress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.mProgress = progress;
            postInvalidate();
        }

    }
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public void onClick(View v) {
    }

    private void cancle() {
        Log.d(TAG, "video cancle() time=" + time / 1000);
        if (mStateChangeListener != null) {
            mStateChangeListener.onFinish(time/1000);
        }
        time = 0;
        setProgress(0);
        mHandler.removeMessages(MSG_GET_GIME);
    }

    private int time = 0;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_GIME:
                    time += TIME_DELAY;
                    Log.d(TAG, "video handleMessage() time:" + time);
                    setProgress(time);
                    mHandler.sendEmptyMessageDelayed(MSG_GET_GIME, TIME_DELAY);
                    break;
            }
            return false;
        }
    });



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX(0);
        int y = (int) event.getY(0);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isLongPress = false;
                isMove = false;
                time = 0;
                mHandler.sendEmptyMessage(MSG_GET_GIME);
                if (mStateChangeListener != null) {
                    mStateChangeListener.onBegin();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;

            case MotionEvent.ACTION_UP:
                cancle();
                break;
            default:
                break;
        }
        return true;
    }
}
