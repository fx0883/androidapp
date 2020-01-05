package com.colorgarden.app6.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModelSubCategory {

    @SerializedName("data")
    @Expose
    public Data data;

    public class Data {

        @SerializedName("success")
        @Expose
        public int success;
        @SerializedName("images")
        @Expose
        public List<Image> images = null;

    }

    public class Image {

        @SerializedName("image_id")
        @Expose
        public String imageId;
        @SerializedName("category_id")
        @Expose
        public String categoryId;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("image")
        @Expose
        public String image;
//        @SerializedName("created_at")
//        @Expose
//        public String createdAt;
//        @SerializedName("is_active")
//        @Expose
//        public String isActive;

    }
}
