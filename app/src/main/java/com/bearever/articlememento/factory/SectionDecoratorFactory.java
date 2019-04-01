package com.bearever.articlememento.factory;

import android.content.Context;
import android.text.TextUtils;

import com.bearever.articlememento.decorator.SectionDecorator;
import com.bearever.articlememento.model.Section;

/**
 * 段落生成工厂
 * Created by luoming on 2018/3/29.
 */

public class SectionDecoratorFactory {
    private Context context;

    public SectionDecoratorFactory(Context context) {
        this.context = context;
    }

    /**
     * 通过反射生产一个段落
     *
     * @return SectionDecorator
     */
    public SectionDecorator createSectionDecorator(Section section) {
        if (section == null)
            return null;

        if (TextUtils.isEmpty(section.getDecorator()))
            return null;

        try {
            Class clz = Class.forName(section.getDecorator());
            SectionDecorator sectionDecorator = (SectionDecorator) clz.getConstructor(
                    Context.class, Section.class).newInstance(context, section);
            return sectionDecorator;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
