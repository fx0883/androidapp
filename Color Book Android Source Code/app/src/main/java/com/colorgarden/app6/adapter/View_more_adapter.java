package com.colorgarden.app6.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.colorgarden.app6.R;
import com.colorgarden.app6.constant.Constant;
import com.colorgarden.app6.model.ModelCategory;
import com.colorgarden.app6.model.ModelSubCategory;
import com.colorgarden.app6.utils.ConnectionDetector;
import com.colorgarden.app6.utils.OfflineData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.colorgarden.app6.constant.Constant.BASE_URL;
import static com.colorgarden.app6.constant.Constant.THUMB_URL;
import static com.colorgarden.app6.constant.Constant.UPLOAD_URL;
import static com.colorgarden.app6.constant.Constant.getBitmapFromURL;
import static com.colorgarden.app6.constant.Constant.getSavedCatImgPath;
import static com.colorgarden.app6.constant.Constant.getSavedImgPath;
import static com.colorgarden.app6.constant.Constant.imgSavedOrNot;
import static com.colorgarden.app6.utils.OfflineData.KEY_CAT;
import static com.colorgarden.app6.utils.OfflineData.MY_PREF;


public class View_more_adapter extends BaseAdapter {
    private Context context;
    private List < ModelSubCategory.Image > item_data;
    private LayoutInflater inflater;
    private List < ModelCategory.Category > categories = new ArrayList <>();
    private SharedPreferences preferences;

    public View_more_adapter(Context context, List < ModelSubCategory.Image > item_data) {
        this.context = context;
        this.item_data = item_data;
        preferences = context.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        if (connectionDetector.isConnectingToInternet()) {
            new GetDataOnline().execute();
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return item_data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private boolean contains(List < ModelCategory.Category > list, String name) {
        for (ModelCategory.Category item : list) {
            if (item.categoryId.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View vi = convertView;
        if (convertView == null) {
            vi = inflater.inflate(R.layout.subcat_item, viewGroup, false);
        }
        final ProgressBar progress = vi.findViewById(R.id.progress);
        ImageView img = vi.findViewById(R.id.subcat_img);
        ImageView img_download = vi.findViewById(R.id.img_download);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (imgSavedOrNot(item_data.get(position).image, getSavedCatImgPath(context))) {
            img_download.setVisibility(View.GONE);
        } else {
            img_download.setVisibility(View.VISIBLE);
        }
        if (OfflineData.isOffline(context)) {
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(getSavedCatImgPath(context) + item_data.get(position).image);
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("img_not_found", "" + e.getMessage());


            }
        } else {
            imageLoader.displayImage(THUMB_URL + item_data.get(position).image, img, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    progress.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    progress.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
        }

        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = preferences.getString(KEY_CAT, null);
                String sub_cat_id = item_data.get(position).categoryId;
                Gson gson = new Gson();
                List < ModelCategory.Category > categoryList = new ArrayList <>();
                if (data != null) {
                    categoryList = gson.fromJson(data, new TypeToken < List < ModelCategory.Category > >() {
                    }.getType());

                }
                if (getCategoryById(sub_cat_id) != null) {
                    ModelCategory.Category category = getCategoryById(sub_cat_id);
                    if (!contains(categoryList, sub_cat_id)) {
                        categoryList.add(category);
                        String list = gson.toJson(categoryList);
                        OfflineData.setData(KEY_CAT, list, context);
                    }
                }


                List < ModelSubCategory.Image > imageList = new ArrayList <>();
                String sub_data = preferences.getString(sub_cat_id, null);
                if (sub_data != null) {
                    imageList = gson.fromJson(sub_data, new TypeToken < List < ModelSubCategory.Image > >() {
                    }.getType());
                }
                imageList.add(item_data.get(position));
                Bitmap bitmap = getBitmapFromURL(THUMB_URL + item_data.get(position).image);
                Bitmap bitmap_org = getBitmapFromURL(UPLOAD_URL + item_data.get(position).image);
                Constant.saveImages(item_data.get(position).image, getSavedCatImgPath(context), bitmap);
                Constant.saveImages(item_data.get(position).image, getSavedImgPath(context), bitmap_org);
                String sub_list = gson.toJson(imageList);
                OfflineData.setData(sub_cat_id, sub_list, context);
                notifyDataSetChanged();
            }
        });
        return vi;
    }


    private ModelCategory.Category getCategoryById(String ids) {
        if (categories != null) {
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).categoryId.equals(ids)) {
                    return categories.get(i);
                }
            }
        }
        return null;
    }

    @SuppressLint("StaticFieldLeak")
    public class GetDataOnline extends AsyncTask < Void, Void, String > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(BASE_URL + "/category.php");
            HttpResponse httpResponse;
            HttpEntity httpEntity = null;
            String res;

            try {
                httpResponse = httpClient.execute(httpGet);
                httpEntity = httpResponse.getEntity();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpEntity != null) {
                try {
                    res = EntityUtils.toString(httpEntity);
                    return res;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                final ModelCategory categoryModel = gson.fromJson(aVoid, ModelCategory.class);
                categories.addAll(categoryModel.data.category);
            }
        }
    }


}
