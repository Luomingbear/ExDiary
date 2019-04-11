package com.bearever.business.creatediary;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.bearever.baselib.permission.PermissionManager;
import com.bearever.baselib.permission.info.PermissionGrantInfo;
import com.bearever.diary.R;
import com.bearever.articlememento.memento.ArticleMemento;
import com.bearever.articlememento.memento.ArticleMementoUtil;
import com.bearever.articlememento.model.Section;
import com.bearever.articlememento.richtext.OnRichTextChangeListener;
import com.bearever.articlememento.richtext.RichTextEditor;
import com.bearever.baselib.mvp.BaseActivity;
import com.bearever.diarybase.constant.DyConstants;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

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

    private static final int REQUEST_CODE_CHOOSE = 101;

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
                checkPermissionAndSelectPic();
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

    private void checkPermissionAndSelectPic() {
        if (PermissionManager.isApplyPermission(this, Manifest.permission_group.STORAGE)) {
            selectPic();
        } else {
            PermissionManager.applyPermission(this, 2, new PermissionManager.OnPermissionListener() {
                @Override
                public void onFinish(List<PermissionGrantInfo> grantList, boolean isAllAllow) {
                    if (isAllAllow) {
                        selectPic();
                    }
                }
            }, true, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void selectPic() {
        Matisse.from(this)
                .choose(MimeType.ofAll(), false)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, DyConstants.MATISSE_FILE_PROVIDER))
                .countable(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CHOOSE) {
            afterSelectPic(data);
        }
    }

    private void afterSelectPic(Intent data) {
        List<String> picList = Matisse.obtainPathResult(data);
        if (picList != null && !picList.isEmpty()) {
            mRichTextEditor.addImageSection(picList.get(0));
        }
    }


    @Override
    public void postSucceed() {
        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
//        PictureFileUtils.deleteCacheDirFile(this);
    }


    @Override
    public void postFailed(String msg) {
        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
//        PictureFileUtils.deleteCacheDirFile(this);

    }


}
