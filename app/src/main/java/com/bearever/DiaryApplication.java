package com.bearever;

import android.app.Application;

import com.bearever.baselib.http.LogInterceptor;
import com.bearever.baselib.mickeystore.MickeyStore;
import com.bearever.diarybase.constant.DyConstants;
import com.bearever.diarybase.database.DiaryDBManager;
import com.bearever.diarybase.database.sql.DiaryDB;
import com.lzy.okgo.OkGo;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * luoming
 * 2019/3/23
 **/
public class DiaryApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        initHttp();
        initDatabase();
    }

    private void initHttp() {
        //初始化网络请求
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        LogInterceptor logInterceptor = new LogInterceptor();
        //全局的读取超时时间
        builder.readTimeout(DyConstants.TIME_OUT, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(DyConstants.TIME_OUT, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(DyConstants.TIME_OUT, TimeUnit.MILLISECONDS);
        //添加拦截器
        if (DyConstants.DEBUG) {
            builder.addInterceptor(logInterceptor);
        }
        OkGo.getInstance().init(this).setOkHttpClient(builder.build());
    }

    private void initDatabase() {
        MickeyStore.getInstance().init(this);
        DiaryDBManager.getInstance().init(this);
    }
}
