package com.bearever.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bearever.business.mydiary.MyDiaryFragment;
import com.bearever.business.receivediary.ReceiveDiaryFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * luoming
 * 2019/3/30
 **/
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new MyDiaryFragment());
        fragmentList.add(new ReceiveDiaryFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
