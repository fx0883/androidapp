package com.childhealthdiet.app2.adapter;

import android.content.Context;
import android.widget.TextView;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MonthBean;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.SuperViewHolder;
import com.childhealthdiet.app2.ui.base.adapter.ListBaseAdapter;


/**
 * Created by Lzx on 2016/12/30.
 */

public class DataAdapter extends ListBaseAdapter<MonthBean> {

    public DataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recipe_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MonthBean item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.recipeName);
        titleText.setText(item.title);
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {

        super.onViewRecycled(holder);
    }

}
