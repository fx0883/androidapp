package com.colorgarden.app6.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.colorgarden.app6.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdapterCreation extends RecyclerView.Adapter<AdapterCreation.MyViewHolder> {
    private Context context;
    private List<String> pathList;
    private RecClickInterface interfaceObj;

    public AdapterCreation(Context context, List<String> pathList) {
        this.context = context;
        this.pathList = pathList;
    }

    public void setInterface(RecClickInterface anInterface) {
        this.interfaceObj = anInterface;
    }

    @NonNull
    @Override
    public AdapterCreation.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_creation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCreation.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.ic_share_black_24dp);
        assert drawable != null;
        drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Bitmap bitmap = BitmapFactory.decodeFile(pathList.get(position));
        holder.img_creation.setImageBitmap(bitmap);

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interfaceObj != null) {
                    interfaceObj.ItemDeleteClick(position, pathList.get(position));
                }

            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (interfaceObj != null) {
                    interfaceObj.ItemEditClick(position, pathList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList.size();
    }

    public interface RecClickInterface {
        void ItemClick(View view, int pos);

        void ItemDeleteClick(int pos, String path);

        void ItemEditClick(int pos, String path);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.img_creation)
        ImageView img_creation;
        @BindView(R.id.btn_delete)
        ImageView btn_delete;
        @BindView(R.id.btn_edit)
        ImageView btn_edit;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (interfaceObj != null) {
                interfaceObj.ItemClick(view, getAdapterPosition());
            }
        }
    }
}
