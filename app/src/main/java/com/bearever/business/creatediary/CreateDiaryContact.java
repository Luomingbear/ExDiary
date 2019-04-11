package com.bearever.business.creatediary;

import com.bearever.baselib.listener.EventCallBack;
import com.bearever.baselib.mvp.BaseContact;
import com.bearever.bean.CreateDiaryDO;

/**
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryContact {
    public interface View extends BaseContact.View {
        /**
         * 显示初始化的ui
         *
         * @param initDiaryDO
         */
        void showInitUI(CreateDiaryDO initDiaryDO);

        /**
         * 上传成功
         */
        void postSucceed();

        /**
         * 上传失败
         *
         * @param msg
         */
        void postFailed(String msg);
    }

    public interface Presenter extends BaseContact.Presenter {
        /**
         * 上传
         *
         * @param postData
         */
        void post(CreateDiaryDO postData);

        /**
         * 保存在本地
         */
        void saveLocal(CreateDiaryDO postData);

        /**
         * 更新在本地
         */
        void updateLocal(CreateDiaryDO postData);

        /**
         * 页面结束和销毁的时候
         *
         * @param saveData
         * @param editModel
         */
        void onStopAndFinish(CreateDiaryDO saveData, boolean editModel);
    }

    public interface Model extends BaseContact.Model {
        /**
         * 上传
         *
         * @param postData
         * @param callBack
         */
        void postNet(CreateDiaryDO postData, EventCallBack callBack);

        /**
         * 保存在本地
         *
         * @param saveData
         * @param callBack
         */
        void saveLocal(CreateDiaryDO saveData, EventCallBack callBack);

        /**
         * 更新本地的记录
         *
         * @param saveData
         * @param callBack
         */
        void updateLocal(CreateDiaryDO saveData, EventCallBack callBack);

        /**
         * 删除一条记录
         *
         * @param saveData
         * @param callBack
         */
        void deleteLocal(CreateDiaryDO saveData, EventCallBack callBack);
    }
}
