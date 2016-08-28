package com.tanjinc.tmediaplayer.utils;

import android.app.Service;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by tanjincheng on 16/8/15.
 */
public class WindowUtil {
    private static final String TAG = "WindowUtil";
    private View mView;
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private boolean mIsShown;

    private static WindowUtil mWindowUtil;

    public static synchronized WindowUtil getInstance() {
        if (mWindowUtil == null) {
            mWindowUtil = new WindowUtil();
        }
        return mWindowUtil;
    }

    public void initWindow(final Context context, View rootView) {
        mView = rootView;
        mContext = context.getApplicationContext();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();

        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.format = PixelFormat.RGBA_8888;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |  WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mParams.gravity = Gravity.CENTER;
        mParams.x = 0;
        mParams.y = 0;
        mParams.alpha = 1.0f;
        mParams.token = rootView.getApplicationWindowToken();
        mWindowManager.addView(rootView, mParams);
    }

    public void showWindow() {
        if (mIsShown) {
            Log.d(TAG, "video showFloatWindow: ");
            return;
        }
        mIsShown = true;
        mView.setVisibility(View.VISIBLE);
    }

    public void hideWindow() {
        if (mIsShown && mView != null) {
            mView.setVisibility(View.INVISIBLE);
            mIsShown = false;
        }
    }

    public void changeWindowSize(int width, int height, int x, int y) {
        mParams.width = width;
        mParams.height = height;
        mParams.x = x;
        mParams.y = y;
        mWindowManager.updateViewLayout(mView, mParams);
    }

    /**
     *
     * @param enable
     */
    public void setEnableInput(boolean enable) {
        if (enable) {
            mParams.flags = mParams.flags & ~WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            mParams.flags = mParams.flags | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        mWindowManager.updateViewLayout(mView, mParams);
    }

    /**
     *
     *
     */
    public void changeToFull() {

    }

    /**
     *
     */
    public void changeToFloat() {

    }

}
