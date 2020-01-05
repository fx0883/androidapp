package com.colorgarden.app6.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.activity.PaintActivity;
import com.colorgarden.app6.model.ModelCategory;
import com.colorgarden.app6.model.ModelSubCategory;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static com.colorgarden.app6.constant.Constant.BASE_URL;
import static com.colorgarden.app6.constant.Constant.HOME_IMG_SIZE;
import static com.colorgarden.app6.utils.InternetDialog.WIFI_REQ_CODE;
import static com.android.volley.Request.Method.POST;


public class CategoryImageAdapter extends RecyclerView.Adapter < CategoryImageAdapter.MyViewHolder > implements InternetDialog.InternetClick, Sub_cat_Adapter_New.ClickInterface {
    private static List < ModelCategory.Category > imgList1;
    String Mainposition = "";
    private Activity context;
    private ClickInterface interfcaeobj;
    private List < ModelSubCategory.Image > images = new ArrayList <>();
    private Sub_cat_Adapter_New subcatAdapter;
    private ConnectionDetector cd;
    private SharedPreferences preferences;

    public CategoryImageAdapter(Activity context, List < ModelCategory.Category > imgList) {
        this.context = context;
        imgList1 = imgList;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        cd = new ConnectionDetector(context);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new FadeInBitmapDisplayer(300)).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(defaultOptions).build();

        ImageLoader.getInstance().init(config);
        preferences = context.getSharedPreferences(OfflineData.MY_PREF, MODE_PRIVATE);


    }

    public static boolean contains(List < ModelCategory.Category > list, String name) {
        for (ModelCategory.Category item : list) {
            if (item.categoryId.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static ModelCategory.Category getCategoryById(String ids) {
        if (imgList1 != null) {
            for (int i = 0; i < imgList1.size(); i++) {
                if (imgList1.get(i).categoryId.equals(ids)) {
                    return imgList1.get(i);
                }
            }
        }
        return null;
    }

    public void setListeners(ClickInterface listeners) {
        interfcaeobj = listeners;
    }

    @NonNull
    @Override
    public CategoryImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_image_category_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        String pref_id = imgList1.get(position).categoryId;
        String json_data = preferences.getString(pref_id, null);

        holder.tv_cat.setText(imgList1.get(position).name);
        holder.tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interfcaeobj != null) {
                    interfcaeobj.buttonClick(imgList1.get(position).categoryId, imgList1.get(position).name);
                }
            }
        });
        holder.rec_cat.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        if (OfflineData.isOffline(context)) {
            setOfflineAdapterDataSub(json_data, holder.rec_cat, holder.rec_progress);
        } else if (cd.isConnectingToInternet()) {
            getOnlineDatas(pref_id, holder.rec_cat, holder.rec_progress);
        } else {
            InternetDialog.ShowInternetDialog(context, this);

        }

    }

    @Override
    public int getItemCount() {
        return imgList1.size();
    }

    @Override
    public void onYesClick() {
        context.startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), WIFI_REQ_CODE);

    }

    @Override
    public void recItemClick(View view, String i) {
        Mainposition = i;
//        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
//            mInterstitialAd.show();
//            mInterstitialAd.setAdListener(new AdListener() {
//                public void onAdClosed() {
//                    continueintent();
//
//                }
//            });
//        } else {
//            continueintent();
//        }


        continueintent();
    }

    private void continueintent() {
        Intent intent = new Intent(context, PaintActivity.class);
        intent.putExtra("pos", Mainposition);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.animate_card_enter, R.anim.animate_card_exit);
    }

    private void getOnlineDatas(String str_id, final RecyclerView recyclerView, final ProgressBar bar) {
        bar.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(POST, BASE_URL + "/images.php?category_id=" + str_id, null, new Response.Listener < JSONObject >() {
            @Override
            public void onResponse(JSONObject response) {
                bar.setVisibility(View.GONE);
                setAdapterDataSub(response.toString(), recyclerView, bar);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    private void setAdapterDataSub(String value, RecyclerView recyclerView, ProgressBar bar) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        if (value != null) {
            if (bar.getVisibility() == View.VISIBLE) {
                bar.setVisibility(View.GONE);
            }
            final ModelSubCategory subCategoryModel = gson.fromJson(value, ModelSubCategory.class);
            images = new ArrayList <>();
            if (subCategoryModel.data.images != null && subCategoryModel.data.images.size() <= HOME_IMG_SIZE) {
                images.addAll(subCategoryModel.data.images);
            } else {
                for (int i = 0; i < HOME_IMG_SIZE; i++) {
                    images.add(Objects.requireNonNull(subCategoryModel.data.images).get(i));
                }
            }

            subcatAdapter = new Sub_cat_Adapter_New(context, images);
            recyclerView.setAdapter(subcatAdapter);
            subcatAdapter.setListeners(this);

        }
    }

    private void setOfflineAdapterDataSub(String value, RecyclerView recyclerView, ProgressBar bar) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        if (value != null) {
            if (bar.getVisibility() == View.VISIBLE) {
                bar.setVisibility(View.GONE);
            }

            List < ModelSubCategory.Image > sub_img_list = gson.fromJson(value, new TypeToken < List < ModelSubCategory.Image > >() {
            }.getType());
            images = new ArrayList <>();
            if (sub_img_list != null && sub_img_list.size() <= HOME_IMG_SIZE) {
                images.addAll(sub_img_list);
            } else {
                for (int i = 0; i < HOME_IMG_SIZE; i++) {
                    if (sub_img_list != null) {
                        images.add(sub_img_list.get(i));
                    }
                }
            }

            subcatAdapter = new Sub_cat_Adapter_New(context, images);
            recyclerView.setAdapter(subcatAdapter);
            subcatAdapter.setListeners(this);

        } else {
            recyclerView.setAdapter(null);
        }
    }

    public interface ClickInterface {

        void buttonClick(String i, String name);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rec_cat;
        TextView tv_cat, tv_more;
        ProgressBar rec_progress;

        MyViewHolder(View itemView) {
            super(itemView);
            rec_cat = itemView.findViewById(R.id.rec_cat);
            tv_more = itemView.findViewById(R.id.tv_more);
            tv_cat = itemView.findViewById(R.id.tv_cat);
            rec_progress = itemView.findViewById(R.id.rec_progress);
        }

    }

}
