package com.ChildHealthDiet.app2.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class MonthRecipe implements Parcelable {
    public String key;
    public String title;
    public String subTitle;
    public List<String> images;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.key);
        dest.writeString(this.title);
        dest.writeString(this.subTitle);
        dest.writeStringList(this.images);

    }


    protected MonthRecipe(Parcel in) {
        this.key = in.readString();
        this.title = in.readString();
        this.subTitle = in.readString();
        this.images = new ArrayList<String>();
//        this.images = null;
        in.readStringList(this.images);
    }


    public static final Parcelable.Creator<MonthRecipe> CREATOR = new Parcelable.Creator<MonthRecipe>() {
        @Override
        public MonthRecipe createFromParcel(Parcel source) {
            return new MonthRecipe(source);
        }

        @Override
        public MonthRecipe[] newArray(int size) {
            return new MonthRecipe[size];
        }
    };
}
