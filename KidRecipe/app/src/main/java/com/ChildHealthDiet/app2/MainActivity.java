package com.ChildHealthDiet.app2;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.ChildHealthDiet.app2.ui.custom.NoAnimationViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.viewpager.widget.ViewPager;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.ChildHealthDiet.app2.adapter.ViewPagerAdapter;
import com.ChildHealthDiet.app2.event.ChangeTabEvent;
import com.ChildHealthDiet.app2.ui.base.BaseActivity;
import com.ChildHealthDiet.app2.ui.fragment.CategoryFragment;
import com.ChildHealthDiet.app2.ui.fragment.HomeFragment;
import com.ChildHealthDiet.app2.ui.fragment.MineFragment;
import com.ChildHealthDiet.app2.utils.BottomNavigationViewHelper;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    NoAnimationViewPager viewPager;

    MenuItem menuItem;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.disableItemScale(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        viewPager.isCanScroll = true;
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_category:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_mine:
                                viewPager.setCurrentItem(2);
                                break;
//                            case R.id.navigation_basket:
//                                viewPager.setCurrentItem(2);
//                                break;
//                            case R.id.navigation_mine:
//                                viewPager.setCurrentItem(3);
//                                break;
                        }
                        viewPager.isCanScroll = false;
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });

        //禁止ViewPager滑动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        setupViewPager(viewPager);

        initEvent();

    }

    private void initEvent(){
        Disposable tabDisp = RxBus.getInstance()
                .toObservable(ChangeTabEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        event -> {
//                            //使用Toast提示
//                            ToastUtils.show(event.message);
                            MainActivity.this.viewPager.setCurrentItem(event.tabIndex);
                        }
                );
        addDisposable(tabDisp);
    }

    @Override
    protected void initData(Bundle savedInstanceState){
        super.initData(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23) {
            checkAndRequestPermission();
        }
    }

    @Override
    protected void processLogic(){
        super.processLogic();
//        check();

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉
//
//
//    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new CategoryFragment());
        adapter.addFragment(new MineFragment());
//        adapter.addFragment(new BasketFragment());
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);

    }





}
