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

        setClickListener();


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
                if(binding.etMapSearchActSearchLocation.getText().toString() != null){
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

                binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
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
        Call<SearchFeedResponse> postsearch = networkService.postSearches("application/json", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY", gsonObject);

        postsearch.enqueue(new Callback<SearchFeedResponse>() {
            @Override
            public void onResponse(Call<SearchFeedResponse> call, Response<SearchFeedResponse> response) {
                //Log.e("LOG::", response.body().toString());
                //setContents();
                if (response.body().getStatus() == 200) {
                    Log.v("t", "검색 성공");

                    if(response.body().getData() == null)
                        return;

                    Log.e("test : ",response.body().getData().toString());

                    ArrayList<Course> courseArrayList  = response.body().getData().getCourses();
                    if(courseArrayList == null){
                        return;
                    }
                    Log.e("setContents???",courseArrayList.toString());
                    setContents(courseArrayList);


                } else if (response.body().getStatus() == 404) {
                    Log.v("T", "검색 결과 없.");
                    searchPost();

                } else {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<SearchFeedResponse> call, Throwable t) {

            }
        });
    }

    public void setContents(ArrayList<Course> coursesArrayList) {


        if (locationDataArrayList != null) {
            Log.e("보여랏0 :","왜안보이징");
            locationDataArrayList.clear();
        }

        Log.e("setContents",coursesArrayList.toString());
        for (int i = 0; i < coursesArrayList.size() - 1; i++) {

            Log.e("add item :",coursesArrayList.get(i).toString()+"아아디:"+i);

            locationDataArrayList.add(new LocationData(
                    coursesArrayList.get(i).getCourse().getMainAddress(),
                    coursesArrayList.get(i).getCourse().getSubAddress(),
                    Double.parseDouble(coursesArrayList.get(i).getCourse().getLat()),
                    Double.parseDouble(coursesArrayList.get(i).getCourse().getLng())
            ));

        }


        Log.e("보여랏1 :","왜안보이징");
        RecyclerView mRecyclerView = binding.rvMapSearchActList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        locationRecyclerviewAdapter = new LocationRecyclerviewAdapter(locationDataArrayList, this);
        locationRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(locationRecyclerviewAdapter);

        locationRecyclerviewAdapter.setOnItemClickListener(new LocationRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String mainAddress) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("search", binding.etMapSearchActSearchLocation.getText().toString());
                intent.putExtra("data", position);
                setResult(Activity.RESULT_OK, intent);
                finish();
                //Activity로 돌아감
                //position얘만 있어도됨
            }
        });
    }
}
