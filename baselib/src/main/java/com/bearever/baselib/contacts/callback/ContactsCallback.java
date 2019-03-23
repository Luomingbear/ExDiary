package com.bearever.baselib.contacts.callback;


import com.bearever.baselib.contacts.data.ContactInfo;

/**
 * 联系人的信息获取监听接口
 * Created by luoming on 2018/3/27.
 */

public interface ContactsCallback {

    void onGet(ContactInfo contactInfo);

    void onError();

    void onFinish();

}
