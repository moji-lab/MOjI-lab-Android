package com.mojilab.moji.ui.main.map;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.data.MapSearchData;

import java.util.ArrayList;

public class MapSearchListRecyclerviewAdapter extends RecyclerView.Adapter<MapSearchListRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<MapSearchData> dataList = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private MapSearchListRecyclerviewAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(MapSearchListRecyclerviewAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }

    public MapSearchListRecyclerviewAdapter(ArrayList<MapSearchData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_search_list, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(view, position);
                    }
                }
            }
        });

        Glide.with(context).load(dataList.get(position).img).into(holder.img);
        holder.img.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);


        holder.main.setText(dataList.get(position).mainAddress);
        holder.sub.setText(dataList.get(position).subAddress);

        holder.likeCnt.setText(dataList.get(position).likeCnt + "");

        //회원 아이디에 따라
        if (dataList.get(position).isLike)
            holder.likeImg.setSelected(true);
        else
            holder.likeImg.setSelected(false);

        holder.likeImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.likeImg.setSelected(!holder.likeImg.isSelected());
                }
            }
        );

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout container;
        protected ImageView img;

        protected TextView main;
        protected TextView sub;
        protected ImageView likeImg;
        protected TextView likeCnt;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_item_search_list_container);
            this.img = view.findViewById(R.id.iv_item_search_list_img);
            this.main = view.findViewById(R.id.tv_item_search_list_main_address);
            this.sub = view.findViewById(R.id.tv_item_search_list_sub_address);
            this.likeImg = view.findViewById(R.id.iv_item_search_heart_selector);
            this.likeCnt = view.findViewById(R.id.tv_item_search_heart_cnt);


        }
    }

}
