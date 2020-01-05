package com.colorgarden.app6.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.colorgarden.app6.R;
import com.colorgarden.app6.colorManager.ModelColor;
import com.colorgarden.app6.colorManager.ModelSetColor;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterDialog extends RecyclerView.Adapter<AdapterDialog.MyViewHolder> {

    private Context context;
    private List<ModelColor> getColors;
    private ClickInterface interfaceObj;


    public AdapterDialog(Context context, List<ModelColor> getColors, ClickInterface interfaceObj) {
        this.context = context;
        this.getColors = getColors;
        this.interfaceObj = interfaceObj;
    }

    @NonNull
    @Override
    public AdapterDialog.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dialog_color, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterDialog.MyViewHolder holder, int position) {
        holder.dialog_tv_name.setText(getColors.get(position).nameResId);
        List<ModelSetColor> setColorList = getColors.get(position).getSetColors();
        holder.color1.setColorFilter(Color.parseColor(setColorList.get(0).color));
        holder.color2.setColorFilter(Color.parseColor(setColorList.get(1).color));
        holder.color3.setColorFilter(Color.parseColor(setColorList.get(2).color));
        holder.color4.setColorFilter(Color.parseColor(setColorList.get(3).color));
        holder.color5.setColorFilter(Color.parseColor(setColorList.get(4).color));
        holder.color6.setColorFilter(Color.parseColor(setColorList.get(5).color));
        holder.color7.setColorFilter(Color.parseColor(setColorList.get(6).color));
        holder.color8.setColorFilter(Color.parseColor(setColorList.get(7).color));
        holder.color9.setColorFilter(Color.parseColor(setColorList.get(8).color));
    }

    @Override
    public int getItemCount() {
        return getColors.size();
    }

    public interface ClickInterface {
        void recItemClick(View view, int i);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.color1)
        ImageView color1;
        @BindView(R.id.color2)
        ImageView color2;
        @BindView(R.id.color3)
        ImageView color3;
        @BindView(R.id.color4)
        ImageView color4;
        @BindView(R.id.color5)
        ImageView color5;
        @BindView(R.id.color6)
        ImageView color6;
        @BindView(R.id.color7)
        ImageView color7;
        @BindView(R.id.color8)
        ImageView color8;
        @BindView(R.id.color9)
        ImageView color9;
        @BindView(R.id.dialog_tv_name)
        TextView dialog_tv_name;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (interfaceObj != null) {
                interfaceObj.recItemClick(view, getAdapterPosition());
            }
        }
    }
}
