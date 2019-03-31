package com.mihwapp.womanrecipe.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.mihwapp.womanrecipe.R;
import com.mihwapp.womanrecipe.RecipeApplication;
import com.mihwapp.womanrecipe.model.bean.DaoSession;
import com.mihwapp.womanrecipe.model.bean.RecipeBean;
import com.mihwapp.womanrecipe.model.bean.RecipeBeanDao;
import com.mihwapp.womanrecipe.model.services.RecipeService;
import com.mihwapp.womanrecipe.utils.SpacesItemDecoration;
import com.mihwapp.womanrecipe.view.adapters.OnItemClickListener;
import com.mihwapp.womanrecipe.view.adapters.RecipeListAdapter;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnLongClickListener, OnItemClickListener {


    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.channel_recipe_list)
    RecyclerView recipeListView;
    @BindView(R.id.imgv_search)
    ImageView imvSearch;
    @BindView(R.id.llBatchManagement)
    LinearLayout llBatchManagement;

    @BindView(R.id.tvSelectAll)
    TextView tvSelectAll;

    @BindView(R.id.txt_Title)
    TextView tvTitle;

    Toolbar toolbar;


    ActionBarDrawerToggle toggle;


    private RecipeListAdapter recipeListAdapter;
//    private Subscription channelsSubscription = Subscriptions.empty();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitle("213123123123123");


        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView.setNavigationItemSelectedListener(this);

//        testDb();
        setupViews();

       toolbar.getNavigationIcon().setVisible(false,false);
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showAppRatingDialog();
        }
    }

    private static final String KEY_hasRateTheApp = "KEY_hasRateTheApp";
    private static final String appStoreUrl = "https://play.google.com/store/apps/details?id=com.mihwapp.womanrecipe";

    private void setHasRateTheApp(Boolean hasRateTheApp) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(KEY_hasRateTheApp, hasRateTheApp).apply();

    }

    private Boolean hasRateTheApp() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(KEY_hasRateTheApp, false);
    }

    private void showAppRatingDialog() {
        if (hasRateTheApp()) {
            MainActivity.super.onBackPressed();
            return;
        }

        new MaterialStyledDialog.Builder(this)

                .setStyle(Style.HEADER_WITH_ICON)
                .setTitle("找到喜欢的菜谱了吗？")
                .setDescription(getString(R.string.ask_rating))
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveText(getString(R.string.ok_sure))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appStoreUrl)));
                        setHasRateTheApp(true);
                    }
                })
                .setNegativeText(R.string.not_now)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        MainActivity.super.onBackPressed();
                    }
                })
                .setCancelable(true)
                .show();
    }

    @OnClick(R.id.imgv_search)
    public void onClickImgvSearch() {
        startActivityForResult(new Intent(MainActivity.this, SearchActivity.class), 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_early_month:
            case R.id.nav_middle_month:
            case R.id.nav_late_month:
            case R.id.nav_function_recipe:
                this.hideMonthItem(id);
                break;

            case R.id.nav_one_month:
            case R.id.nav_two_month:
            case R.id.nav_three_month:
            case R.id.nav_four_month:
            case R.id.nav_five_month:
            case R.id.nav_six_month:
            case R.id.nav_seven_month:
            case R.id.nav_eight_month:
            case R.id.nav_nine_month:
            case R.id.nav_ten_month:
            case R.id.nav_morning_sickness_recipe:
            case R.id.nav_enrichtheblood_recipe:
            case R.id.nav_vitamin_recipe:
            case R.id.nav_advantage_recipe:
                this.updateRecipe(id);
//                RecipeApplication.getApplication().isCollectMode = false;
                collectMode(false);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_collect:
                this.updateRecipe(id);
//                RecipeApplication.getApplication().isCollectMode = true;
                collectMode(true);
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.nav_send:
                this.sendEmail();
                break;

            case R.id.nav_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appStoreUrl)));
                setHasRateTheApp(true);
                break;
        }


        return true;
    }

    private void collectMode(boolean isCollectModel) {
        RecipeApplication.getApplication().isCollectMode = isCollectModel;
        imvSearch.setVisibility(isCollectModel ? View.GONE : View.VISIBLE);
    }


    private void hideMonthItem(int itemId) {

        int curMonth[] = null;
        int earlyMonth[] = {R.id.nav_one_month, R.id.nav_two_month, R.id.nav_three_month};
        int middleMonth[] = {R.id.nav_four_month, R.id.nav_five_month, R.id.nav_six_month};
        int lateMonth[] = {R.id.nav_seven_month, R.id.nav_eight_month, R.id.nav_nine_month, R.id.nav_ten_month};
        int functionRecipe[] = {R.id.nav_morning_sickness_recipe, R.id.nav_enrichtheblood_recipe, R.id.nav_vitamin_recipe, R.id.nav_advantage_recipe};
        switch (itemId) {
            case R.id.nav_early_month:
                curMonth = earlyMonth;
                break;
            case R.id.nav_middle_month:
                curMonth = middleMonth;
                break;
            case R.id.nav_late_month:
                curMonth = lateMonth;
                break;
            case R.id.nav_function_recipe:
                curMonth = functionRecipe;
                break;


        }
        for (int curItemId : curMonth) {
            MenuItem item = navigationView.getMenu().findItem(curItemId);
            item.setVisible(!item.isVisible());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        disposables.clear();
    }


    private void setupViews() {
        disposables.add(RecipeService.getInstance().getRecipeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
                    @Override
                    public void onComplete() {
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposables.clear();
                    }

                    @Override
                    public void onNext(List<RecipeBean> recipes) {
                        setupViews(recipes);

                    }
                }));
