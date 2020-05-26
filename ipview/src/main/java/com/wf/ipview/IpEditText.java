package com.wf.ipview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * ZoomLayout -> com.jzcy.iqiyi.setting.views.widget -> IpEditText
 *
 * @Author: wf-pc
 * @Date: 2020-04-08 16:06
 */
public class IpEditText extends LinearLayout implements TextWatcher, View.OnFocusChangeListener {
    private final String TAG = IpEditText.class.getSimpleName();

    private final int DEFAULT_TEXT_MAX_LENGTH = 3;
    private final int DEFAULT_TEXT_SIZE = 15;
    private final int DEFAULT_TEXT_COLOR = Color.WHITE;
    private final int DEFAULT_TEXT_LIGHT_COLOR = Color.parseColor("#4579E6");

    private final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private final int DEFAULT_BORDER_WIDTH = 2;
    private final int DEFAULT_POINT_COLOR = Color.WHITE;
    private final int DEFAULT_POINT_RADIUS = 2;
    private final int DEFAULT_IP_TEXT_LENGTH = 4;
    private final int DEFAULT_FOCUS_BG_COLOR = Color.parseColor("#FF45AF3C");
    private final int DEFAULT_NORMAL_BG_COLOR = Color.parseColor("#33828181");

    private Paint paint;

    private int textLength;
    private int textSize;
    private int textColor;
    private int highLightColor;

    private int borderColor;
    private int borderWidth;

    private int pointColor;
    private int pointRadius;
    private int editNumber;

    private int focusBgColor;
    private int normalBgColor;

    private List<EditText> mEditList = new ArrayList<EditText>();
    private IpTextWatcher mIpTextWatcher;
    private OnFocusChangeListener mIpFocusChangeListener;
    private boolean mLastFocus;
    private View mFocusParent;

    public interface IpTextWatcher {
        void afterTextChanged(String ip);
    }

    public IpEditText(Context context) {
        this(context, null);
    }

