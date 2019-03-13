package com.childhealthdiet.app2.context;

import android.content.Context;

import com.childhealthdiet.app2.utils.SPUtils;

public class UserContext {
    final String nicknameKey = "nicknameKey";
    final String birthdayKey = "brithdayKey";

    private volatile static UserContext singleton;

    private UserContext() {
    }

    public static UserContext getInstance() {

        if (singleton == null) {

            synchronized (UserContext.class) {

                if (singleton == null) {

                    singleton = new UserContext();
                }
            }
        }
        return  singleton;
    }

    public Kidinfo getmKidinfo(Context context) {
        if(mKidinfo == null){
            mKidinfo = new Kidinfo("","");
        }
        mKidinfo.setNickName((String) SPUtils.get(context,nicknameKey,""));
        mKidinfo.setBirthdate((String)SPUtils.get(context,birthdayKey,""));
        return mKidinfo;
    }

    public void setmKidinfo(Kidinfo mKidinfo) {
        this.mKidinfo = mKidinfo;
    }

    public Kidinfo getmKidinfo() {
        if(mKidinfo == null){
            mKidinfo = new Kidinfo("","");
        }
        return mKidinfo;
    }

    public void setmKidinfo(Context context,String strNickName,String strBirthday) {
        if(!strNickName.equals("")){
            SPUtils.put(context,nicknameKey,strNickName);
        }
        if(!strBirthday.equals("")){
            SPUtils.put(context,birthdayKey,strBirthday);
        }

    }


    private Kidinfo mKidinfo = null;
}