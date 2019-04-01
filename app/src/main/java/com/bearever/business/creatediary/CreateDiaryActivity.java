package com.bearever.business.creatediary;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bearever.articlememento.memento.ArticleMemento;
import com.bearever.articlememento.memento.ArticleMementoUtil;
import com.bearever.articlememento.model.Section;
import com.bearever.articlememento.richtext.OnRichTextChangeListener;
import com.bearever.articlememento.richtext.RichTextEditor;
import com.bearever.baselib.mvp.BaseActivity;
import com.bearever.diary.R;

import java.util.List;

/**
 * 创建日记
 * luoming
 * 2019/3/30
 **/
public class CreateDiaryActivity extends BaseActivity<CreateDiaryPresenter> implements CreateDiaryContact.View {
    private static final String TAG = "CreateDiaryActivity";
    private RichTextEditor mRichTextEditor; //富文本编辑器
    private ArticleMementoUtil mArticleMementoUtil = new ArticleMementoUtil();

    public static void start(Activity context, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, CreateDiaryActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_create_diary;
    }

    @Override
    protected void initView() {
        mRichTextEditor = findViewById(R.id.rich_text_editor);
        mArticleMementoUtil.setSectionList(mRichTextEditor.getContentSectionList());
    }

    @Override
    protected void initPresenter() {
        mPresenter = new CreateDiaryPresenter(this, this);
    }

    @Override
    protected void initListener() {
        findViewById(R.id.ll_undo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleMemento memento = mArticleMementoUtil.undo();
                if (memento != null) {
                    Log.i(TAG, "onClick: undo:" + memento.toString());
                    mRichTextEditor.setContentByArticleMemento(memento);
                }
            }
        });

        findViewById(R.id.ll_redo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArticleMemento memento = mArticleMementoUtil.redo();
                if (memento != null) {
                    Log.i(TAG, "onClick: redo:" + memento.toString());
                    mRichTextEditor.setContentByArticleMemento(memento);
                }
            }
        });


        findViewById(R.id.ll_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mRichTextEditor.setOnRichTextChangeListener(new OnRichTextChangeListener() {
            @Override
            public void onChanged(List<Section> sectionList) {
                mArticleMementoUtil.setSectionList(sectionList);
            }
        });
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    public void postSucceed() {

    }

    @Override
    public void postFailed(String msg) {

    }
}
