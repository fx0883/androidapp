package com.childhealthdiet.app2.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.childhealthdiet.app2.R;
import com.childhealthdiet.app2.adapter.CategoryRecipeDataAdapter;
import com.childhealthdiet.app2.adapter.MineDataAdapter;
import com.childhealthdiet.app2.model.bean.MineItem;
import com.childhealthdiet.app2.model.bean.RecipeCategory;
import com.childhealthdiet.app2.presenter.MinePresenter;
import com.childhealthdiet.app2.presenter.contract.CategorysContract;
import com.childhealthdiet.app2.presenter.contract.MineContract;
import com.childhealthdiet.app2.ui.activitys.BasketActivity;
import com.childhealthdiet.app2.ui.activitys.RecipeDetailActivity;
import com.childhealthdiet.app2.ui.activitys.RecipeKeywordListActivity;
import com.childhealthdiet.app2.ui.base.BaseFragment;
import com.childhealthdiet.app2.ui.base.BaseMVPFragment;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;


public class MineFragment extends BaseMVPFragment<MineContract.Presenter> implements  MineContract.View {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;


    @BindView(R.id.mine_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private MineDataAdapter mMineDataAdapter = null;

    public MineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void updateMineItem(List<MineItem> mineItems) {
        mMineDataAdapter.setDataList(mineItems);
        mLRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected MineContract.Presenter bindPresenter() {
        return new MinePresenter();
    }

    @Override
    public void showError() {

    }

    @Override
    public void complete() {

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

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(view.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//                long recipeId = mMineDataAdapter.getDataList().get(position).getId();
//                RecipeDetailActivity.startActivity(RecipeKeywordListActivity.this,view,recipeId);

                BasketActivity.startActivity(MineFragment.this.getContext());
            }

        });

    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getMineItems(this.getContext());
    }



//    @Override
//    public void updateCategorysList(List<RecipeCategory> categoryslist) {
//        mCategoryRecipeDataAdapter.setDataList(categoryslist);
//        mLRecyclerViewAdapter.notifyDataSetChanged();
//    }


    private void initLRecylerView(){
        mMineDataAdapter = new MineDataAdapter(getActivity());
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mMineDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);
        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);
    }

}
