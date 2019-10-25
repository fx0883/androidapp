package com.ChildHealthDiet.app2.adapter;

import android.content.Context;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.model.bean.MonthRecipe;
import com.ChildHealthDiet.app2.ui.base.SuperViewHolder;
import com.ChildHealthDiet.app2.ui.base.adapter.ListBaseAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

public class MonthRecipeDataAdapter extends ListBaseAdapter<MonthRecipe> {

    public MonthRecipeDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.month_recipe_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        MonthRecipe item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.recipeName);
        titleText.setText(item.title);

        TextView numberText = holder.getView(R.id.recipeNumber);
        numberText.setText(item.subTitle+"篇菜谱");

        RoundedImageView iv1 = holder.getView(R.id.iv_month_recipe_item1);
        RoundedImageView iv2 = holder.getView(R.id.iv_month_recipe_item2);
        RoundedImageView iv3 = holder.getView(R.id.iv_month_recipe_item3);
        RoundedImageView iv4 = holder.getView(R.id.iv_month_recipe_item4);

        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.images.get(0))
                .into(iv1);
        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.images.get(1))
                .into(iv2);
        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.images.get(2))
                .into(iv3);
        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.images.get(3))
                .into(iv4);
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {

        super.onViewRecycled(holder);
    }

}
