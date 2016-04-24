package com.tanjinc.tmediaplayer.player;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.TimeUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tanjinc.tmediaplayer.R;
import com.tanjinc.tmediaplayer.widgets.PlayerMenuAdapter;
import com.tanjinc.tmediaplayer.widgets.PlayerMenuWidget;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * Created by tanjincheng on 16/3/30.
 */
public class MovieController extends RelativeLayout implements IController{

    private static final int SEEK_BAR_MAX = 10000;
    private static final String TAG = "MovieController";
    private Context mContext;
    private View mTopPart;
    private View mBottomPart;
    private boolean mIsShowing;
    private IVideoView mPlayer;

    private ImageButton mPlayBtn;
    private Button mMenuBtn;
    private SeekBar mSeekBar;
    private TextView mCurrentTimeTv;
    private TextView mDurationTv;
    private TextView mTitleTv;

    private boolean mIsHorizontal;
    //widget
    private PlayerMenuWidget mPlayerMenuWidget;

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
        LayoutInflater.from(mContext).inflate(R.layout.player_controller_layout, this);

        mIsHorizontal = mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        mTopPart = findViewById(R.id.top_layout);
        mBottomPart = findViewById(R.id.bottom_layout);

        mTitleTv = (TextView) findViewById(R.id.title);

        mSeekBar = (SeekBar) findViewById(R.id.seekbar);
        mSeekBar.setMax(SEEK_BAR_MAX);
        mSeekBar.setOnSeekBarChangeListener(mSeekBarChangeListener);

        mCurrentTimeTv = (TextView) findViewById(R.id.currenttime_tv);
        mDurationTv = (TextView) findViewById(R.id.duration_tv);

        mPlayBtn = (ImageButton) findViewById(R.id.play);
        mPlayBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    mHandler.removeCallbacks(progressRunnable);
                } else {
                    mPlayer.start();
                    mHandler.post(progressRunnable);
                }
            }
        });

        mMenuBtn = (Button) findViewById(R.id.player_menu_btn);
        mMenuBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayerMenuWidget.isShown()) {
                    mPlayerMenuWidget.hideWithAnim(true);
                } else {
                    mPlayerMenuWidget.showWithAnim(true);
                }
            }
        });

        addWidgets();
        resetLayout();
    }

    private void addWidgets() {
        mPlayerMenuWidget = new PlayerMenuWidget(mContext);
        addView(mPlayerMenuWidget);

        ArrayList<PlayerMenuWidget.PlayerMenuData> menuData = new ArrayList<PlayerMenuWidget.PlayerMenuData>();
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY, "播放"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY,false,"单个循环"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_PLAY,false,"连续播放"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN,"窗口大小"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN,true,"屏幕适应"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN,false, "全屏"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_SCREEN,false,"原尺寸"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, "DLNA"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "codi"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "xbmc"));
        menuData.add(new PlayerMenuWidget.PlayerMenuData(PlayerMenuWidget.MENU_ITEM_TYPE.MENU_DNLA, false, "xiaomi"));
        mPlayerMenuWidget.setMenuData(menuData);
        mPlayerMenuWidget.hideWithAnim(false);
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
            if (mPlayer != null && mSeekBar != null) {
                long currenttime = mPlayer.getCurrentPosition();
                long duration = mPlayer.getDuration();

                mCurrentTimeTv.setText(VideoUtils.length2time(currenttime));
                mDurationTv.setText(VideoUtils.length2time(duration));
                if (duration != 0) {
                    mSeekBar.setProgress((int) (currenttime * SEEK_BAR_MAX / duration));
                }
                mHandler.removeCallbacks(progressRunnable);
                mHandler.postDelayed(progressRunnable, 1000);
            }
        }
    };

    @Override
    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    @Override
    public void showController() {
        mBottomPart.setVisibility(VISIBLE);
        mTopPart.setVisibility(VISIBLE);
        mIsShowing = true;
    }

    @Override
    public void hideController() {
        mTopPart.setVisibility(GONE);
        mBottomPart.setVisibility(GONE);
        mIsShowing = false;
    }

    @Override
    public void pause() {
        mHandler.removeCallbacks(progressRunnable);
    }

    @Override
    public void start() {
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
        if (mPlayerMenuWidget.isShown()) {
            mPlayerMenuWidget.hideWithAnim(true);
            return false;
        }
        if(mIsShowing) {
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
}
