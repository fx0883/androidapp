package com.recipes.app2;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.etsy.android.grid.StaggeredGridView;
import com.recipes.app2.model.bean.DaoSession;
import com.recipes.app2.model.bean.RecipeBean;
import com.recipes.app2.model.bean.RecipeBeanDao;
import com.recipes.app2.model.services.RecipeService;
import com.recipes.app2.utils.SpacesItemDecoration;
import com.recipes.app2.view.adapters.RecipeListAdapter;




import java.util.List;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.channel_recipe_list)
    RecyclerView recipeListView;


    private RecipeListAdapter recipeListAdapter;
//    private Subscription channelsSubscription = Subscriptions.empty();

    private final CompositeDisposable disposables = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        testDb();
        setupViews();
    }



    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

//        if (id == R.id.nav_early_month) {
//            this.hideMonthItem(id);
//        } else if (id == R.id.nav_middle_month) {
//            this.hideMonthItem(id);
//        } else if (id == R.id.nav_late_month) {
//            this.hideMonthItem(id);
//        }
//        else if (id == R.id.nav_function_recipe) {
//            this.hideMonthItem(id);
//        }
//

        switch (id)
        {
            case R.id.nav_early_month:
            case R.id.nav_middle_month:
            case R.id.nav_late_month:
            case R.id.nav_function_recipe:
                this.hideMonthItem(id);
                break;

            case R.id.nav_one_month:
                this.updateRecipe(R.id.nav_one_month);
                break;
            case R.id.nav_two_month:
                this.updateRecipe(R.id.nav_two_month);
                break;
            case R.id.nav_three_month:
                this.updateRecipe(R.id.nav_three_month);
                break;
            case R.id.nav_four_month:

                this.updateRecipe(R.id.nav_four_month);
                break;
            case R.id.nav_five_month:
                this.updateRecipe(R.id.nav_five_month);

                break;
            case R.id.nav_six_month:

                this.updateRecipe(R.id.nav_six_month);
                break;
            case R.id.nav_seven_month:
                this.updateRecipe(R.id.nav_seven_month);

                break;
            case R.id.nav_eight_month:
                this.updateRecipe(R.id.nav_eight_month);
                break;
            case R.id.nav_nine_month:

                this.updateRecipe(R.id.nav_nine_month);
                break;
            case R.id.nav_ten_month:

                this.updateRecipe(R.id.nav_ten_month);
                break;





            case R.id.nav_morning_sickness_recipe:
            case R.id.nav_enrichtheblood_recipe:
            case R.id.nav_vitamin_recipe:
            case R.id.nav_advantage_recipe:
                this.updateRecipe(id);
                break;
        }

//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void hideMonthItem(int itemId){
//        earlyQi
        int curMonth[] = null;
        int earlyMonth[] = {R.id.nav_one_month,R.id.nav_two_month,R.id.nav_three_month};
        int middleMonth[] = {R.id.nav_four_month,R.id.nav_five_month,R.id.nav_six_month};
        int lateMonth[] = {R.id.nav_seven_month,R.id.nav_eight_month,R.id.nav_nine_month,R.id.nav_ten_month};
        int functionRecipe[] = {R.id.nav_morning_sickness_recipe,R.id.nav_enrichtheblood_recipe,R.id.nav_vitamin_recipe,R.id.nav_advantage_recipe};
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
        for(int curItemId:curMonth)
        {
            MenuItem item = navigationView.getMenu().findItem(curItemId);
            item.setVisible(!item.isVisible());
        }

    }
    @Override protected void onDestroy() {
        super.onDestroy();
//        disposables.clear();
    }



    private void setupViews() {
        disposables.add(RecipeService.getInstance().getRecipeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
                    @Override public void onComplete() {
                        disposables.clear();
                    }

                    @Override public void onError(Throwable e) {
                        disposables.clear();
                    }

                    @Override public void onNext(List<RecipeBean> recipes) {
                        setupViews(recipes);

                    }
                }));
//        this.setupViews(null);
    }

    private void updateRecipe(int id){
        disposables.add(RecipeService.getInstance().getRecipeObservable(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
                    @Override public void onComplete() {
                        disposables.clear();
                    }

                    @Override public void onError(Throwable e) {
                        disposables.clear();
                    }

                    @Override public void onNext(List<RecipeBean> recipes) {
                        recipeListAdapter.updateRecipe(recipes);

                    }
                }));
    }



    private void setupViews(List<RecipeBean> recipes) {
        recipeListView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recipeListAdapter = new RecipeListAdapter(recipes);
        recipeListAdapter.activity = this;

        recipeListView.setAdapter(recipeListAdapter);
        //设置item之间的间隔
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        recipeListView.addItemDecoration(decoration);

    }





    public void testDb() {
        DaoSession daoSession = ((RecipeApplication) getApplication()).daoSession;
        RecipeBeanDao recipeBeanDao = daoSession.getRecipeBeanDao();
        List<RecipeBean> recipeBeans= recipeBeanDao.loadAll();

//        for(int i=0;i<recipeBeans)

        for( int i = 0 ; i < recipeBeans.size() ; i++) {//内部不锁定，效率最高，但在多线程要考虑并发操作的问题。
            System.out.println(recipeBeans.get(i));
        }
//        TownDao townDao = daoSession.getTownDao();
//        List<Town> towns = townDao.loadAll();

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




}
