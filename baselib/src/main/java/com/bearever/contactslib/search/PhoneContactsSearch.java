package com.bearever.contactslib.search;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.bearever.contactslib.callback.ContactsCallback;
import com.bearever.contactslib.data.ContactInfo;

import java.io.InputStream;

/**
 * 手机联系人获取
 * Created by luoming on 2018/3/27.
 */

public class PhoneContactsSearch extends BaseContactsSearch {
    public PhoneContactsSearch(Context context, ContactsCallback callback, OnContactsSearchFinishListener finishListener) {
        super(context, callback, finishListener);
    }

    @Override
    public void search() {
        if (context == null) {
            return;
        }
        ContentResolver resolver = context.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, "sort_key");

        try {
            // 不为空
            if (phoneCursor != null) {
                while (phoneCursor.moveToNext()) {

                    // 得到手机号码
                    String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                    // 当手机号码为空的或者为空字段 跳过当前循环
                    if (TextUtils.isEmpty(phoneNumber))
                        continue;

                    // 得到联系人名称
                    String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

                    //名字的拼写
                    String sortKey = phoneCursor.getString(PHONE_SORT_KEY);

                    // 得到联系人ID
                    Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID);

                    // 得到联系人头像ID
                    Long imgid = phoneCursor.getLong(PHONES_PHOTO_ID);

                    // 得到联系人头像Bitamp
                    Bitmap bitmap = null;

                    // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                    if (imgid > 0) {
                        Uri uri = ContentUris.withAppendedId(
                                ContactsContract.Contacts.CONTENT_URI, contactid);
                        InputStream input = ContactsContract.Contacts
                                .openContactPhotoInputStream(resolver, uri);
                        bitmap = BitmapFactory.decodeStream(input);
                    } else {
                        // 设置默认
                        bitmap = BitmapFactory.decodeResource(context.getResources(), 0);
                    }

                    //回调
                    ContactInfo info = new ContactInfo();
                    info.setContactsName(contactName.trim());
                    info.setSortKey(sortKey);
                    info.setContactsPhone(phoneNumber);
                    info.setAvatar(bitmap);
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