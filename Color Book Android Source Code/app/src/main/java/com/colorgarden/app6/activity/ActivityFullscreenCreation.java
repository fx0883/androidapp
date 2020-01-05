package com.colorgarden.app6.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.constant.Constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.colorgarden.app6.constant.Constant.shareImg;


public class ActivityFullscreenCreation extends AppCompatActivity {

    public static List<String> imgList1 = new ArrayList<>();
    int getPos;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager_creation)
    ViewPager pager_creation;
    List<String> getAllFile = new ArrayList<>();
    FullScreenAdapter fullScreenAdapter;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_creation);
        ButterKnife.bind(this);
        getPos = getIntent().getIntExtra("position", 0);

        init();
    }

    // initialize all view
    private void init() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
                startActivity(intent);
            }
        });

        getAllFile = Constant.getAllCreationList();
        imgList1 = Constant.getAllCreationList();
        Collections.reverse(getAllFile);
        Collections.reverse(imgList1);
        imgPath = imgList1.get(getPos);
        if (!getAllFile.isEmpty()) {

            fullScreenAdapter = new FullScreenAdapter(getApplicationContext());
            pager_creation.setAdapter(fullScreenAdapter);
            pager_creation.setCurrentItem(getPos);

        }

        pager_creation.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                imgPath = imgList1.get(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.iv_share) {
            shareImg(imgPath, getApplicationContext());
        }
        return super.onOptionsItemSelected(item);
    }

    // method for backpressed
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), CreationActivity.class);
        startActivity(intent);
    }

    public class FullScreenAdapter extends PagerAdapter {

        Context context;

        ImageView img_full_screen;

        FullScreenAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = LayoutInflater.from(context).inflate(R.layout.ite_fullscreen_image, container, false);
            img_full_screen = view.findViewById(R.id.img_full_screen);
            Bitmap bitmap = BitmapFactory.decodeFile(imgList1.get(position));
            img_full_screen.setImageBitmap(bitmap);
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return imgList1.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
