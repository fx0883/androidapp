package com.childhealthdiet.app2.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/2/21.
 */

public class CookRecipeMethod implements Parcelable {

    private String img;
    private String step;

    public CookRecipeMethod(){

    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.img);
        dest.writeString(this.step);
    }

    protected CookRecipeMethod(Parcel in) {
        this.img = in.readString();
        this.step = in.readString();
    }

    public static final Creator<CookRecipeMethod> CREATOR = new Creator<CookRecipeMethod>() {
        @Override
        public CookRecipeMethod createFromParcel(Parcel source) {
            return new CookRecipeMethod(source);
        }

        @Override
        public CookRecipeMethod[] newArray(int size) {
            return new CookRecipeMethod[size];
        }
    };
}
