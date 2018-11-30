package com.Recipes.app2.view.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.Recipes.app2.RecipeApplication;
import com.Recipes.app2.model.bean.CookRecipeMethod;
import com.Recipes.app2.model.bean.RecipeBean;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.Recipes.app2.R;
import com.Recipes.app2.model.services.RecipeService;
import com.Recipes.app2.view.components.SwitchIconView;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2017/2/21.
 */

public class CookDetailAdapter extends RecyclerView.Adapter<CookDetailAdapter.ItemViewHolder>{

    private Context context;
    private RecipeBean srcData;
    private List<CookDetailStruct> datas;

    private String sumary;
    private ArrayList<String> ingredientsDatas;
    private ArrayList<CookRecipeMethod> cookRecipeMethods;
    private boolean isShowCollection;

//    private Gson gson;
//    private GlideUtil glideUtil;

    public CookDetailAdapter(Context context, RecipeBean data, boolean isShowCollection) {
        this.context = context;
        this.srcData = data;
        this.isShowCollection = isShowCollection;

        sumary = data.getPrompt();
        sumary = sumary.replaceAll("<br>","");

        String str = data.getIngredients();
        if(null == str || TextUtils.isEmpty(str)) {
            ingredientsDatas = new ArrayList<>();
        }
        else{
            String[] ingredients = str.split("<br>");

            ingredientsDatas = new ArrayList<String>();
//            ingredientsDatas.addAll(ingredients);

            for(int i=0;i<ingredients.length;i++) {

                ingredientsDatas.add(ingredients[i]);
            }
        }

        str = data.getSteps();
        if(null == str || TextUtils.isEmpty(str)){
            cookRecipeMethods = new ArrayList<>();
        }
        else {
            String[] steps = str.split("<br>");

            cookRecipeMethods = new ArrayList<>();

            for(int i=0;i<steps.length;i++) {
                CookRecipeMethod cookRecipeMethod = new CookRecipeMethod();
                cookRecipeMethod.setStep(steps[i]);
                cookRecipeMethods.add(cookRecipeMethod);
            }

        }
        this.datas = CookDetailStruct.create(cookRecipeMethods);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(Cook_Detail_Item_Type_CookMan == viewType){
            CookManItemViewHolder holder = new CookManItemViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_cook_detail_cookman, parent, false)
            );

            return holder;
        }
        else if(Cook_Detail_Item_Type_Header == viewType){
            HeaderItemViewHolder holder = new HeaderItemViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_cook_detail_header, parent, false)
            );

            return holder;
        }
        else{
            StepItemViewHolder holder = new StepItemViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.item_cook_detail_step, parent, false)
            );

            return holder;
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position){

        if(Cook_Detail_Item_Type_CookMan == getItemViewType(position)){
            final CookManItemViewHolder holderView = (CookManItemViewHolder)holder;

            if(isShowCollection){
//                holderView.switchIconView.setVisibility(View.VISIBLE);
//
//                holderView.switchIconView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if(holderView.switchIconView.isIconEnabled()){
////                            CookCollectionManager.getInstance().delete(srcData);
////                        }
////                        else{
////
////
////                            CookCollectionManager.getInstance().add(srcData);
////                        }
//                        holderView.switchIconView.switchState();
//                    }
//                });
            }
            else{
//                holderView.switchIconView.setVisibility(View.GONE);
            }



            holderView.textSumary.setText(sumary);
            return ;
        }


        if(Cook_Detail_Item_Type_Header == getItemViewType(position)){
            HeaderItemViewHolder holderView = (HeaderItemViewHolder)holder;
            if(ingredientsDatas.size() < 1){
                holderView.view1.setVisibility(View.GONE);
                holderView.view2.setVisibility(View.GONE);
                holderView.view3.setVisibility(View.GONE);

                holderView.textIngredients.setVisibility(View.GONE);
            }
            else if(ingredientsDatas.size() < 2) {
                holderView.view1.setVisibility(View.VISIBLE);
                holderView.view2.setVisibility(View.GONE);
                holderView.view3.setVisibility(View.GONE);

                holderView.textIngredientsContent1.setText(ingredientsDatas.get(0));
            }
            else if(ingredientsDatas.size() < 3){
                holderView.view1.setVisibility(View.VISIBLE);
                holderView.view2.setVisibility(View.VISIBLE);
                holderView.view3.setVisibility(View.GONE);

                holderView.textIngredientsContent1.setText(ingredientsDatas.get(0));
                holderView.textIngredientsContent2.setText(ingredientsDatas.get(1));
            }
            else{
                holderView.view1.setVisibility(View.VISIBLE);
                holderView.view2.setVisibility(View.VISIBLE);
                holderView.view3.setVisibility(View.VISIBLE);

                holderView.textIngredientsContent1.setText(ingredientsDatas.get(0));
                holderView.textIngredientsContent2.setText(ingredientsDatas.get(1));
                holderView.textIngredientsContent3.setText(ingredientsDatas.get(2));
            }

            if(RecipeApplication.getApplication().isCollectMode) {
                holderView.imgButtonCollect.setVisibility(View.GONE);
            }
            else{
                holderView.imgButtonCollect.setVisibility(View.VISIBLE);
            }

            if(srcData.getCollect())
            {
                holderView.imgButtonCollect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.shoucanghuang));
            }
            else
            {
                holderView.imgButtonCollect.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.shoucangbai));
            }



            holderView.imgButtonCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    srcData.setCollect(!srcData.getCollect());

