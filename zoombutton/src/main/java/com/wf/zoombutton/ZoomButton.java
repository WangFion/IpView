package com.wf.zoombutton;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

import androidx.appcompat.widget.AppCompatButton;

/**
 * JZCY_iQIYI_SETTING -> com.jzcy.iqiyi.setting.views.widget -> ZoomButton
 *
 * @Author: wf-pc
 * @Date: 2020-04-20 14:10
 */
public class ZoomButton extends AppCompatButton {

    private int focusColor = Color.parseColor("#FFF8F8F8");
    private int normalColor = Color.parseColor("#40FFFFFF");

    private boolean enableBottomShake = false;
    private boolean enableTopShake = false;
    private boolean enableLeftShake = false;
    private boolean enableRightShake = false;

    public ZoomButton(Context context) {
        super(context);
        init();
    }

    public ZoomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ZoomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //setBackgroundResource(R.drawable.bg_button_selector);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            scaleUp();
        } else {
            scaleDown();
        }
    }

    private void scaleUp() {
        setTextColor(focusColor);
        this.animate()
                .setDuration(300)
                .scaleX(1.02f)
                .scaleY(1.08f)
                //.setInterpolator(new BounceInterpolator())//回弹
                .start();
        this.bringToFront();
    }

    private void scaleDown() {
        setTextColor(normalColor);
        this.animate()
                .setDuration(200)
                .scaleX(1f)
                .scaleY(1f)
                .start();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (enableTopShake && keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                shakeY(this);
            }
            if (enableBottomShake && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                shakeY(this);
            }
            if (enableLeftShake && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                shakeX(this);
            }
            if (enableRightShake && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                shakeX(this);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void shakeX(View view) {
        TranslateAnimation animation = new TranslateAnimation(
                -2.0f, 2.0f, 0f, 0f);
        animation.setDuration(500);//设置动画持续时间
        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        animation.setInterpolator(new CycleInterpolator(3));
        view.bringToFront();
        view.startAnimation(animation);
    }

    public void shakeY(View view) {
        TranslateAnimation animation = new TranslateAnimation(
                0f, 0f, -2.0f, 2.0f);
        animation.setDuration(500);//设置动画持续时间
        animation.setRepeatMode(Animation.REVERSE);//设置反方向执行
        animation.setInterpolator(new CycleInterpolator(3));
        view.bringToFront();
        view.startAnimation(animation);
    }

    public void setFocusColor(int focusColor) {
        this.focusColor = focusColor;
    }

    public void setFocusColorRes(int resId) {
        this.focusColor = getContext().getResources().getColor(resId);
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public void setNormalColorRes(int resId) {
        this.normalColor = getContext().getResources().getColor(resId);
    }

    public ZoomButton openLeftShake() {
        enableLeftShake = true;
        return this;
    }

    public ZoomButton closeLeftShake() {
        enableLeftShake = false;
        return this;
    }

    public ZoomButton openRightShake() {
        enableRightShake = true;
        return this;
    }

    public ZoomButton closeRightShake() {
        enableRightShake = false;
        return this;
    }

    public ZoomButton openTopShake() {
        enableTopShake = true;
        return this;
    }

    public ZoomButton closeTopShake() {
        enableTopShake = false;
        return this;
    }

    public ZoomButton openBottomShake() {
        enableBottomShake = true;
        return this;
    }

    public ZoomButton closeBottomShake() {
        enableBottomShake = false;
        return this;
    }

}
