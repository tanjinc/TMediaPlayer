package com.tanjinc.tmediaplayer.player;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.utils.AnimaUtils;
import com.tanjinc.tmediaplayer.utils.FFmpegUtils;
import com.tanjinc.tmediaplayer.utils.KeyboardUtil;
import com.tanjinc.tmediaplayer.utils.ScreenUtil;
import com.tanjinc.tmediaplayer.utils.VideoUtils;
import com.tanjinc.tmediaplayer.utils.WindowUtil;
import com.tanjinc.tmediaplayer.widgets.BaseWidget;
import com.tanjinc.tmediaplayer.widgets.PlayerMenuWidget;
import com.tanjinc.tmediaplayer.widgets.RecordingView;
import com.tanjinc.tmediaplayer.widgets.ShareWidget;
import com.tanjinc.tmediaplayer.widgets.TimeAndPowerView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by tanjincheng on 16/3/30.
 */
public class MovieController extends RelativeLayout implements IController {

    private static final int SEEK_BAR_MAX = 10000;
    private static final String TAG = "MovieController";
    private static final int MSG_SHOW_DANMU_INPUT = 1001;

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.share_btn)
    Button mShareBtn;
    @BindView(R.id.top_layout)
    RelativeLayout mTopLayout;
    @BindView(R.id.play)
    ImageButton mPlayBtn;
    @BindView(R.id.currenttime_tv)
    TextView mCurrenttimeTextView;
    @BindView(R.id.seekbar)
    SeekBar mSeekbar;
    @BindView(R.id.duration_tv)
    TextView mDurationTextView;
    @BindView(R.id.player_menu_btn)
    Button mPlayerMenuBtn;
    @BindView(R.id.bottom_layout)
    RelativeLayout mBottomLayout;
    @BindView(R.id.danmu_edit)
    EditText mEditText;
    @BindView(R.id.danmu_layout)
    RelativeLayout mDanmuInputLayout;

    @BindView(R.id.time_power_view)
    TimeAndPowerView mTimeAndPowerView;
    
    @BindView(R.id.switch_float_btn)
    ImageView mSwitchBtn;

    @BindView(R.id.recording_view) RecordingView mRecordingView;

    private Context mContext;
    private boolean mIsShowing;
    private IVideoView mPlayer;

    private int mDanmuInputMargin;
    private boolean mIsHorizontal;
    private int mCurrentPostion;
    private String mGifname;
    //widget
    private PlayerMenuWidget mPlayerMenuWidget;
