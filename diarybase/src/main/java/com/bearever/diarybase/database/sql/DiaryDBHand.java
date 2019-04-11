package com.bearever.diarybase.database.sql;

import android.content.Context;

import com.bearever.diarybase.database.DiaryDBInterface;
import com.bearever.diarybase.database.DiaryDBManager;
import com.bearever.diarybase.util.TimeUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DiaryDBHand implements DiaryDBInterface {
    private DiaryDB mDiaryDB;
    private ThreadPoolExecutor mThreadPool;

    public DiaryDBHand(Context appContext) {
        //定义一个单线程池，最多有50个请求排队
        mThreadPool = new ThreadPoolExecutor(1, 1, 30,
                TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50));
        mDiaryDB = new DiaryDB(appContext.getApplicationContext());
    }

    @Override
    public void getByID(int id, DiaryDBManager.OnDiaryDBListener listener) {
        DiaryRunDO runDO = new DiaryRunDO();
        runDO.runEnum = DiaryRunEnum.QUERY_ONE;
        runDO.data = new DiaryDB.DiaryDatabaseDO();
        runDO.data.setDid(id);
        runDO.listener = listener;
        mThreadPool.execute(new DiaryRunable(runDO));
    }

    @Override
    public void getByPage(int p, DiaryDBManager.OnDiaryDBListener listener) {
        DiaryRunDO runDO = new DiaryRunDO();
        runDO.runEnum = DiaryRunEnum.QUERY_BY_PAGE;
        runDO.setP(p);
        runDO.listener = listener;
        mThreadPool.execute(new DiaryRunable(runDO));
    }

    @Override
    public void insert(DiaryDB.DiaryDatabaseDO data, DiaryDBManager.OnDiaryDBListener listener) {
        DiaryRunDO runDO = new DiaryRunDO();
        runDO.runEnum = DiaryRunEnum.INSERT;
        runDO.data = data;
        runDO.listener = listener;
        mThreadPool.execute(new DiaryRunable(runDO));
    }

    @Override
    public void update(DiaryDB.DiaryDatabaseDO data, DiaryDBManager.OnDiaryDBListener listener) {
        DiaryRunDO runDO = new DiaryRunDO();
        runDO.runEnum = DiaryRunEnum.UPDATE;
        runDO.data = data;
        runDO.listener = listener;
        mThreadPool.execute(new DiaryRunable(runDO));
    }

    @Override
    public void delete(int id, DiaryDBManager.OnDiaryDBListener listener) {
        DiaryRunDO runDO = new DiaryRunDO();
        runDO.runEnum = DiaryRunEnum.DELETE;
        runDO.data = new DiaryDB.DiaryDatabaseDO();
        runDO.data.setDid(id);
        runDO.listener = listener;
        mThreadPool.execute(new DiaryRunable(runDO));
    }

    private class DiaryRunable implements Runnable {
        private DiaryRunDO runDO;

        public DiaryRunable(DiaryRunDO runDO) {
            this.runDO = runDO;
        }

        @Override
        public void run() {
            boolean suc = false;
            switch (runDO.runEnum) {
                case INSERT:
                    mDiaryDB.open();
                    runDO.data.setTime(TimeUtils.currentTime());
                    suc = mDiaryDB.insert(runDO.data);
                    DiaryDB.DiaryDatabaseDO insertDO = mDiaryDB.getByTime(runDO.data.getTime());
                    mDiaryDB.close();
                    if (runDO.listener != null) {
                        if (suc) {
                            runDO.listener.finish(Collections.singletonList(insertDO), false);
                        } else {
                            runDO.listener.finish(null, false);
                        }
                    }
                    break;
                case DELETE:
                    mDiaryDB.open();
                    DiaryDB.DiaryDatabaseDO item = mDiaryDB.getById(runDO.data.getDid());
                    if (item == null && runDO.listener != null) { //数据不存在就不能删除了
                        runDO.listener.finish(null, false);
                        break;
                    }
                    suc = mDiaryDB.delete(runDO.data.getDid());
                    mDiaryDB.close();
                    if (runDO.listener != null) {
                        if (suc) {
                            runDO.listener.finish(Collections.singletonList(item), false);
                        } else {
                            runDO.listener.finish(null, false);
                        }
                    }
                    break;
                case UPDATE:
                    mDiaryDB.open();
                    runDO.data.setTime(TimeUtils.currentTime());
                    suc = mDiaryDB.update(runDO.data);
                    mDiaryDB.close();
                    if (runDO.listener != null) {
                        if (suc) {
                            runDO.listener.finish(Collections.singletonList(runDO.data), false);
                        } else {
                            runDO.listener.finish(null, false);
                        }
                    }
                    break;
                case QUERY_ONE:
                    mDiaryDB.open();
                    DiaryDB.DiaryDatabaseDO data = mDiaryDB.getById(runDO.data.getDid());
                    mDiaryDB.close();
                    if (runDO.listener != null) {
                        if (data != null) {
                            runDO.listener.finish(Collections.singletonList(data), false);
                        } else {
                            runDO.listener.finish(null, false);
                        }
                    }
                    break;
                case QUERY_BY_PAGE:
                    mDiaryDB.open();
                    List<DiaryDB.DiaryDatabaseDO> list = mDiaryDB.getByPage(runDO.getP());
                    boolean next = mDiaryDB.hasNext(runDO.getP());
                    mDiaryDB.close();
                    if (runDO.listener != null) {
                        if (list != null && !list.isEmpty()) {
                            runDO.listener.finish(list, next);
                        } else {
                            runDO.listener.finish(null, false);
                        }
                    }
                    break;
            }
        }
    }

    public class DiaryRunDO {
        private DiaryRunEnum runEnum;
        private int p;
        private DiaryDB.DiaryDatabaseDO data;
        private DiaryDBManager.OnDiaryDBListener listener;

        public DiaryRunDO() {
        }

        public DiaryRunEnum getRunEnum() {
            return runEnum;
        }

        public void setRunEnum(DiaryRunEnum runEnum) {
            this.runEnum = runEnum;
        }

        public int getP() {
            return p;
        }

        public void setP(int p) {
            this.p = p;
        }

        public DiaryDB.DiaryDatabaseDO getData() {
            return data;
        }

        public void setData(DiaryDB.DiaryDatabaseDO data) {
            this.data = data;
        }

        public DiaryDBManager.OnDiaryDBListener getListener() {
            return listener;
        }

        public void setListener(DiaryDBManager.OnDiaryDBListener listener) {
            this.listener = listener;
        }
    }

    public enum DiaryRunEnum {
        INSERT,
        DELETE,
        UPDATE,
        QUERY_ONE,
        QUERY_BY_PAGE,
    }
}
