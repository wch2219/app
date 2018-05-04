package com.xxc.shoppingmall.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxc.shoppingmall.R;


/**
 * Created by xuxingchen on 2017/11/14.
 * 自定义title
 */
public class CommonTitle extends RelativeLayout {

    private TextView mTitle;
    private TextView mLeftTxt;
    private TextView mRightTxt;
    private ImageView mLeftImg;
    private ImageView mRightImg;
    private View mTitleDivider;
    private RelativeLayout mTitleLeft;
    private RelativeLayout mTitleRight;

    public CommonTitle(Context context) {
        super(context);
        init(context, null);
    }

    public CommonTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CommonTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.common_title, this);
        mTitle = (TextView) findViewById(R.id.common_title);
        mLeftTxt = (TextView) findViewById(R.id.title_left_txt);
        mRightTxt = (TextView) findViewById(R.id.title_right_txt);
        mLeftImg = (ImageView) findViewById(R.id.title_img_left);
        mRightImg = (ImageView) findViewById(R.id.title_img_right);
        mTitleLeft = (RelativeLayout) findViewById(R.id.title_left);
        mTitleRight = (RelativeLayout) findViewById(R.id.title_right);
        mTitleDivider = findViewById(R.id.title_divider);

        if (null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonTitle);

            String title = ta.getString(R.styleable.CommonTitle_Titile_Txt);
            String leftTxt = ta.getString(R.styleable.CommonTitle_Left_Txt);
            String rightTxt = ta.getString(R.styleable.CommonTitle_Right_Txt);

            Drawable leftRes = ta.getDrawable(R.styleable.CommonTitle_Left_Res);
            Drawable rightRes = ta.getDrawable(R.styleable.CommonTitle_Right_Res);

            int titleColor = ta.getColor(R.styleable.CommonTitle_Title_Color, Color.WHITE);
            int leftColor = ta.getColor(R.styleable.CommonTitle_Left_Color, Color.WHITE);
            int rightColor = ta.getColor(R.styleable.CommonTitle_Right_Color, Color.WHITE);

            boolean dividerVisiable = ta.getBoolean(R.styleable.CommonTitle_Divider_Visiable, true);

            mTitle.setText(title);
            mTitle.setTextColor(titleColor);

            if (!TextUtils.isEmpty(leftTxt)) {
                mLeftTxt.setText(leftTxt);
                mLeftTxt.setTextColor(leftColor);
            } else {
                mLeftTxt.setVisibility(GONE);
            }

            if (!TextUtils.isEmpty(rightTxt)) {
                mRightTxt.setText(rightTxt);
                mRightTxt.setTextColor(rightColor);
            } else {
                mRightTxt.setVisibility(GONE);
            }

            if (null != leftRes) {
                mLeftImg.setImageDrawable(leftRes);
                mTitleLeft.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getContext() instanceof Activity) {
                            ((Activity) getContext()).finish();
                        }
                    }
                });
            } else {
                mLeftImg.setVisibility(GONE);
            }

            if (null != rightRes) {
                mRightImg.setImageDrawable(rightRes);
            } else {
                mRightImg.setVisibility(GONE);
            }

            if (!dividerVisiable) {
                mTitleDivider.setVisibility(GONE);
            }
        }
    }

    public String getTitle() {
        return mTitle.getText().toString().trim();
    }

    public void setTitle(String title) {
        if (mTitle != null) {
            mTitle.setText(title);
        }
    }

    public String getLeftTxt() {
        return mLeftTxt.getText().toString().trim();
    }

    public void setLeftTxt(String leftTxt) {
        if (mLeftTxt != null) {
            mLeftTxt.setText(leftTxt);
        }
    }

    public String getRightTxt() {
        return mRightTxt.getText().toString().trim();
    }

    public void setRightTxt(String rightTxt) {
        if (null != mRightTxt) {
            mRightTxt.setText(rightTxt);
        }
    }

    public ImageView getLeftImg() {
        return mLeftImg;
    }

    public void setLeftImg(ImageView leftImg) {
        mLeftImg = leftImg;
    }

    public ImageView getRightImg() {
        return mRightImg;
    }

    public void setRightImg(ImageView rightImg) {
        mRightImg = rightImg;
    }

    public void setRightClickListener(OnClickListener listener) {
        if (null != mTitleRight) {
            mTitleRight.setOnClickListener(listener);
        }
    }

    public void setLeftClickListener(OnClickListener listener) {
        if (null != mTitleLeft) {
            mTitleLeft.setOnClickListener(listener);
        }
    }
}