//    private ShareWidget mShareWidget;
    private ArrayList<BaseWidget> mWidgetArray = new ArrayList<>();

    @OnClick({R.id.share_btn, R.id.play, R.id.player_menu_btn, R.id.switch_float_btn, R.id.recording_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_btn:
//                mShareWidget.show();
                break;
            case R.id.play:
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mPlayBtn.setActivated(false);
                    mHandler.removeCallbacks(progressRunnable);
                } else {
                    mPlayer.start();
                    mPlayBtn.setActivated(true);
                    mHandler.post(progressRunnable);
                }
                break;
            case R.id.player_menu_btn:
//                if (mPlayerMenuWidget.isShown()) {
//                    showController();
//                    mPlayerMenuWidget.hide();
//                } else {
//                    hideController();
//                    mPlayerMenuWidget.show();
//                }
//                showDanmu();
//                mPlayerMenuWidget.show();
//                mShareWidget.show();
                hideController();
                break;
            case R.id.switch_float_btn:
                Log.d(TAG, "video onClick: ");
                WindowUtil.getInstance().changeWindowSize(300, 200, 10, 10);
                mContext.sendBroadcast(new Intent(VideoPlayActivity.ACTION_CLOSE_SELF));
                break;
            case R.id.recording_view:

                Log.d(TAG, "video onClick() mCurrentPostion:" + mCurrentPostion);
                break;
            default:
                break;
        }
    }


    private void showDanmu() {
        KeyboardUtil.toggleSoftInput(mContext);
    }


    public MovieController(Context context, IVideoView player) {
        super(context);
        mContext = context;
        mPlayer = player;
        init();
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.player_controller_layout, this);
        ButterKnife.bind(this, view);
        mIsHorizontal = mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        mSeekbar.setMax(SEEK_BAR_MAX);
        mSeekbar.setOnSeekBarChangeListener(mSeekBarChangeListener);
        mSeekbar.setThumb(null);
        mShareBtn.setVisibility(GONE);
        mDanmuInputLayout.setVisibility(GONE);
        initRecording();
        addWidgets();
        resetLayout();
        mHandler.post(progressRunnable);
        KeyboardUtil.addSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
    }

    /**
     * 初始话 截屏转换gif
     */
    private void initRecording() {
        mRecordingView.setOnStateChangeListener(new RecordingView.OnStateChangeListener() {
            @Override
            public void onBegin() {
                mCurrentPostion = mPlayer.getCurrentPosition();
            }

            @Override
            public void onFinish(int duration) {
                Log.d(TAG, "video onFinish() time=" + duration);
                mGifname = "/sdcard/"+mPlayer.getTitle()+".gif";
                int videoWidth = mPlayer.getVideoWidth();
                int videoHeight = mPlayer.getVideoHeight();
                int scaleNum;
                if (videoWidth >= 1080 || videoHeight >= 1080) {
                    scaleNum = 2;
                } else {
                    scaleNum = 1;
                }
                String scale = ((int)videoWidth / scaleNum)+"x"+((int)videoHeight / scaleNum);
                String startTime = VideoUtils.length2time(mCurrentPostion);
                FFmpegUtils.video2Gif(mPlayer.getUri().toString(), mGifname,scale, startTime, duration);
            }
        });
        FFmpegUtils.setOnCompleteListener(new FFmpegUtils.OnCompleteListener() {
            @Override
            public void onComplete() {
                Toast.makeText(mContext, mGifname + " 已经生成", Toast.LENGTH_SHORT).show();
                //由文件得到uri
                Intent shareIntent = new Intent();
                File f = new File(mGifname);
                if (f != null && f.exists() && f.isFile()) {
                    shareIntent.setType("image/jpg");
                    Uri u = Uri.fromFile(f);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, u);
                }

                shareIntent.setAction(Intent.ACTION_SEND);
                mContext.startActivity(Intent.createChooser(shareIntent, "分享到"));
            }
        });
    }
    
    private KeyboardUtil.OnSoftKeyboardChangeListener mSoftKeyboardChangeListener = new KeyboardUtil.OnSoftKeyboardChangeListener() {
        @Override
        public void onSoftKeyBoardChange(int softKeybardHeight, boolean visible) {
            if (visible) {
//            Log.d(TAG, "video onSoftKeyBoardChange() called with: " + "softKeybardHeight = [" + softKeybardHeight + "], visible = [" + visible + "]");
                mHandler.sendEmptyMessageDelayed(MSG_SHOW_DANMU_INPUT, 100);
                if (mDanmuInputMargin != softKeybardHeight) {
                    mDanmuInputMargin = softKeybardHeight;
                    RelativeLayout.LayoutParams layoutParams = (LayoutParams) mDanmuInputLayout.getLayoutParams();
                    layoutParams.bottomMargin = softKeybardHeight;
                    layoutParams.addRule(ALIGN_PARENT_BOTTOM);
                    mDanmuInputLayout.setLayoutParams(layoutParams);
                }
                mEditText.requestFocus();
            } else {
                mDanmuInputLayout.setVisibility(GONE);
            }
        }
    };

    private void addWidgets() {
        mWidgetArray.clear();
        mPlayerMenuWidget = new PlayerMenuWidget(mContext);
        ArrayList<PlayerMenuWidget.PlayerMenuData> menuData = new ArrayList<PlayerMenuWidget.PlayerMenuData>();
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY, "播放"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY, false, "单个循环"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY, false, "连续播放"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN, "窗口大小"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN, true, "屏幕适应"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN, false, "全屏"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN, false, "原尺寸"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, "DLNA"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "codi"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "xbmc"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "xiaomi"));
        mPlayerMenuWidget.setMenuData(menuData);
        addView(mPlayerMenuWidget, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

//        mShareWidget = new ShareWidget(mContext);
//        mShareWidget.setOnDismissListener(new BaseWidget.OnDismissListener(){
//            @Override
//            public void onDismiss() {
//                showController();
//            }
//        });
//        addView(mShareWidget,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//
//        mWidgetArray.add(mShareWidget);
        mWidgetArray.add(mPlayerMenuWidget);
    }

    private void resetLayout() {
        mPlayerMenuWidget.resetLayout(mIsHorizontal);
//        mShareWidget.resetLayout(mIsHorizontal);
    }

    private SeekBar.OnSeekBarChangeListener mSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                int postion = progress * mPlayer.getDuration() / SEEK_BAR_MAX;
                mPlayer.seekTo(postion);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SHOW_DANMU_INPUT:
                    mDanmuInputLayout.setVisibility(VISIBLE);
                    mEditText.requestFocus();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPlayer != null && mSeekbar != null) {
                long currenttime = mPlayer.getCurrentPosition();
                long duration = mPlayer.getDuration();
                long bufferposition = mPlayer.getBufferedPosition();
                if (bufferposition < 0) {
                    bufferposition = 0;
                }

                mCurrenttimeTextView.setText(VideoUtils.length2time(currenttime));
                mDurationTextView.setText(VideoUtils.length2time(duration));
                if (duration != 0) {
                    mSeekbar.setProgress((int) (currenttime * SEEK_BAR_MAX / duration));
                    mSeekbar.setSecondaryProgress((int) (bufferposition * SEEK_BAR_MAX / duration));
//                    Log.e(TAG, "video current: " +currenttime +" buffer: "+ bufferposition);
                }
                mHandler.removeCallbacks(progressRunnable);
                mHandler.postDelayed(progressRunnable, 1000);
            }
        }
    };

    @Override
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    @Override
    public void showController() {
        Log.d(TAG, "video showController: ");
        AnimaUtils.showControllerTranslate(mTopLayout, true);
        AnimaUtils.showControllerTranslate(mBottomLayout, false);
        AnimaUtils.setMask(this, true);
//        mTimeAndPowerView.star();
        mIsShowing = true;
//        ScreenUtil.setStatusBarVisible((Activity) mContext, true);
    }

    @Override
    public void hideController() {
        Log.d(TAG, "video hideController: ");
        AnimaUtils.hideControllerTranslate(mTopLayout, true);
        AnimaUtils.hideControllerTranslate(mBottomLayout, false);
        AnimaUtils.setMask(this, false);
//        mTimeAndPowerView.stop();
        mIsShowing = false;
//        ScreenUtil.setStatusBarVisible((Activity) mContext, false);
    }

    @Override
    public void pause() {
        mHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void start() {
//        mPlayBtn.setActivated(true);
//        mHandler.post(progressRunnable);
    }

    @Override
    public void seek(int seekTo) {

    }

    @Override
    public void replay() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, "video onTouchEvent: ");
        if (mIsShowing) {
            hideController();
        } else {
            showController();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        mIsHorizontal = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
        resetLayout();
        super.onConfigurationChanged(newConfig);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        mShareWidget.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "video onKeyDown() called with: " + "keyCode = [" + keyCode + "], event = [" + event + "]");
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            for (BaseWidget i : mWidgetArray) {
                if (i.isShown()) {
                    i.hide();
                    return true;
                }
            }
            ((Activity)mContext).finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "video onDetachedFromWindow: ");
        KeyboardUtil.removeSoftKeyboardChangedListener(mSoftKeyboardChangeListener);
        super.onDetachedFromWindow();
    }
}
