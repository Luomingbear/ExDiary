package com.bearever.business.mydiary;

import com.bearever.baselib.listener.EventCallBack;
import com.bearever.baselib.mvp.BaseContact;
import com.bearever.bean.DiaryItemDO;

import java.util.List;

/**
 * luoming
 * 2019/3/23
 **/
public class MyDiaryContact {
    public interface View extends BaseContact.View {
        /**
         * 显示日记
         *
         * @param list
         * @param next
         */
        void showDiary(List<DiaryItemDO> list, boolean next);

        /**
         * 显示更多的日记
         *
         * @param list
         * @param next
         */
        void showMoreDiary(List<DiaryItemDO> list, boolean next);
    }

    public interface Model extends BaseContact.Model {
        /**
         * 获取本地日记数据
         *
         * @param page
         * @param callBack
         */
        void getDiaryFromLocal(int page, EventCallBack callBack);

        /**
         * 获取网络日记数据
         *
         * @param page
         * @param callBack
         */
        void getDiaryFromNet(int page, EventCallBack callBack);
    }

    public interface Presenter extends BaseContact.Presenter {
        /**
         * 刷新
         */
        void refresh();

        /**
         * 加载更多
         */
        void loadMore();
    }
}
