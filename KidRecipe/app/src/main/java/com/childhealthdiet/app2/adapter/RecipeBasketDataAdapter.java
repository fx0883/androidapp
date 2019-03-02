package com.childhealthdiet.app2.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.SuperViewHolder;
import com.childhealthdiet.app2.ui.base.adapter.ListBaseAdapter;


/**
 * Created by Lzx on 2016/12/30.
 */

public class RecipeBasketDataAdapter extends ListBaseAdapter<RecipeBean> {

    public RecipeBasketDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.basket_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RecipeBean item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.tv_basket_repice_name);
        titleText.setText(item.getName());

        TextView tvContent = holder.getView(R.id.tv_basket_repice_ingredients);
        tvContent.setText(item.getIngredients().replaceAll("<br>","\r\n"));

        TextView tvMonth = holder.getView(R.id.tv_basket_repice_month);
        tvMonth.setText(item.getMonth()+"个月");

    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
