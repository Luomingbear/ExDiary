package com.bearever.baselib.contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.bearever.baselib.contacts.callback.ContactsCallback;
import com.bearever.baselib.contacts.data.ContactInfo;
import com.bearever.baselib.contacts.search.BaseContactsSearch;
import com.bearever.baselib.contacts.search.PhoneContactsSearch;
import com.bearever.baselib.contacts.search.SimContactsSearch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 联系人信息工具类
 * Created by luoming on 2018/3/27.
 */

public class ContactsHelper {
    private static ContactsHelper instance;
    private ThreadPoolExecutor mPoolExecutor; // 线程池
    private List<BaseContactsSearch> mContactsSearchList; //搜索的执行方式列表

    public static ContactsHelper getInstance() {
        if (instance == null) {
            synchronized (ContactsHelper.class) {
                if (instance == null)
                    instance = new ContactsHelper();
            }
        }
        return instance;
    }

    private ContactsHelper() {
        mPoolExecutor = new ThreadPoolExecutor(1, 5, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        mContactsSearchList = new ArrayList<>();
    }

    private int icout = 0; //扫描完成的数量

    /**
     * 获取联系人列表
     *
     * @param contactsCallback
     */
    public void getContacts(Context context, final ContactsCallback contactsCallback) {
        //清理之前的记录
        for (BaseContactsSearch search : mContactsSearchList) {
            search.destroy();
        }
        mContactsSearchList.clear();
        //添加新的搜索类
        icout = 0;
        BaseContactsSearch.OnContactsSearchFinishListener finishListener =
                new BaseContactsSearch.OnContactsSearchFinishListener() {
                    @Override
                    public void onError() {
                        if (contactsCallback != null) {
                            contactsCallback.onError();
                        }
                    }

                    @Override
                    public void onFinish() {
                        icout++;
                        if (icout == mContactsSearchList.size() && contactsCallback != null)
                            contactsCallback.onFinish();
                    }
                };
        BaseContactsSearch phoneContactsSearch =
                new PhoneContactsSearch(context, contactsCallback, finishListener);
        mContactsSearchList.add(phoneContactsSearch);
        BaseContactsSearch simContactsSearch =
                new SimContactsSearch(context, contactsCallback, finishListener);
        mContactsSearchList.add(simContactsSearch);

        //使用单线程执行搜索
        for (int i = 0; i < mContactsSearchList.size(); i++) {
            final int ii = i;
            mPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mContactsSearchList.get(ii).search();
                }
            });
        }
    }

    private static final int REQUEST_CONTACT = 0x22;

    /**
     * 从系统通讯录里面选择联系人
     *
     * @param activity
     */
    public void selectContactFromContactBook(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        activity.startActivityForResult(intent, REQUEST_CONTACT);
    }

    /**
     * 选择联系人返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * @param callback
     */
    public void onSelectContactFromContactBookResult(Context context, int requestCode, int resultCode, Intent data,
                                                     ContactsCallback callback) {
        if (callback == null || requestCode != REQUEST_CONTACT || resultCode != Activity.RESULT_OK) {
            return;
        }
        try {
            ContentResolver reContentResolverol = context.getContentResolver();
            Uri contactData = data.getData();
            String[] projections = {ContactsContract.Contacts._ID, ContactsContract.Contacts.HAS_PHONE_NUMBER};
            Cursor cursor = reContentResolverol.query(contactData, projections, null, null, null);
            if (cursor == null) {
                return;
            }
            cursor.moveToFirst();
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = reContentResolverol.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                    null,
                    null);
            if (phone == null) {
                return;
            }
            while (phone.moveToNext()) {
                ContactInfo info = new ContactInfo();
                info.setContactsName(phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                info.setContactsPhone(phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).trim());
                callback.onGet(info);
            }
            cursor.close();
            phone.close();
        } catch (Exception e) {
            callback.onError();
        } finally {
            callback.onFinish();
        }
    }
}
