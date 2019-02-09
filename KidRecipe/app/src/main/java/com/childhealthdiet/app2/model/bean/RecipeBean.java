package com.childhealthdiet.app2.model.bean;



import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity(
        nameInDb = "childrenfood",
        createInDb = false
)
public class RecipeBean {
    //public class RecipeBean {
    @Property(nameInDb = "id")
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String ingredients;
    private String practice;
    private String prompt;

    private String symptoms;
    private String month;
    private String lock;
    private String eattime;
    private String type;

    private String picture;
    private String material;
    private Boolean collect;
    private Boolean basket;
    private Boolean bedit;

    @Transient
    public Boolean showCheckBox = false;
    @Transient
    public Boolean isCanDelete = false;
@Generated(hash = 2062217939)
public RecipeBean(Long id, String name, String ingredients, String practice,
        String prompt, String symptoms, String month, String lock,
        String eattime, String type, String picture, String material,
        Boolean collect, Boolean basket, Boolean bedit) {
    this.id = id;
    this.name = name;
    this.ingredients = ingredients;
    this.practice = practice;
    this.prompt = prompt;
    this.symptoms = symptoms;
    this.month = month;
    this.lock = lock;
    this.eattime = eattime;
    this.type = type;
    this.picture = picture;
    this.material = material;
    this.collect = collect;
    this.basket = basket;
    this.bedit = bedit;
}
@Generated(hash = 1492456445)
public RecipeBean() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getName() {
    return this.name;
}
public void setName(String name) {
    this.name = name;
}
public String getIngredients() {
    return this.ingredients;
}
public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
}
public String getPractice() {
    return this.practice;
}
public void setPractice(String practice) {
    this.practice = practice;
}
public String getPrompt() {
    return this.prompt;
}
public void setPrompt(String prompt) {
    this.prompt = prompt;
}
public String getSymptoms() {
    return this.symptoms;
}
public void setSymptoms(String symptoms) {
    this.symptoms = symptoms;
}
public String getMonth() {
    return this.month;
}
public void setMonth(String month) {
    this.month = month;
}
public String getLock() {
    return this.lock;
}
public void setLock(String lock) {
    this.lock = lock;
}
public String getEattime() {
    return this.eattime;
}
public void setEattime(String eattime) {
    this.eattime = eattime;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getPicture() {
    return this.picture;
}
public void setPicture(String picture) {
    this.picture = picture;
}
public String getMaterial() {
    return this.material;
}
public void setMaterial(String material) {
    this.material = material;
}
public Boolean getCollect() {
    return this.collect;
}
public void setCollect(Boolean collect) {
    this.collect = collect;
}
public Boolean getBasket() {
    return this.basket;
}
public void setBasket(Boolean basket) {
    this.basket = basket;
}
public Boolean getBedit() {
    return this.bedit;
}
public void setBedit(Boolean bedit) {
    this.bedit = bedit;
}

}
