package com.childhealthdiet.app2.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.adapter.MonthRecipeDataAdapter;
import com.childhealthdiet.app2.model.bean.MonthRecipe;
import com.childhealthdiet.app2.presenter.FragmentHomePresenter;
import com.childhealthdiet.app2.presenter.contract.FragmentHomeContract;
import com.childhealthdiet.app2.ui.base.BaseMVPFragment;
import com.childhealthdiet.app2.utils.FileUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

//    List<MonthRecipe> mMonthRecipeList;

//    private OnFragmentInteractionListener mListener;

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
            }
        });

        //分类点击事件
        mGridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),name[position],Toast.LENGTH_LONG).show();
            }
        });

        RoundedImageView iv_nourish =  (RoundedImageView)headView.findViewById(R.id.iv_nourish);
        iv_nourish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        RoundedImageView iv_food_therapy =  (RoundedImageView)headView.findViewById(R.id.iv_food_therapy);
        iv_food_therapy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
            }

        });
    }

    @OnClick(R.id.btn_top_search)
    void onClickSearchButton(){

    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.loadCategoryField(this.getContext());
        mPresenter.loadMonthRecipe(this.getContext());
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

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}