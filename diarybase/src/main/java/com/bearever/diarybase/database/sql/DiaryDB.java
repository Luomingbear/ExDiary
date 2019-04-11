package com.bearever.diarybase.database.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.bearever.diarybase.util.TimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 日记的数据库
 */
public class DiaryDB {

    private static final String TAG = "MKDatabase";
    private static String mDatabaseName = "exdiary.db";
    private static int mDatabaseVersion = 2;
    private String mDataTab = "mickeys";
    private String ID = "did";
    private String TITLE = "title";
    private String CONTENT = "content";
    private String TIME = "time";
    private String LOCATION = "location";
    private String MUSIC = "music";
    private String EXCHANGE = "exchange";
    private String CREATE_TAB;
    private String CLEAR_TAB;

    private Context mContext;
    private DiarySQLHelper mDiarySqlLiteHelper;
    private SQLiteDatabase mSqLiteDatabase;

    public DiaryDB(Context context) {
        this(context, "diary");
    }

    public DiaryDB(Context context, String tabName) {
        this.mContext = context.getApplicationContext();
        if (!TextUtils.isEmpty(tabName)) {
            this.mDataTab = tabName;
        }

        CREATE_TAB = "CREATE TABLE " + mDataTab + "( "
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TITLE + " VARCHAR(255),"
                + CONTENT + " TEXT,"
                + TIME + " DATETIME,"
                + EXCHANGE + " INTEGER,"
                + LOCATION + " VARCHAR(255),"
                + MUSIC + " VARCHAR(255)"
                + ")";

        CLEAR_TAB = "DELETE FROM " + mDataTab;
    }

    public void open() {
        if (mDiarySqlLiteHelper == null) {
            mDiarySqlLiteHelper = new DiarySQLHelper(mContext);
        }
        mSqLiteDatabase = mDiarySqlLiteHelper.getWritableDatabase();
    }

    public void close() {
        if (mDiarySqlLiteHelper != null) {
            mDiarySqlLiteHelper.close();
        }
    }

