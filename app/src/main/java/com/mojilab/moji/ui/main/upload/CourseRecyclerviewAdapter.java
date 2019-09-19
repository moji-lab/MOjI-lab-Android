package com.mojilab.moji.ui.main.upload;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.data.CourseData;

import java.util.ArrayList;

public class CourseRecyclerviewAdapter extends RecyclerView.Adapter<CourseRecyclerviewAdapter.ViewHolder> {

    static final String DETAILIMG = "DetailImg";
    Context context;

    private ArrayList<CourseData> mData = null ;


    public CourseRecyclerviewAdapter(ArrayList<CourseData> list, Context context) {
        mData = list ;
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
        //holder.container;
        holder.order.setText(mData.get(position).order+"");
        holder.location.setText(mData.get(position).location);
        holder.date.setText(mData.get(position).date);
        holder.contents.setText(mData.get(position).contents);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected LinearLayout container;
        protected TextView order;
        protected TextView location;
        protected TextView date;
        //리사이클러뷰 하..ㅜ
        protected TextView contents;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.ll_upload_course_item_container);
            this.order = view.findViewById(R.id.tv_upload_course_item_order);
            this.location = view.findViewById(R.id.tv_upload_course_item_location);
            this.date = view.findViewById(R.id.tv_upload_course_item_date);
            //리사이클러뷰....
            this.contents = view.findViewById(R.id.tv_upload_course_item_contents);

        }
    }

}