    public IpEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IpEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProp(context, attrs, defStyleAttr);
        initPaint();
        initItem();
    }

    private void initProp(Context context, AttributeSet attrs, int defStyleAttr) {
        /*<?xml version="1.0" encoding="utf-8"?>
        <resources>
            <declare-styleable name="SuperEditText">
                <attr name="textLength" format="integer" />
                <attr name="textSize" format="dimension" />
                <attr name="textColor" format="color" />
                <attr name="borderColor" format="color" />
                <attr name="borderWidth" format="dimension" />
                <attr name="pointColor" format="color" />
                <attr name="pointWidth" format="dimension" />
                <attr name="editNumber" format="integer" />
            </declare-styleable>
        </resources>*/
        /*TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SuperEditText, defStyleAttr, 0);
        textLength = ta.getInt(R.styleable.SuperEditText_textLength, DEFAULT_TEXT_MAX_LENGTH);

        textSize = (int) ta.getDimension(R.styleable.SuperEditText_textSize, DEFAULT_TEXT_SIZE);
        textColor = ta.getColor(R.styleable.SuperEditText_textColor, DEFAULT_TEXT_COLOR);

        borderColor = ta.getColor(R.styleable.SuperEditText_borderColor, DEFAULT_BORDER_COLOR);
        borderWidth = (int) ta.getDimension(R.styleable.SuperEditText_borderWidth, DEFAULT_BORDER_WIDTH);

        pointColor = ta.getColor(R.styleable.SuperEditText_pointColor, DEFAULT_POINT_COLOR);
        pointWidth = (int) ta.getDimension(R.styleable.SuperEditText_pointWidth, DEFAULT_POINT_WIDTH);

        editNumber = ta.getInt(R.styleable.SuperEditText_editNumber, DEFAULT_IP_EDITTEXT_LENGTH);*/

        textLength = DEFAULT_TEXT_MAX_LENGTH;
        textSize = sp2px(DEFAULT_TEXT_SIZE);
        textColor = DEFAULT_TEXT_COLOR;
        highLightColor = DEFAULT_TEXT_LIGHT_COLOR;

        borderColor = DEFAULT_BORDER_COLOR;
        borderWidth = dp2px(DEFAULT_BORDER_WIDTH);

        pointColor = DEFAULT_POINT_COLOR;
        pointRadius = dp2px(DEFAULT_POINT_RADIUS);

        editNumber = DEFAULT_IP_TEXT_LENGTH;

        focusBgColor = DEFAULT_FOCUS_BG_COLOR;
        normalBgColor = DEFAULT_NORMAL_BG_COLOR;

        setOrientation(LinearLayout.HORIZONTAL);
        setFocusable(true);
        setFocusableInTouchMode(true);
        super.setOnFocusChangeListener(this);

    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(textSize);
    }

    private void initItem() {
        removeAllViews();
        for (int i = 0; i < editNumber; i++) {
            EditText edit = new EditText(getContext());
            edit.setBackground(null);
            edit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(textLength)});
            edit.setTextSize(textSize);
            edit.setTextColor(textColor);
            edit.setGravity(Gravity.CENTER);
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            edit.setTag(i);
            edit.setMaxLines(1);
            edit.setSelectAllOnFocus(true);
            edit.setHighlightColor(highLightColor);
            //edit.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1));
            edit.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
            int width = (int) edit.getPaint().measureText("000");
            edit.getLayoutParams().width = width + edit.getPaddingLeft() + edit.getPaddingRight();

            edit.addTextChangedListener(this);
            edit.setOnFocusChangeListener(this);
            edit.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

            edit.setNextFocusDownId(getNextFocusDownId());
            edit.setNextFocusUpId(getNextFocusUpId());
            edit.setNextFocusForwardId(getNextFocusForwardId());
            if (i == 0) {
                edit.setNextFocusLeftId(getNextFocusLeftId());
            }
            if (i == editNumber - 1) {
                edit.setNextFocusRightId(getNextFocusRightId());
            }
            addView(edit);
            mEditList.add(edit);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mEditList.size() <= 0) return;

        /*int l = getPaddingLeft();
        int t = getPaddingTop();
        int r = getMeasuredWidth() - getPaddingRight();
        int b = getMeasuredHeight() - getPaddingBottom();
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(l, t, r, b, paint);*/

        paint.setStrokeWidth(pointRadius);
        paint.setColor(pointColor);
        int y = mEditList.get(0).getBaseline() + getPaddingTop();
        for (int i = 1; i < mEditList.size(); i++) {
            canvas.drawPoint(mEditList.get(i).getLeft(), y, paint);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (mIpTextWatcher != null) {
            mIpTextWatcher.afterTextChanged(getIpString());
        }
        if (s.length() == 3) {
            for (int i = 0; i < mEditList.size(); i++) {
                EditText edit = mEditList.get(i);
                String val = edit.getText().toString();
                if (val.length() == 0) {
                    edit.requestFocus();
                    return;
                }
            }
        } else if (s.length() == 0) {
            for (int i = mEditList.size() - 1; i >= 0; i--) {
                EditText edit = mEditList.get(i);
                edit.setFocusable(true);
                String val = edit.getText().toString();
                if (val.length() == 3) {
                    edit.requestFocus();
                    edit.setSelection(3);
                    return;
                }
            }
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        boolean focus = false;
        for (int i = 0; i < mEditList.size(); i++) {
            if (mEditList.get(i).hasFocus()) {
                focus = true;
            }
        }
        //焦点在外层layout上时
        if (!focus && v == this && hasFocus) {
            focus = true;
            if (mEditList.size() > 0) {
                mEditList.get(0).requestFocus();
            }
        }
        if (mLastFocus != focus) {
            mLastFocus = focus;
            scaleFocusParent(focus);
            if (mIpFocusChangeListener != null) {
                mIpFocusChangeListener.onFocusChange(this, focus);
            }
        }
    }

    private void scaleFocusParent(boolean hasFocus) {
        if (mFocusParent == null) return;
        if (hasFocus) {
            mFocusParent.setBackgroundColor(focusBgColor);
            mFocusParent.animate()
                    .setDuration(500)
                    .scaleX(1.02f)
                    .scaleY(1.08f)
                    .start();
            mFocusParent.bringToFront();
        } else {
            mFocusParent.setBackgroundColor(normalBgColor);
            mFocusParent.animate()
                    .setDuration(200)
                    .scaleX(1f)
                    .scaleY(1f)
                    .start();
        }
    }


    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int px2dp(float pxValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    private int px2sp(float pxValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public void bindFocusParent(View view) {
        mFocusParent = view;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        mIpFocusChangeListener = listener;
    }

    public void setTextWatcher(IpTextWatcher listener) {
        mIpTextWatcher = listener;
    }

    public String getIpString() {
        StringBuilder ip = new StringBuilder();
        for (int i = 0; i < mEditList.size(); i++) {
            ip.append(mEditList.get(i).getText().toString());
            if (i != mEditList.size() - 1) {
                ip.append(".");
            }
        }
        return ip.toString();
    }

    public String[] getIpValue() {
        String[] val = new String[mEditList.size()];
        for (int i = 0; i < mEditList.size(); i++) {
            val[i] = mEditList.get(i).getText().toString();
        }
        return val;
    }

    public void setIpString(String ip) {
        if (TextUtils.isEmpty(ip) || mEditList.size() <= 0) return;
        String[] ips = ip.split("\\.");
        for (int i = 0; i < mEditList.size(); i++) {
            if (checkIp(ips[i])) {
                mEditList.get(i).setText(ips[i]);
            }
        }
    }

    public void setIpValue(String[] ips) {
        if (ips == null || ips.length <= 0 || mEditList.size() <= 0) return;
        for (int i = 0; i < mEditList.size(); i++) {
            if (checkIp(ips[i])) {
                mEditList.get(i).setText(ips[i]);
            }
        }
    }

    private boolean checkIp(String ip) {
        try {
            return Integer.parseInt(ip) <= 255;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void setTextColor(int color) {
        textColor = color;
        pointColor = color;
        invalidate();
        if (mEditList.size() <= 0) return;
        for (int i = 0; i < mEditList.size(); i++) {
            mEditList.get(i).setTextColor(color);
        }
    }

    @Override
    public void setFocusable(boolean focusable) {
        super.setFocusable(focusable);
        if (mEditList.size() <= 0) return;
        for (int i = 0; i < mEditList.size(); i++) {
            mEditList.get(i).setFocusable(focusable);
        }
    }
}
