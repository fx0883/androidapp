package com.mihwapp.womanrecipe.view.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mihwapp.womanrecipe.R;
import com.mihwapp.womanrecipe.model.bean.RecipeBean;
import com.mihwapp.womanrecipe.model.services.RecipeService;
import com.bumptech.glide.Glide;
//import com.squareup.picasso.Picasso;

import java.util.List;







public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder>  {
    public List<RecipeBean> recipes = null;
    public Activity activity = null;
    public ImageView iv = null;
    public OnItemClickListener onItemClickListener = null;
    public View.OnLongClickListener onLongClickListener = null;


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
        final RecipeBean recipe = recipes.get(position);
        String recipeUrl = "file:///android_asset/recipesImage/" + recipe.getPhoto();


//        Picasso.with(recipeViewHolder.itemView.getContext())
//                .load(recipeUrl)
//                .into(recipeViewHolder.ivRecipe);

        Glide.with(recipeViewHolder.itemView.getContext())
                .load(recipeUrl)
                .into(recipeViewHolder.ivRecipe);
        recipeViewHolder.tvTitle.setText(recipes.get(position).getName());


        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                CookDetailActivity.startActivity(activity, recipeViewHolder.ivRecipe, recipes.get(position), true);

                if(onItemClickListener!=null){
                    onItemClickListener.onItemClick(recipeViewHolder.ivRecipe,position);
                }
            }
        });

//        recipeViewHolder.itemView.setOnLongClickListener(this);
        if(onLongClickListener!=null){
            recipeViewHolder.itemView.setOnLongClickListener(onLongClickListener);
        }

        recipeViewHolder.chk.setVisibility(recipe.getShowCheckBox()?View.VISIBLE:View.GONE);

        recipeViewHolder.chk.setChecked(recipe.isCanDelete);

//        recipeViewHolder.chk.setOnClickListener((arg0) -> {
//
//        });

        recipeViewHolder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                CookDetailActivity.startActivity(activity, recipeViewHolder.ivRecipe, recipes.get(position), true);
                recipe.isCanDelete = recipeViewHolder.chk.isChecked();
            }
        });
    }

    public void deleteRecipes(){
        RecipeService.getInstance().deleteCollectRecipe();
        recipes = RecipeService.getInstance().recipeBeans;
        notifyDataSetChanged();
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
        CheckBox chk;
        public RecipeViewHolder(View itemView){
            super(itemView);
            ivRecipe= (ImageView) itemView.findViewById(R.id.recipe_image );
            tvTitle= (TextView) itemView.findViewById(R.id.title_text);
            chk = (CheckBox) itemView.findViewById(R.id.chkRecipeItem);
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