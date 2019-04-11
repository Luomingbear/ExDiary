package com.bearever.diarybase.database;

import com.bearever.diarybase.database.sql.DiaryDB;

public interface DiaryDBInterface {
    void getByID(int id, DiaryDBManager.OnDiaryDBListener listener);

    void getByPage(int p, DiaryDBManager.OnDiaryDBListener listener);

    void insert(DiaryDB.DiaryDatabaseDO data, DiaryDBManager.OnDiaryDBListener listener);

    void update(DiaryDB.DiaryDatabaseDO data, DiaryDBManager.OnDiaryDBListener listener);

    void delete(int id, DiaryDBManager.OnDiaryDBListener listener);
}
