package com.mojilab.moji.ui.main.map;

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
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.databinding.ActivityMapSearchBinding;
import com.mojilab.moji.ui.main.MainActivity;
import com.mojilab.moji.ui.main.feed.SearchFeed.Course;
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchData;
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchFeedResponse;
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchNotTagResponse;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.addCourse.LocationRecyclerviewAdapter;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetFriendsTagResponse;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class MapSearchActivity extends AppCompatActivity {

    ActivityMapSearchBinding binding;
    InputMethodManager imm;
    String inputStr;
    boolean tagSearch;
    private static final int MAP_SEARCH = 101;
    ArrayList<Course> courseArrayList;

    NetworkService networkService;

    LocationRecyclerviewAdapter locationRecyclerviewAdapter;
    private ArrayList<LocationData> locationDataArrayList = new ArrayList<>();

    final static String TAG = "MapSearchAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_map_search);
        binding.setActivity(this);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        binding.etMapSearchActSearchLocation.requestFocus();

        binding.ivMapSearchActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputStr = binding.etMapSearchActSearchLocation.getText().toString();
                setResult(MAP_SEARCH, getIntent());
                getIntent().putExtra("inputStr", inputStr);
                getIntent().putExtra("searchBtnCheck", 1);

                //키보드 내리기
                imm.hideSoftInputFromWindow(binding.etMapSearchActSearchLocation.getWindowToken(), 0);
                finish();
            }
        });
        setClickListener();


        binding.etMapSearchActSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                // 텍스트 내용이 비어있지않다면
                switch (i) {
                    // Search 버튼일경우
                    case EditorInfo.IME_ACTION_SEARCH:
                        inputStr = binding.etMapSearchActSearchLocation.getText().toString();
                        setResult(MAP_SEARCH, getIntent());
                        getIntent().putExtra("inputStr", inputStr);
                        getIntent().putExtra("searchBtnCheck", 1);

                        binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                        binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
                        //키보드 내리기
                        imm.hideSoftInputFromWindow(binding.etMapSearchActSearchLocation.getWindowToken(), 0);
                        finish();
                        break;
                    // Enter 버튼일경우
                    default:
                        return false;
                }
                return false;
            }
        });
    }

    public void setClickListener() {
        binding.etMapSearchActSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.v(TAG, " onTextChanged");

            }

            @Override
            public void afterTextChanged(Editable editable) {

                Toast.makeText(MapSearchActivity.this, "떠랑", Toast.LENGTH_SHORT).show();
                if(binding.etMapSearchActSearchLocation.getText().toString().length() > 0){
                    if(binding.etMapSearchActSearchLocation.getText().toString().charAt(0) == '#'){
                        tagSearch = true;
                    }
                    else{
                        tagSearch = false;
                    }
                    searchPost();
                }

                binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
                //통신
            }
        });

        binding.ivMapSearchActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                inputStr = binding.etMapSearchActSearchLocation.getText().toString();
                setResult(MAP_SEARCH, getIntent());
                getIntent().putExtra("inputStr", inputStr);
                getIntent().putExtra("searchBtnCheck", 1);

                binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
                //키보드 내리기
                imm.hideSoftInputFromWindow(binding.etMapSearchActSearchLocation.getWindowToken(), 0);
                finish();
            }
        });

        binding.etMapSearchActSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {


                searchPost();
            }
        });

        binding.ivMapSearchActBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    //검색 api
    public void searchPost() {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("keyword", binding.etMapSearchActSearchLocation.getText().toString());
            Log.e("ㅎㅎ","keyword"+binding.etMapSearchActSearchLocation.getText().toString());

            if (getIntent().getStringExtra("startDate") != null & getIntent().getStringExtra("endDate") != null) {

                Log.e("ㅎㅎ","startDate"+getIntent().getStringExtra("startDate"));

                jsonObject.put("startDate", getIntent().getStringExtra("startDate"));
                jsonObject.put("endDate", getIntent().getStringExtra("endDate"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Gson 라이브러리의 Json Parser을 통해 객체를 Json으로!
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        // 태그 검색인 경우
        if(tagSearch){
            Call<SearchFeedResponse> postsearch = networkService.postSearches("application/json", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY", gsonObject);

            postsearch.enqueue(new Callback<SearchFeedResponse>() {
                @Override
                public void onResponse(Call<SearchFeedResponse> call, Response<SearchFeedResponse> response) {
                    //Log.e("LOG::", response.body().toString());
                    //setContents();
                    if (response.isSuccessful()){
                        if (response.body().getStatus() == 200) {
                            Log.v("t", "검색 성공");

                            Log.e("test : ",response.body().getData().toString());

                            if(response.body().getData() == null)
                                return;

                            courseArrayList  = response.body().getData().getCourses();
                            if(courseArrayList == null){
                                return;
                            }
                            Log.e("setContents???",courseArrayList.toString());
                            setContents(courseArrayList);


                        } else if (response.body().getStatus() == 404) {
                            Log.v("T", "검색 결과 없.");
                            setContents(null);

                        } else {
                            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchFeedResponse> call, Throwable t) {
                }
            });
        }
        // 장소 검색일 경우
        else{
            Call<SearchNotTagResponse> postsearch = networkService.postNotTagSearches("application/json", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY", gsonObject);

            postsearch.enqueue(new Callback<SearchNotTagResponse>() {
                @Override
                public void onResponse(Call<SearchNotTagResponse> call, Response<SearchNotTagResponse> response) {
                    //Log.e("LOG::", response.body().toString());
                    //setContents();
                    if (response.isSuccessful()){
                        if (response.body().getStatus() == 200) {
                            Log.e("test : ",response.body().getData().toString());

                            if(response.body().getData() == null)
                                return;

                            courseArrayList  = response.body().getData().getSearchCourseRes().getCourses();
                            if(courseArrayList == null){
                                return;
                            }
                            Log.e("setContents???",courseArrayList.toString());
                            setContents(courseArrayList);


                        } else if (response.body().getStatus() == 404) {
                            Log.v("T", "검색 결과 없.");
                            setContents(null);

                        } else {
                            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<SearchNotTagResponse> call, Throwable t) {
                }
            });
        }

    }

    public void setContents(ArrayList<Course> coursesArrayList) {

        if (locationDataArrayList != null) {
            locationDataArrayList.clear();
        }

        String str;
        // 태그 검색일 경우
        if(tagSearch){
            str = binding.etMapSearchActSearchLocation.getText().toString().substring(1, binding.etMapSearchActSearchLocation.getText().toString().length());
        }
        else{
            str = binding.etMapSearchActSearchLocation.getText().toString();
        }

        if(coursesArrayList != null){
            //        Log.e("setContents",coursesArrayList.toString());
            for (int i = 0; i < coursesArrayList.size(); i++) {

                Log.e("add item :",coursesArrayList.get(i).toString()+"아아디:"+i);
                // 태그 검색
                if(tagSearch){
                    // 태그 포함하는 경우만
                    if(coursesArrayList.get(i).getCourse().getTagInfo().contains(str)){
                        locationDataArrayList.add(new LocationData(
                                coursesArrayList.get(i).getCourse().getMainAddress(),
                                coursesArrayList.get(i).getCourse().getSubAddress(),
                                Double.parseDouble(coursesArrayList.get(i).getCourse().getLat()),
                                Double.parseDouble(coursesArrayList.get(i).getCourse().getLng())
                        ));
                    }
                }
                // 장소 검색
                else{
                    // 장소가 일부 포함된 경우만
                    if(coursesArrayList.get(i).getCourse().getSubAddress().contains(str)){
                        locationDataArrayList.add(new LocationData(
                                coursesArrayList.get(i).getCourse().getMainAddress(),
                                coursesArrayList.get(i).getCourse().getSubAddress(),
                                Double.parseDouble(coursesArrayList.get(i).getCourse().getLat()),
                                Double.parseDouble(coursesArrayList.get(i).getCourse().getLng())
                        ));
                    }

                }

            }
        }

        RecyclerView mRecyclerView = binding.rvMapSearchActList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        locationRecyclerviewAdapter = new LocationRecyclerviewAdapter(locationDataArrayList, this);
        locationRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(locationRecyclerviewAdapter);

        locationRecyclerviewAdapter.setOnItemClickListener(new LocationRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String mainAddress) {

                getIntent().putExtra("inputStr", binding.etMapSearchActSearchLocation.getText().toString());

                getIntent().putExtra("searchBtnCheck", 0);
                getIntent().putExtra("lat", locationDataArrayList.get(position).lat);
                getIntent().putExtra("lng", locationDataArrayList.get(position).lng);
                Log.v(TAG, "보내는 lat=" + locationDataArrayList.get(position).lat + ", lng = " + locationDataArrayList.get(position).lng);
                getIntent().putExtra("position", position);
                setResult(MAP_SEARCH, getIntent());
                //키보드 내리기
                imm.hideSoftInputFromWindow(binding.etMapSearchActSearchLocation.getWindowToken(), 0);
                finish();
                //Activity로 돌아감
                //position얘만 있어도됨
            }
        });
    }
}
