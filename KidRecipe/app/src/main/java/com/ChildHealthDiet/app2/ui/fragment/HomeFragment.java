package com.ChildHealthDiet.app2.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.RxBus;
import com.ChildHealthDiet.app2.adapter.MonthRecipeDataAdapter;
import com.ChildHealthDiet.app2.event.ChangeTabEvent;
import com.ChildHealthDiet.app2.model.bean.KeyValueBean;
import com.ChildHealthDiet.app2.model.bean.MonthRecipe;
import com.ChildHealthDiet.app2.presenter.FragmentHomePresenter;
import com.ChildHealthDiet.app2.presenter.contract.FragmentHomeContract;
import com.ChildHealthDiet.app2.ui.activitys.RecipeKeywordListActivity;
import com.ChildHealthDiet.app2.ui.activitys.RecipeSearchActivity;
import com.ChildHealthDiet.app2.ui.base.BaseMVPFragment;
import com.ChildHealthDiet.app2.ui.categorys.RECIPETYPE;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;


public class HomeFragment extends BaseMVPFragment<FragmentHomeContract.Presenter> implements  BGABanner.Adapter<ImageView, String>,FragmentHomeContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private BGABanner mHomeTopBanner;

    //category 数据
    private ArrayList<HashMap<String ,String>> categoryItemArrayList;
    SimpleAdapter gvSimpleAdapter;

    @BindView(R.id.home_recycler_view)
    LRecyclerView mRecyclerView;

    GridView mGridViewCategory;
    View headView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private MonthRecipeDataAdapter mMonthRecipeMonthRecipeDataAdapter = null;


    String[] topBannerTips;
    String[] topBannerImgs;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //loadTopBanner
        topBannerTips = getResources().getStringArray(R.array.home_banner_labels);
        topBannerImgs = getResources().getStringArray(R.array.home_banner_img);
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_header_view,null);
        mHomeTopBanner = headView.findViewById(R.id.home_top_banner);
        initTopBanner(mHomeTopBanner);
        initLRecylerView();
        mLRecyclerViewAdapter.addHeaderView(headView);
        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);
        initCategoryGridView(headView);
    }

    @Override
    protected void initClick() {
        super.initClick();
        mHomeTopBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {

            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, String model, int position) {
//                Toast.makeText(banner.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                String[] strs = {"早餐","午餐","晚餐"};
                RecipeKeywordListActivity.startActivity(HomeFragment.this.getContext(),RECIPETYPE.Eattime,strs[position]);

            }
        });

        //分类点击事件
        mGridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),name[position],Toast.LENGTH_LONG).show();

                if(position == categoryItemArrayList.size()-1){
                    RxBus.getInstance().post(new ChangeTabEvent(1));
                }
                else {
                    HashMap<String, String> map = categoryItemArrayList.get(position);
                    String strKey = map.get("itemText");
                    RecipeKeywordListActivity.startActivity(HomeFragment.this.getContext(),RECIPETYPE.Symptoms,strKey);
                }



            }
        });

        RoundedImageView iv_nourish =  (RoundedImageView)headView.findViewById(R.id.iv_nourish);
        iv_nourish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeKeywordListActivity.startActivity(HomeFragment.this.getContext(),RECIPETYPE.Type,"营养进补");
            }
        });

        RoundedImageView iv_food_therapy =  (RoundedImageView)headView.findViewById(R.id.iv_food_therapy);
        iv_food_therapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeKeywordListActivity.startActivity(HomeFragment.this.getContext(),RECIPETYPE.Type,"健康食疗");
            }
        });


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(view.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();

                MonthRecipe monthRecipe =  mMonthRecipeMonthRecipeDataAdapter.getDataList().get(position);
                KeyValueBean keyValueBean = new KeyValueBean(monthRecipe.getKey(),monthRecipe.getTitle());
                RecipeKeywordListActivity.startActivity(getContext(),RECIPETYPE.Month,keyValueBean);

            }

        });
    }


    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.loadCategoryField(this.getContext());
        mPresenter.loadMonthRecipe(this.getContext());
    }

    @OnClick(R.id.btn_top_search)
    void onClickSearchButton(){
        startActivityForResult(new Intent(this.getContext(), RecipeSearchActivity.class), 1);
    }

    private void initCategoryGridView(View view){
        mGridViewCategory = view.findViewById(R.id.gridview_category);
        categoryItemArrayList = new ArrayList<HashMap<String,String>>();
        gvSimpleAdapter = new SimpleAdapter(getActivity(),categoryItemArrayList,
                R.layout.gv_category_item,new String[]{"itemTitle"},new int[]{R.id.tv_category_title});
        mGridViewCategory.setAdapter(gvSimpleAdapter);
        //添加消息处理

    }

    private void initLRecylerView(){
        mMonthRecipeMonthRecipeDataAdapter = new MonthRecipeDataAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMonthRecipeMonthRecipeDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    void initTopBanner(BGABanner banner){
        banner.setAutoPlayAble(topBannerTips.length > 1);
        banner.setAdapter(HomeFragment.this);
        banner.setData(Arrays.asList(topBannerImgs), Arrays.asList(topBannerTips));
    }



    @Override
    protected int getContentId() {
        return R.layout.fragment_home;
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        Glide.with(itemView.getContext())
                .load(model)
                .into(itemView);
    }

    @Override
    public void updateCategory(String[] aryCategory) {
        for(int i=0; i<aryCategory.length; i++){
            HashMap<String, String> map=new HashMap<String,String>();
            map.put("itemTitle", aryCategory[i]);
            map.put("itemText", aryCategory[i]);
            categoryItemArrayList.add(map);
        }
        gvSimpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void updateMonthRecipeMenu(List<MonthRecipe> monthRecipes) {
        mMonthRecipeMonthRecipeDataAdapter.setDataList(monthRecipes);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected FragmentHomeContract.Presenter bindPresenter() {
        return new FragmentHomePresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }


    /**
     * 为了得到传回的数据，必须在前面的Activity中（指MainActivity类）重写onActivityResult方法
     * <p>
     * <p>
     * <p>
     * requestCode 请求码，即调用startActivityForResult()传递过去的值
     * <p>
     * resultCode 结果码，结果码用于标识返回数据来自哪个新Activity
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 1:

//                if (data != null) {
//                    String searchStr = data.getExtras().getString("searchKey");
//                    tvTitle.setText("关键字"+searchStr+"的搜索结果");
////                    searchStr = "鱼";
//                    disposables.add(RecipeService.getInstance().searchRecipeObservable(searchStr)
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribeWith(new DisposableObserver<List<RecipeBean>>() {
//                                @Override
//                                public void onComplete() {
//                                    disposables.clear();
//                                }
//
//                                @Override
//                                public void onError(Throwable e) {
//                                    disposables.clear();
//                                }
//
//                                @Override
//                                public void onNext(List<RecipeBean> recipes) {
//                                    recipeListAdapter.updateRecipe(recipes);
//
//                                }
//                            }));
//                }


            case 2:

                //来自按钮2的请求，作相应业务处理

        }


        //得到新Activity 关闭后返回的数据


    }


}
