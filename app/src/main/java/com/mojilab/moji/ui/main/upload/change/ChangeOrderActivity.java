package com.mojilab.moji.ui.main.upload.change;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.OrderData;
import com.mojilab.moji.data.RegisteredTagData;
import com.mojilab.moji.databinding.ActivityChangeOrderBinding;
import com.mojilab.moji.ui.main.upload.tag.TagRecyclerviewAdapter;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;

import java.util.ArrayList;

public class ChangeOrderActivity extends AppCompatActivity implements ItemDragListener {

    ActivityChangeOrderBinding binding;
    ItemTouchHelper itemTouchHelper;
    OrderRecyclerviewAdapter orderRecyclerviewAdapter;

    SQLiteDatabase database;
    DatabaseHelper helper;

    ArrayList<CourseData> courseDataArrayList = new ArrayList<>();
    CourseData courseData = new CourseData();
    CourseTable courseTable;

    private ArrayList<OrderData> orderDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_order);
        binding.rlChangeOrderActAddBtn.setSelected(true);
        binding.rlChangeOrderActAddBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                clickSubmitBtn();
            }

        });


        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        courseTable = new CourseTable(this);

        setOrderRecyclerView();

    }

    public void clickSubmitBtn() {
        Log.e("clickSubmitBtn 데이터 확인", orderDataArrayList.get(0).location.toString());
        //순서 변경

        for(int i=0;i<orderDataArrayList.size();i++){
            if((i+1) != orderDataArrayList.get(i).order){

                //db 수정
                Log.e("change0",orderDataArrayList.get(i).order+", cnt :"+i);
                orderDataArrayList.get(i).order = i+1;
                Log.e("change1",orderDataArrayList.get(i).order+", cnt :"+i);
            }
        }

        finish();
        //setResult();
    }

    public void setOrderRecyclerView() {

        OrderData orderData = new OrderData(0, 1, "승희집", "2019년 09월 04일");
        OrderData orderData1 = new OrderData(0, 2, "누리집", "2019년 09월 05일");
        OrderData orderData2 = new OrderData(0, 3, "제민집", "2019년 09월 06일");
        OrderData orderData3 = new OrderData(0, 4, "무돌집", "2019년 09월 07일");
        OrderData orderData4 = new OrderData(0, 5, "영우집", "2019년 09월 08일");

        orderDataArrayList.add(orderData);
        orderDataArrayList.add(orderData1);
        orderDataArrayList.add(orderData2);
        orderDataArrayList.add(orderData3);
        orderDataArrayList.add(orderData4);

        RecyclerView recyclerView = binding.rvChangeOrderActOrderList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        orderRecyclerviewAdapter = new OrderRecyclerviewAdapter(orderDataArrayList, this, this);
        recyclerView.setAdapter(orderRecyclerviewAdapter);

        itemTouchHelper = new ItemTouchHelper(new ItemTouchHelperCallback(orderRecyclerviewAdapter));
        itemTouchHelper.attachToRecyclerView(binding.rvChangeOrderActOrderList);
    }

    @Override
    public void onStartDrag(OrderRecyclerviewAdapter.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }


    public void test(){
        if (courseDataArrayList != null)
            courseDataArrayList.clear();
        courseDataArrayList = courseTable.selectData();

        if (courseDataArrayList == null)
            return;

/*        for(int i =0; i <courseDataArrayList.size();i++){
            courseDataArrayList.get(i).order;
            courseDataArrayList.get(i).visitTime;
            courseDataArrayList.get(i).mainAddress;

        }*/
    }
}
