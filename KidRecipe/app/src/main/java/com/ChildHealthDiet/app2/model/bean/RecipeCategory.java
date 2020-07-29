package com.ChildHealthDiet.app2.model.bean;

import java.util.List;

public class RecipeCategory {
    public String name;
    public List<CategoryName> categorys;

    public class CategoryName {
        public String key;
        public String name;
    }
}
