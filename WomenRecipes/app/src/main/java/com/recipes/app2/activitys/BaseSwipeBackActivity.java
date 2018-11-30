package com.Recipes.app2.activitys;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


import com.Recipes.app2.view.components.app.SwipeBackActivity;
import com.Recipes.app2.view.components.app.SwipeBackActivityBase;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/17.
 */

public abstract class BaseSwipeBackActivity extends SwipeBackActivity implements SwipeBackActivityBase {
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(getLayoutId());
        context = this;
        ButterKnife.bind(this);

        init(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();



    }

    @Override
    protected void onPause() {
        super.onPause();



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    public Context getContext(){
        return context;
    }


    protected abstract int getLayoutId();
    protected abstract void init(Bundle savedInstanceState);

}
