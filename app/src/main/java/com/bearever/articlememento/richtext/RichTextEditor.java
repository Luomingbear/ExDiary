package com.bearever.articlememento.richtext;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bearever.articlememento.decorator.SectionDecorator;
import com.bearever.articlememento.factory.SectionDecoratorFactory;
import com.bearever.articlememento.memento.ArticleMemento;
import com.bearever.articlememento.model.Section;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 富文本编辑器
 * Created by luoming on 2018/3/29.
 */

public class RichTextEditor extends ScrollView implements RichTextEditorInterface, SectionDecorator.OnSectionChangedListener {
    private static final String TAG = "RichTextEditor";
    private List<SectionDecorator> mSectionDecoratorList = new ArrayList<>(); //段落控件列表
    private LinearLayout mRootLayout; //段落布局的位置
    private SectionDecoratorFactory mSectionDecoratorFactory; //用于生产段落布局的工厂
    private OnRichTextChangeListener mListener; //富文本变化监听
    private int mIndex = 1; //段落index标识
    private int mFocusIndex = 0; //当前焦点的段落index
    private boolean isSetRichText = false; //是否正在填充富文本内容；适用于外部使用section列表或者section的json字符串设置内容的时候
    private String CLASS_TEXT_SECTION_DECORATOR = "com.bearever.articlememento.decorator.TextSectionDecorator";
    private String CLASS_IMAGE_SECTION_DECORATOR = "com.bearever.articlememento.decorator.ImageSectionDecorator";

    public RichTextEditor(Context context) {
        super(context);
        init();
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initRootLayout();
        mSectionDecoratorFactory = new SectionDecoratorFactory(getContext());
        //默认添加一个文本输入框
        addTextSection();
//        Section section = new Section();
//        section.setIndex(mIndex);
//        section.setDecorator(CLASS_TEXT_SECTION_DECORATOR);
//        mSectionDecoratorList.add(section);
    }

    /**
     * 初始化root布局
     */
    private void initRootLayout() {
        mRootLayout = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mRootLayout.setLayoutParams(params);
        mRootLayout.setOrientation(LinearLayout.VERTICAL);
        addView(mRootLayout);
    }

    /**
     * 设置富文本变化监听器
     *
     * @param listener
     */
    public void setOnRichTextChangeListener(OnRichTextChangeListener listener) {
        this.mListener = listener;
    }

    @Override
    public void setContentByArticleMemento(ArticleMemento memento) {
        if (memento == null || memento.getSectionList().size() == 0) {
            return;
        }

        setContentBySectionList(memento.getSectionList());
    }

    /**
     * 设置内容的section列表，根据列表的内容显示富文本
     * 将@param psections和mSectionDecoratorList进行比对，遍历sectinos的每一项，如果类型不一致就重新添加控件，
     * 否则只需要更新一下控件内容就可以了
     *
     * @param sections
     */
    @Override
    public void setContentBySectionList(List<Section> sections) {
        if (sections == null) {
            throw new NullPointerException("sections 不可以为null");
        }
        isSetRichText = true;

        List<Section> copyList = getContentSectionList();

        //将老数据和新数据之间相同数量的控件更新掉
        int minSize = Math.min(copyList.size(), sections.size());
        for (int i = 0; i < minSize; i++) {
            Section section = copyList.get(i); //得到之前这个位置的控件信息
            Section sectionFrom = sections.get(i); //新的控件信息
            if (section.getDecorator().equals(sectionFrom.getDecorator())) {
                //类型一致，更新下数据就可以了
                if (section != sectionFrom)
                    mSectionDecoratorList.get(i).setSection(sectionFrom);
            } else {
                //类型不一致，需要切换控件
                mRootLayout.removeViewAt(i);
                mSectionDecoratorList.remove(i);
                addSection(sectionFrom, i);
            }
            mIndex = sectionFrom.getIndex();
        }

        //更新新老数据之间的差值
        int difcount = sections.size() - copyList.size();
        if (difcount > 0) { //说明新的数据长度大于老的数据，需要将差的数据补上
            for (int i = sections.size() - difcount; i < sections.size(); i++) {
                Section sectionFrom = sections.get(i);
                addSection(sectionFrom, i);
                mIndex = sectionFrom.getIndex();
            }
        } else { //说明新的数据长度小于老的数据 ，需要将多的控件移除
            mRootLayout.removeViews(copyList.size() + difcount, -difcount);
            mSectionDecoratorList.subList(copyList.size() + difcount, copyList.size()).clear();
        }

        isSetRichText = false;
        //滚动到底部
        fullScroll(ScrollView.FOCUS_DOWN);

        //最后的一个控件如果是文本输入框，就需要获取焦点
    }

    @Override
    public List<Section> getContentSectionList() {
        List<Section> list = new ArrayList<>();
        for (SectionDecorator sectionDecorator : mSectionDecoratorList) {
            list.add(sectionDecorator.getSection());
        }
        return list;
    }

