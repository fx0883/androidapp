package com.ChildHealthDiet.app2.model.bean;


import android.os.Parcel;
import android.os.Parcelable;

public class KeyValueBean implements Parcelable {

    String key;
    String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.key);
        dest.writeString(this.value);
    }
    protected KeyValueBean(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
    }

    public KeyValueBean(String strKey,String strValue) {
        this.key = strKey;
        this.value =strValue;
    }

    public static final Parcelable.Creator<KeyValueBean> CREATOR = new Parcelable.Creator<KeyValueBean>() {
        @Override
        public KeyValueBean createFromParcel(Parcel source) {
            return new KeyValueBean(source);
        }

        @Override
        public KeyValueBean[] newArray(int size) {
            return new KeyValueBean[size];
        }
    };
}
