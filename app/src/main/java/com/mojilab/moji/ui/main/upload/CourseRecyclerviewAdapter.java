package com.mojilab.moji.ui.main.upload;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.mojilab.moji.R;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.ui.main.upload.add.AddActivity;

import java.util.ArrayList;

public class CourseRecyclerviewAdapter extends RecyclerView.Adapter<CourseRecyclerviewAdapter.ViewHolder> {

    static final String DETAILIMG = "DetailImg";

    ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    Context context;

    private ArrayList<CourseData> mData = null;

    public CourseRecyclerviewAdapter(ArrayList<CourseData> list, Context context) {
        mData = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_upload_course, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddActivity.class);
                //intent.putExtra("_id",mData.get(position).order);
                context.startActivity(intent);
            }
        });


        holder.order.setText(mData.get(position).order + "");
        holder.location.setText(mData.get(position).mainAddress);
        holder.date.setText(mData.get(position).visitTime);
        holder.contents.setText(mData.get(position).content);

        //holder.recyclerView
        RecyclerView mRecyclerView = holder.recyclerView;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(mData.get(position).photos,context);
        mRecyclerView.setAdapter(imageRecyclerViewAdapter);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout container;
        protected TextView order;
        protected TextView location;
        protected TextView date;
        protected RecyclerView recyclerView;
        protected TextView contents;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.ll_upload_course_item_container);
            this.order = view.findViewById(R.id.tv_upload_course_item_order);
            this.location = view.findViewById(R.id.tv_upload_course_item_location);
            this.date = view.findViewById(R.id.tv_upload_course_item_date);
            this.recyclerView = view.findViewById(R.id.rv_upload_course_item_img_list);
            this.contents = view.findViewById(R.id.tv_upload_course_item_contents);

        }
    }

}
