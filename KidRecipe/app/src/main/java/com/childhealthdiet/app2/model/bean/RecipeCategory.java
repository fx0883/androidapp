package com.childhealthdiet.app2.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecipeCategory {
    public String name;
    public List<CategoryName> categorys;

    public class CategoryName {
        public String key;
        public String name;
    }
}
