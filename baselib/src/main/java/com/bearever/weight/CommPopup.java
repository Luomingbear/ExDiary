package com.bearever.weight;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bearever.baselib.R;
import com.bearever.listener.CommPopupListener;
import com.bearever.util.DisplayUtil;

/**
 * 公用的弹窗
 * created by JiangHua on 2019/3/3
 */
public class CommPopup extends PopupWindow {

    private static final int PARENT_WIDTH = 300;//默认宽度dp

    private int rlParentHeight;
    private int parentBgResId;
    private String txtWord;
    private Context context;
    private String tvCancelWord;
    private String tvSureWord;
    private int mTvCancelColor;
    private int mTvTitleColor;
    private int mTvSureWordColor;
    private int cancelVisibility;
    private int sureVisibility;
    private int editVisibility;
    private CommPopupListener commPopupListener;
    private View popupView;
    private String title;
    private int gravity;

    //是否显示的是html
    private boolean isHtml;

    @Override
    public void dismiss() {
        //清空text
        EditText editText = popupView.findViewById(R.id.et_input);
        editText.setText("");
        super.dismiss();
    }

    public static class Builder {
        private final String txtWord;
        private final Context context;
        private int rlParentHeight = 150;
        private int parentBgResId = 0;
        private int cancelVisibility = View.VISIBLE;
        private int sureVisibility = View.VISIBLE;
        private int editVisibility = View.GONE;
        private String tvCancelWord;
        private String tvSureWord;
        private int mTvCancelColor;
        private int mTvTitleColor;
        private int mTvSureWordColor;
        private String title;
        private int gravity = Gravity.CENTER;
        private CommPopupListener commPopupListener;
        private boolean isHtml = false;//是否显示的是html

        /**
         * @param txtWord 内容文字
         * @param context 上下文
         */
        public Builder(String txtWord, Context context) {
            this.txtWord = txtWord;
            this.context = context;
        }

        /**
         * 设置监听
         *
         * @param commPopupListener 监听
         */
        public Builder setCommPopupListener(CommPopupListener commPopupListener) {
            this.commPopupListener = commPopupListener;
            return this;
        }

        /**
         * 是否有编辑框
         *
         * @param visibility 隐藏显示
         */
        public Builder setEditVisility(int visibility) {
            this.editVisibility = visibility;
            return this;
        }

        /**
         * @param rlParentHeight 高度
         * @return 设置父控件的高度
         */
        public Builder setParentHeight(int rlParentHeight) {
            this.rlParentHeight = rlParentHeight;
            return this;
        }

        /**
         * 设置弹窗背景
         * @param bgResId 资源文件id
         */
        public Builder setParentBgResId(int bgResId) {
            this.parentBgResId = bgResId;
            return this;
        }

        /**
         * 取消按钮隐藏状态
         * @param visibility 状态
         */
        public Builder setCancelVisibility(int visibility) {
            this.cancelVisibility = visibility;
            return this;
        }

        /**
         * 确定按钮隐藏状态
         * @param visibility 状态
         */
        public Builder setSureVisibility(int visibility) {
            this.sureVisibility = visibility;
            return this;
        }

        /**
         * @param cancelWord 取消按钮文字
         * @return 设置取消按钮是否显示、设置文字、按键的监听
         */
        public Builder setCancelWord(String cancelWord) {
            this.tvCancelWord = cancelWord;
            return this;
        }

        /**
         * 设置内容的位置
         *
         * @param gravity 位置
         */
        public Builder setContentGravaty(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * @param sureWord 确定按钮文字
         * @return 设置确定按钮是否显示、设置文字、按键的监听
         */
        public Builder setSureAndWord(String sureWord) {
            this.tvSureWord = sureWord;
            return this;
        }

        /**
         * 确定按钮颜色
         * @param color 颜色值
         */
        public Builder setTitleTextColor(int color) {
            this.mTvTitleColor = color;
            return this;
        }

        /**
         * 确定按钮颜色
         * @param color 颜色值
         */
        public Builder setSureTextColor(int color) {
            this.mTvSureWordColor = color;
            return this;
        }

        /**
         * 取消按钮颜色
         * @param color 颜色值
         */
        public Builder setCancelTextColor(int color) {
            this.mTvCancelColor = color;
            return this;
        }

        /**
         * w文本内容是否为html
         * @param isHtml 是否是html
         */
        public Builder isHtml(boolean isHtml) {
            this.isHtml = isHtml;
            return this;
        }

        public CommPopup build() {
            return new CommPopup(this);
        }
    }

