package com.childhealthdiet.app2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.ui.base.SuperViewHolder;
import com.childhealthdiet.app2.ui.base.adapter.ListBaseAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.Set;


/**
 * Created by Lzx on 2016/12/30.
 */

public class CategoryRecipeDataAdapter extends ListBaseAdapter<RecipeCategory> {

    public CategoryRecipeDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.category_recipe_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        RecipeCategory item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.id_categorys_name);
        titleText.setText(item.name);

        final LayoutInflater mInflater = LayoutInflater.from(mContext);
        TagFlowLayout mFlowLayout = (TagFlowLayout) holder.getView(R.id.id_categorys_flowlayout);
        mFlowLayout.setMaxSelectCount(3);
        mFlowLayout.setAdapter(new TagAdapter<RecipeCategory.CategoryName>(item.categorys)
        {

            @Override
            public View getView(FlowLayout parent, int position, RecipeCategory.CategoryName s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.tag_tv,
                        mFlowLayout, false);
                tv.setText(s.name);
                return tv;
            }

            @Override
            public boolean setSelected(int position, RecipeCategory.CategoryName s)
            {
                return false;
            }
        });

        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int p, FlowLayout parent)
            {
                Toast.makeText(mContext, mDataList.get(position).categorys.get(p).key, Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                return true;
            }
        });
//
//
        mFlowLayout.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                getActivity().setTitle("choose:" + selectPosSet.toString());
            }
        });
    }

    @Override
    public void onViewRecycled(SuperViewHolder holder) {

        super.onViewRecycled(holder);
    }

}
