package com.tanjinc.tmediaplayer;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tanjinc.tmediaplayer.data.DoubanMovieDataSource;
import com.tanjinc.tmediaplayer.data.LocalVideoDataSource;
import com.tanjinc.tmediaplayer.utils.VideoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.left_menu)
    ListView mLeftMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.tablayout)
    TabLayout mTablayout;

    private Fragment mLocalVideoFragment;
    private Fragment mDoubanMovieFragment;
    private ViewPagerAdapter mViewPagerAdapter;

    private ArrayList<Fragment> mFragmentArrayList = new ArrayList<>();
    private ArrayList<String> mTitleList = new ArrayList<>();

    private LocalVideoPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initToolbar();
        initDrawerLayout();
        initViewPager();
    }


    private void initViewPager() {
        // 采用Google官方MVP模板
        // create view
        mLocalVideoFragment = new LocalVideoFragment();
        mDoubanMovieFragment = new DoubanMovieFragment();

        // create presenter
        new LocalVideoPresenter((VideoContract.View) mLocalVideoFragment, new LocalVideoDataSource(this));
        new LocalVideoPresenter((VideoContract.View) mDoubanMovieFragment, new DoubanMovieDataSource(this));

        mFragmentArrayList.clear();
        mFragmentArrayList.add(mLocalVideoFragment);
        mFragmentArrayList.add(mDoubanMovieFragment);

        mTitleList.add("本地视频");
        mTitleList.add("豆瓣Top10");


        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "video onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "video onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG, "video onPageScrollStateChanged: " + state);
            }
        });

        mTablayout.setupWithViewPager(mViewPager);
    }

    private void initDrawerLayout() {
        //设置阴影
        mDrawerLayout.setDrawerShadow(android.R.drawable.dialog_frame, GravityCompat.START);
        mLeftMenu = (ListView) findViewById(R.id.left_menu);
        mLeftMenu.setAdapter(new LeftMenuAdapter(this));
        mLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    VideoUtils.setPlayerType(getApplicationContext(), VideoUtils.PLAYER_TYPE_EXO);
                } else if (position == 4) {
                    VideoUtils.setPlayerType(getApplicationContext(), VideoUtils.PLAYER_TYPE_DEF);
                }
            }
        });
    }

    private void initToolbar() {
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

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            if (mFragmentArrayList != null && mFragmentArrayList.size() > position) {
                return mFragmentArrayList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return mFragmentArrayList != null ? mFragmentArrayList.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList != null && position < mTitleList.size() ? mTitleList.get(position) : null;
        }
    }
}