    /**
     * 插入数据
     *
     * @param data
     */
    public boolean insert(DiaryDatabaseDO data) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, data.tittle);
        contentValues.put(CONTENT, data.content);
        contentValues.put(LOCATION, data.location);
        contentValues.put(MUSIC, data.music);
        contentValues.put(EXCHANGE, data.exchange);
        contentValues.put(TIME, data.time);
        return mSqLiteDatabase.insert(mDataTab, null, contentValues) != -1;
    }

    /**
     * 更新数据
     *
     * @param data
     */
    public boolean update(DiaryDatabaseDO data) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, data.did);
        contentValues.put(TITLE, data.tittle);
        contentValues.put(CONTENT, data.content);
        contentValues.put(LOCATION, data.location);
        contentValues.put(TIME, data.time);
        contentValues.put(MUSIC, data.music);
        contentValues.put(EXCHANGE, data.exchange);
        return mSqLiteDatabase.update(mDataTab, contentValues, ID + " = ?", new String[]{data.getDid() + ""}) > 0;
    }


    /**
     * 获取数据
     *
     * @param id
     * @return
     */
    public DiaryDatabaseDO getById(int id) {
        if (mSqLiteDatabase == null) {
            return null;
        }
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.query(mDataTab, new String[]{TITLE, CONTENT, LOCATION, TIME, MUSIC, EXCHANGE},
                    ID + " = ?", new String[]{id + ""}, null, null, null, " 1");
            if (cursor != null && cursor.moveToNext()) {
                DiaryDatabaseDO result = new DiaryDatabaseDO();
                result.did = id;
                result.tittle = cursor.getString(cursor.getColumnIndex(TITLE));
                result.content = cursor.getString(cursor.getColumnIndex(CONTENT));
                result.time = cursor.getString(cursor.getColumnIndex(TIME));
                result.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                result.music = cursor.getString(cursor.getColumnIndex(MUSIC));
                result.exchange = cursor.getInt(cursor.getColumnIndex(EXCHANGE)) == 1;
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 通过时间获取数据
     *
     * @param time
     * @return
     */
    public DiaryDatabaseDO getByTime(String time) {
        if (mSqLiteDatabase == null) {
            return null;
        }
        Cursor cursor = null;
        try {
            cursor = mSqLiteDatabase.query(mDataTab, new String[]{ID, TITLE, CONTENT, LOCATION, TIME, MUSIC, EXCHANGE},
                    TIME + " = ?", new String[]{time}, null, null, null, " 1");
            if (cursor != null && cursor.moveToNext()) {
                DiaryDatabaseDO result = new DiaryDatabaseDO();
                result.did = cursor.getInt(cursor.getColumnIndex(ID));
                result.tittle = cursor.getString(cursor.getColumnIndex(TITLE));
                result.content = cursor.getString(cursor.getColumnIndex(CONTENT));
                result.time = cursor.getString(cursor.getColumnIndex(TIME));
                result.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                result.music = cursor.getString(cursor.getColumnIndex(MUSIC));
                result.exchange = cursor.getInt(cursor.getColumnIndex(EXCHANGE)) == 1;
                return result;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    /**
     * 按页码获取数据
     *
     * @param p ; 页码 0开始
     * @return
     */
    public List<DiaryDatabaseDO> getByPage(int p) {
        // TODO: 2019/4/11 解决数据混乱，例如刚好插入了一条数据之后，在分页会有问题
        if (mSqLiteDatabase == null) {
            return null;
        }
        Cursor cursor = null;
        int size = 10; //每一页10条
        int start = p * size;
        try {
            cursor = mSqLiteDatabase.query(mDataTab, new String[]{ID, TITLE, CONTENT, LOCATION, TIME, MUSIC, EXCHANGE},
                    null, null, null, null, TIME + " desc", start + "," + size);
            List<DiaryDatabaseDO> list = new ArrayList<>();
            if (cursor == null) {
                return null;
            }
            while (cursor.moveToNext()) {
                DiaryDatabaseDO item = new DiaryDatabaseDO();
                item.did = cursor.getInt(cursor.getColumnIndex(ID));
                item.tittle = cursor.getString(cursor.getColumnIndex(TITLE));
                item.content = cursor.getString(cursor.getColumnIndex(CONTENT));
                item.time = cursor.getString(cursor.getColumnIndex(TIME));
                item.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                item.music = cursor.getString(cursor.getColumnIndex(MUSIC));
                item.exchange = cursor.getInt(cursor.getColumnIndex(EXCHANGE)) == 1;
                list.add(item);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     * 是否还有下一页数据
     *
     * @param p ; 页码 0开始
     * @return
     */
    public boolean hasNext(int p) {
        // TODO: 2019/4/11 解决数据混乱，例如刚好插入了一条数据之后，在分页会有问题
        if (mSqLiteDatabase == null) {
            return false;
        }
        Cursor cursor = null;
        int size = 10; //每一页10条
        int start = (p + 1) * size; //下一页
        try {
            cursor = mSqLiteDatabase.query(mDataTab, new String[]{ID}, null,
                    null, null, null, TIME + " desc", start + "," + size);
            List<DiaryDatabaseDO> list = new ArrayList<>();
            if (cursor == null) {
                return false;
            }
            return cursor.moveToNext();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    /**
     * 删除数据
     *
     * @param id
     * @return
     */
    public boolean delete(int id) {
        if (mSqLiteDatabase == null) {
            return false;
        }
        return mSqLiteDatabase.delete(mDataTab, ID + " = ?", new String[]{id + ""}) > 0;
    }

    /**
     * 清空数据
     */
    public void clear() {
        if (mSqLiteDatabase == null) {
            return;
        }
        mSqLiteDatabase.execSQL(CLEAR_TAB);
    }


    private class DiarySQLHelper extends SQLiteOpenHelper {

        public DiarySQLHelper(@Nullable Context context) {
            super(context, mDatabaseName, null, mDatabaseVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TAB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(CLEAR_TAB);
            sqLiteDatabase.execSQL(CREATE_TAB);
        }
    }

    public static class DiaryDatabaseDO {
        private int did;
        private String tittle;
        private String content;
        private String time;
        private String location;
        private String music;
        private boolean exchange;

        public DiaryDatabaseDO() {
        }

        public int getDid() {
            return did;
        }

        public void setDid(int did) {
            this.did = did;
        }

        public String getTittle() {
            return tittle;
        }

        public void setTittle(String tittle) {
            this.tittle = tittle;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getMusic() {
            return music;
        }

        public void setMusic(String music) {
            this.music = music;
        }

        public boolean isExchange() {
            return exchange;
        }

        public void setExchange(boolean exchange) {
            this.exchange = exchange;
        }
    }
}
