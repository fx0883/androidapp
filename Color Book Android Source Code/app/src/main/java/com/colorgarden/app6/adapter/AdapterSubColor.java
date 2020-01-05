package com.colorgarden.app6.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.colorManager.ModelSetColor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.colorgarden.app6.activity.PaintActivity.selected_color;


public class AdapterSubColor extends RecyclerView.Adapter<AdapterSubColor.MyViewHolder> {
    private static ClickInterface interfaceObj;
    private Context context;
    private List<ModelSetColor> imgList;
    private int TYPE1 = 123;
    private int TYPE2 = 345;
    private int pagerPos;

    public AdapterSubColor(Context context, List<ModelSetColor> imgList, int pos) {
        this.context = context;
        this.imgList = imgList;
        this.pagerPos = pos;
    }

    public static void setInterface(ClickInterface anInterface) {
        interfaceObj = anInterface;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return TYPE1;
        } else {
            return TYPE2;
        }
    }

    @NonNull
    @Override
    public AdapterSubColor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE1) {
            view = LayoutInflater.from(context).inflate(R.layout.item_color_pager, parent, false);
        } else if (viewType == TYPE2) {
            view = LayoutInflater.from(context).inflate(R.layout.item_color_sub_pager, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubColor.MyViewHolder holder, int position) {
        holder.img_color.setColorFilter(Color.parseColor(imgList.get(position).color));

        if (selected_color.equals(imgList.get(position).color)) {
            holder.img_selected.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    public interface ClickInterface {
        void ItemClick(View view, int i, String s, int pagerPosition);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_color)
        ImageView img_color;
        @BindView(R.id.img_selected)
        ImageView img_selected;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (interfaceObj != null) {
                interfaceObj.ItemClick(view, getAdapterPosition(), imgList.get(getAdapterPosition()).color, pagerPos);
            }
        }
    }
}
