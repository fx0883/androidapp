package com.recipes.app2.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.recipes.app2.R;
import com.recipes.app2.activitys.CookDetailActivity;
import com.recipes.app2.model.bean.RecipeBean;
import com.squareup.picasso.Picasso;

import java.util.List;





public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>{
    private List<RecipeBean> recipes = null;
    public Activity activity = null;
    public ImageView iv = null;



    public RecipeListAdapter(List<RecipeBean> list) {
        recipes=list;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_recipe, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecipeViewHolder recipeViewHolder, final int position) {
        RecipeBean recipe = recipes.get(position);
        String recipeUrl = "file:///android_asset/recipesImage/" + recipe.getPhoto();


        Picasso.with(recipeViewHolder.itemView.getContext())
                .load(recipeUrl)
                .into(recipeViewHolder.ivRecipe);
        recipeViewHolder.tvTitle.setText(recipes.get(position).getName());


        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                // 点击事件
                //Toast.makeText(recipeViewHolder.itemView.getContext(), position + "", 1000).show();
                CookDetailActivity.startActivity(activity, recipeViewHolder.ivRecipe, recipes.get(position), true);
            }
        });


    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(recipes != null)
        {
            count = recipes.size();
        }
        return count;
    }

    public static class RecipeViewHolder extends  RecyclerView.ViewHolder{
        ImageView ivRecipe;
        TextView tvTitle;
        View curView;
        public RecipeViewHolder(View itemView){
            super(itemView);
            ivRecipe= (ImageView) itemView.findViewById(R.id.recipe_image );
            tvTitle= (TextView) itemView.findViewById(R.id.title_text);
            curView = itemView;
        }
    }

//    @Override protected vxoid onDestroy() {
//        super.onDestroy();
//        disposables.clear();
//    }

    public void updateRecipe(List<RecipeBean> list) {
        recipes=list;
        this.notifyDataSetChanged();
    }
}