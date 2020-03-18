package com.childhealthdiet.app2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

//import com.colorgarden.app6.model.ImagesModel;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.android.volley.Request.Method.GET;


public class AdUtils {
    private final String URL = "http://fx0883.github.io/MySite/childrenrecipe2.json";

    public final String AD_PREF = "Ad_Pref";

    public final String AppStoreName = "huawei";

    private Context mContext = null;
    //类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static AdUtils instance;

    private final String showadKey = "showadkey";

    public boolean isbIsShowAd() {
        return bIsShowAd;
    }

    public boolean bIsShowAd = false;

    //构造器私有化
    private AdUtils(Context context){
        mContext = context;
        init();
    }

    //方法同步，调用效率低
    public static AdUtils getInstance(Context context){

        if (instance == null) {
            synchronized (AdUtils.class) {
                if (instance == null) {
                    instance=new AdUtils(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public boolean getData(String key,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(showadKey,false);
    }

    public void setData(String key, boolean isShowAd, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, isShowAd);
        editor.apply();
    }

    private void init(){
        bIsShowAd = getData(showadKey,mContext);
        loadAdParam();
    }

    private void loadAdParam(){

        try{
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(GET, URL, null, new Response.Listener <JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("res====", "" + response.toString());
                    try {
//                    boolean isFirst = response.getBoolean("bisfirst");
//                    isFirst = true;
//                    com.ChildHealthDiet.app2.utils.SharedPreferencesUtil.getInstance(mContext).putSPBool("bisfirst",isFirst);

                        Boolean bShowAd=response.getBoolean(AdUtils.this.AppStoreName);
                        AdUtils.this.setData(showadKey,bShowAd,AdUtils.this.mContext);
                    }
                    catch (Exception e){
                        Log.e("error====", "" + e.getMessage());
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            requestQueue.add(jsonObjectRequest);
        }
        catch (Exception e){

        }





    }
}
