package com.mojilab.moji.ui.main.upload.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class AddActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    static final int ADDRESS_ACTIVITY= 123;

    SQLiteDatabase database;
    DatabaseHelper helper;

    String location;

    CourseData courseData = new CourseData();
    CourseTable courseTable;

    ActivityAddBinding binding;
    AddViewModel viewModel;

    UploadImgRecyclerviewAdapter uploadImgRecyclerviewAdapter;
    private ArrayList<UploadImgData> uploadImgDataArrayList = new ArrayList<>();

    int ACCESSGALLERY = 2018;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);

        test();

        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        courseTable = new CourseTable(this);


        binding.rlAddActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUploadData();
            }
        });
    }

    public void test() {
        /*        if(binding.etAddActWriteLocation.getText()!= null || binding.etAddActSelectDate.getText() != null
                || binding.etAddActContents.getText()!= null || binding.etAddActTag.getText()!= null){
            viewModel.isSubmit.setValue(true);
        }*/

        Log.e("test", binding.etAddActSelectDate.getText().toString() + "/" + binding.etAddActTag.getText() + "/" + binding.etAddActContents.getText());

/*        if(binding.etAddActSelectDate.getText() != null &&
                 binding.etAddActContents.getText() != null && binding.etAddActTag.getText() != null){
            viewModel.isSubmit.setValue(true);
            binding.rlAddActAddBtn.setSelected(true);
            Log.e("null","?");
        }else {
            binding.rlAddActAddBtn.setSelected(false);
            Log.e("not null","?");
        }*/

    }

    @Override
    public void callAddCourseActivity() {
        Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
        intent.putExtra("add",10);
        startActivityForResult(intent,ADDRESS_ACTIVITY);
    }

    public void accessCameraGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Log.e("accessCameraGallery", intent.getData().toString());

        startActivityForResult(intent, ACCESSGALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "들어왔능가2" + data);

        if (requestCode == ACCESSGALLERY) {
            Log.e("onActivityResult", "들어왔능가1" + data);
            Log.e("onActivityResult", "들어왔능가0" + data);

            if (data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    Log.e("URI0:",imageUri.toString());
                    Log.e("URI1:","+++"+getRealPathFromURI(imageUri)+"+++");

                    setCourseRecyclerView(getRealPathFromURI(imageUri));
                }
            } else if (data.getData() != null) {
                Uri imagePath = data.getData();
                setCourseRecyclerView(getRealPathFromURI(imagePath));
            }
        }

        if (requestCode == ADDRESS_ACTIVITY) {
            location = data.getStringExtra("main");
            binding.etAddActWriteLocation.setText(location);
            binding.ivAddActLocSelector.setSelected(true);
        }
    }

    public String getRealPathFromURI(Uri contentUri){
        Cursor cursor = null;

        String[] proj = new String[]{MediaStore.Images.Media.DATA};
        cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void callDatePicker() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            binding.etAddActSelectDate.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");
            courseData.visitTime = year + "-" + monthOfYear + "-" + dayOfMonth;
        }
    };

    public void storeUploadData() {

        //해시태그 처리 안함!!!!!

        //courseData.mainAddress = binding.etAddActWriteLocation.getText().toString();
/*        if(binding.etAddActContents.getText() !=null && courseData.photos.size() == 0 && courseData.share.size() ==0 ){

            Log.e("size", courseData.photos.size()+"");
            Toast.makeText(this, "모든 양식을 채워야 저장이 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }*/

        courseData.mainAddress = location;
        courseData.subAddress = "경기도 안양시 만안구";
        courseData.lat = (float) 1.3;
        courseData.log = (float) 3.5;

        courseData.content = binding.etAddActContents.getText().toString();

        courseData.photos = new ArrayList<>();
        courseData.share = new ArrayList<>();

        for(int i = 0; i<uploadImgDataArrayList.size();i++){

            courseData.photos.add(uploadImgDataArrayList.get(i).image.toString());

            //잠기면 true 1
            //안잠기만 false 0
            if(uploadImgDataArrayList.get(i).lock)
                courseData.share.add(1);
            else
                courseData.share.add(0);
        }

        courseData.order = courseTable.getCount()+1; //데이터 개수 조회 한 후, 삽입

        //데이터 insert
        courseTable.insertData(courseData);

        Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
        setResult(Activity.RESULT_OK,intent);

        finish();
    }

    public void setCourseRecyclerView(String testImg) {

        binding.rvAddActImgList.setVisibility(View.VISIBLE);

        UploadImgData uploadImgData = new UploadImgData(0, false, true, testImg);

        uploadImgDataArrayList.add(uploadImgData);

        RecyclerView mRecyclerView = binding.rvAddActImgList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        uploadImgRecyclerviewAdapter = new UploadImgRecyclerviewAdapter(uploadImgDataArrayList, this);
        mRecyclerView.setAdapter(uploadImgRecyclerviewAdapter);
    }
}
