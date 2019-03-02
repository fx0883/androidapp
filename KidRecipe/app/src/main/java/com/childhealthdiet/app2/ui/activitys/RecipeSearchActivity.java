package com.childhealthdiet.app2.ui.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.model.SimpleModelImpl;
import com.childhealthdiet.app2.model.contract.SimpleModel;
import com.childhealthdiet.app2.ui.base.BaseActivity;
import com.childhealthdiet.app2.ui.categorys.RECIPETYPE;
import com.czp.searchmlist.mSearchLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class RecipeSearchActivity extends BaseActivity {

    @BindView(com.czp.searchmlist.R.id.msearchlayout)
    protected mSearchLayout msearchLy;


    List<String> skills = null;
    List<String> skillHots = null;
//    String shareHotData;
    @Override
    protected int getContentId() {
        return R.layout.activity_recipe_search;
    }

    @Override
    protected void initData(Bundle savedInstanceState){
    }
    /**
     * 初始化零件
     */
    @Override
    protected void initWidget() {

        SimpleModel simpleModel = new SimpleModelImpl();
//        shareData = "澳洲美食,长沙美食,韩国料理,日本料理,舌尖上的中国,意大利餐,山西菜";
//        List<String> skills = Arrays.asList(shareData.split(","));
        this.skills = simpleModel.loadFieldByKey(this,R.array.city_hot);

//        String shareHotData ="粤菜,浙菜,苏菜,苏菜,苏菜";
//        List<String> skillHots = Arrays.asList(shareHotData.split(","));
        this.skillHots = simpleModel.loadFieldByKey(this,R.array.gv_category);

    }
    /**
     * 初始化点击事件
     */
    @Override
    protected void initClick(){
        msearchLy.initData(this.skills, this.skillHots, new mSearchLayout.setSearchCallBackListener() {
            @Override
            public void Search(String str) {
//                //进行或联网搜索
//
//                //数据是使用Intent返回
//
//                Intent intent = new Intent();
//
//                //把返回数据存入Intent
//
//                intent.putExtra("searchKey", str);
//
//                //设置返回数据
//
//                RecipeSearchActivity.this.setResult(RESULT_OK, intent);
//
//                //关闭Activity
//
//                RecipeSearchActivity.this.finish();

                RecipeKeywordListActivity.startActivity(RecipeSearchActivity.this,RECIPETYPE.Keyword,str);


            }
            @Override
            public void Back() {
                finish();
            }

            @Override
            public void ClearOldData() {
                //清除历史搜索记录  更新记录原始数据
            }
            @Override
            public void SaveOldData(ArrayList<String> AlloldDataList) {
                //保存所有的搜索记录
            }
        });
    }
    /**
     * 逻辑使用区
     */
    @Override
    protected void processLogic(){

    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(com.czp.searchmlist.R.layout.demo);
//        msearchLy = (mSearchLayout)findViewById(com.czp.searchmlist.R.id.msearchlayout);
//        initData();
//    }

    protected void initData() {



    }

}
