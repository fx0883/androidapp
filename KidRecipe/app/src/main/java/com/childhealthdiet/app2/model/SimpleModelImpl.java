package com.childhealthdiet.app2.model;

import android.content.Context;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.ui.base.BaseModel;

import java.util.Arrays;
import java.util.List;

public class SimpleModelImpl implements SimpleModel {

    @Override
    public String[] loadCategoryField(Context context) {
        String[] gvCategorys = context.getResources().getStringArray(R.array.gv_category);
        return gvCategorys;
    }
    @Override
    public List<String> loadFieldByKey(Context context, int intKey){
        String[] strs = context.getResources().getStringArray(intKey);
        return Arrays.asList(strs);
    }
}
