package com.bearever.contactslib.search;

import android.content.Context;
import android.provider.ContactsContract;

import com.bearever.contactslib.callback.ContactsCallback;

/**
 * 联系人搜索的基类
 * Created by luoming on 2018/3/27.
 */

public abstract class BaseContactsSearch {
    protected ContactsCallback callback; //联系人获取监听回调
    protected OnContactsSearchFinishListener finishListener;
    protected Context context;

    /**
     * 获取库Phone表字段
     **/
    protected static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.SORT_KEY_PRIMARY
    };

    /**
     * 联系人显示名称
     **/
    protected static final int PHONES_DISPLAY_NAME = 0;

    /**
     * 电话号码
     **/
    protected static final int PHONES_NUMBER = 1;

    /**
     * 头像ID
     **/
    protected static final int PHONES_PHOTO_ID = 2;

    /**
     * 联系人的ID
     **/
    protected static final int PHONES_CONTACT_ID = 3;

    /**
     * 名字的拼写
     **/
    protected static final int PHONE_SORT_KEY = 4;

    public BaseContactsSearch(Context context, ContactsCallback callback, OnContactsSearchFinishListener finishListener) {
        this.callback = callback;
        this.context = context;
        this.finishListener = finishListener;
    }

    public abstract void search();

    public void destroy() {
        this.context = null;
        this.callback = null;
        this.finishListener = null;
    }

    public interface OnContactsSearchFinishListener {
        void onFinish();

        void onError();
    }
}
