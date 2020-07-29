package com.ChildHealthDiet.app2.ui.custom;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;



/**
 * Created by apk2sf on 2017/12/2.
 * email: apk2sf@163.com
 * QQ：337081267
 */

public class NoAnimationViewPager extends ViewPager {

    public boolean isCanScroll = false;

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
        super.setCurrentItem(item, false);
    }


    @Override
    public void scrollTo(int x, int y) {
        if(isCanScroll){
            super.scrollTo(x, y);
        }
    }
}


