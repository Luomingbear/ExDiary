package com.bearever.contactslib.search;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.bearever.contactslib.callback.ContactsCallback;
import com.bearever.contactslib.data.ContactInfo;

/**
 * sim卡联系人获取
 * Created by luoming on 2018/3/27.
 */

public class SimContactsSearch extends BaseContactsSearch {
    public SimContactsSearch(Context context, ContactsCallback callback, OnContactsSearchFinishListener finishListener) {
        super(context, callback, finishListener);
    }

    @Override
    public void search() {
        if (context == null) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, "sort_key");

        try {
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                    // 得到名字的拼写
                    String sortKey = phoneCursor.getString(PHONE_SORT_KEY);

                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;
                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME).trim();

                    // Sim卡中没有联系人头像
                    ContactInfo info = new ContactInfo();
                    info.setContactsPhone(phoneNumber);
                    info.setContactsName(contactName);
                    info.setSortKey(sortKey);
                    if (callback != null)
                        callback.onGet(info);
                }
                phoneCursor.close();
            }
        } catch (Exception e) {
            if (finishListener != null)
                finishListener.onError();
        } finally {
            if (finishListener != null)
                finishListener.onFinish();
            destroy();
        }
    }
}
