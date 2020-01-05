package com.colorgarden.app6.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.colorgarden.app6.R;
import com.colorgarden.app6.constant.Constant;
import com.colorgarden.app6.model.ModelCategory;
import com.colorgarden.app6.model.ModelSubCategory;
import com.colorgarden.app6.utils.OfflineData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import static com.colorgarden.app6.adapter.CategoryImageAdapter.contains;
import static com.colorgarden.app6.adapter.CategoryImageAdapter.getCategoryById;
import static com.colorgarden.app6.constant.Constant.THUMB_URL;
import static com.colorgarden.app6.constant.Constant.UPLOAD_URL;
import static com.colorgarden.app6.constant.Constant.getBitmapFromURL;
import static com.colorgarden.app6.constant.Constant.getSavedCatImgPath;
import static com.colorgarden.app6.constant.Constant.getSavedImgPath;
import static com.colorgarden.app6.constant.Constant.imgSavedOrNot;
import static com.colorgarden.app6.utils.OfflineData.KEY_CAT;
import static com.colorgarden.app6.utils.OfflineData.MY_PREF;


public class Sub_cat_Adapter_New extends RecyclerView.Adapter<Sub_cat_Adapter_New.MyViewHolder> {
    private Context context;
    private List<ModelSubCategory.Image> item_data;
    private ImageLoader imageLoader;
    private ClickInterface interface_obj;
    private SharedPreferences preferences;


    Sub_cat_Adapter_New(Context context, List<ModelSubCategory.Image> item_data) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        this.context = context;
        this.item_data = item_data;
        imageLoader = ImageLoader.getInstance();
        preferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
    }

    void setListeners(ClickInterface listeners) {
        interface_obj = listeners;
    }

    @NonNull
    @Override
    public Sub_cat_Adapter_New.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vi = LayoutInflater.from(context).inflate(R.layout.subcat_item_new, viewGroup, false);
        return new MyViewHolder(vi);
    }

    @Override
    public void onBindViewHolder(@NonNull final Sub_cat_Adapter_New.MyViewHolder holder, @SuppressLint("RecyclerView") final int i) {
        if (imgSavedOrNot(item_data.get(i).image, getSavedCatImgPath(context))) {
            holder.img_download.setVisibility(View.GONE);
        } else {
            holder.img_download.setVisibility(View.VISIBLE);
        }

        if (OfflineData.isOffline(context)) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(getSavedCatImgPath(context) + item_data.get(i).image);
                holder.img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("img_not_found", "" + e.getMessage());


            }

//            imageLoader.displayImage(Constant.getSavedCatImgPath(context) + item_data.get(i).image, holder.img, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String imageUri, View view) {
//                    holder.progress.setVisibility(View.VISIBLE);
//
//                }
//
//                @Override
//                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                    Log.e("img_not_found", "" +failReason.getCause().getMessage());
//                }
//
//                @Override
//                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                    holder.progress.setVisibility(View.VISIBLE);
//
//                }
//
//                @Override
//                public void onLoadingCancelled(String imageUri, View view) {
//
//                }
//            });
        } else {
            imageLoader.displayImage(THUMB_URL + item_data.get(i).image, holder.img, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    holder.progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    Log.e("img_not_found", "" + failReason.getCause().getMessage());
                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }
        holder.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = preferences.getString(KEY_CAT, null);
                Gson gson = new Gson();
                String cat_id = item_data.get(i).categoryId;
                List<ModelCategory.Category> categoryList = new ArrayList<>();
                if (data != null) {
                    categoryList = gson.fromJson(data, new TypeToken<List<ModelCategory.Category>>() {
                    }.getType());

                }
                if (getCategoryById(cat_id) != null) {
                    ModelCategory.Category category = getCategoryById(cat_id);
                    if (!contains(categoryList, cat_id)) {
                        categoryList.add(category);
                        String list = gson.toJson(categoryList);
                        OfflineData.setData(KEY_CAT, list, context);
                    }
                }


                List<ModelSubCategory.Image> imageList = new ArrayList<>();
                String sub_data = preferences.getString(cat_id, null);
                if (sub_data != null) {
                    imageList = gson.fromJson(sub_data, new TypeToken<List<ModelSubCategory.Image>>() {
                    }.getType());
                }
                imageList.add(item_data.get(i));
                String img_name = item_data.get(i).image;
                Bitmap bitmap = getBitmapFromURL(THUMB_URL + img_name);
                Bitmap bitmap_org = getBitmapFromURL(UPLOAD_URL + img_name);
                Constant.saveImages(img_name, getSavedCatImgPath(context), bitmap);
                Constant.saveImages(img_name, getSavedImgPath(context), bitmap_org);
                String sub_list = gson.toJson(imageList);
                OfflineData.setData(cat_id, sub_list, context);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return item_data.size();
    }


    public interface ClickInterface {
        void recItemClick(View view, String i);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ProgressBar progress;
        ImageView img, img_download;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            progress = itemView.findViewById(R.id.progress);
            img = itemView.findViewById(R.id.subcat_img);
            img_download = itemView.findViewById(R.id.img_download);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (interface_obj != null) {
                interface_obj.recItemClick(v, item_data.get(getAdapterPosition()).image);
            }
        }
    }


}