    @Override
    public void setContentByString(String content) {
        try {
            JSONArray jsonArray = new JSONArray(content);
            List<Section> list = new ArrayList<>();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Section section = (Section) jsonArray.get(i);
                    list.add(section);
                }
            }
            setContentBySectionList(list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getContentString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < mSectionDecoratorList.size(); i++) {
            sb.append(mSectionDecoratorList.get(i).getSection().toJson());
            if (i != mSectionDecoratorList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public void addTextSection() {
        Section section = new Section();
        section.setDecorator(CLASS_TEXT_SECTION_DECORATOR);
        addSection(section);
    }

    @Override
    public void addImageSection(String url) {
        Section section = new Section();
        section.setDecorator(CLASS_IMAGE_SECTION_DECORATOR);
        section.setContent(url);
        addSection(section);
        //在图片底部自动加入文本输入框
        addTextSection();
    }

    @Override
    public void addSection(Section section) {
        addSection(section, -1);
    }

    /**
     * 添加控件，需要注意添加之前需要判断下上一个控件（不是第一个控件）是不是文本，如果是文本的话要检查是否为空
     * 如果为空则需要将上一个控件移除掉
     *
     * @param section
     * @param position
     */
    @Override
    public void addSection(Section section, int position) {
        //如果没有index，则更新index为最新
        if (section.getIndex() == 0) {
            mIndex++;
            section.setIndex(mIndex);
        }
        //
        SectionDecorator sectionDecorator =
                mSectionDecoratorFactory.createSectionDecorator(section);
        sectionDecorator.setOnSectionChangedListener(this);
        //如果上一个文本输入框（不是第一个输入框）为空，则将该输入框移除
        if (mSectionDecoratorList.size() > 1) {
            SectionDecorator last = mSectionDecoratorList.get(mSectionDecoratorList.size() - 1);
            if (last.getSection().getDecorator().equals(CLASS_TEXT_SECTION_DECORATOR)
                    && TextUtils.isEmpty(last.getSection().getContent())) {
                mRootLayout.removeViewAt(mSectionDecoratorList.size() - 1);
                mSectionDecoratorList.remove(last);
            }
        }
        //添加新的控件
        if (position != -1)
            mRootLayout.addView(sectionDecorator.display(), position);
        else
            mRootLayout.addView(sectionDecorator.display());
        mSectionDecoratorList.add(sectionDecorator);
        mRootLayout.getChildAt(Math.max(0, mRootLayout.getChildCount() - 1)).requestFocus();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        /**
         * 手指按下回车键的时候需要构造一个新的输入框
         */
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
            addTextSection();
            return true;
        }

        /**
         * 手指按下删除键的时候，
         *
         */
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
            checkDelete();
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    /**
     * 如果当前焦点是文本输入，则需要判断该文本框的内容是否为空，如果为空还在点击删除就可以将该控件删除
     * 如果当前焦点是图片，则直接删除
     * ++++++最后面必须保留一个文本输入框++++++
     */
    private void checkDelete() {
        for (int i = 1; i < mSectionDecoratorList.size(); i++) {
            Section section = mSectionDecoratorList.get(i).getSection();
            if (section.getIndex() == mFocusIndex) {
                if (TextUtils.isEmpty(section.getContent())) {
                    // 如果最后的控件是文本，当文本内容为空，仍然在点击删除按钮的话，需要判断一下文本框的上一个是不是图片，
                    // 如果是图片，则删除图片控件保留最后的文本框
                    // 如果是文本的话就可以直接删除当前的文本框
                    if (i == mSectionDecoratorList.size() - 1
                            && section.getDecorator().equals(CLASS_TEXT_SECTION_DECORATOR)) {
                        if (mSectionDecoratorList.get(i - 1).getSection().getDecorator().equals(CLASS_IMAGE_SECTION_DECORATOR)) {
                            mRootLayout.removeViewAt(i - 1);
                            mSectionDecoratorList.remove(i - 1);
                            if (mListener != null) {
                                mListener.onChanged(getContentSectionList());
                            }
                            return;
                        }
                    }
                    mRootLayout.removeViewAt(i);
                    mSectionDecoratorList.remove(i);
                    mRootLayout.getChildAt(mRootLayout.getChildCount() - 1).requestFocus();
                    if (mListener != null) {
                        mListener.onChanged(getContentSectionList());
                    }
                }
                return;
            }
        }
    }


    /**
     * 段落内容更新
     * 如果变化的是图片，并且content为空，则表示点击了删除按钮，需要将该图片控件移除
     *
     * @param section
     */
    @Override
    public void onChanged(Section section) {
        if (section.getDecorator().equals(CLASS_IMAGE_SECTION_DECORATOR)
                && TextUtils.isEmpty(section.getContent())) {
            for (int i = 0; i < mSectionDecoratorList.size(); i++) {
                Section sec = mSectionDecoratorList.get(i).getSection();
                if (sec.getIndex() == section.getIndex()) {
                    //删除图片控件
                    mRootLayout.removeViewAt(i);
                    mSectionDecoratorList.remove(i);
                    break;
                }
            }
        } else {
            mFocusIndex = section.getIndex();
        }

        //
        if (mListener != null && !isSetRichText)
            mListener.onChanged(getContentSectionList());
        Log.d(TAG, "onChanged: mSectionDecoratorList:" + getContentString());
    }
}
