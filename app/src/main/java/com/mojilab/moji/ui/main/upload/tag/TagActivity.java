package com.mojilab.moji.ui.main.upload.tag;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.RegisteredTagData;
import com.mojilab.moji.data.TagData;
import com.mojilab.moji.databinding.ActivityTagBinding;

import java.util.ArrayList;

public class TagActivity extends BaseActivity<ActivityTagBinding, TagViewModel> implements TagNavigator {

    ActivityTagBinding binding;
    TagViewModel viewModel;


    TagRecyclerviewAdapter tagRecyclerviewAdapter;
    private ArrayList<TagData> tagDataArrayList = new ArrayList<>();

    RegisteredTagRecyclerviewAdapter registeredTagRecyclerviewAdapter;
    private ArrayList<RegisteredTagData> registeredTagData = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_tag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(TagViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setTagViewModel(viewModel);

        setOrderRecyclerView();
        setRegisteredRecyclerView();

        binding.ivTagActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터 크기 1 이상일 경우
                if(true){
                    binding.llTagActListContainer.setVisibility(View.VISIBLE);
                }else{
                    binding.llTagActListContainer.setVisibility(View.GONE);
                }
            }
        });

        binding.rlTagActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //통신
                finish();
            }
        });
    }

    public void setRegisteredRecyclerView(){
        //체크하면 등록되도록

        RegisteredTagData registeredData = new RegisteredTagData(0,"송");
        RegisteredTagData registeredData1 = new RegisteredTagData(1,"초록괴물");
        RegisteredTagData registeredData2 = new RegisteredTagData(2,"수면양말");
        RegisteredTagData registeredData3 = new RegisteredTagData(3,"양광규");
        RegisteredTagData registeredData4 = new RegisteredTagData(4,"송이버섯");
        RegisteredTagData registeredData5 = new RegisteredTagData(5,"초록괴물");

        registeredTagData.add(registeredData);
        registeredTagData.add(registeredData1);
        registeredTagData.add(registeredData2);
        registeredTagData.add(registeredData3);
        registeredTagData.add(registeredData4);
        registeredTagData.add(registeredData5);

        RecyclerView recyclerView = binding.rvTagActFriendRegisteredList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        registeredTagRecyclerviewAdapter = new RegisteredTagRecyclerviewAdapter(registeredTagData,this);
        recyclerView.setAdapter(registeredTagRecyclerviewAdapter);
    }

    public void setOrderRecyclerView(){

        TagData orderData = new TagData(0,"송","0603yang@naver.com",false);
        TagData orderData1 = new TagData(1,"초록괴물","060325yang@gmail.com", false);
        TagData orderData2 = new TagData(2,"수면양말","060325yang@gmail.com", false);
        TagData orderData3 = new TagData(3,"양광규","0603yang@naver.com", false);
        TagData orderData4 = new TagData(4,"송이버섯","0603yang@naver.com",false);
        TagData orderData5 = new TagData(5,"초록괴물","060325yang@gmail.com", false);
        TagData orderData6 = new TagData(6,"수면양말","060325yang@gmail.com", false);
        TagData orderData7 = new TagData(7,"양광규","0603yang@naver.com", false);

        tagDataArrayList.add(orderData);
        tagDataArrayList.add(orderData1);
        tagDataArrayList.add(orderData2);
        tagDataArrayList.add(orderData3);
        tagDataArrayList.add(orderData4);
        tagDataArrayList.add(orderData5);
        tagDataArrayList.add(orderData6);
        tagDataArrayList.add(orderData7);


        RecyclerView recyclerView = binding.rvTagActFriendList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        tagRecyclerviewAdapter = new TagRecyclerviewAdapter(tagDataArrayList,this);
        recyclerView.setAdapter(tagRecyclerviewAdapter);
    }
}
