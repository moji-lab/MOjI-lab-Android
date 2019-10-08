package com.mojilab.moji.ui.main.upload.add;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
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
import com.mojilab.moji.ui.main.upload.tag.TagRecyclerviewAdapter;

import java.util.ArrayList;

import static com.makeramen.roundedimageview.RoundedImageView.TAG;

public class UploadImgRecyclerviewAdapter extends RecyclerView.Adapter<UploadImgRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<UploadImgData> dataList = null;

    public interface setOnItemClickListener {
        void onItemClick(View v, int position);
    }
    public interface setOnLockItemClickListener {
        void onLockItemClick(View v, int position, boolean isLocked);
    }

    // 리스너 객체 참조를 저장하는 변수
    private UploadImgRecyclerviewAdapter.setOnItemClickListener mListener = null ;
    private UploadImgRecyclerviewAdapter.setOnLockItemClickListener lockListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(UploadImgRecyclerviewAdapter.setOnItemClickListener listener) {
        this.mListener = listener ;
    }

    public void setOnLockItemClickListener(UploadImgRecyclerviewAdapter.setOnLockItemClickListener listener) {
        this.lockListener = listener ;
    }

    public UploadImgRecyclerviewAdapter(ArrayList<UploadImgData> list, Context context) {
        dataList = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_add_img, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.screen.setVisibility(View.GONE);

        //is대표사진
        if (position == 0) {
            holder.image.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
            holder.boss.setVisibility(View.VISIBLE);
            holder.lock.setVisibility(View.GONE);
        } else {
            holder.boss.setVisibility(View.GONE);
            holder.lock.setVisibility(View.VISIBLE);
        }

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 이미지 리스트에서 제거
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());

                Log.v(TAG, "삭제 클릭");
                if (position != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(view, position) ;
                    }
                }
            }
        });

        //이미지
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(32));

        Glide.with(context).load(dataList.get(position).image).apply(requestOptions).into(holder.image);

        //lock 상태
        holder.lock.setSelected(dataList.get(position).lock);
        dataList.get(position).lock = holder.lock.isSelected();

        //lock버튼
        holder.lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "락 클릭");
                //lock state 변경
                dataList.get(position).lock = !holder.lock.isSelected();
                holder.lock.setSelected(!holder.lock.isSelected());


                if (position != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (lockListener != null) {
                        lockListener.onLockItemClick(view, position, holder.lock.isSelected()); ;
                    }
                }
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