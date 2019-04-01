package com.bearever.articlememento.richtext;

import com.bearever.articlememento.model.Section;

import java.util.List;

/**
 * 当富文本变化时，回调
 * Created by luoming on 2018/3/30.
 */

public interface OnRichTextChangeListener {
    void onChanged(List<Section> sectionList);
}
