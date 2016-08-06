package com.tanjinc.tmediaplayer.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by tanjincheng on 16/8/6.
 */
public class KeyboardUtil {
    private static final String TAG = "KeyboardUtil";

    private static ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    private static ArrayList<OnSoftKeyboardChangeListener> mOnSoftKeyBoardChangeListenerArray = new ArrayList<>();

    public static void observeSoftKeyboard(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "video observeSoftKeyboard fail activity is null");
            return;
        }
        final View decorView = activity.getWindow().getDecorView();
        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom - rect.top;
                int height = decorView.getHeight();
                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    for (int i = 0; i < mOnSoftKeyBoardChangeListenerArray.size(); i++) {
                        mOnSoftKeyBoardChangeListenerArray.get(i).onSoftKeyBoardChange(keyboardHeight, !hide);
                    }
                }

                previousKeyboardHeight = height;

            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
    }

    public static void unObserveSoftKeyboard(Activity activity) {
        if (activity == null) {
            Log.e(TAG, "video unObserveSoftKeyboard fail activity is null");
            return;
        }
        activity.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        mOnSoftKeyBoardChangeListenerArray.clear();
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
    }

    /**
     * 注册软键盘变化监听
     * @param listener
     */
    public static void addSoftKeyboardChangedListener(OnSoftKeyboardChangeListener listener) {
        if (listener != null) {
            mOnSoftKeyBoardChangeListenerArray.add(listener);
        }
    }

    /**
     * 取消软键盘状态变化监听
     * @param listener
     */
    public static void removeSoftKeyboardChangedListener(OnSoftKeyboardChangeListener listener) {
        if (listener != null) {
            mOnSoftKeyBoardChangeListenerArray.remove(listener);
        }
    }

    /**
     * 显示软键盘
     * @param context
     * @param edit
     */
    public static void showSoftKeyboard(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager methodManager = (InputMethodManager) context.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.showSoftInput(edit,0);
    }

    /**
     * 隐藏软键盘
     * @param context
     * @param edit
     */
    public static void hideSoftKeyboard(Context context, EditText edit) {
        edit.clearFocus();
        InputMethodManager inputmanger = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(edit.getWindowToken(), 0);
    }

    /**
     * 切换键盘显示与否状态
     * @param context
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager inputManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}

