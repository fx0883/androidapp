package com.colorgarden.app6.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.adapter.CategoryImageAdapter;
import com.colorgarden.app6.model.ModelCategory;
import com.colorgarden.app6.utils.ConnectionDetector;
import com.colorgarden.app6.utils.InternetDialog;
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

import java.util.List;
import java.util.Objects;

import static com.colorgarden.app6.constant.Constant.BASE_URL;
import static com.colorgarden.app6.utils.InternetDialog.WIFI_REQ_CODE;
import static com.colorgarden.app6.utils.OfflineData.KEY_CAT;
import static com.colorgarden.app6.utils.OfflineData.setOffLine;
import static com.android.volley.Request.Method.POST;


public class ActivityImageCategory extends AppCompatActivity implements InternetDialog.InternetClick, CategoryImageAdapter.ClickInterface {
    RecyclerView grid;
    CategoryImageAdapter adapter;
    ProgressDialog pd;
    ConnectionDetector cd;
    SharedPreferences preferences;
    String json_data;
    SwitchCompat btn_offline;
    ImageView back;
    ProgressDialog progressBar;

   public static boolean interstitialCanceled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_category);

        cd = new ConnectionDetector(getApplicationContext());
        init();

        preferences = getSharedPreferences(OfflineData.MY_PREF, MODE_PRIVATE);
        json_data = preferences.getString(KEY_CAT, null);
        if (OfflineData.isOffline(getApplicationContext())) {
            setOfflineAdapter(json_data);
        } else if (cd.isConnectingToInternet()) {
            setOnlineData();
        } else {
            showOfflineDialog();
        }
        btn_offline.setChecked(OfflineData.isOffline(getApplicationContext()));
        clickListeners();
    }



    private void clickListeners() {
        btn_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOffLine(getApplicationContext(), btn_offline.isChecked());
                recreateActivity();
            }
        });
    }

    private void recreateActivity() {
        Intent intent = new Intent(getApplicationContext(), ActivityImageCategory.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        interstitialCanceled = false;
        if (getResources().getString(R.string.ADS_VISIBILITY).equals("YES")) {
            CallNewInsertial();
        }
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

        interstitialCanceled = true;
    }
    private void CallNewInsertial() {
        cd = new ConnectionDetector(ActivityImageCategory.this);




    }


    // initialize view
    private void init() {
        grid = findViewById(R.id.grid);
        btn_offline = findViewById(R.id.btn_offline);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pd = new ProgressDialog(ActivityImageCategory.this);


    }

    @Override
    public void onBackPressed() {
        InternetDialog.closeInternetDialog();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void showOfflineDialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityImageCategory.this, R.style.MyDialogTheme);
//        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityImageCategory.this);
        builder.setTitle(getResources().getString(R.string.network_err));
        builder.setMessage(getResources().getString(R.string.enable_offline_mode));
        builder.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btn_offline.performClick();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.create();
        Objects.requireNonNull(alertDialog.getWindow()).getAttributes().windowAnimations = R.style.dialog_animation_fade;
        alertDialog.show();
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
                setOnlineData();
            }
        }
    }

    @Override
    public void buttonClick(String i, String name) {
        try {
            Intent intent = new Intent(ActivityImageCategory.this, Activity_View_More.class);
            intent.putExtra("cat_id", i);
            intent.putExtra("cat_name", name);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setOnlineData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, BASE_URL + "/category.php", null, new Response.Listener < JSONObject >() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("res====", "" + response.toString());
                if (progressBar != null && progressBar.isShowing()) {
                    progressBar.dismiss();
                }
                setAdapterData(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar = new ProgressDialog(ActivityImageCategory.this);
                if (TextUtils.isEmpty(error.getMessage()) || error.getMessage() == null) {
                    progressBar.setMessage("Data Not Available");
                    progressBar.show();
                }
                Log.e("err---", "" + error.getMessage() + "===");
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }


    public void setOfflineAdapter(String json_data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List < ModelCategory.Category > categoryList;
        if (json_data != null) {
            categoryList = gson.fromJson(json_data, new TypeToken < List < ModelCategory.Category > >() {
            }.getType());
            grid.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
            adapter = new CategoryImageAdapter(ActivityImageCategory.this, categoryList);
            grid.setAdapter(adapter);
            adapter.setListeners(this);
            adapter.notifyDataSetChanged();
        } else {
            grid.setAdapter(null);
        }
    }

    public void setAdapterData(String data) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        final ModelCategory categoryModel = gson.fromJson(data, ModelCategory.class);
        grid.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        if (categoryModel.data != null) {
            if (categoryModel.data.success == 1) {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
                adapter = new CategoryImageAdapter(ActivityImageCategory.this, categoryModel.data.category);
                grid.setAdapter(adapter);
                adapter.setListeners(this);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
