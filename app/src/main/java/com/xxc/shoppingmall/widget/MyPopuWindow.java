package com.xxc.shoppingmall.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

public class MyPopuWindow extends PopupWindow {
    public MyPopuWindow(Context context) {
        super(context);
    }

    public MyPopuWindow(View contentView) {
        super(contentView);
    }

    public MyPopuWindow(View context, int attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public MyPopuWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
    }



    @Override
    public void showAsDropDown(View anchor) {
        if (Build.VERSION.SDK_INT >= 24) {
            Rect visibleFrame = new Rect();
            anchor.getGlobalVisibleRect(visibleFrame);
            int height = anchor.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            setHeight(height);
        }
        super.showAsDropDown(anchor);
    }
}
