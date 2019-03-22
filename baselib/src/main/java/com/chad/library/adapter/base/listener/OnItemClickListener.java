package com.chad.library.adapter.base.listener;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

/**
 * Created by AllenCoder on 2016/8/03.
 * <p>
 * <p>
 * A convenience class to extend when you only want to OnItemClickListener for a subset
 * of all the SimpleClickListener. This implements all methods in the
 * {@link SimpleClickListener}
 */
public abstract class OnItemClickListener extends SimpleClickListener {
    @Override
    public void onItemClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {
        onSimpleItemClick(adapter, view, position);
    }

    @Override
    public void onItemLongClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildLongClick(com.chad.library.adapter.base.BaseQuickAdapter adapter, View view, int position) {

    }

    public abstract void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position);
}
