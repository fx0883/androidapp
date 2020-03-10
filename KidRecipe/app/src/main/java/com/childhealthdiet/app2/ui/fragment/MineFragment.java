package com.ChildHealthDiet.app2.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ChildHealthDiet.app2.ConstantAd;
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
import com.childhealthdiet.app2.utils.AdUtils;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;


public class MineFragment extends BaseMVPFragment<MineContract.Presenter> implements  MineContract.View,NativeExpressAD.NativeExpressADListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View headView;
    View footView;
    TextView tvKidNickname;
    TextView tvKidAge;
    Button btnEdit;

    private boolean isPreloadVideo;
    private ViewGroup container;
    private static final String TAG = "MineFragment";
    private NativeExpressAD nativeExpressAD;
    private NativeExpressADView nativeExpressADView;

    @BindView(R.id.mine_recycler_view)
    LRecyclerView mRecyclerView;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    private MineDataAdapter mMineDataAdapter = null;

    public MineFragment() {
    }

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

    private ADSize getMyADSize() {
        int w = ADSize.FULL_WIDTH;
        int h = ADSize.AUTO_HEIGHT;
        return new ADSize(w, h);
    }
    //加载腾讯广告

    private void refreshAd() {
        if(!AdUtils.getInstance(this.getContext()).bIsShowAd){
            return;
        }
        try {

//            adWidth = Integer.valueOf(editTextWidth.getText().toString());
//            adHeight = Integer.valueOf(editTextHeight.getText().toString());
//            hideSoftInput();
            /**
             *  如果选择支持视频的模版样式，请使用{@link PositionId#NATIVE_EXPRESS_SUPPORT_VIDEO_POS_ID}
             */
            nativeExpressAD = new NativeExpressAD(this.getContext(), getMyADSize(), ConstantAd.APPID, ConstantAd.NativePosID, this); // 这里的Context必须为Activity
//            VideoOption option = NativeExpressADActivity.getVideoOption(getIntent());
//            if(option != null){
//                // setVideoOption是可选的，开发者可根据需要选择是否配置
//                nativeExpressAD.setVideoOption(option);
//            }
//            nativeExpressAD.setMinVideoDuration(getMinVideoDuration());
//            nativeExpressAD.setMaxVideoDuration(getMaxVideoDuration());
            /**
             * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
             * 如果广告位仅支持图文广告，则无需调用
             */

            /**
             * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
             *
             * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
             *
             * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
             */
            nativeExpressAD.setVideoPlayPolicy(getVideoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS, this.getContext()));  // 本次拉回的视频广告，在用户看来是否为自动播放的
            nativeExpressAD.loadAD(1);
        } catch (NumberFormatException e) {
            Log.w(TAG, "ad size invalid.");
//            Toast.makeText(this, "请输入合法的宽高数值", Toast.LENGTH_SHORT).show();
        }
    }

    public static int getVideoPlayPolicy(int autoPlayPolicy, Context context){
        if(autoPlayPolicy == VideoOption.AutoPlayPolicy.ALWAYS){
            return VideoOption.VideoPlayPolicy.AUTO;
        }else if(autoPlayPolicy == VideoOption.AutoPlayPolicy.WIFI){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiNetworkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiNetworkInfo != null && wifiNetworkInfo.isConnected() ? VideoOption.VideoPlayPolicy.AUTO
                    : VideoOption.VideoPlayPolicy.MANUAL;
        }else if(autoPlayPolicy == VideoOption.AutoPlayPolicy.NEVER){
            return VideoOption.VideoPlayPolicy.MANUAL;
        }
        return VideoOption.VideoPlayPolicy.UNKNOWN;
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
        btnEdit = headView.findViewById(R.id.btn_eidt_mine);


        footView =  LayoutInflater.from(getActivity()).inflate(R.layout.mine_foot_view,null);

        container = (ViewGroup) footView.findViewById(R.id.container);


        initLRecylerView();
        mLRecyclerViewAdapter.addHeaderView(headView);
        mLRecyclerViewAdapter.addFooterView(footView);


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
            refreshAd();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

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





    private NativeExpressMediaListener mediaListener = new NativeExpressMediaListener() {
        @Override
        public void onVideoInit(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoInit: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoLoading(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoLoading");
        }

        @Override
        public void onVideoCached(NativeExpressADView adView) {
            Log.i(TAG, "onVideoCached");
            // 视频素材加载完成，此时展示视频广告不会有进度条。
            if(isPreloadVideo && nativeExpressADView != null) {
                if(container.getChildCount() > 0){
                    container.removeAllViews();
                }
                // 广告可见才会产生曝光，否则将无法产生收益。
                container.addView(nativeExpressADView);
                nativeExpressADView.render();
            }
        }

        @Override
        public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
            Log.i(TAG, "onVideoReady");
        }

        @Override
        public void onVideoStart(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoStart: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoPause(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPause: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoComplete(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoComplete: "
                    + getVideoInfo(nativeExpressADView.getBoundData().getProperty(AdData.VideoPlayer.class)));
        }

        @Override
        public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
            Log.i(TAG, "onVideoError");
        }

        @Override
        public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageOpen");
        }

        @Override
        public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onVideoPageClose");
        }
    };

    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }

        if (container.getVisibility() != View.VISIBLE) {
            container.setVisibility(View.VISIBLE);
        }

        if (container.getChildCount() > 0) {
            container.removeAllViews();
        }

        nativeExpressADView = adList.get(0);
        Log.i(TAG, "onADLoaded, video info: " + getAdInfo(nativeExpressADView));
        if (nativeExpressADView.getBoundData().getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            nativeExpressADView.setMediaListener(mediaListener);
            if(isPreloadVideo) {
                // 预加载视频素材，加载成功会回调mediaListener的onVideoCached方法，失败的话回调onVideoError方法errorCode为702。
                nativeExpressADView.preloadVideo();
            }
        } else {
            isPreloadVideo = false;
        }
        if(!isPreloadVideo) {
            // 广告可见才会产生曝光，否则将无法产生收益。
            container.addView(nativeExpressADView);
            nativeExpressADView.render();
        }
    }

    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                infoBuilder.append(", video info: ").append(getVideoInfo(adData.getProperty(AdData.VideoPlayer.class)));
            }
            Log.d(TAG, "eCPM = " + adData.getECPM() + " , eCPMLevel = " + adData.getECPMLevel() + " , " +
                    "videoDuration = " + adData.getVideoDuration());
            return infoBuilder.toString();
        }
        return null;
    }

    /**
     * 获取播放器实例
     *
     * 仅当视频回调{@link NativeExpressMediaListener#onVideoInit(NativeExpressADView)}调用后才会有返回值
     *
     * @param videoPlayer
     * @return
     */
    private String getVideoInfo(AdData.VideoPlayer videoPlayer) {
        if (videoPlayer != null) {
            StringBuilder videoBuilder = new StringBuilder();
            videoBuilder.append("{state:").append(videoPlayer.getVideoState()).append(",")
                    .append("duration:").append(videoPlayer.getDuration()).append(",")
                    .append("position:").append(videoPlayer.getCurrentPosition()).append("}");
            return videoBuilder.toString();
        }
        return null;
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onNoAD(AdError adError) {

    }
}