    public CommPopup(Builder builder) {
        this.txtWord = builder.txtWord;
        this.context = builder.context;
        this.rlParentHeight = builder.rlParentHeight;
        this.parentBgResId = builder.parentBgResId;
        this.tvCancelWord = builder.tvCancelWord;
        this.mTvCancelColor = builder.mTvCancelColor;
        this.mTvSureWordColor = builder.mTvSureWordColor;
        this.mTvTitleColor = builder.mTvTitleColor;
        this.tvSureWord = builder.tvSureWord;
        this.title = builder.title;
        this.editVisibility = builder.editVisibility;
        this.cancelVisibility = builder.cancelVisibility;
        this.sureVisibility = builder.sureVisibility;
        this.commPopupListener = builder.commPopupListener;
        this.isHtml = builder.isHtml;
        this.gravity = builder.gravity;
    }

    public void show() {
        initView();
        this.setContentView(popupView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setAnimationStyle(R.style.contextMenuAnim);
        showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
    }

    private void initView() {
        if (popupView == null) {
            popupView = LayoutInflater.from(context).inflate(R.layout.view_common, null);

            //设置宽高
            RelativeLayout parentLayout = popupView.findViewById(R.id.rl_common_parent);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DisplayUtil.dp2px(PARENT_WIDTH), rlParentHeight);
            parentLayout.setLayoutParams(params);
            if(parentBgResId != 0){
                parentLayout.setBackgroundResource(parentBgResId);
            }

            //标题
            TextView tvTitle = popupView.findViewById(R.id.tv_title);

            if (mTvTitleColor != 0 && mTvTitleColor != -1) {
                tvTitle.setTextColor(context.getResources().getColor(mTvTitleColor));
            }

            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            } else {
                tvTitle.setVisibility(View.GONE);
            }

            //内容
            TextView topWord = popupView.findViewById(R.id.tv_content);
            if (gravity != Gravity.CENTER) {
                topWord.setGravity(gravity | Gravity.CENTER_VERTICAL);
            }
            if (!isHtml) {
                topWord.setText(txtWord);
            } else {
                topWord.setText(Html.fromHtml(txtWord));
            }

            final EditText editText = popupView.findViewById(R.id.et_input);
            editText.setVisibility(editVisibility);

            //确定按钮
            TextView tvEnter = popupView.findViewById(R.id.txt_ok);
            tvEnter.setVisibility(sureVisibility == View.VISIBLE ? View.VISIBLE : View.INVISIBLE);
            if (!TextUtils.isEmpty(tvSureWord)) {
                tvEnter.setText(tvSureWord);
            }
            if (mTvSureWordColor != 0 && mTvSureWordColor != -1) {
                tvEnter.setTextColor(context.getResources().getColor(mTvSureWordColor));
            }

            //上下分割线
            View cutLine = popupView.findViewById(R.id.view_pop_line);
            cutLine.setVisibility((cancelVisibility == sureVisibility) ? View.VISIBLE : View.INVISIBLE);

            //按钮分割线
            View line = popupView.findViewById(R.id.bottom_line);
            line.setVisibility((cancelVisibility == sureVisibility) ? View.VISIBLE : View.INVISIBLE);

            //取消按钮
            TextView tvCancel = popupView.findViewById(R.id.txt_cancel);
            tvCancel.setVisibility(cancelVisibility);
            if (!TextUtils.isEmpty(tvCancelWord)) {
                tvCancel.setText(tvCancelWord);
            }

            if (mTvCancelColor != 0 && mTvCancelColor != -1) {
                tvCancel.setTextColor(context.getResources().getColor(mTvCancelColor));
            }

            tvEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (commPopupListener != null) {
                        if (editVisibility == View.VISIBLE) {
                            String inputContent = editText.getText().toString();
                            if (!TextUtils.isEmpty(inputContent)) {
                                commPopupListener.sure(inputContent);
                            } else {
                                commPopupListener.sure();
                            }
                        } else {
                            commPopupListener.sure();
                        }
                    }
                }
            });

            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commPopupListener != null) {
                        commPopupListener.cancel();
                    }
                    dismiss();
                }
            });
        }
    }
}
