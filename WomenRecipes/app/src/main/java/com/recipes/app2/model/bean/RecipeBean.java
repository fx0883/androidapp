package com.recipes.app2.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "recipe",
        createInDb = false
)
public class RecipeBean implements Parcelable {
    private int id;
    private String name;
    private String ingredients;
    private String steps;
    private String prompt;

    private String photo;
    private String suberphoto;
    private String type;
    private String month;
    private String subername;
@Generated(hash = 967799467)
public RecipeBean(int id, String name, String ingredients, String steps,
        String prompt, String photo, String suberphoto, String type,
        String month, String subername) {
    this.id = id;
    this.name = name;
    this.ingredients = ingredients;
    this.steps = steps;
    this.prompt = prompt;
    this.photo = photo;
    this.suberphoto = suberphoto;
    this.type = type;
    this.month = month;
    this.subername = subername;
}
@Generated(hash = 1492456445)
public RecipeBean() {
}
public int getId() {
    return this.id;
}
public void setId(int id) {
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
public String getSteps() {
    return this.steps;
}
public void setSteps(String steps) {
    this.steps = steps;
}
public String getPrompt() {
    return this.prompt;
}
public void setPrompt(String prompt) {
    this.prompt = prompt;
}
public String getPhoto() {
    return this.photo;
}
public void setPhoto(String photo) {
    this.photo = photo;
}
public String getSuberphoto() {
    return this.suberphoto;
}
public void setSuberphoto(String suberphoto) {
    this.suberphoto = suberphoto;
}
public String getType() {
    return this.type;
}
public void setType(String type) {
    this.type = type;
}
public String getMonth() {
    return this.month;
}
public void setMonth(String month) {
    this.month = month;
}
public String getSubername() {
    return this.subername;
}
public void setSubername(String subername) {
    this.subername = subername;
}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.ingredients);
        dest.writeString(this.steps);
        dest.writeString(this.prompt);

        dest.writeString(this.photo);
        dest.writeString(this.suberphoto);
        dest.writeString(this.type);
        dest.writeString(this.month);
        dest.writeString(this.subername);
    }

    protected RecipeBean(Parcel in) {

        this.id = in.readInt();
        this.name = in.readString();;
        this.ingredients = in.readString();;
        this.steps = in.readString();;
        this.prompt = in.readString();;
        this.photo = in.readString();;
        this.suberphoto = in.readString();;
        this.type = in.readString();;
        this.month = in.readString();;
        this.subername = in.readString();;
    }

    public static final Parcelable.Creator<RecipeBean> CREATOR = new Parcelable.Creator<RecipeBean>() {
        @Override
        public RecipeBean createFromParcel(Parcel source) {
            return new RecipeBean(source);
        }

        @Override
        public RecipeBean[] newArray(int size) {
            return new RecipeBean[size];
        }
    };


///    protected RecipeBean(Parcel in) {
//        this.ctgIds = in.createStringArrayList();
//        this.ctgTitles = in.readString();
//        this.menuId = in.readString();
//        this.name = in.readString();
//        this.recipe = in.readParcelable(CookRecipe.class.getClassLoader());
//        this.thumbnail = in.readString();
//    }
}
