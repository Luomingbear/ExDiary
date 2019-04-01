package com.bearever.diary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bearever.adapter.MainViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MainViewPagerAdapter mMainViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化view
        initView();
        //初始化行为
        initEvent();
    }

    /**
     * 初始化view
     */
    private void initView() {
        initFragment();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        mViewPager = findViewById(R.id.view_pager);
        mMainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMainViewPagerAdapter);
    }

    private void initEvent() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mMainViewPagerAdapter.getItem(mViewPager.getCurrentItem()).onActivityResult(requestCode, resultCode, data);
    }
}
