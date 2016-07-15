package com.tanjinc.tmediaplayer;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ListView;

import com.tanjinc.tmediaplayer.data.DoubanMovieDataSource;
import com.tanjinc.tmediaplayer.player.VideoUtils;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private FragmentManager mFagmentManager;
    private VideoContract.View mLocalVideoFragment;
    private Fragment mMainFragment;
    private HorizontalScrollView scrollView;
    private DrawerLayout mDrawerLayout;
    private FrameLayout mFrameLayout;
    private ListView mMenuList;
    private Toolbar mToolbar;
    private LocalVideoPresenter mPresenter;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        mContext = this;
//        android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
        FragmentTransaction transaction =  getSupportFragmentManager().beginTransaction();


        final SharedPreferences sharedPreferences = getSharedPreferences("tanjinc", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        initToolbar();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //设置阴影
        mDrawerLayout.setDrawerShadow(android.R.drawable.dialog_frame, GravityCompat.START);

        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mMenuList = (ListView) findViewById(R.id.left_menu);
        mMenuList.setAdapter(new LeftMenuAdapter(this));
        mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    VideoUtils.setPlayerType(mContext,VideoUtils.PLAYER_TYPE_EXO);
                } else if (position == 4) {
                    VideoUtils.setPlayerType(mContext,VideoUtils.PLAYER_TYPE_DEF);
                }
            }
        });
//
        //create view
        if (savedInstanceState == null) {
            mLocalVideoFragment = new LocalVideoFragment();
//            transaction.add(R.id.fragment_container2, (Fragment) mLocalVideoFragment, "video");
            DoubanMovieFragment doubanMovieFragment = new DoubanMovieFragment();
            transaction.add(doubanMovieFragment, "douban");
            transaction.commit();
        }

        // create presenter
//        mPresenter = new LocalVideoPresenter(mLocalVideoFragment, new LocalVideoDataSource(mContext));
        mPresenter = new LocalVideoPresenter(mLocalVideoFragment, new DoubanMovieDataSource(mContext));
        getContentResolver();

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("MyMediaPlayer");
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "video onKeyDown() called with: " + "keyCode = [" + keyCode + "], event = [" + event + "]");
        return super.onKeyDown(keyCode, event);
    }
}
