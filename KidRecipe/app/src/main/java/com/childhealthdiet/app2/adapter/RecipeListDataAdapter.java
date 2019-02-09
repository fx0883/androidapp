package com.childhealthdiet.app2.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.SuperViewHolder;
import com.childhealthdiet.app2.ui.base.adapter.ListBaseAdapter;
import com.makeramen.roundedimageview.RoundedImageView;


/**
 * Created by Lzx on 2016/12/30.
 */

public class RecipeListDataAdapter extends ListBaseAdapter<RecipeBean> {

    public RecipeListDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.recipe_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RecipeBean item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.tv_title);
        titleText.setText(item.getName());

        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.setText(item.getPrompt());

        ImageView imvRecipe = holder.getView(R.id.imv_recipe);
        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.getPicture())
                .into(imvRecipe);
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
