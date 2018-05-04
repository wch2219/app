package com.xxc.shoppingmall.utils;

/**
 * Created by admin on 2017/8/30.
 */

public class GlideExtra extends ImageLoader.Extras {

    public static final int CENTER_CROP = 1;
    public static final int SHOW_DEFAULT = 0;

    public static final int SHOW_CIRCLE=1;
    public static final int NO_CIRCLE=0;

    public int scaleType = SHOW_DEFAULT;

    public int showCircle = NO_CIRCLE;

    public int placeHolderRes;

}
