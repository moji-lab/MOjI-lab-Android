package com.mojilab.moji.ui.main.upload.tag;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
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
import com.mojilab.moji.ui.main.upload.UploadActivity;

import java.util.ArrayList;

public class TagActivity extends BaseActivity<ActivityTagBinding, TagViewModel> implements TagNavigator {

    ActivityTagBinding binding;
    TagViewModel viewModel;
    InputMethodManager imm;


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

        //태그 된 친구가 1명 이상 일 경우,

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.etTagActWriteFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    setSearchResult();

                    return true;
                }
                return false;
            }
        });

        binding.ivTagActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchResult();
            }
        });

        storeIdx();

    }

    public void storeIdx(){
        binding.rlTagActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);

                if(registeredTagData == null){
                    Log.e("데이터X,,,",registeredTagData.size()+"");
                    finish();
                    return;
                }

                String idxList = new String();
                for(int i=0;i<registeredTagData.size();i++){
                    if(i!=0){
                        idxList +=",";
                    }
                    idxList +=registeredTagData.get(i).idx;
                }

                intent.putExtra("idxList",idxList);
                setResult(Activity.RESULT_OK,intent);
                Log.e("데이터0,,,",idxList);

                finish();
            }
        });
    }

    //더하기
    public void setRegisteredRecyclerView(RegisteredTagData addData) {

        registeredTagData.add(addData);

        RecyclerView recyclerView = binding.rvTagActFriendRegisteredList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        registeredTagRecyclerviewAdapter = new RegisteredTagRecyclerviewAdapter(registeredTagData, this);
        registeredTagRecyclerviewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(registeredTagRecyclerviewAdapter);

        storeIdx();

    }

    //삭제
    public void setRegisteredRecyclerView(int idx) {

        for(int i=0;i<registeredTagData.size();i++){
            if(registeredTagData.get(i).idx == idx){
                registeredTagData.remove(i);
            }
        }

        if(registeredTagData == null){
            return;
        }

        RecyclerView recyclerView = binding.rvTagActFriendRegisteredList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        registeredTagRecyclerviewAdapter = new RegisteredTagRecyclerviewAdapter(registeredTagData, this);
        registeredTagRecyclerviewAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(registeredTagRecyclerviewAdapter);

        storeIdx();
    }

    public void setSearchResult() {
        if (true) {
            binding.llTagActListContainer.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(binding.etTagActWriteFriend.getWindowToken(), 0);
        } else {
            binding.llTagActListContainer.setVisibility(View.GONE);
        }
    }

    public void setOrderRecyclerView() {

        TagData orderData = new TagData(0, "송", "0603yang@naver.com", false);
        TagData orderData1 = new TagData(1, "초록괴물", "060325yang@gmail.com", false);
        TagData orderData2 = new TagData(2, "수면양말", "060325yang@gmail.com", false);
        TagData orderData3 = new TagData(3, "양광규", "0603yang@naver.com", false);
        TagData orderData4 = new TagData(4, "송이버섯", "0603yang@naver.com", false);
        TagData orderData5 = new TagData(5, "초록괴물", "060325yang@gmail.com", false);
        TagData orderData6 = new TagData(6, "수면양말", "060325yang@gmail.com", false);
        TagData orderData7 = new TagData(7, "양광규", "0603yang@naver.com", false);

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

        tagRecyclerviewAdapter = new TagRecyclerviewAdapter(tagDataArrayList, this);
        recyclerView.setAdapter(tagRecyclerviewAdapter);

        tagRecyclerviewAdapter.setOnItemClickListener(new TagRecyclerviewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position, boolean isChecked) {
                Log.e("어뎁터리스너", "?");

                if (isChecked) {
                    binding.rlTagActAddBtn.setSelected(true);
                    RegisteredTagData addData = new RegisteredTagData(tagDataArrayList.get(position).id, tagDataArrayList.get(position).nick_name);
                    setRegisteredRecyclerView(addData);
                } else {
                    binding.rlTagActAddBtn.setSelected(false);
                    setRegisteredRecyclerView(tagDataArrayList.get(position).id);
                }

            }
        });
    }
}
