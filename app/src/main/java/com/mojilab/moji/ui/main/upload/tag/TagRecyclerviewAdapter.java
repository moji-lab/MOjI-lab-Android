package com.mojilab.moji.ui.main.upload.tag;

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
import com.google.android.gms.maps.model.Circle;
import com.mojilab.moji.R;
import com.mojilab.moji.data.TagData;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;

public class TagRecyclerviewAdapter extends RecyclerView.Adapter<TagRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<TagData> dataList = null;

    public interface OnItemClickListener {
        void onItemClick(View v, int position, boolean isChecked) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }


    public TagRecyclerviewAdapter(ArrayList<TagData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_tag, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.isChecked.setSelected(dataList.get(position).isChecked   );
        holder.isChecked.setSelected(dataList.get(position).isChecked);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isChecked.isSelected()){
                    holder.isChecked.setSelected(false);
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position,false) ;
                        }
                    }
                }else {
                    holder.isChecked.setSelected(true);
                    if (position != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(view, position,true) ;
                        }
                    }
                }
            }
        });




/*        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position ;
                if (pos != RecyclerView.NO_POSITION) {
                    // 리스너 객체의 메서드 호출.
                    if (mListener != null) {
                        mListener.onItemClick(v, pos) ;
                    }
                }
            }
        });*/

        if(dataList.get(position).photoUrl != null){
            holder.profileImg.setVisibility(View.VISIBLE);
            Glide.with(context).load(dataList.get(position).photoUrl).centerCrop().circleCrop().into(holder.profileImg);
        }else
            holder.profileImg.setVisibility(View.GONE);

        String thumb_name;
        if(dataList.get(position).nickname.length()>1){
            thumb_name = (String) dataList.get(position).nickname.subSequence(0,2);
        }else
            thumb_name = dataList.get(position).nickname;

        holder.thumb_name.setText(thumb_name);

        holder.nick_name.setText(dataList.get(position).nickname);
        //holder.email.setText(dataList.get(position).email);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout container;

        protected TextView thumb_name;
        protected TextView nick_name;
        protected TextView email;

        protected ImageView isChecked;
        protected ImageView profileImg;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_order_item_container);

            this.thumb_name = view.findViewById(R.id.tv_tag_item_nick);
            this.nick_name = view.findViewById(R.id.tv_tag_item_name);
            //this.email = view.findViewById(R._id.tv_tag_item_name);

            this.isChecked = view.findViewById(R.id.iv_tab_item_check_selector);
            this.profileImg = view.findViewById(R.id.iv_tag_item_profile_img);
        }
    }

}
