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
    }
}
