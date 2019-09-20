package com.mojilab.moji.ui.main.upload.add;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;

import java.util.ArrayList;

public class AddActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    Uri testImg ;
    ActivityAddBinding binding;
    AddViewModel viewModel;

    UploadImgRecyclerviewAdapter uploadImgRecyclerviewAdapter;
    private ArrayList<UploadImgData> uploadImgDataArrayList = new ArrayList<>();

    @Override
    public int getLayoutId() { return R.layout.activity_add; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);

        binding.ivAddActUploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        //setCourseRecyclerView();
    }

    @Override
    public void callAddCourseActivity() {
        startActivity(new Intent(getApplicationContext(), AddCourseActivity.class));
    }

    public void test(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,333);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==333){
            if(requestCode == Activity.RESULT_OK){
                if(data !=null){

                    testImg = data.getData();
                    setCourseRecyclerView();

                }
            }
        }
    }

    public void setCourseRecyclerView(){

/*        UploadImgData uploadImgData = new UploadImgData(0,false,true,"https://images.otwojob.com/product/x/U/6/xU6PzuxMzIFfSQ9.jpg/o2j/resize/852x622%3E");
        UploadImgData uploadImgData1 = new UploadImgData(1,true,false,"https://images.otwojob.com/product/x/U/6/xU6PzuxMzIFfSQ9.jpg/o2j/resize/852x622%3E");
        UploadImgData uploadImgData2 = new UploadImgData(2,true,false,"https://t1.daumcdn.net/cfile/tistory/234AD34C55A896901A");
        UploadImgData uploadImgData3 = new UploadImgData(3,false,false,"https://t1.daumcdn.net/cfile/tistory/234AD34C55A896901A");
        UploadImgData uploadImgData4 = new UploadImgData(4,false,false,"https://t1.daumcdn.net/cfile/tistory/263E6B4C55A8969037"); */

        UploadImgData uploadImgData = new UploadImgData(0,false,true,testImg);
        UploadImgData uploadImgData1 = new UploadImgData(1,true,false,testImg);
        UploadImgData uploadImgData2 = new UploadImgData(2,false,false,testImg);
        UploadImgData uploadImgData3 = new UploadImgData(3,false,false,testImg);
        UploadImgData uploadImgData4 = new UploadImgData(4,false,false,testImg);

        uploadImgDataArrayList.add(uploadImgData);
        uploadImgDataArrayList.add(uploadImgData1);
        uploadImgDataArrayList.add(uploadImgData2);
        uploadImgDataArrayList.add(uploadImgData3);
        uploadImgDataArrayList.add(uploadImgData4);

        RecyclerView mRecyclerView = binding.rvAddActImgList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        uploadImgRecyclerviewAdapter = new UploadImgRecyclerviewAdapter(uploadImgDataArrayList, this);
        mRecyclerView.setAdapter(uploadImgRecyclerviewAdapter);
    }
}
