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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mojilab.moji.R;
import com.mojilab.moji.data.RegisteredTagData;

import java.util.ArrayList;

public class RegisteredTagRecyclerviewAdapter extends RecyclerView.Adapter<RegisteredTagRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<RegisteredTagData> dataList = null;


    public RegisteredTagRecyclerviewAdapter(ArrayList<RegisteredTagData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_tag_registered_friend, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        //-버튼
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //해당 tagInfo 리스트에서 제거
                dataList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dataList.size());
            }
        });

        //닉네임
        //길이가 2보다 클 경우, 작을경우 구분
        String thumb_name;
        if(dataList.get(position).nick_name.length()>1){
            thumb_name = (String) dataList.get(position).nick_name.subSequence(0,2);
        }else
            thumb_name = dataList.get(position).nick_name;

        holder.nick_name.setText(thumb_name);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout container;
        protected TextView nick_name;
        protected ImageView remove;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_tag_registered_item_container);
            this.nick_name = view.findViewById(R.id.rl_tag_registered_item_nick_name);
            this.remove = view.findViewById(R.id.rl_tag_registered_item_remove_btn);

        }
    }

}
