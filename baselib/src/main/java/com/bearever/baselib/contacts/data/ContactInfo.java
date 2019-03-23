package com.bearever.baselib.contacts.data;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by luoming on 2018/8/17.
 */

public class ContactInfo implements Serializable {
    private String contactsPhone = ""; //手机号码
    private String contactsName = ""; // 显示的名称
    private String sortKey = ""; //名字的拼写
    private Bitmap avatar; //头像

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public ContactInfo() {
    }

    public String getContactsPhone() {
        return contactsPhone;
    }

    public void setContactsPhone(String contactsPhone) {
        this.contactsPhone = contactsPhone;
    }

    public String getContactsName() {
        return contactsName;
    }

    public void setContactsName(String contactsName) {
        this.contactsName = contactsName;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "name:" + getContactsName() + "--phone:" + getContactsPhone() + "--avatar:" + (avatar == null ? "null" : "有" + "isSelected" + isSelected);
    }
}