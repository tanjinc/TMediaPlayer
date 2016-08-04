package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.widgets.IWidget;
import com.tanjinc.tmediaplayer.widgets.PlayerMenuWidget;
import com.tanjinc.tmediaplayer.widgets.ShareWidget;

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
    @BindView(R.id.edit_danmu)
    EditText mEditText;

    private Context mContext;
    private boolean mIsShowing;
    private IVideoView mPlayer;


    private boolean mIsHorizontal;
    //widget
    private PlayerMenuWidget mPlayerMenuWidget;
    private ShareWidget mShareWidget;

    private ArrayList<IWidget> mWidgets = new ArrayList<>();

    @OnClick({R.id.share_btn, R.id.play, R.id.player_menu_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.share_btn:
                mShareWidget.showWithAnim(true);
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
//                    mPlayerMenuWidget.hideWithAnim(true);
//                } else {
//                    mPlayerMenuWidget.showWithAnim(true);
//                }
                showDanmu();
                break;
        }
    }


    private void showDanmu() {
        InputMethodManager methodManager = (InputMethodManager) mContext.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.toggleSoftInput(0,0);
        mEditText.requestFocus();
    }
    private enum PlayState {

    }

    public MovieController(Context context, IVideoView player) {
        super(context);
        mContext = context;
        mPlayer = player;
        init();
    }

    public MovieController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
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
        addWidgets();
        resetLayout();
    }

    private void addWidgets() {
        mPlayerMenuWidget = new PlayerMenuWidget(mContext);
        addView(mPlayerMenuWidget);
        mWidgets.add(mPlayerMenuWidget);

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
        mPlayerMenuWidget.hideWithAnim(false);

//        mShareWidget = new ShareWidget(mContext);
//        addView(mShareWidget);
//        mShareWidget.resetLayout(mIsHorizontal);
//        mWidgets.add(mShareWidget);


    }

    private void resetLayout() {
        mPlayerMenuWidget.resetLayout(mIsHorizontal);
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


    private Handler mHandler = new Handler();
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
        mBottomLayout.setVisibility(VISIBLE);
        mTopLayout.setVisibility(VISIBLE);
        mIsShowing = true;
    }

    @Override
    public void hideController() {
        mBottomLayout.setVisibility(GONE);
        mTopLayout.setVisibility(GONE);
        mIsShowing = false;
    }

    @Override
    public void pause() {
        mHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void start() {
        mPlayBtn.setActivated(true);
        mHandler.post(progressRunnable);
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
//        if (mPlayerMenuWidget.isShown()) {
//            mPlayerMenuWidget.hideWithAnim(true);
//            return false;
//        }

        for (IWidget i : mWidgets) {
            i.hideWithAnim(true);
        }
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
        mShareWidget.onActivityResult(requestCode, resultCode, data);
    }
}
