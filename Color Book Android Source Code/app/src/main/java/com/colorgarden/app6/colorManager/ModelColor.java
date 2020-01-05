package com.colorgarden.app6.colorManager;

import java.util.ArrayList;
import java.util.List;


public class ModelColor {

//    public String name;
    public Integer nameResId;
    private List<ModelSetColor> setColors = new ArrayList<>();

    public List<ModelSetColor> getSetColors() {
        return setColors;
    }
}
