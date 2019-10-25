package com.ChildHealthDiet.app2.adapter;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ChildHealthDiet.app2.R;
import com.ChildHealthDiet.app2.model.bean.MineItem;
import com.ChildHealthDiet.app2.ui.base.SuperViewHolder;
import com.ChildHealthDiet.app2.ui.base.adapter.ListBaseAdapter;

public class MineDataAdapter extends ListBaseAdapter<MineItem> {

    public MineDataAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.mine_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {

        MineItem item = mDataList.get(position);

        TextView titleText = holder.getView(R.id.tv_item_name);
        titleText.setText(item.getName());

        ImageView imvitem = holder.getView(R.id.mineitem_imv);
        Glide.with(mContext)
                .load(item.getPicture())
                .into(imvitem);
        View lineView = holder.getView(R.id.mine_item_line);
        if(position == mDataList.size()-1){
            lineView.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void onViewRecycled(SuperViewHolder holder) {
        super.onViewRecycled(holder);
    }
}
