package com.childhealthdiet.app2.model.contract;

import android.content.Context;

import java.util.List;

public interface SimpleModel {
    String[] loadCategoryField(Context context);
    List<String> loadFieldByKey(Context context, int intKey);
}
