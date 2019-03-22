package com.bearever.baselib.mvp;

/**
 * mvp的契约类
 * luoming
 * 2019/3/21
 **/
public class BaseContact {
    public interface View {
        /**
         * 显示loading弹窗
         */
        void showLoading();

        /**
         * 隐藏loading弹窗
         */
        void hideLoading();
    }

    public interface Model {

    }

    public interface Presenter {

    }
}
