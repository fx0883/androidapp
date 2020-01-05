package com.colorgarden.app6.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.adapter.View_more_adapter;
import com.colorgarden.app6.model.ModelSubCategory;
import com.colorgarden.app6.utils.ConnectionDetector;
import com.colorgarden.app6.utils.InternetDialog;
import com.colorgarden.app6.utils.NoInternetDialog;
import com.colorgarden.app6.utils.OfflineData;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.colorgarden.app6.constant.Constant.BASE_URL;
import static com.colorgarden.app6.constant.Constant.PAGINATION_SIZE;
import static com.colorgarden.app6.utils.InternetDialog.WIFI_REQ_CODE;
import static com.android.volley.Request.Method.POST;


public class Activity_View_More extends AppCompatActivity implements InternetDialog.InternetClick, NoInternetDialog.wifiOninterface {
    public static String CAT_ID, CAT_NAME;
    GridView sub_grid;
    ImageView sub_img;
    ProgressDialog pd;
    TextView sub_Title;
    List<ModelSubCategory.Image> images = new ArrayList<>();
    List<ModelSubCategory.Image> all_images = new ArrayList<>();


    ConnectionDetector cd;
    View_more_adapter subcatAdapter;
    CoordinatorLayout layout_main;
    SharedPreferences preferences;
    String json_data;
    Toolbar toolbar;
    boolean userScrolled = false;
    ProgressBar progress_view;
    NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);

        preferences = getSharedPreferences(OfflineData.MY_PREF, MODE_PRIVATE);

        init();
//        showBanner();
        CAT_ID = getIntent().getStringExtra("cat_id");
        CAT_NAME = getIntent().getStringExtra("cat_name");
        Objects.requireNonNull(getSupportActionBar()).setTitle(CAT_NAME);
        cd = new ConnectionDetector(getApplicationContext());
        json_data = preferences.getString(CAT_ID, null);
        if (OfflineData.isOffline(getApplicationContext())) {
            setAdapterOffline(json_data);
        } else if (cd.isConnectingToInternet()) {
            getOnlineData();
        } else {
            noInternetDialog = new NoInternetDialog.Builder(Activity_View_More.this).build();
            noInternetDialog.seDialogListeners(Activity_View_More.this);
            noInternetDialog.show();

        }


    }

    private void showBanner() {
//        AdView adView = findViewById(R.id.nativeadView);
//        AdRequest request = new AdRequest.Builder().build();
//        adView.loadAd(request);
    }


    @Override
    public void onBackPressed() {
//        InternetDialog.closeInternetDialog();
        if (noInternetDialog != null) {
            noInternetDialog.onDestroy();
        }
        finish();
//        Intent intent = new Intent(getApplicationContext(), ActivityImageCategory.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadData();
    }

    // initialize all view
    private void init() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sub_grid = findViewById(R.id.sub_grid);
        sub_Title = findViewById(R.id.sub_title);
        sub_img = findViewById(R.id.sub_img);
        pd = new ProgressDialog(Activity_View_More.this);
        layout_main = findViewById(R.id.layout_main);
        progress_view = findViewById(R.id.progress_view);
    }


    @Override
    public void onYesClick() {
        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), WIFI_REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WIFI_REQ_CODE) {
            if (cd.isConnectingToInternet()) {
                getOnlineData();
            }
        }
    }

    public void getOnlineData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, BASE_URL + "/images.php?category_id=" + CAT_ID, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setAdapterDataSub(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void setAdapterData() {
        subcatAdapter = new View_more_adapter(Activity_View_More.this, images);
        sub_grid.setAdapter(subcatAdapter);
        sub_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = new Intent(getApplicationContext(), PaintActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("pos", images.get(position).image);
                startActivity(intent);
                overridePendingTransition(R.anim.animate_card_enter, R.anim.animate_card_exit);
            }
        });
        sub_grid.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (images != null && all_images != null && images.size() < all_images.size()) {
                    if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount) {
                        userScrolled = false;
                        updateListView();
                    }
                }
            }
        });
    }

    public void setAdapterDataSub(String value) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        images = new ArrayList<>();
        if (value != null) {
            final ModelSubCategory subCategoryModel = gson.fromJson(value, ModelSubCategory.class);
            all_images = new ArrayList<>();
            all_images.addAll(subCategoryModel.data.images);
            if (all_images != null && all_images.size() <= PAGINATION_SIZE) {
                images.addAll(all_images);
            } else {
                for (int i = 0; i < PAGINATION_SIZE; i++) {
                    images.add(Objects.requireNonNull(all_images).get(i));
                }
            }
            setAdapterData();

        }
    }

    public void setAdapterOffline(String value) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        if (value != null) {
            List<ModelSubCategory.Image> subCategoryModel = gson.fromJson(value, new TypeToken<List<ModelSubCategory.Image>>() {
            }.getType());
            all_images = new ArrayList<>();
            all_images.addAll(subCategoryModel);
            if (all_images != null && all_images.size() <= PAGINATION_SIZE) {
                images.addAll(all_images);
            } else {
                for (int i = 0; i < PAGINATION_SIZE; i++) {
                    images.add(Objects.requireNonNull(all_images).get(i));
                }
            }
            setAdapterData();
        } else {
            sub_grid.setAdapter(null);
        }
    }

    private void updateListView() {
        progress_view.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                int last_pos = images.size();
                int total_pos = last_pos + PAGINATION_SIZE;
                if (all_images.size() < total_pos) {
                    total_pos = all_images.size();
                }
                for (int i = last_pos; i < total_pos; i++) {
                    images.add(all_images.get(i));
                }
                subcatAdapter.notifyDataSetChanged();
                progress_view.setVisibility(View.GONE);


            }
        }, 1000);

    }

    public void reloadData() {
        if (!OfflineData.isOffline(getApplicationContext())) {
            if (images == null || images.size() == 0) {
                getOnlineData();
            }
        }
    }

    @Override
    public void wifiEnabled() {
        reloadData();
    }
}
