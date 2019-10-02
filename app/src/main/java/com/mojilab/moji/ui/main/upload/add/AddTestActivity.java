package com.mojilab.moji.ui.main.upload.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.service.Common;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.HashTagData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetHashTagResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTestActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    static final int ADDRESS_ACTIVITY = 123;
    final String TAG = "AddActivity ::";

    SQLiteDatabase database;
    DatabaseHelper helper;

    String location;

    CourseData courseData = new CourseData();
    CourseTable courseTable;

    ActivityAddBinding binding;
    AddViewModel viewModel;

    NetworkService networkService;

    UploadImgRecyclerviewAdapter uploadImgRecyclerviewAdapter;
    private ArrayList<UploadImgData> uploadImgDataArrayList = new ArrayList<>();

    HashTagRecyclerviewAdapter hashTagRecyclerviewAdapter;
    private ArrayList<HashTagData> hashTagDataArrayList = new ArrayList<>();

    int ACCESSGALLERY = 2018;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("코스추가를 종료하시면, 작성중이던 데이터가 삭제됩니다. 그래도 종료하시겠습니까?");
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);

        //내부db
        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        courseTable = new CourseTable(this);

        //통신
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);


        // 확인 버튼
        binding.rlAddActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    @Override
    public void callAddCourseActivity() {
        Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
        intent.putExtra("add", 10);
        startActivityForResult(intent, ADDRESS_ACTIVITY);
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

            if (data != null) {
                //사진 선택하지 않고 나왔을 때 처리
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        Log.e("test transform origin :", imageUri.toString());
                        Log.e("URI0:", imageUri.toString());

                        File file;
                        String fileName = imageUri.getLastPathSegment();
                        try {
                            file = File.createTempFile(fileName, ".jpg", getCacheDir());

                            Log.e("test0", file.getAbsoluteFile() + "");
                            Log.e("test1", file.getName() + "");
                            Log.e("test2", file.getPath() + "");
                            Log.e("test3", file.getAbsolutePath() + "");
                            Log.e("test4", file.toURI() + "");
                            Log.e("test5", getCacheDir().getAbsoluteFile() + "");
                            Log.e("test6", getApplication().fileList().length + "");
                            Log.e("test7", Environment.getDownloadCacheDirectory() + "");

                            File callFile = new File(getCacheDir().toString());
                            File[] files = callFile.listFiles();
                            for (File tempFile : files) {

                                Log.d("MyTag", tempFile.getName());

                                Log.d("MyTag", " "+tempFile.getName()+" ");
                                Log.d("MyTag", tempFile.getPath());
                                Log.d("MyTag", tempFile.toString());
                                Log.d("MyTag", String.valueOf(tempFile.getClass()));


                                //Uri photoUri = Uri.fromFile(file1.path);
                                //Glide.with(this).load(file.getPath()).into(binding.ivAddActUploadImg);
                                grantUriPermission( getPackageName(), FileProvider.getUriForFile(this,"com.mojilab.moji.fileprovider",tempFile) , Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                //Glide.with(this).load(FileProvider.getUriForFile(this,"com.mojilab.moji.fileprovider",tempFile)).into(binding.ivAddActUploadImg);

                                Uri imgUri = FileProvider.getUriForFile(this,"com.mojilab.moji.fileprovider",tempFile);
                                Glide.with(this).load(Uri.parse("file:/"+file.getAbsoluteFile())).into(binding.ivAddActUploadImg);
                                //Uri.parse("content:/"+tempFile.getAbsoluteFile())

                                //Glide.with(this).load(imageUri).into(binding.ivAddActUploadImg);
                                Log.e("??","file:/"+file.getAbsoluteFile());
                                Log.e("??",imageUri.toString());

                                Log.e("Change",FileProvider.getUriForFile(this,"com.mojilab.moji.fileprovider",tempFile).toString());

                                //getApplication().deleteFile(fileName);

                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Log.e("URI1:", "+++" + getRealPathFromURI(imageUri) + "+++");
                        //File imgFile = new File(getRealPathFromURI(imageUri));

                        setCourseRecyclerView(imageUri.toString());
                    }
                }
            }
            return;

        }

        if (requestCode == ADDRESS_ACTIVITY) {
            if (data == null) {
                return;
            }
            location = data.getStringExtra("main");
            binding.etAddActWriteLocation.setText(location);
            binding.ivAddActLocSelector.setSelected(true);
        }
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
            binding.etAddActSelectDate.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
            courseData.visitTime = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
        }
    };

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
