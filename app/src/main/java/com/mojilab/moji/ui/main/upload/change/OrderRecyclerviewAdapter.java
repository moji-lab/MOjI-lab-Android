package com.mojilab.moji.ui.main.upload.change;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.OrderData;
import com.mojilab.moji.ui.main.upload.ImageRecyclerViewAdapter;
import com.mojilab.moji.ui.main.upload.add.AddActivity;

import java.util.ArrayList;

public class OrderRecyclerviewAdapter extends RecyclerView.Adapter<OrderRecyclerviewAdapter.ViewHolder> implements ItemActionListener {

    static final String ID = "id";

    Context context;

    ItemDragListener itemDragListener;

    private ArrayList<OrderData> mData = null;
    private Object ViewHolder;

    public OrderRecyclerviewAdapter(ArrayList<OrderData> list, Context context, ItemDragListener itemDragListener) {
        mData = list;
        this.context = context;
        this.itemDragListener = itemDragListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rv_order, viewGroup, false);

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

                //상세페이지?
                Intent intent = new Intent(context, AddActivity.class);
                intent.putExtra(ID, mData.get(position).id);
                context.startActivity(intent);
            }
        });

        holder.btn.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    itemDragListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.order.setText(position+1 + "");
        holder.location.setText(mData.get(position).location);
        holder.date.setText(mData.get(position).date);
    }

    @Override
    public void onItemMoved(int from, int to) {
        if (from == to) {
            return;
        }

        OrderData fromItem = mData.remove(from);
        mData.add(to, fromItem);
        notifyItemMoved(from, to);
        notifyDataSetChanged();

    }

    @Override
    public void onItemSwiped(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout container;
        protected TextView order;
        protected TextView location;
        protected TextView date;
        protected ImageView btn;


        public ViewHolder(View view) {
            super(view);

            this.container = view.findViewById(R.id.cv_order_item_container);
            this.order = view.findViewById(R.id.tv_order_item_order);
            this.location = view.findViewById(R.id.tv_order_item_location);
            this.date = view.findViewById(R.id.tv_order_item_date);
            this.btn = view.findViewById(R.id.ib_order_item_drag_btn);
        }
    }

}
