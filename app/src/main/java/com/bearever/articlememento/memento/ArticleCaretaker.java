package com.bearever.articlememento.memento;

import java.util.ArrayList;
import java.util.List;

/**
 * 文章备忘录的负责人
 * Created by luoming on 2018/3/22.
 */

public class ArticleCaretaker {
    private List<ArticleMemento> mMementoList; //备忘录列表
    private int mMaxLength = 20; //记录的步数 ，限制记录的次数有助于减少内存使用
    private int index = 0; //记录当前的步数

    public ArticleCaretaker() {
        mMementoList = new ArrayList<>();
    }

    /**
     * 记录一次备忘
     *
     * @param memento
     */
    public void saveMemento(ArticleMemento memento) {
        //将index后面的删除掉
        List<ArticleMemento> removes = new ArrayList<>();
        for (int i = index + 1; i < mMementoList.size(); i++) {
            removes.add(mMementoList.get(i));
        }
        mMementoList.removeAll(removes);
        if (memento != null) {
            mMementoList.add(memento);
            //长度超过了最大值就需要将第一个备忘删除掉
            if (mMementoList.size() > mMaxLength) {
                mMementoList.remove(0);
            }
        }

        index = Math.max(0, mMementoList.size() - 1);
    }

    /**
     * 获取第几步的备忘
     *
     * @param i
     * @return
     */
    public ArticleMemento getMemento(int i) {
        if (i > 1 && i < mMementoList.size() - 1) {
            index = i;
            return mMementoList.get(i);
        } else return null;
    }

    /**
     * 获取上一个步骤的备忘
     *
     * @return
     */
    public ArticleMemento undo() {
        index = Math.max(0, index - 1);
        if (mMementoList.size() > 1) {
            return mMementoList.get(index);
        } else return null;
    }

    /**
     * 恢复一个步骤
     *
     * @return
     */
    public ArticleMemento redo() {
        //没有记录返回空
        if (mMementoList.size() == 0) {
            return null;
        }

        index = Math.min(mMementoList.size() - 1, index + 1);
        return mMementoList.get(index);
    }

    /**
     * 备忘录的步数
     *
     * @return
     */
    public int length() {
        return mMementoList.size();
    }
}
