package com.childhealthdiet.app2.ui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;



/**
 * Created by apk2sf on 2017/12/2.
 * email: apk2sf@163.com
 * QQ：337081267
 */

public class NoAnimationViewPager extends ViewPager {

    public NoAnimationViewPager(Context context) {
        super(context);
    }

    public NoAnimationViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        //去除页面切换时的滑动翻页效果
        super.setCurrentItem(item, false);
    }
}


