package com.mojilab.moji.ui.main.upload.addCourse;

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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.ui.main.upload.tag.TagRecyclerviewAdapter;

import java.util.ArrayList;

public class LocationRecyclerviewAdapter extends RecyclerView.Adapter<LocationRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<LocationData> dataList = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position, String main) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private LocationRecyclerviewAdapter.OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(LocationRecyclerviewAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public LocationRecyclerviewAdapter(ArrayList<LocationData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_location_list, viewGroup, false);

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
                        mListener.onItemClick(view, position,dataList.get(position).mainAddress) ;
                    }
                }
            }
        });

        holder.main.setText(dataList.get(position).mainAddress);
        holder.color.setText(dataList.get(position).mainAddress);

        holder.sub.setText(dataList.get(position).subAddress);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout container;
        protected TextView main;
        protected TextView sub;
        protected TextView color;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_item_location_container);
            this.main = view.findViewById(R.id.tv_item_location_main_address);
            this.sub = view.findViewById(R.id.tv_item_location_sub_address);
            this.color = view.findViewById(R.id.tv_item_location_main_address_color);


        }
    }

}
