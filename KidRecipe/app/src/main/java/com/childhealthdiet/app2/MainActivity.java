package com.childhealthdiet.app2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.childhealthdiet.app2.adapter.ViewPagerAdapter;
import com.childhealthdiet.app2.ui.base.BaseActivity;
import com.childhealthdiet.app2.ui.fragment.BasketFragment;
import com.childhealthdiet.app2.ui.fragment.CategoryFragment;
import com.childhealthdiet.app2.ui.fragment.HomeFragment;
import com.childhealthdiet.app2.ui.fragment.MineFragment;
import com.childhealthdiet.app2.utils.BottomNavigationViewHelper;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    MenuItem menuItem;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @Override
    protected int getContentId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget(){
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        BottomNavigationViewHelper.disableItemScale(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.navigation_category:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.navigation_basket:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.navigation_mine:
                                viewPager.setCurrentItem(3);
                                break;
                        }
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

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认 >3 的选中效果会影响ViewPager的滑动切换时的效果，故利用反射去掉




    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new CategoryFragment());
        adapter.addFragment(new BasketFragment());
        adapter.addFragment(new MineFragment());
        viewPager.setAdapter(adapter);
    }

}
