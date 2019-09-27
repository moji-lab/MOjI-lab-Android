package com.mojilab.moji.ui.main.upload.change;

import android.app.Activity;
import android.content.Intent;
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
import com.mojilab.moji.ui.main.upload.UploadActivity;
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

        //변화 없을 경우. 조건 다시 걸기
        if(orderDataArrayList == null){
            finish();
        }

        Log.e("clickSubmitBtn 데이터 확인", orderDataArrayList.get(0).location+", id :"+orderDataArrayList.get(0).id+", order :"+orderDataArrayList.get(0).order);

        for(int i=0;i<orderDataArrayList.size();i++){

            //순서 변경
            if((i+1) != orderDataArrayList.get(i).order){
                //db 수정
                //index가 작은 애 부터 시작하겠다!
                Log.e("change0",orderDataArrayList.get(i).order+", cnt :"+(i+1));
                courseTable.updateOrderData(orderDataArrayList.get(i).id,orderDataArrayList.get(i).order, i+1);
                //orderDataArrayList.get(i).order = i+1;
                Log.e("change1",orderDataArrayList.get(i).order+", cnt :"+(i+1));
            }
        }

        //변화가 있 경우, DB에 order 칼럼 내용 수정
        //order값이랑 idx

        Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setOrderRecyclerView() {


        if (courseDataArrayList != null)
            courseDataArrayList.clear();

        courseDataArrayList = courseTable.selectData();

        if(courseDataArrayList == null)
            return;

        for(int i = 0;i<courseDataArrayList.size();i++){
            OrderData orderData = new OrderData(courseDataArrayList.get(i).id, courseDataArrayList.get(i).order, courseDataArrayList.get(i).mainAddress, courseDataArrayList.get(i).visitTime);
            Log.e("orderDataItem",orderData.id+"<-id"+orderData.order+orderData.location+"이전순서/장소");
            orderDataArrayList.add(orderData);
        }



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

}