//        this.setupViews(null);
    }

    private void updateRecipe(int id) {

        String strTitleText = this.getTitleText(id);

        tvTitle.setText(strTitleText);
        disposables.add(RecipeService.getInstance().getRecipeObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
                    @Override
                    public void onComplete() {
                        disposables.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposables.clear();
                    }

                    @Override
                    public void onNext(List<RecipeBean> recipes) {
                        recipeListAdapter.updateRecipe(recipes);

                    }
                }));
    }


    private void setupViews(List<RecipeBean> recipes) {
        recipeListView.setLayoutManager(new GridLayoutManager(this, 3));
        recipeListAdapter = new RecipeListAdapter(recipes);
        recipeListAdapter.activity = this;
        recipeListAdapter.onLongClickListener = this;
        recipeListAdapter.onItemClickListener = this;
        recipeListView.setAdapter(recipeListAdapter);

        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(2);
        recipeListView.addItemDecoration(decoration);


    }

    /**
     * 显示批量管理布局
     */
    private void showBatchManagementLayout() {
        visible(llBatchManagement);
        for (RecipeBean bean : recipeListAdapter.recipes) {
            bean.showCheckBox = true;
        }
        recipeListAdapter.notifyDataSetChanged();
    }

    /**
     * 显示批量管理布局
     */
    private void hideBatchManagementLayout() {
        gone(llBatchManagement);
        for (RecipeBean bean : recipeListAdapter.recipes) {
            bean.showCheckBox = false;
        }
        recipeListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onLongClick(View v) {
        if (!RecipeApplication.getApplication().isCollectMode) {
            return true;
        }
        showBatchManagementLayout();
        toggle.setDrawerIndicatorEnabled(false);
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        if (isVisible(llBatchManagement)) //批量管理时，屏蔽点击事件
            return;
        CookDetailActivity.startActivity(this, view, recipeListAdapter.recipes.get(position), true);
    }

    @OnClick(R.id.tvSelectAll)
    public void onClickSelectAll() {
        Boolean isDealChk = false;
        String strShowText = "";
        if (tvSelectAll.getText().equals("全选")) {
            isDealChk = true;
            strShowText = "全不选";
        } else {
            isDealChk = false;
            strShowText = "全选";
        }
        for (RecipeBean bean : recipeListAdapter.recipes) {
            bean.setIsCanDelete(isDealChk);
        }
        recipeListAdapter.notifyDataSetChanged();
        tvSelectAll.setText(strShowText);
    }

    @OnClick(R.id.tvDelete)
    public void onClickDelete() {
        showNormalDialog();

    }

    @OnClick(R.id.tvCancel)
    public void onClickCancel() {
        for (RecipeBean bean : recipeListAdapter.recipes) {
            bean.setShowCheckBox(false);
//            bean.isCanDelete(false);
            bean.setIsCanDelete(false);
        }
        recipeListAdapter.notifyDataSetChanged();
        hideBatchManagementLayout();
        toggle.setDrawerIndicatorEnabled(true);
    }


    public void testDb() {
        DaoSession daoSession = ((RecipeApplication) getApplication()).daoSession;
        RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
        List<RecipeBean> recipeBeans = recipeBeanDao.loadAll();

//        for(int i=0;i<recipeBeans)

        for (int i = 0; i < recipeBeans.size(); i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
            System.out.println(recipeBeans.get(i));
        }
//        TownDao townDao = daoSession.getTownDao();
//        List<Town> towns = townDao.loadAll();

    }

    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * <p>
     * <p>
     * <p>
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * <p>
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 1:

                if (data != null) {
                    String searchStr = data.getExtras().getString("searchKey");
                    tvTitle.setText("'"+searchStr+"' 的搜索结果");
//                    searchStr = "鱼";
                    disposables.add(RecipeService.getInstance().searchRecipeObservable(searchStr)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
                                @Override
                                public void onComplete() {
                                    disposables.clear();
                                }

                                @Override
                                public void onError(Throwable e) {
                                    disposables.clear();
                                }

                                @Override
                                public void onNext(List<RecipeBean> recipes) {
                                    recipeListAdapter.updateRecipe(recipes);

                                }
                            }));
                }


            case 2:

                //来自按钮2的请求，作相应业务处理

        }


        //得到新Activity 关闭后返回的数据


    }


    protected void gone(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void visible(final View... views) {
        if (views != null && views.length > 0) {
            for (View view : views) {
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }


    //    /**
//     * Copies your database from your local assets-folder to the just created
//     * empty database in the system folder, from where it can be accessed and
//     * handled. This is done by transfering bytestream.
//     * */
//    private void copyDataBase(String dbname) throws IOException {
//        // Open your local db as the input stream
//        InputStream myInput = this.getAssets().open(dbname);
//        // Path to the just created empty db
//        File outFileName = this.getDatabasePath(dbname);
//
//        if (!outFileName.exists()) {
//            outFileName.getParentFile().mkdirs();
//
//            // Open the empty db as the output stream
//            OutputStream myOutput = new FileOutputStream(outFileName);
//            // transfer bytes from the inputfile to the outputfile
//            byte[] buffer = new byte[1024];
//            int length;
//            while ((length = myInput.read(buffer)) > 0) {
//                myOutput.write(buffer, 0, length);
//            }
//            // Close the streams
//            myOutput.flush();
//            myOutput.close();
//            myInput.close();
//        }
//    }
    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(MainActivity.this);
//    normalDialog.setIcon(R.drawable.icon_dialog);
        normalDialog.setTitle("提示");
        normalDialog.setMessage("你确定要点删除这些吗?");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        recipeListAdapter.deleteRecipes();
                        hideBatchManagementLayout();
                        dialog.dismiss();
                        toggle.setDrawerIndicatorEnabled(true);
                    }
                });
        normalDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                        dialog.dismiss();
                    }
                });
        // 显示
        normalDialog.show();

    }

    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        // i.setType("text/plain"); //模拟器请使用这行
        i.setType("message/rfc822"); // 真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{getString(R.string.feedbackEmail)});

        String txtTitle = getResources().getString(R.string.feedbackTitle);
        txtTitle = String.format(txtTitle, getString( R.string.app_name));


        i.putExtra(Intent.EXTRA_SUBJECT, txtTitle);
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedbackBody));
        ArrayList<Uri> uris = new ArrayList<>();
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(i,
                "选择email程序."));
    }


    String getTitleText(int id){
        String strRet = getString(R.string.app_name);
        switch (id) {
            case R.id.nav_one_month:
                strRet="第一个月食谱";
                break;
            case R.id.nav_two_month:
                strRet="第二个月食谱";
                break;
            case R.id.nav_three_month:
                strRet="第三个月食谱";
                break;
            case R.id.nav_four_month:
                strRet="第四个月食谱";
                break;
            case R.id.nav_five_month:
                strRet="第五个月食谱";
                break;
            case R.id.nav_six_month:
                strRet="第六个月食谱";
                break;
            case R.id.nav_seven_month:
                strRet="第七个月食谱";
                break;
            case R.id.nav_eight_month:
                strRet="第八个月食谱";
                break;
            case R.id.nav_nine_month:
                strRet="第九个月食谱";
                break;
            case R.id.nav_ten_month:
                strRet="第十个月食谱";
                break;
            case R.id.nav_morning_sickness_recipe:
                strRet="缓解孕吐食谱";
                break;
            case R.id.nav_enrichtheblood_recipe:
                strRet="补血食谱";
                break;
            case R.id.nav_vitamin_recipe:
                strRet="补维生素食谱";
                break;
            case R.id.nav_advantage_recipe:
                strRet="优生食谱";
                break;

            case R.id.nav_collect:
                strRet="我喜欢的食谱";
                break;
        }
        return strRet;
    }
}
