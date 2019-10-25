package com.ChildHealthDiet.app2.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.model.bean.RecipeBean;
import com.ChildHealthDiet.app2.ui.base.SuperViewHolder;
import com.ChildHealthDiet.app2.ui.base.adapter.ListBaseAdapter;

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

        TextView tvMonth = holder.getView(R.id.tv_month);
        String strMonth = item.getMonth() != null ? item.getMonth()+"个月":(item.getSymptoms() != null?item.getSymptoms():"");
        tvMonth.setText(strMonth);

        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.setText(item.getPrompt());

        ImageView imvRecipe = holder.getView(R.id.imv_recipe);
        Glide.with(mContext)
                .load("file:///android_asset/recipeimage/"+item.getPicture())
                .into(imvRecipe);


        CheckBox chk = holder.getView(R.id.chkRecipeItem);
        chk.setVisibility(item.getShowCheckBox()?View.VISIBLE:View.GONE);

        chk.setChecked(item.isCanDelete);

        chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                item.isCanDelete = chk.isChecked();
            }
        });
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
