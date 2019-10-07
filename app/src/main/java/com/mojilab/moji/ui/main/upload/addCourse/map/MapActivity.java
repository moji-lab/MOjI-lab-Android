package com.mojilab.moji.ui.main.upload.addCourse.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mojilab.moji.R;
import com.mojilab.moji.data.HashTagData;
import com.mojilab.moji.data.TourData;
import com.mojilab.moji.databinding.ActivityMapBinding;
import com.mojilab.moji.ui.main.upload.addCourse.map.adapter.TourMapAdapter;
import com.mojilab.moji.ui.main.upload.addCourse.map.coarsename.CoarseNameRegisterActivity;
import com.mojilab.moji.ui.main.upload.addCourse.map.coursesearch.CourseSearchActivity;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.TourApiClient;
import com.mojilab.moji.util.network.TourNetworkService;
import com.mojilab.moji.util.network.get.GetHashTagResponse;
import com.mojilab.moji.util.network.get.GetTourDataResponse;
import com.mojilab.moji.util.network.get.GetTourDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {

    //구글맵참조변수
    GoogleMap mMap;
    LocationManager locationManager;
    NetworkService networkService;
    TourNetworkService tourNetworkService;
    ArrayList<GetTourDetail> tours;
    RequestManager requestManager;

    MarkerOptions mOptions;

    TourMapAdapter tourMapAdapter;
    ArrayList<TourData> tourList;

    private final String SERVICE_KEY = "qRYEuZ2CaSIosY5zJByoD%2By9%2FIhLsZssGVEJCGeM39s%2FDAE1zlfzua79E3iWCak5t6k2dkT%2B01YNt7XUNSs7SQ%3D%3D";
    final String TAG = "MapActivity";

    MapActivity mapActivity;

    ActivityMapBinding binding;

    //나의 위도 경도 고도
    double mLatitude;  //위도
    double mLongitude; //경도

    // 받아온 위경도
    double receivedLat;
    double receivedLng;

    @Override
    public void onClick(View v) {
        int position = binding.rvTouristMap.getChildAdapterPosition(v);
        double lat = tourList.get(position).getLat();
        double lng = tourList.get(position).getLng();

        // 마커의 스니펫(간단한 텍스트) 설정
        mOptions.snippet(String.valueOf(lat) + ", " + String.valueOf(lng));
        // LatLng: 위도 경도 쌍을 나타냄
        mOptions.position(new LatLng(lat, lng));
        // 마커(핀) 추가
        mMap.addMarker(mOptions);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tourNetworkService = TourApiClient.INSTANCE.getRetrofit().create(TourNetworkService.class);
        Log.v(TAG, "서비스키 = " + SERVICE_KEY);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map);
        binding.setActivity(this);
        requestManager = Glide.with(this);
        binding.rvTouristMap.setVisibility(View.INVISIBLE);

        mapActivity = this;

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_content_activity_map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.

        //LocationManager
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

        binding.btnGpsMapActivityMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMyLocation();
            }
        });

        binding.btnSearchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CourseSearchActivity.class);
                intent.putExtra("keyword", binding.editSearchMap.getText().toString());
                startActivityForResult(intent, 29);
            }
        });

        // 엔터키 이벤트
        binding.editSearchMap.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        Intent intent = new Intent(getApplicationContext(), CourseSearchActivity.class);
                        intent.putExtra("keyword", binding.editSearchMap.getText().toString());
                        startActivityForResult(intent, 29);
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

        binding.btnConfirmMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CoarseNameRegisterActivity.class);
                intent.putExtra("lat" , receivedLat);
                intent.putExtra("lng" , receivedLng);
                startActivity(intent);
            }
        });

        //GPS가 켜져있는지 체크
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
            finish();
        }

        //마시멜로 이상이면 권한 요청하기
        if(Build.VERSION.SDK_INT >= 23){
            //권한이 없는 경우
            if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION} , 1);
            }
            //권한이 있는 경우
            else{
                requestMyLocation();
            }
        }
        //마시멜로 아래
        else{
            requestMyLocation();
        }
    }

    //권한 요청후 응답 콜백
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //ACCESS_COARSE_LOCATION 권한
        if(requestCode==1){
            //권한받음
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                requestMyLocation();
            }
            //권한못받음
            else{
                Toast.makeText(this, "권한없음", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    // 다시 돌아왔을 때
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 지도 화면에서 돌아왔을 때
        if(requestCode == 29) {
            Log.v(TAG, "here");
            receivedLat = data.getDoubleExtra("lat", 0.0);
            receivedLng = data.getDoubleExtra("lng", 0.0);
            String mainAddress = data.getStringExtra("mainAddress");

            binding.editSearchMap.setText(mainAddress);

            Log.v(TAG, "받아온 위경도 값 : lat =  " + receivedLat + ", lng = " + receivedLng);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(receivedLat, receivedLng)));

        }
    }

    //나의 위치 요청
    public void requestMyLocation(){
        if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        Log.v(TAG, "지도 = 위치요청 " );
        //요청
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                Log.v(TAG, "여기");
                return;
            }
            //나의 위치를 한번만 가져오기 위해
            locationManager.removeUpdates(locationListener);

            //위도 경도
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도

            Log.v(TAG, "현재 위치 lat = " + mLatitude + ", lng = " + mLongitude);
            //맵생성
            SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map_content_activity_map);
            //콜백클래스 설정
            mapFragment.getMapAsync(MapActivity.this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("gps", "onStatusChanged"); }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };


    @Override //구글맵을 띄울준비가 됬으면 자동호출된다.
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // 구글 제공 현재 위치 띄우기
//        mMap.setMyLocationEnabled(true);


        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng point) {
                // 마커 다 지우고 시작(한 개만 보여야 하므로)
                googleMap.clear();
                // 새로 하나 추가
                mOptions = new MarkerOptions();
                // 마커 타이틀
                mOptions.title("선택");
                Double latitude = point.latitude; // 위도
                Double longitude = point.longitude; // 경도
                // 마커의 스니펫(간단한 텍스트) 설정
                mOptions.snippet(latitude.toString() + ", " + longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(latitude, longitude));
                // 마커(핀) 추가
                googleMap.addMarker(mOptions);

                binding.rvTouristMap.setVisibility(View.VISIBLE);
                getTourData();
                binding.btnConfirmMap.setVisibility(View.VISIBLE);
            }
        });
        ////////////////////

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        //지도타입 - 일반
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //나의 위치 설정
        LatLng position = new LatLng(mLatitude , mLongitude);

        //화면중앙의 위치와 카메라 줌비율
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15));
    }

    ////////////////////////  구글맵 마커 여러개생성 및 띄우기 //////////////////////////
    public void manyMarker() {
        // for loop를 통한 n개의 마커 생성
        for (int idx = 0; idx < 10; idx++) {
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(37.52487 + idx, 126.92723))
                    .title("마커" + idx); // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }
        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener);

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener);

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.5661654, 126.9773143)));
    }

    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            String markerId = marker.getId();
            Toast.makeText(MapActivity.this, "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
        }
    };

    //마커 클릭 리스너
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            String markerId = marker.getId();
            //선택한 타겟위치
            LatLng location = marker.getPosition();
            Toast.makeText(MapActivity.this, "마커 클릭 Marker ID : "+markerId+"("+location.latitude+" "+location.longitude+")", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    public void getTourData() {

        Call<GetTourDataResponse> getTourDataResponse = tourNetworkService.getTourData( 30, 1, "AND", "모지", receivedLng, receivedLat, 1000, "json");

        getTourDataResponse.enqueue(new Callback<GetTourDataResponse>() {
            @Override
            public void onResponse(Call<GetTourDataResponse> call, Response<GetTourDataResponse> response) {
                Log.v(TAG, "주변 관광지 조회 성공"+response.toString());
                if (response.isSuccessful()) {
                    Log.v(TAG, "주변 관광지 조회 성공 = " + response.body().toString());
                    tours = response.body().getResponse().getBody().getItems().getItem();

                    tourList = new ArrayList<>();
                    if(tours.size() > 0){
                        if(tours.size() > 10){
                            for(int i=0; i<tours.size(); i++){
                                if(tours.get(i).getFirstimage() != null && tours.get(i).getTitle() != null && tours.get(i).getAddr1() != null){
                                    tourList.add(new TourData(tours.get(i).getFirstimage(), tours.get(i).getTitle(), tours.get(i).getAddr1(), tours.get(i).getMapy(), tours.get(i).getMapx()));
                                }
                            }
                        }
                        // 10개만 저장
                        else{
                            for(int i=0; i<10; i++){
                                if(tours.get(i).getFirstimage() != null && tours.get(i).getTitle() != null && tours.get(i).getAddr1() != null){
                                    tourList.add(new TourData(tours.get(i).getFirstimage(), tours.get(i).getTitle(), tours.get(i).getAddr1(), tours.get(i).getMapy(), tours.get(i).getMapx()));
                                }
                            }
                        }
                    }

                    binding.rvTouristMap.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));
                    tourMapAdapter = new TourMapAdapter(tourList, requestManager);

                    binding.rvTouristMap.setAdapter(tourMapAdapter);
                    tourMapAdapter.setOnItemClickListener(mapActivity);

                } else{

                }
            }

            @Override
            public void onFailure(Call<GetTourDataResponse> call, Throwable t) {
                Log.v(TAG+"::", t.toString());

            }
        });
    }
}