package com.mojilab.moji.ui.main.upload;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.mojilab.moji.R;

import java.util.ArrayList;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.ViewHolder> {

    static final String DETAILIMG = "DetailImg";
    Context context;

    private ArrayList<String> imgList = null;


    public ImageRecyclerViewAdapter(ArrayList<String> list, Context context) {
        imgList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_upload_course_img, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return imgList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Uri uri = Uri.parse(imgList.get(position));
        Log.e("URI:","+++"+uri.toString()+"+++");
        //url
        Glide.with(context).load(uri).into(holder.img);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView img;

        public ViewHolder(View view) {
            super(view);

            this.img = view.findViewById(R.id.iv_upload_course_img_item);
        }
    }

}
