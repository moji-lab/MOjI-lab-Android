package com.mojilab.moji.ui.main.upload.tag;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.RegisteredTagData;
import com.mojilab.moji.data.TagData;
import com.mojilab.moji.databinding.ActivityTagBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetFriendsTagResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class TagActivity extends BaseActivity<ActivityTagBinding, TagViewModel> implements TagNavigator {

    final String TAG = "TagActivity ::";

    ActivityTagBinding binding;
    TagViewModel viewModel;
    InputMethodManager imm;

    NetworkService networkService;

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

        //setOrderRecyclerView();

        //태그 된 친구가 1명 이상 일 경우,

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.etTagActWriteFriend.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String str = binding.etTagActWriteFriend.getText().toString();
                    if (str.length() > 0) {
                        getFriendsTagResponse(str);
                    }

                    return true;
                }
                return false;
            }
        });

        binding.ivTagActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = binding.etTagActWriteFriend.getText().toString();
                if (str.length() > 0) {
                    getFriendsTagResponse(str);
                }
            }
        });

        storeIdx();

    }

    public void storeIdx() {
        binding.rlTagActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), UploadActivity.class);

                if (registeredTagData.size() == 0) {
                    Toast.makeText(TagActivity.this, "한 명 이상의 친구를 등록 해 주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                String idxList = new String();
                for (int i = 0; i < registeredTagData.size(); i++) {
                    if (i != 0) {
                        idxList += ",";
                    }
                    idxList += registeredTagData.get(i).idx;
                }

                intent.putExtra("idxList", idxList);
                setResult(Activity.RESULT_OK, intent);
                Log.e("데이터0,,,", idxList);

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

        for (int i = 0; i < registeredTagData.size(); i++) {
            if (registeredTagData.get(i).idx == idx) {
                registeredTagData.remove(i);
            }
        }

        if (registeredTagData == null) {
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

    public void setSearchResult(boolean is) {
        if (is) {
            binding.llTagActListContainer.setVisibility(View.VISIBLE);
            binding.llTagActNoResultContainer.setVisibility(View.GONE);
            imm.hideSoftInputFromWindow(binding.etTagActWriteFriend.getWindowToken(), 0);
        } else {
            binding.llTagActListContainer.setVisibility(View.GONE);
            binding.llTagActNoResultContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setOrderRecyclerView(TagData tagData) {

        if(tagDataArrayList != null)
            tagDataArrayList.clear();
        Log.e("tagData",tagData.toString());
        tagDataArrayList.add(tagData);

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
                    RegisteredTagData addData = new RegisteredTagData(tagDataArrayList.get(position).userIdx, tagDataArrayList.get(position).nickname, tagDataArrayList.get(position).photoUrl);
                    setRegisteredRecyclerView(addData);
                } else {
                    binding.rlTagActAddBtn.setSelected(false);
                    setRegisteredRecyclerView(tagDataArrayList.get(position).userIdx);
                }

            }
        });
    }


    public void getFriendsTagResponse(String keyword) {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        Call<GetFriendsTagResponse> getFriendsTagResponse = networkService.getFriendsTagResponse("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY", keyword);

        getFriendsTagResponse.enqueue(new Callback<GetFriendsTagResponse>() {
            @Override
            public void onResponse(Call<GetFriendsTagResponse> call, Response<GetFriendsTagResponse> response) {
                Log.e("LOG::", response.toString());
                if (response.isSuccessful()) {
                    Log.e("LOG1::", String.valueOf(response.body().getStatus()));
                    if (response.body().getStatus() == 200) {
                        Log.v(TAG, "조회 성공");
                        setSearchResult(true);

                        //동그라미 아이템 아이디와 비교하여
                        //같으면 true처리하기!
                        for (int i =0; i<registeredTagData.size();i++){
                            if(response.body().getData().userIdx  == registeredTagData.get(i).idx)
                            {
                                setOrderRecyclerView(new TagData(response.body().getData().email,response.body().getData().nickname,response.body().getData().userIdx,response.body().getData().photoUrl,true));
                                return;
                            }
                            Log.v(TAG+":::", "1."+response.body().getData().userIdx +"2."+registeredTagData.get(i).idx );
                        }

                        setOrderRecyclerView(new TagData(response.body().getData().email,response.body().getData().nickname,response.body().getData().userIdx,response.body().getData().photoUrl,false));


                    }else if (response.body().getStatus() == 404) {
                        Log.v(TAG, "검색한 사용자가 존재하지 않습니다.");
                        setSearchResult(false);


                    } else {
                        Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                    }
                }else
                {
                    Log.e("LOG1::", String.valueOf(response));

                }

            }

            @Override
            public void onFailure(Call<GetFriendsTagResponse> call, Throwable t) {

            }
        });
    }
}
