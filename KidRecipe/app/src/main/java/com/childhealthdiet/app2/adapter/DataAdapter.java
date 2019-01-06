package com.childhealthdiet.app2.adapter;

import android.content.Context;
import android.widget.TextView;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.ui.base.SuperViewHolder;
import com.childhealthdiet.app2.ui.base.adapter.ListBaseAdapter;


/**
 * Created by Lzx on 2016/12/30.
 */

public class DataAdapter extends ListBaseAdapter<MonthRecipe> {

    public DataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recipe_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MonthRecipe item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.recipeName);
        titleText.setText(item.title);

        TextView numberText = holder.getView(R.id.recipeNumber);
        numberText.setText(item.subTitle+"篇菜谱");
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {

        super.onViewRecycled(holder);
    }

}
