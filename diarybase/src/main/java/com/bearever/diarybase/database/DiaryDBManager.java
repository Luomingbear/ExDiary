package com.bearever.diarybase.database;

import android.content.Context;

import com.bearever.diarybase.database.sql.DiaryDB;
import com.bearever.diarybase.database.sql.DiaryDBHand;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiaryDBManager implements DiaryDBInterface {
    private static DiaryDBManager instance;
    private DiaryDBHand mDiaryDBHand;

    public static DiaryDBManager getInstance() {
        if (instance == null) {
            synchronized (DiaryDBManager.class) {
                if (instance == null) {
                    instance = new DiaryDBManager();
                }
            }
        }
        return instance;
    }

    private DiaryDBManager() {
    }

    public void init(Context context) {
        mDiaryDBHand = new DiaryDBHand(context);
    }

    @Override
    public void getByID(int id, OnDiaryDBListener listener) {
        if (mDiaryDBHand == null) {
            throw new NullPointerException("请先执行init方法");
        }
        mDiaryDBHand.getByID(id, listener);
    }

    @Override
    public void getByPage(int p, OnDiaryDBListener listener) {
        if (mDiaryDBHand == null) {
            throw new NullPointerException("请先执行init方法");
        }
        mDiaryDBHand.getByPage(p, listener);
    }

    @Override
    public void insert(DiaryDB.DiaryDatabaseDO data, OnDiaryDBListener listener) {
        if (mDiaryDBHand == null) {
            throw new NullPointerException("请先执行init方法");
        }
        mDiaryDBHand.insert(data, listener);
    }

    @Override
    public void update(DiaryDB.DiaryDatabaseDO data, OnDiaryDBListener listener) {
        if (mDiaryDBHand == null) {
            throw new NullPointerException("请先执行init方法");
        }
        mDiaryDBHand.update(data, listener);
    }

    @Override
    public void delete(int id, OnDiaryDBListener listener) {
        if (mDiaryDBHand == null) {
            throw new NullPointerException("请先执行init方法");
        }
        mDiaryDBHand.delete(id, listener);
    }

    public interface OnDiaryDBListener {
        /**
         * 结果回调
         *
         * @param results null说明失败了
         * @param next 是否还有下一页
         */
        void finish(List<DiaryDB.DiaryDatabaseDO> results, boolean next);
    }
}
