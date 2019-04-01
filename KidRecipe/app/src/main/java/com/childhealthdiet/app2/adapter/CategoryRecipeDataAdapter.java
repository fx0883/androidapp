package com.ChildHealthDiet.app2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.model.bean.KeyValueBean;
import com.ChildHealthDiet.app2.model.bean.RecipeCategory;
import com.ChildHealthDiet.app2.ui.activitys.RecipeKeywordListActivity;
import com.ChildHealthDiet.app2.ui.base.SuperViewHolder;
import com.ChildHealthDiet.app2.ui.base.adapter.ListBaseAdapter;
import com.ChildHealthDiet.app2.ui.categorys.RECIPETYPE;
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
//        mFlowLayout.setMaxSelectCount(3);
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
//                Toast.makeText(mContext, mDataList.get(position).categorys.get(p).key, Toast.LENGTH_SHORT).show();
                //view.setVisibility(View.GONE);
                String strKey = item.categorys.get(p).key;
                switch (position){
                    case 0:
                        RecipeKeywordListActivity.startActivity(mContext,RECIPETYPE.Eattime,strKey);
                        break;
                    case 1:
                        KeyValueBean keyValueBean = new KeyValueBean(strKey,item.categorys.get(p).name+"宝宝食谱");
                        RecipeKeywordListActivity.startActivity(mContext,RECIPETYPE.Month,keyValueBean);
                        break;
                    case 2:
                    case 3:
                        RecipeKeywordListActivity.startActivity(mContext,RECIPETYPE.Symptoms,strKey);
                        break;

                    case 4:
                    case 5:
                    case 6:
                        RecipeKeywordListActivity.startActivity(mContext,RECIPETYPE.Ingredients,strKey);
                        break;
                }
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