//                    RecipeApplication.getApplication().daoSession.getRecipeBeanDao().update(srcData);

                    RecipeService.getInstance().updateRecipe(srcData);
                    notifyDataSetChanged();
                }
            });

            return ;
        }
//
        StepItemViewHolder holderView = (StepItemViewHolder)holder;
        CookRecipeMethod data = datas.get(position).getData();
        holderView.textContent.setText(data.getStep());

        if(data.getImg() != null && (!TextUtils.isEmpty(data.getImg()))) {
            holderView.imgvStep.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(data.getImg())
                    .into(holderView.imgvStep);
        }
        else{
            holderView.imgvStep.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){
        return datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    private final static int Cook_Detail_Item_Type_CookMan = 0;
    private final static int Cook_Detail_Item_Type_Header = 1;
    private final static int Cook_Detail_Item_Type_Step = 2;
    private static class CookDetailStruct{

        private int type;
        private CookRecipeMethod data;

        public CookDetailStruct(int type){
            this.type = type;
        }

        public CookDetailStruct(int type, CookRecipeMethod data){
            this.type = type;
            this.data = data;
        }

        public int getType() {
            return type;
        }

        public CookRecipeMethod getData() {
            return data;
        }

        public static List<CookDetailStruct> create(ArrayList<CookRecipeMethod> cookRecipeMethods){
            List<CookDetailStruct> datas = new ArrayList<>();


            datas.add(new CookDetailStruct(Cook_Detail_Item_Type_Header));

            if(null == cookRecipeMethods)
                return datas;

            for(CookRecipeMethod item : cookRecipeMethods) {
                datas.add(new CookDetailStruct(Cook_Detail_Item_Type_Step, item));
            }
            datas.add(new CookDetailStruct(Cook_Detail_Item_Type_CookMan));
            return datas;
        }
    }

    public class StepItemViewHolder extends ItemViewHolder{

        @BindView(R.id.text_content)
        public TextView textContent;
        @BindView(R.id.imgv_step)
        public ImageView imgvStep;

        public StepItemViewHolder(View itemView){
            super(itemView);
        }
    }

    public class CookManItemViewHolder extends ItemViewHolder{

//        @BindView(R.id.switchIconView_collection)
//        public SwitchIconView switchIconView;
        @BindView(R.id.text_sumary)
        public TextView textSumary;



        public CookManItemViewHolder(View itemView){
            super(itemView);
        }
    }

    public class HeaderItemViewHolder extends ItemViewHolder{

        @BindView(R.id.text_ingredients)
        public TextView textIngredients;

        @BindView(R.id.relative_view1)
        public RelativeLayout view1;
        @BindView(R.id.relative_view2)
        public RelativeLayout view2;
        @BindView(R.id.relative_view3)
        public RelativeLayout view3;

        @BindView(R.id.text_ingredients_content1)
        public TextView textIngredientsContent1;
        @BindView(R.id.text_ingredients_content2)
        public TextView textIngredientsContent2;
        @BindView(R.id.text_ingredients_content3)
        public TextView textIngredientsContent3;


        @BindView(R.id.recipe_collect)
        public ImageButton imgButtonCollect;

        public HeaderItemViewHolder(View itemView){
            super(itemView);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        public ItemViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
