package com.childhealthdiet.app2.ui.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;
import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.adapter.DataAdapter;
import com.childhealthdiet.app2.model.bean.MonthBean;
import com.childhealthdiet.app2.model.bean.RecipeBean;
import com.childhealthdiet.app2.ui.base.BaseFragment;
import com.childhealthdiet.app2.utils.FileUtils;
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;




public class HomeFragment extends BaseFragment implements BGABanner.Delegate<ImageView, String>, BGABanner.Adapter<ImageView, String> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private BGABanner mHomeTopBanner;

    @BindView(R.id.home_recycler_view)
    LRecyclerView mRecyclerView;

//    @BindView(R.id.gridview_category)
    GridView mGridViewCategory;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private DataAdapter mDataAdapter = null;



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
    protected void initWidget(Bundle savedInstanceState){
        //header头部
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_header_view,null);
        mHomeTopBanner = headView.findViewById(R.id.home_top_banner);
        loadTopBanner(mHomeTopBanner);
        initLRecylerView();
        mLRecyclerViewAdapter.addHeaderView(headView);

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);

        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

//        mLRecyclerViewAdapter.notifyDataSetChanged();

        initCategoryGridView(headView);

    }

    private void initCategoryGridView(View view){
        mGridViewCategory = view.findViewById(R.id.gridview_category);

        String[] gvCategorys = getResources().getStringArray(R.array.gv_category);
        //生成动态数组，并且转入数据
        ArrayList<HashMap<String ,String>> listItemArrayList=new ArrayList<HashMap<String,String>>();
        for(int i=0; i<gvCategorys.length; i++){
            HashMap<String, String> map=new HashMap<String,String>();
            map.put("itemTitle", gvCategorys[i]);
            map.put("itemText", gvCategorys[i]);
            listItemArrayList.add(map);
        }


//        //生成适配器的ImageItem 与动态数组的元素相对应
//        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(),
//                listItemArrayList,//数据来源
//                R.layout.item,//item的XML
//
//                //动态数组与ImageItem对应的子项
//                new String[]{"itemImage", "itemText"},
//
//                //ImageItem的XML文件里面的一个ImageView,TextView ID
//                new int[]{R.id.img_shoukuan, R.id.txt_shoukuan});
        SimpleAdapter gvSimpleAdapter = new SimpleAdapter(getActivity(),listItemArrayList,
                R.layout.gv_category_item,new String[]{"itemTitle"},new int[]{R.id.tv_category_title});
        mGridViewCategory.setAdapter(gvSimpleAdapter);
        //添加消息处理
        mGridViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),name[position],Toast.LENGTH_LONG).show();
            }
        });


    }

    private void initLRecylerView(){
        mDataAdapter = new DataAdapter(getActivity());

//        ArrayList<RecipeBean> dataList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            RecipeBean itemModel = new RecipeBean();
//            itemModel.name = "好菜";
//            dataList.add(itemModel);
//        }

        String jsonFilePath = getResources().getString(R.string.month_json_path);
        String monthJson = FileUtils.getJson(this.getActivity(),jsonFilePath);


//        ArrayList<MonthBean> dataList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            MonthBean itemModel = new MonthBean();
//            itemModel.title = "好菜";
//            dataList.add(itemModel);
//        }


        Gson gson = new Gson();
        List<MonthBean> dataList = (List<MonthBean>)gson .fromJson(monthJson,
                new TypeToken<List<MonthBean>>(){}.getType());

        mDataAdapter.addAll(dataList);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

//        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
//                .setHeight(R.dimen.default_divider_height)
//                .setPadding(R.dimen.default_divider_padding)
//                .setColorResource(R.color.colorAccent)
//                .build();
//        //mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
//        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
    }

    void loadTopBanner(BGABanner banner){

        String[] topBannerTips = getResources().getStringArray(R.array.home_banner_labels);
        String[] topBannerImgs = getResources().getStringArray(R.array.home_banner_img);
//        TypedArray topBannerImgArray = getResources().obtainTypedArray(R.array.home_banner_img);
//        int[] topBannerImgArray = getResources().getIntArray(R.array.home_banner_img);
        banner.setAutoPlayAble(topBannerTips.length > 1);

        banner.setAdapter(HomeFragment.this);
        banner.setData(Arrays.asList(topBannerImgs), Arrays.asList(topBannerTips));
// Bitmap 的宽高在 maxWidth maxHeight 和 minWidth minHeight 之间
//        BGALocalImageSize localImageSize = new BGALocalImageSize(720, 1280, 320, 640);
// 设置数据源
//        banner.setData(localImageSize, ImageView.ScaleType.CENTER_CROP,
//                R.drawable.uoko_guide_background_1,
//                R.drawable.uoko_guide_background_2,
//                R.drawable.uoko_guide_background_3);


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
    public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {

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
