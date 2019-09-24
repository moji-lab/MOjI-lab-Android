package com.mojilab.moji.ui.main.upload.change;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.OrderData;
import com.mojilab.moji.databinding.ActivityChangeOrderBinding;

import java.util.ArrayList;

public class ChangeOrderActivity extends AppCompatActivity implements ItemDragListener{

    ActivityChangeOrderBinding binding;
    ItemTouchHelper itemTouchHelper;
    OrderRecyclerviewAdapter orderRecyclerviewAdapter;

    private ArrayList<OrderData> orderDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_order);

        setOrderRecyclerView();

    }

    public void setOrderRecyclerView(){

        OrderData orderData = new OrderData(0,1,"승희집","2019년 09월 04일");
        OrderData orderData1 = new OrderData(0,2,"누리집","2019년 09월 05일");
        OrderData orderData2 = new OrderData(0,3,"제민집","2019년 09월 06일");
        OrderData orderData3 = new OrderData(0,4,"무돌집","2019년 09월 07일");
        OrderData orderData4 = new OrderData(0,5,"영우집","2019년 09월 08일");

        orderDataArrayList.add(orderData);
        orderDataArrayList.add(orderData1);
        orderDataArrayList.add(orderData2);
        orderDataArrayList.add(orderData3);
        orderDataArrayList.add(orderData4);

        RecyclerView recyclerView = binding.rvChangeOrderActOrderList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        orderRecyclerviewAdapter = new OrderRecyclerviewAdapter(orderDataArrayList,this, this);
        recyclerView.setAdapter(orderRecyclerviewAdapter);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(orderRecyclerviewAdapter));
        itemTouchHelper.attachToRecyclerView(binding.rvChangeOrderActOrderList);
    }

    @Override
    public void onStartDrag(OrderRecyclerviewAdapter.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
