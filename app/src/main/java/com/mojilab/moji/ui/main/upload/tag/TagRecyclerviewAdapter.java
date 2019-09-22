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
import com.mojilab.moji.data.TagData;
import com.mojilab.moji.data.UploadImgData;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class TagRecyclerviewAdapter extends RecyclerView.Adapter<TagRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<TagData> dataList = null;


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

        holder.isChecked.setSelected(dataList.get(position).isChecked);

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.isChecked.isSelected()){
                    holder.isChecked.setSelected(false);
                }else
                    holder.isChecked.setSelected(true);
            }
        });

        String thumb_name = (String) dataList.get(position).nick_name.subSequence(0,2);
        holder.thumb_name.setText(thumb_name);

        holder.nick_name.setText(dataList.get(position).nick_name);
        //holder.email.setText(dataList.get(position).email);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout container;

        protected TextView thumb_name;
        protected TextView nick_name;
        protected TextView email;

        protected ImageView isChecked;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_order_item_container);

            this.thumb_name = view.findViewById(R.id.tv_tag_item_nick);
            this.nick_name = view.findViewById(R.id.tv_tag_item_name);
            //this.email = view.findViewById(R.id.tv_tag_item_name);

            this.isChecked = view.findViewById(R.id.iv_tab_item_check_selector);

        }
    }

}
