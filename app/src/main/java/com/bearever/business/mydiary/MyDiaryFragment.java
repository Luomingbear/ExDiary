package com.bearever.business.mydiary;

import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.bearever.adapter.MyDiaryAdapter;
import com.bearever.baselib.mvp.BaseFragment;
import com.bearever.bean.DiaryItemDO;
import com.bearever.business.creatediary.CreateDiaryActivity;
import com.bearever.diary.R;
import com.bearever.event.DiaryCreateEvent;
import com.bearever.event.DiaryDeleteEvent;
import com.bearever.event.DiaryUpdateEvent;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
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
    private static final int REQUEST_CODE_EDIT = REQUEST_CODE_CREATE + 1;

    @Override
    protected int initLayout() {
        return R.layout.fragment_my_diary;
    }

    @Override
    protected void getIntentArguments() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
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
                DiaryItemDO itemDO = (DiaryItemDO) adapter.getItem(position);
                CreateDiaryActivity.start(MyDiaryFragment.this, REQUEST_CODE_EDIT, itemDO);
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
                CreateDiaryActivity.start(MyDiaryFragment.this, REQUEST_CODE_CREATE);
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MyDiaryPresenter(this, getContext());
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.refresh();
    }

    @Override
    protected void setView() {

    }

    @Override
    public void showDiary(List<DiaryItemDO> list, boolean next) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.getData().clear();
                mAdapter.notifyDataSetChanged();
                mAdapter.addData(list);
                mAdapter.setEnableLoadMore(next);
                mAdapter.loadMoreComplete();
            }
        });
    }

    @Override
    public void showMoreDiary(List<DiaryItemDO> list, boolean next) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.addData(list);
                mAdapter.setEnableLoadMore(next);
                mAdapter.loadMoreComplete();
            }
        });
    }

    /**
     * 新增日记之后
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void afterCreateSucceed(DiaryCreateEvent event) {
        if (event == null) {
            return;
        }
        DiaryItemDO itemDO = DiaryItemDO.from(event.data);
        if (itemDO != null) {
            mAdapter.addData(0, itemDO);
            mMyDiaryRv.scrollToPosition(0);
        }
    }

    /**
     * 修改日记之后
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void afterEditSucceed(DiaryUpdateEvent event) {
        if (event == null) {
            return;
        }
        DiaryItemDO itemDO = DiaryItemDO.from(event.data);
        if (itemDO != null) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                DiaryItemDO item = mAdapter.getItem(i);
                if (item.getDid() == event.data.getDid()) {
                    //交换数据位置
                    if (i > 0) {
                        for (int j = i; j > 0; j--) { //往后摞一位
                            Collections.swap(mAdapter.getData(), j, j - 1);
                        }
                        mAdapter.notifyItemMoved(i, 0);
                    }
                    mAdapter.setData(0, itemDO);
                    return;
                }
            }
        }
    }

    /**
     * 删除日记之后
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void afterDeleteSucceed(DiaryDeleteEvent event) {
        if (event == null) {
            return;
        }
        DiaryItemDO itemDO = DiaryItemDO.from(event.data);
        if (itemDO != null) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                DiaryItemDO item = mAdapter.getItem(i);
                if (item.getDid() == event.data.getDid()) {
                    mAdapter.remove(i);
                    return;
                }
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    private H mHandler = new H(this, Looper.getMainLooper());

    private static class H extends Handler {
        private WeakReference<MyDiaryFragment> reference;

        public H(MyDiaryFragment fragment, Looper looper) {
            super(looper);
            this.reference = new WeakReference<>(fragment);
        }
    }
}
