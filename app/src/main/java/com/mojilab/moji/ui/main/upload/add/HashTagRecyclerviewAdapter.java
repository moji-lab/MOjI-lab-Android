package com.mojilab.moji.ui.main.upload.add;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.data.HashTagData;

import java.util.ArrayList;

public class HashTagRecyclerviewAdapter extends RecyclerView.Adapter<HashTagRecyclerviewAdapter.ViewHolder> {

    static final String TEST = "test";
    Context context;

    private ArrayList<HashTagData> dataList = null;

    public interface OnItemClickListener {
        void onItemClick(View v, HashTagData hashTagData) ;
    }

    // 리스너 객체 참조를 저장하는 변수
    private HashTagRecyclerviewAdapter.OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(HashTagRecyclerviewAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public HashTagRecyclerviewAdapter(ArrayList<HashTagData> list, Context context) {
        dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_hash_tag_list, viewGroup, false);

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
                        mListener.onItemClick(view, dataList.get(position)) ;
                    }
                }
            }
        });

        holder.tag.setText("#"+dataList.get(position).tagInfo);
        holder.search.setText("#"+dataList.get(position).tagInfo);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        protected RelativeLayout container;
        protected TextView tag;
        protected TextView search;

        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.rl_item_hashtag_container);
            this.tag = view.findViewById(R.id.tv_item_hashtag_tag);
            this.search = view.findViewById(R.id.tv_item_hashtag_tag_search);


        }
    }

}
