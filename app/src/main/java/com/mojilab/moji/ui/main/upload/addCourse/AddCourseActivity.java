package com.mojilab.moji.ui.main.upload.addCourse;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddCourseBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.add.AddActivity;
import com.mojilab.moji.ui.main.upload.add.UploadImgRecyclerviewAdapter;
import com.mojilab.moji.ui.main.upload.addCourse.map.MapActivity;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetAddressDataResponse;
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCourseActivity extends AppCompatActivity {

    ActivityAddCourseBinding binding;
    NetworkService networkService;
    InputMethodManager imm;
    final String TAG = "AddCourseActivity";

    LocationRecyclerviewAdapter locationRecyclerviewAdapter;
    private ArrayList<LocationData> locationDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_add_course);
        binding.setActivity(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.etAddCourseActSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    binding.llAddCourseActHelpComment.setVisibility(View.GONE);
                    binding.llAddCourseActRvContainer.setVisibility(View.VISIBLE);
                    setData();

                    return true;
                }
                return false;
            }
        });

        binding.ivAddCourseActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.llAddCourseActHelpComment.setVisibility(View.GONE);
                binding.llAddCourseActRvContainer.setVisibility(View.VISIBLE);
                getAddressData();
            }
        });

        binding.ivAddCourseActBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 등록하기 취소 버튼
        binding.tvCancelCousreAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.etAddCourseActSearchLocation.setText("");
                binding.etAddCourseActSearchLocation.requestFocus();
            }
        });

        binding.btnAddCousreAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivityForResult(intent, 29);
            }
        });

        // 검색 창에 입력할때마다 실행
        binding.etAddCourseActSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 빈 문자일때
                if(binding.etAddCourseActSearchLocation.getText().toString().equals("")){
                    binding.llAddCourseActHelpComment.setVisibility(View.VISIBLE);
                    binding.llAddCourseActRvContainer.setVisibility(View.GONE);
                    binding.llAddCourseActEmptyContainer.setVisibility(View.GONE);
                }
            }
        });

        // 엔터키 이벤트
        binding.etAddCourseActSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                       // 검색 동작
                        binding.llAddCourseActHelpComment.setVisibility(View.GONE);
                        binding.llAddCourseActRvContainer.setVisibility(View.VISIBLE);

                        getAddressData();
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });
    }

    public void setData(){
        if(locationDataArrayList !=null){
            locationDataArrayList.clear();
        }
    }

    // 주소 검색 조회
    public void getAddressData() {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        Call<GetAddressDataResponse> getAddressDataResponse = networkService.getAddressData(binding.etAddCourseActSearchLocation.getText().toString());
        getAddressDataResponse.enqueue(new Callback<GetAddressDataResponse>() {
            @Override
            public void onResponse(Call<GetAddressDataResponse> call, Response<GetAddressDataResponse> response) {
                if (response.body().getStatus() == 200) {

                    locationDataArrayList = response.body().getData();
                    Log.v(TAG, "Get Address Success = " + locationDataArrayList.toString());

                    // 데이터 값 없을 때
                    if(locationDataArrayList.size() == 0){
                        binding.rvAddCourseActList.setVisibility(View.GONE);
                        binding.llAddCourseActEmptyContainer.setVisibility(View.VISIBLE);
                    }
                    else{
                        binding.rvAddCourseActList.setVisibility(View.VISIBLE);
                        binding.llAddCourseActEmptyContainer.setVisibility(View.GONE);
                        RecyclerView mRecyclerView = binding.rvAddCourseActList;
                        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
                        mRecyclerView.setLayoutManager(mLinearLayoutManager);

                        locationRecyclerviewAdapter = new LocationRecyclerviewAdapter(locationDataArrayList, getApplicationContext());
                        locationRecyclerviewAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(locationRecyclerviewAdapter);

                        locationRecyclerviewAdapter.setOnItemClickListener(new LocationRecyclerviewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int position, String mainAddress) {

                                if(getIntent().getIntExtra("add",0)==10){
                                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                                    intent.putExtra("main",mainAddress);
                                    intent.putExtra("lat", locationDataArrayList.get(position).lat);
                                    intent.putExtra("lng", locationDataArrayList.get(position).lng);

                                    setResult(Activity.RESULT_OK,intent);
                                }else {
                                    Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                                    intent.putExtra("main",mainAddress);
                                    intent.putExtra("lat", locationDataArrayList.get(position).lat);
                                    intent.putExtra("lng", locationDataArrayList.get(position).lng);
                                    setResult(Activity.RESULT_OK,intent);
                                }
                                finish();
                            }
                        });
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GetAddressDataResponse> call, Throwable t) {
                Log.v(TAG, "주소 검색 서버 연결 실패 = " + t.toString());
            }
        });
    }
}
