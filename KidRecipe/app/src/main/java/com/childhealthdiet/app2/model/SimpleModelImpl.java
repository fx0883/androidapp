package com.childhealthdiet.app2.model;

import android.content.Context;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.ui.base.BaseModel;

public class SimpleModelImpl implements SimpleModel {

    @Override
    public String[] loadCategoryField(Context context) {
        String[] gvCategorys = context.getResources().getStringArray(R.array.gv_category);
        return gvCategorys;
    }
}
