package com.ChildHealthDiet.app2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AdUtils {
    private final String URL = "http://fx0883.github.io/MySite/kidrecipe_mi_ad.json";

    public final String AD_PREF = "Ad_Pref";

    public final String AppStoreName = "xiaomi";

    private Context mContext = null;
    //类初始化时，不初始化这个对象(延时加载，真正用的时候再创建)
    private static AdUtils instance;

    private final String showadKey = "showadkey";

    private final String giveCommentKey = "givecommentkey";

    private final String startTimeKey = "starttimekey";

    public boolean isbIsShowAd() {
        return bIsShowAd;
    }

    public boolean bIsShowAd = false;

    public boolean bIsGiveComment = false;

    private final int showGiveCommentTimes = 6;

    private int curStartTime = 0;

    private final String bannerPosKey = "bannerPosKey";
    private final String interteristalPosKey = "interteristalPosKey";

    public boolean isbIsShowBanner() {
        return bIsShowBanner;
    }

    public boolean isbIsShowInterteristal() {
        Boolean bret = bIsShowInterteristal;
        if (bret == true){
            bret = canShowAd();
        }
        return bret;
    }

    long start = 0;
    long min = 1;

    private final String minKey = "minKey";



    public Boolean canShowAd(){
        long end = System.currentTimeMillis();
        long minute = (end - start) / (1000 * 60);
        Boolean bIsRet = false;
        if(minute > min){
            start = end;
            bIsRet = true;
        }
        return bIsRet;
    }


    private boolean bIsShowBanner = false;

    private boolean bIsShowInterteristal = false;
    //构造器私有化
    private AdUtils(Context context){
        mContext = context;
        init();
    }

    public boolean getBIsGiveComment(){
        bIsGiveComment = getData(giveCommentKey,mContext);
        curStartTime = getDataInt(startTimeKey,mContext);
        return bIsGiveComment || (curStartTime > showGiveCommentTimes);
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


    public int getDataInt(String key,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        return preferences.getInt(key,0);
    }

    public void setDataInt(String key, int number, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, number);
        editor.apply();
    }


    public boolean getData(String key,Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean(key,false);
    }

    public void setData(String key, boolean isShowAd, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AD_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, isShowAd);
        editor.apply();
    }

    private void init(){
        bIsShowAd = getData(showadKey,mContext);
        bIsGiveComment = getData(giveCommentKey,mContext);
        curStartTime = getDataInt(startTimeKey,mContext);
        curStartTime++;
        setDataInt(startTimeKey,curStartTime,mContext);
        bIsShowBanner = getData(bannerPosKey,mContext);
        bIsShowInterteristal = getData(interteristalPosKey,mContext);


        loadAdParam();
    }

    public void setGiveComment(){

        this.setData(giveCommentKey,true,mContext);
    }

    private void loadAdParam(){
        //异步请求
        OkHttpClient okHttpClient=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(URL)
                .get()
                .build();
        final Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("okhttp_error",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response)  {
//                Gson gson=new Gson();



                //获取数据
                try {
                    String responseData=response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);

                    JSONObject curJsonObject = jsonObject.getJSONObject(AdUtils.this.AppStoreName);

//                    Boolean bShowAd=jsonObject.getBoolean(AdUtils.this.AppStoreName);
//                    AdUtils.this.setData(showadKey,bShowAd,AdUtils.this.mContext);
                    Boolean bshowBanner = curJsonObject.getBoolean(bannerPosKey);
                    Boolean bShowInterteristal = curJsonObject.getBoolean(interteristalPosKey);

                    AdUtils.this.min = jsonObject.getLong(AdUtils.this.minKey);

                    AdUtils.this.setData(bannerPosKey,bshowBanner,AdUtils.this.mContext);
                    AdUtils.this.setData(interteristalPosKey,bShowInterteristal,AdUtils.this.mContext);
                } catch (Exception e) {
                    e.printStackTrace();
                }



//                Log.d("okhttp_success",response.body().string());
            }
        });
    }
}
