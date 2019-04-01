package com.bearever.business.mydiary;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.bearever.adapter.MyDiaryAdapter;
import com.bearever.baselib.mvp.BaseFragment;
import com.bearever.bean.DiaryItemDO;
import com.bearever.business.creatediary.CreateDiaryActivity;
import com.bearever.diary.R;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的日记的fragment
 * luoming
 * 2019/3/23
 **/
public class MyDiaryFragment extends BaseFragment<MyDiaryPresenter> implements MyDiaryContact.View {
    private static final String TAG = "MyDiaryFragment";
    private FloatingActionButton mCreateFab; //创建日记的按钮
    private RecyclerView mMyDiaryRv; //显示日记的rv
    private SwipeRefreshLayout mSwipeRefreshLayout; //刷新布局

    private MyDiaryAdapter mAdapter;
    private static final int REQUEST_CODE_CREATE = 11;

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
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        initSwipeLayout();
        initRv();
        initFloatBtn();
    }

    private void initSwipeLayout() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refresh();
            }
        });
    }

    private void initRv() {
        mAdapter = new MyDiaryAdapter(new ArrayList<>());
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.i(TAG, "onItemClick: ");
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.loadMore();
            }
        });

        mMyDiaryRv.setAdapter(mAdapter);
        mMyDiaryRv.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initFloatBtn() {
        mCreateFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDiaryActivity.start(getActivity(), REQUEST_CODE_CREATE);
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyDiaryPresenter(this, getContext());
        mPresenter.refresh();
    }

    @Override
    protected void setView() {

    }

    @Override
    public void showDiary(List<DiaryItemDO> list, boolean next) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.getData().clear();
        mAdapter.notifyLoadMoreToLoading();
        mAdapter.addData(list);
        mAdapter.setEnableLoadMore(next);
        mAdapter.loadMoreComplete();
    }

    @Override
    public void showMoreDiary(List<DiaryItemDO> list, boolean next) {
        mSwipeRefreshLayout.setRefreshing(false);
        mAdapter.addData(list);
        mAdapter.setEnableLoadMore(next);
        mAdapter.loadMoreComplete();
    }
}
