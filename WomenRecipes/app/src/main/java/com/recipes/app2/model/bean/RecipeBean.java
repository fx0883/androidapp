package com.recipes.app2.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity(
        nameInDb = "recipe",
        createInDb = false
)
public class RecipeBean {
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

}
