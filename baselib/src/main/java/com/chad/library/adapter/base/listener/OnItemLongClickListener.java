package com.chad.library.adapter.base.listener;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * create by: allen on 16/8/3.
 */

public abstract class OnItemLongClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemLongClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {
        onSimpleItemLongClick(adapter, view, position);
    }

    @Override
    public void onItemChildClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {
    }

    public abstract void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position);
}
