package com.ChildHealthDiet.app2.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.adapter.MineDataAdapter;
import com.ChildHealthDiet.app2.context.Kidinfo;
import com.ChildHealthDiet.app2.context.UserContext;
import com.ChildHealthDiet.app2.model.bean.MineItem;
import com.ChildHealthDiet.app2.presenter.MinePresenter;
import com.ChildHealthDiet.app2.presenter.contract.MineContract;
import com.ChildHealthDiet.app2.ui.activitys.BasketActivity;
import com.ChildHealthDiet.app2.ui.activitys.KidEidtActivity;
import com.ChildHealthDiet.app2.ui.activitys.RecipeKeywordListActivity;
import com.ChildHealthDiet.app2.ui.base.BaseMVPFragment;
import com.ChildHealthDiet.app2.ui.categorys.RECIPETYPE;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;

import java.text.ParseException;
import java.util.ArrayList;
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

    View headView;
    TextView tvKidNickname;
    TextView tvKidAge;
    Button btnEdit;

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

        headView = LayoutInflater.from(getActivity()).inflate(R.layout.mine_header_view,null);
        tvKidNickname = headView.findViewById(R.id.tv_kid_nickname);
        tvKidAge = headView.findViewById(R.id.tv_kid_age);
//        mHomeTopBanner = headView.findViewById(R.id.home_top_banner);
//        initTopBanner(mHomeTopBanner);
        btnEdit = headView.findViewById(R.id.btn_eidt_mine);


        initLRecylerView();
        mLRecyclerViewAdapter.addHeaderView(headView);
    }

    @Override
    protected void initClick() {
        super.initClick();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KidEidtActivity.startActivity(MineFragment.this);
            }
        });

        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(view.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
//                long recipeId = mMineDataAdapter.getDataList().get(position).getId();
//                RecipeDetailActivity.startActivity(RecipeKeywordListActivity.this,view,recipeId);
                List<MineItem> mineItems = MineFragment.this.mMineDataAdapter.getDataList();
                String strKey = mineItems.get(position).getKey();
                switch (strKey)
                {
                    case "collect":
                        RecipeKeywordListActivity.startActivity(MineFragment.this.getContext(),RECIPETYPE.Collect);
                        break;
                    case "basket":
                        BasketActivity.startActivity(MineFragment.this.getContext());
                        break;
                    case "feedback":
                        MineFragment.this.sendEmail();
                        break;
                }

            }

        });

    }

    @Override
    protected void processLogic() {
        super.processLogic();
        mPresenter.getMineItems(this.getContext());

        try {
            loadKidinfo();
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case 10030:
                try {
                    loadKidinfo();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;


            case 2:

                //来自按钮2的请求，作相应业务处理

        }



    }

    private void loadKidinfo() throws ParseException {
        Kidinfo kidinfo = UserContext.getInstance().getmKidinfo(this.getContext());
        if(!kidinfo.getNickName().equals("")){
            this.tvKidNickname.setText(kidinfo.getNickName());
        }
        if(!kidinfo.getBirthdate().equals("")){

            this.tvKidAge.setText(kidinfo.getAge());
        }
    }

    public void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        // i.setType("text/plain"); //模拟器请使用这行
        i.setType("message/rfc822"); // 真机上使用这行
        i.putExtra(Intent.EXTRA_EMAIL,
                new String[]{getString(R.string.feedbackEmail)});

        String txtTitle = getResources().getString(R.string.feedbackTitle);
        txtTitle = String.format(txtTitle, getString( R.string.app_name));


        i.putExtra(Intent.EXTRA_SUBJECT, txtTitle);
        i.putExtra(Intent.EXTRA_TEXT, getString(R.string.feedbackBody));
        ArrayList<Uri> uris = new ArrayList<>();
        i.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivity(Intent.createChooser(i,
                "选择email程序."));
    }

}