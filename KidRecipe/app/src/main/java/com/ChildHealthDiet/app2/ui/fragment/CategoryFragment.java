package com.ChildHealthDiet.app2.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.adapter.CategoryRecipeDataAdapter;
import com.ChildHealthDiet.app2.model.bean.RecipeCategory;
import com.ChildHealthDiet.app2.presenter.FragmentCategoryPresenter;
import com.ChildHealthDiet.app2.presenter.contract.CategorysContract;
import com.ChildHealthDiet.app2.ui.base.BaseMVPFragment;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;


public class CategoryFragment extends BaseMVPFragment<CategorysContract.Presenter> implements  CategorysContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    @BindView(R.id.categorys_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private CategoryRecipeDataAdapter mCategoryRecipeDataAdapter = null;
//    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //loadTopBanner
    }

    @Override
    protected void initWidget(Bundle savedInstanceState) {
        super.initWidget(savedInstanceState);
        initLRecylerView();
    }

    @Override
    protected void initClick() {
        super.initClick();

    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getCategorysList(this.getContext());
    }



    @Override
    public void updateCategorysList(List<RecipeCategory> categoryslist) {
        mCategoryRecipeDataAdapter.setDataList(categoryslist);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected CategorysContract.Presenter bindPresenter() {
        return new FragmentCategoryPresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_category;
    }


    private void initLRecylerView(){
        mCategoryRecipeDataAdapter = new CategoryRecipeDataAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mCategoryRecipeDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);
    }
}
