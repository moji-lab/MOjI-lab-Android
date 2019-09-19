package com.mojilab.moji.ui.main.upload.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mojilab.moji.R;
import com.mojilab.moji.data.UploadImgData;

import java.util.ArrayList;

public class UploadImgRecyclerviewAdapter extends RecyclerView.Adapter<UploadImgRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<UploadImgData> dataList = null;


    public UploadImgRecyclerviewAdapter(ArrayList<UploadImgData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_upload_img, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //is대표사진
        if (dataList.get(position).boss) {
            holder.screen.setVisibility(View.VISIBLE);
            holder.boss.setVisibility(View.VISIBLE);
            holder.lock.setVisibility(View.GONE);
        } else {
            holder.screen.setVisibility(View.GONE);
            holder.boss.setVisibility(View.GONE);
            holder.lock.setVisibility(View.VISIBLE);
        }

        //이미지
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(32));

        Glide.with(context).load(dataList.get(position).image).apply(requestOptions).into(holder.image);

        //lock 상태
        holder.lock.setSelected(dataList.get(position).lock);


        //-버튼
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 이미지 리스트에서 제거
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        //lock버튼
        holder.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lock state 변경
                holder.lock.setSelected(!holder.lock.isSelected());
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout container;

        protected RelativeLayout screen;
        protected TextView boss;

        protected ImageView remove;
        protected ImageView lock;
        protected ImageView image;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_upload_img_item_container);

            this.screen = view.findViewById(R.id.rl_upload_img_item_screen);
            this.boss = view.findViewById(R.id.tv_upload_img_item_boss);

            this.remove = view.findViewById(R.id.iv_upload_img_item_remove);
            this.lock = view.findViewById(R.id.iv_upload_act_item_btn_lock);
            this.image = view.findViewById(R.id.iv_upload_img_item_img);

        }
    }

}
