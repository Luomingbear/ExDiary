package com.bearever.business.mydiary;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.bearever.baselib.mvp.BaseFragment;
import com.bearever.bean.DiaryItemDO;
import com.bearever.diary.R;

import java.util.List;

/**
 * 我的日记的fragment
 * luoming
 * 2019/3/23
 **/
public class MyDiaryFragment extends BaseFragment<MyDiaryPresenter> implements MyDiaryContact.View {
    private FloatingActionButton mCreateFab; //创建日记的按钮
    private RecyclerView mMyDiaryRv; //显示日记的rv

    @Override
    protected int initLayout() {
        return R.layout.fragment_my_diary;
    }

    @Override
    protected void getIntentArguments() {

    }

    @Override
    protected void initView() {
        mCreateFab = (FloatingActionButton) findViewById(R.id.fab_create);
        mMyDiaryRv = (RecyclerView) findViewById(R.id.rv_my_diary);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void setView() {

    }

    @Override
    public void showDiary(List<DiaryItemDO> list, boolean next) {

    }

    @Override
    public void showMoreDiary(List<DiaryItemDO> list, boolean next) {

    }
}
