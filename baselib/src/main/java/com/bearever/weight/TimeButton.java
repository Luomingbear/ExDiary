package com.bearever.weight;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: X_Meteor
 * @description: 类描述
 * @version: V_1.0.0
 * @date: 2017/3/23 0023 15:51
 * @email: lx802315@163.com
 */
public class TimeButton extends Button implements View.OnClickListener {

    //倒计时时长(默认为60s)
    private long length = 60 * 1000;
    //设置点击前显示
    private String beforText = "发送验证码";
    //设置点击后显示
    private String afrerText = "秒后重发";
    private final String TIME = "time";
    private final String CTIME = "ctime";
    private OnClickListener mOnClickListener;
    private Timer t;
    private TimerTask tt;
    private long time;
    private Context context;
    Map<String, Long> map = new HashMap<>();

    public TimeButton(Context context) {
        this(context, null);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        this.context = context;
        TimeButton.this.setText(beforText);
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            TimeButton.this.setText(time / 1000 + afrerText);
            time -= 1000;
            Log.e("XX_Meteor", "handleMessage: " + time);
            if (time > 2000) {
                if (mOnTimeCountdownLisenter != null) {
                    mOnTimeCountdownLisenter.timeCountdown();
                }
                return;
            } else {
                if (time == 0) {
                    if (onTimeOverLisenter != null) {
                        onTimeOverLisenter.timeOver();
                        Log.e("XX_Meteor", "调用结束接口 ");
                    }
                    TimeButton.this.setEnabled(true);
                    TimeButton.this.setText(beforText);
                    clearTimer();
                    return;
                } else if (time < -1000) {
                    Log.e("XX_Meteor", "错误调用结束接口 ");
                    TimeButton.this.setEnabled(true);
                    TimeButton.this.setText(beforText);
                    clearTimer();
                    return;
                }
            }

        }
    };

    private OnTimeOverLisenter onTimeOverLisenter;

    public void setOnTimeOverLisenter(OnTimeOverLisenter onTimeOverLisenter) {
        this.onTimeOverLisenter = onTimeOverLisenter;
    }

    public interface OnTimeOverLisenter {
        void timeStart();

        void timeOver();
    }

    private OnTimeCountdownLisenter mOnTimeCountdownLisenter;

    public void setOnTimeCountdownLisenter(OnTimeCountdownLisenter onTimeCountdownLisenter) {
        mOnTimeCountdownLisenter = onTimeCountdownLisenter;
    }

    public interface OnTimeCountdownLisenter {
        void timeCountdown();
    }

    private void initTimer() {
        time = length;
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                Log.i("XX_Meteor", time / 1000 + " ");
                handler.sendEmptyMessage(0);
            }
        };
    }

    private void clearTimer() {
        Log.e("XX_Meteor", "计时结束 ");
        if (this.tt != null) {
            this.tt.cancel();
            this.tt = null;
        }
        if (this.t != null) {
            this.t.cancel();
        }
        this.t = null;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        if (l instanceof TimeButton) {
            super.setOnClickListener(l);
        } else {
            this.mOnClickListener = l;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(v);
        }
        if (onTimeOverLisenter != null) {
            onTimeOverLisenter.timeStart();
        }
    }

    public void start() {
        Log.e("XX_Meteor", "Start_time: " + time);
        initTimer();
        this.setText(time / 1000 + afrerText);
        this.setEnabled(false);
        t.schedule(tt, 0, 1000);
    }

    public void stop() {
        Log.e("XX_Meteor", "stop_time: " + time);
        time = length;
        TimeButton.this.setEnabled(true);
        TimeButton.this.setText(beforText);
        clearTimer();
    }

    /**
     * 设置点击之前的文本
     *
     * @param text0
     * @return
     */
    public TimeButton setTextBefore(String text0) {
        this.beforText = text0;
        this.setText(beforText);
        return this;
    }

    /**
     * 设置计时时候显示的文本
     *
     * @param text1
     * @return
     */
    public TimeButton setTextAfter(String text1) {
        this.afrerText = text1;
        return this;
    }

    /**
     * 设置倒计时长度
     * 时间默认  毫秒
     *
     * @param lenght
     * @return
     */
    public TimeButton setLenght(long lenght) {
        this.length = lenght;
        return this;
    }
}
