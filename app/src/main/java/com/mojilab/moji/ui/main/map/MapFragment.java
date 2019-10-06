package com.mojilab.moji.ui.main.map;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.*;

import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.data.MapSearchData;
import com.mojilab.moji.databinding.FragmentMapBinding;

import com.mojilab.moji.ui.main.MainActivity;
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity;
import com.mojilab.moji.ui.main.feed.SearchFeed.Course;
import com.mojilab.moji.ui.main.feed.SearchFeed.CourseX;
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchFeedResponse;
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchNotTagResponse;
import com.mojilab.moji.ui.main.home.HomeFragment;
import com.mojilab.moji.ui.main.upload.addCourse.LocationRecyclerviewAdapter;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    InputMethodManager imm;
    NetworkService networkService;
    BottomSheetBehavior bottomSheetBehavior;
    CourseX tempCourse;
    MarkerOptions searchMarker;
    String inputStr;
    MyItem offsetItem;
    boolean searchBtnFlag;
    int searchBtnCheck;
    boolean shouldCluster_zoom;
    ArrayList<MapSearchData> mapSearchDataArrayListResult;
    int selectedPosition;
    ArrayList<Course> courseArrayList;
    double receivedLat, receivedLng;

    public MapFragment() {
    }

    private static MapFragment mapFragment = null;

    public static MapFragment getMapFragment() {
        if (mapFragment == null) mapFragment = new MapFragment();
        return mapFragment;
    }

    MapSearchListRecyclerviewAdapter mapSearchListRecyclerviewAdapter;
    ArrayList<MapSearchData> mapSearchDataArrayList;
    private MapView mapView = null;
    FragmentMapBinding binding;
    String TAG = "MAP FRAGMENT";
    TextView startDateTv, endDateTv;

    // Declare a variable for the cluster manager.
    private ClusterManager<MyItem> mClusterManager;

    private GoogleMap mMap;
    private Marker currentMarker = null;
    String markerTitle;
    String markerSnippet;

    private static final int MAP_SEARCH = 101;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int UPDATE_INTERVAL_MS = 1000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 500; // 0.5초

    // onRequestPermissionsResult로부터 수신된 결과에서 ActivityCompat.requestPermissions를 사용한 퍼미션 요청을 구별하기 위해 사용
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    boolean needRequest = false;

    // 앱을 실행하기 위해 필요한 퍼미션을 정의
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};  // 외부 저장소

    Location mCurrentLocatiion;
    LatLng currentPosition;

    String selectedBoardIdx;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private Location location;
    private ArrayList<LocationData> locationDataArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        View v = inflater.inflate(R.layout.fragment_map, container, false);
        locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_MS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);

        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder();

        builder.addLocationRequest(locationRequest);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        startDateTv = v.findViewById(R.id.tv_start_date_map);
        endDateTv = v.findViewById(R.id.tv_endt_date_map);

        startDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker(0);
            }
        });

        endDateTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callDatePicker(1);
            }
        });

        return v;
    }



    public void callDatePicker(int flag) {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog;
        if (flag == 0) {
            dialog = new DatePickerDialog(getContext(), startListener, year, month, day);
        } else {
            dialog = new DatePickerDialog(getContext(), endListener, year, month, day);
        }

        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener startListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            startDateTv.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
        }
    };

    private DatePickerDialog.OnDateSetListener endListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            endDateTv.setText(year + "년 " + (monthOfYear + 1) + "월 " + dayOfMonth + "일");
        }
    };


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = DataBindingUtil.bind(view);
        mapView = binding.mapViewMap;
        mapView.getMapAsync(this);
        binding.btnGpsMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkPermission()) {
                    startLocationUpdates(); // 위치 업데이트 시작
                    if (mMap != null)
                        mMap.setMyLocationEnabled(true);
                    //현재 위치에 마커 생성하고 이동
                    setCurrentLocation(location, markerTitle, markerSnippet);
                    mFusedLocationClient.removeLocationUpdates(locationCallback);
                }
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        binding.bottomSheet.setVisibility(View.GONE);

//        setSearchListRecyclerView();
        setBottomSheetClickListener();

        binding.etMapFragContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MapSearchActivity.class);
                intent.putExtra("serachData", binding.etMapFragContainer.getText().toString());

                if (binding.tvStartDateMap.getText().length() > 0 & binding.tvEndtDateMap.getText().length() > 0) {
                    intent.putExtra("startDate", binding.tvStartDateMap.getText());
                    intent.putExtra("endDate", binding.tvEndtDateMap.getText());
                }


                Log.d(TAG, "isStart????");

                startActivityForResult(intent, MAP_SEARCH);
            }
        });

        imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        // 상단 터치 - 1
        binding.rlTopBottomSheetMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.bottomSheet.setVisibility(View.VISIBLE);
                return false;
            }
        });

        // 상단 터치 - 2
        binding.llBottomContentMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                binding.bottomSheet.setVisibility(View.VISIBLE);
                return false;
            }
        });

        // 하나 아이템 나왔을 경우
        binding.llBottomContentMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 상세페이지로 연결하자
                Intent intent = new Intent(getContext(), DetailFeedActivity.class);
                intent.putExtra("boardIdx", mapSearchDataArrayList.get(selectedPosition).boardIdx);
                startActivity(intent);
            }
        });




    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");

        if (checkPermission()) {
            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);

            if (mMap != null)
                mMap.setMyLocationEnabled(true);
        }

        Bundle bundle = new Bundle();
        if(bundle!=null && HomeFragment.Companion.getKeyword() != ""){
            Log.d(TAG, "성공 :"+ HomeFragment.Companion.getKeyword());
            // 지도에 띄운 후 초기화 ㅜ
            // 검색하고 온 뒤
            if (mMap != null) mMap.clear();
            if(mClusterManager!=null){
                mClusterManager.clearItems();
                mClusterManager.cluster();
            }


            // 배열에 값이 존재한다면 초기화
            if(mapSearchDataArrayListResult != null) mapSearchDataArrayListResult.clear();
            if(mapSearchDataArrayList != null) mapSearchDataArrayList.clear();
            inputStr=HomeFragment.Companion.getKeyword();
            //이부분에 넣어야함
            binding.etMapFragContainer.setText(HomeFragment.Companion.getKeyword());

            searchBtnFlag = true;

            searchPost(searchBtnFlag);

            HomeFragment.Companion.setKeyword("");  //keword 초기화
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //액티비티가 처음 생성될 때 실행되는 함수
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에 지도의 초기위치를 서울로 이동
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        LatLng startLoc = new LatLng(37.2706008, 127.01357559999997);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLoc));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tesl:046487"));
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.2939104, 127.2003777), 10));
        mMap.getUiSettings().setMapToolbarEnabled(false);
        // 위치 퍼미션을 가지고 있는지 체크
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);

        // 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // 사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유 설명
                Snackbar.make(binding.rlMainMap, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.",
                        Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 사용자게에 퍼미션 요청
                        ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                                PERMISSIONS_REQUEST_CODE);
                    }
                }).show();

            } else {
                // 사용자가 퍼미션 거부를 한 적이 없는 경우
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }
        }
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        mClusterManager = new ClusterManager<>(getContext(), mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//
//                Intent intent = new Intent(getContext(), DetailFeedActivity.class);
//                startActivity(intent);
//            }
//        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                CameraPosition position = mMap.getCameraPosition();
                shouldCluster_zoom = position.zoom < 11; //disables the cluster at 9 and higher zoom levels
                mClusterManager.cluster();
            }
        });
        setDefaultLocation();
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d(TAG, "onMapClick :");
            }
        });

        final MarkerClusterRenderer renderer = new MarkerClusterRenderer(getContext(), mMap, mClusterManager);
        mClusterManager.setRenderer(renderer);

    }

    private void addItems() {
        double lat = 37.2706008;
        double lng = 127.01357559999997;

        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
                offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
        }
    }


    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            List<Location> locationList = locationResult.getLocations();

            if (locationList.size() > 0) {
                location = locationList.get(0);
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

                markerTitle = getCurrentAddress(currentPosition);
                markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                        + " 경도:" + String.valueOf(location.getLongitude());

                Log.d(TAG, "onLocationResult : " + markerSnippet);

                mCurrentLocatiion = location;
            }
        }

    };

    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {
            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        } else {

            int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION);


            if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                    hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call mFusedLocationClient.requestLocationUpdates");

            mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

            if (checkPermission())
                mMap.setMyLocationEnabled(true);

        }

    }

    public String getCurrentAddress(LatLng latlng) {
        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
//            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
//            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }


        if (addresses == null || addresses.size() == 0) {
//            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {

        if (currentMarker != null) currentMarker.remove();

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(currentLatLng);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);


        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
        mMap.moveCamera(cameraUpdate);
        mMap.getUiSettings().setMapToolbarEnabled(false);
    }

    //  위치 정보
    public void setDefaultLocation() {

        //디폴트 위치, Seoul
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 요부 확인하세요";

        mMap.getUiSettings().setMapToolbarEnabled(false);

    }

    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    private boolean checkPermission() {

        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;

    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if (check_result) {
                // 퍼미션을 허용했다면 위치 업데이트를 시작
                startLocationUpdates();
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱 사용 가능
                    Snackbar.make(binding.rlMainMap, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            getActivity().finish();
                        }
                    }).show();

                } else {


                    // "다시 묻지 않음"을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱 사용 가능
                    Snackbar.make(binding.rlMainMap, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            getActivity().finish();
                        }
                    }).show();
                }
            }

        }
    }

    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  GPS_ENABLE_REQUEST_CODE){
            //사용자가 GPS 활성 시켰는지 검사
            if (checkLocationServicesStatus()) {
                if (checkLocationServicesStatus()) {

                    Log.d(TAG, "onActivityResult : GPS 활성화 되있음");
                    needRequest = true;

                    return;
                }
            }
        }
        // 지도 검색하고 온 뒤
        else if (requestCode == MAP_SEARCH) {
            mMap.clear();
            mClusterManager.clearItems();
            mClusterManager.cluster();

            // 배열에 값이 존재한다면 초기화
            if(mapSearchDataArrayListResult != null) mapSearchDataArrayListResult.clear();
            if(mapSearchDataArrayList != null) mapSearchDataArrayList.clear();

            if (data == null) {
                return;
            }
            inputStr = data.getStringExtra("inputStr");
            searchBtnCheck = data.getIntExtra("searchBtnCheck", 0);
            selectedPosition = data.getIntExtra("position", 0);
            receivedLat = data.getDoubleExtra("lat", 0.0);
            receivedLng = data.getDoubleExtra("lng", 0.0);
            binding.etMapFragContainer.setText(inputStr);

            if(searchBtnCheck == 0){
                mMap.moveCamera(CameraUpdateFactory.newLatLng( new LatLng(receivedLat, receivedLng)));
            }

            // 특정 아이템 출력(리스트 아이템을 클릭함) -> 해당 아이템 출력함
            if(searchBtnCheck == 0){
                searchBtnFlag = false;
            }
            // 키워드 입력후 바로 검색 누르면 -> 리스트 출력함
            else{
                searchBtnFlag = true;
            }
            searchPost(searchBtnFlag);
        }
    }

    //장소 리스트
    public void setSearchListRecyclerView(final ArrayList<MapSearchData> mapSearchDataArrayList1) {
        mapSearchDataArrayList = new ArrayList<>();

        mapSearchDataArrayList = mapSearchDataArrayList1;

        RecyclerView mRecyclerView = binding.rvMapFragSearchList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mapSearchListRecyclerviewAdapter = new MapSearchListRecyclerviewAdapter(mapSearchDataArrayList, getContext());
        mapSearchListRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mapSearchListRecyclerviewAdapter);

        mapSearchListRecyclerviewAdapter.setOnItemClickListener(new MapSearchListRecyclerviewAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                mClusterManager.clearItems();
                mClusterManager.cluster();
                selectedPosition = position;
                // 마커 띄우기
                mMap.moveCamera(CameraUpdateFactory.newLatLng( new LatLng(mapSearchDataArrayList.get(selectedPosition).lat, mapSearchDataArrayList.get(selectedPosition).log)));
                setSelectedContents(selectedPosition);
//                mClusterManager.clearItems();
//                mClusterManager.cluster();
                offsetItem = new MyItem(mapSearchDataArrayList.get(selectedPosition).lat, mapSearchDataArrayList.get(selectedPosition).log, mapSearchDataArrayList.get(selectedPosition).mainAddress, mapSearchDataArrayList1.get(selectedPosition).subAddress, mapSearchDataArrayList1.get(selectedPosition).boardIdx);
                mClusterManager.addItem(offsetItem);
            }
        });
    }

    //선택한 아이템
    public void setSelectedContents(int position) {
        binding.bottomSheet.setVisibility(View.GONE);
        binding.rlMapFragContainer.setVisibility(View.VISIBLE);

        Log.v(TAG, "클릭 = " + position + ", 프사 url = " + mapSearchDataArrayList.get(position).img);
        Log.v(TAG, "총 사이즈 = " + mapSearchDataArrayList.size());

        if(mapSearchDataArrayList.get(position).img != null){
            Log.v(TAG, "프사 존재");
            Glide.with(getContext()).load(mapSearchDataArrayList.get(position).img).into(binding.ivMapFragSelectedImg);
        }
        else{
            Log.v(TAG, "프사 널값");
            Glide.with(getContext()).load("https://www.yokogawa.com/public/img/default_image.png").into(binding.ivMapFragSelectedImg);
        }
        binding.ivMapFragSelectedImg.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
        binding.tvMapFragSelectedMain.setText(mapSearchDataArrayList.get(position).mainAddress);
        binding.tvMapFragSelectedSub.setText(mapSearchDataArrayList.get(position).subAddress);

        binding.tvMapFragSelectedHeartCnt.setText(mapSearchDataArrayList.get(position).likeCnt + "");
    }

    //클릭아이템
    public void setBottomSheetClickListener() {

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Toast.makeText(getContext(), "newState = " + newState, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                Toast.makeText(getContext(), "slideOffset = " + slideOffset, Toast.LENGTH_SHORT).show();

            }
        });
    }

    // searchBtnFlag : false = 특정 아이템 출력
    // searchBtnFlag : true = 리스트 출력
    public void searchPost(final boolean searchBtnFlag) {
        JSONObject jsonObject = new JSONObject();
        final boolean tagUse;
        // 맨 앞자리 #일경우 태그 검색
        if(inputStr.charAt(0) == '#'){
            tagUse = true;
        }
        else{
            tagUse = false;
        }

        try {
            Log.v(TAG, "POST 지도 검색 입력 값 = " + inputStr);
            jsonObject.put("keyword",inputStr);

            if (binding.tvStartDateMap.getText().toString() != null & binding.tvEndtDateMap.getText().toString() != null) {

                jsonObject.put("startDate", "1000-01-08");
                jsonObject.put("endDate", "2999-09-20");
//                jsonObject.put("startDate",binding.tvStartDateMap.getText().toString());
//                jsonObject.put("endDate", binding.tvEndtDateMap.getText().toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Gson 라이브러리의 Json Parser을 통해 객체를 Json으로!
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        // 태그 사용 -> 태그 검색
        if(tagUse){
            Call<SearchFeedResponse> postsearch = networkService.postSearches("application/json", SharedPreferenceController.INSTANCE.getAuthorization(getContext()), gsonObject);

            postsearch.enqueue(new Callback<SearchFeedResponse>() {
                @Override
                public void onResponse(Call<SearchFeedResponse> call, Response<SearchFeedResponse> response) {
                    Log.e("LOG::", response.body().toString());
                    //setContents();
                    if (response.body().getStatus() == 200) {
                        if(response.body().getData() == null)
                            return;
                        ArrayList<Course> courseArrayList  = response.body().getData().getCourses();
                        if(courseArrayList == null){
                            return;
                        }
                        if(searchBtnFlag){
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(courseArrayList.get(0).getCourse().getLat()), Double.parseDouble(courseArrayList.get(0).getCourse().getLng()))));
                        }

                        mapSearchDataArrayListResult = new ArrayList<>();

                        Log.v(TAG, "지도 검색 데이터 = " + courseArrayList.toString());
                        mapSearchDataArrayList = new ArrayList<>();

                        // 맨 앞에 #제거
                        inputStr = inputStr.substring(1, inputStr.length());

                        for (int i = 0; i < courseArrayList.size(); i++) {
                            Log.v(TAG, "코스 크기 = " + courseArrayList.size());
                            tempCourse = courseArrayList.get(i).getCourse();
                            Log.v(TAG, "비교, 받아온 str = " + inputStr + ", 비교문 = " + tempCourse.getMainAddress() + "태그문 = " + tempCourse.getTagInfo().toString());

                            // 리스트 아이템 눌렀을 경우 해당 아이템만 출력
                            if(!searchBtnFlag){
                                Log.v(TAG, "태그 하나 출력");
                                // 선택한 리스트 아이템 하나만 일단 저장
                                if(i == 0 ){
                                    mapSearchDataArrayList.add(new MapSearchData(

                                            courseArrayList.get(selectedPosition).getCourse().get_id(),
                                            courseArrayList.get(selectedPosition).getCourse().component9().get(0).getPhotoUrl(),
                                            courseArrayList.get(selectedPosition).getCourse().getMainAddress(),
                                            courseArrayList.get(selectedPosition).getCourse().getSubAddress(),
                                            Float.parseFloat(courseArrayList.get(selectedPosition).getCourse().getLat()),
                                            Float.parseFloat(courseArrayList.get(selectedPosition).getCourse().getLng()),
                                            courseArrayList.get(selectedPosition).getLikeCount(),
                                            courseArrayList.get(selectedPosition).getLiked(),
                                            courseArrayList.get(selectedPosition).getCourse().getBoardIdx()
                                    ));
                                    offsetItem = new MyItem(Double.parseDouble(courseArrayList.get(selectedPosition).getCourse().getLat()), Double.parseDouble(courseArrayList.get(selectedPosition).getCourse().getLng()), courseArrayList.get(selectedPosition).getCourse().getMainAddress(), courseArrayList.get(selectedPosition).getCourse().getSubAddress(), courseArrayList.get(selectedPosition).getCourse().getBoardIdx());
                                    mClusterManager.addItem(offsetItem);
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(9); // 범위 높을수록 확대가 커집니다.
                                    mMap.animateCamera(zoom); //해당위치로 카메라
                                }

                                // 입력한 문자열이 태그에 포함된 경우
                                if(tempCourse.getTagInfo().contains(inputStr) ){
                                    Log.v(TAG, "지도 검색 후 태그 일치 장소 = " + inputStr);
                                    offsetItem = new MyItem(Double.parseDouble(tempCourse.getLat()), Double.parseDouble(tempCourse.getLng()), tempCourse.getMainAddress(),tempCourse.getSubAddress(), tempCourse.getBoardIdx());
                                    mClusterManager.addItem(offsetItem);

                                    mapSearchDataArrayListResult.add(new MapSearchData(
                                            courseArrayList.get(i).getCourse().get_id(),
                                            courseArrayList.get(i).getCourse().getPhotos().get(0).getPhotoUrl(),
                                            courseArrayList.get(i).getCourse().getMainAddress(),
                                            courseArrayList.get(i).getCourse().getSubAddress(),
                                            Float.parseFloat(courseArrayList.get(i).getCourse().getLat()),
                                            Float.parseFloat(courseArrayList.get(i).getCourse().getLng()),
                                            courseArrayList.get(i).getLikeCount(),
                                            courseArrayList.get(i).getLiked(),
                                            courseArrayList.get(i).getCourse().getBoardIdx()
                                    ));
                                }
                            }
                            else{
                                Log.v(TAG, "리스트 출력2 = " + tempCourse.getMainAddress());
                                offsetItem = new MyItem(Double.parseDouble(tempCourse.getLat()), Double.parseDouble(tempCourse.getLng()), tempCourse.getMainAddress(), tempCourse.getSubAddress(), tempCourse.getBoardIdx());
                                Log.v(TAG, "마커 메인주소 = " + offsetItem.getTitle() + ", 상세 주소 = " + offsetItem.getSnippet());
                                mClusterManager.addItem(offsetItem);

                                mapSearchDataArrayListResult.add(new MapSearchData(
                                        courseArrayList.get(i).getCourse().get_id(),
                                        courseArrayList.get(i).getCourse().component9().get(0).getPhotoUrl(),
                                        courseArrayList.get(i).getCourse().getMainAddress(),
                                        courseArrayList.get(i).getCourse().getSubAddress(),
                                        Float.parseFloat(courseArrayList.get(i).getCourse().getLat()),
                                        Float.parseFloat(courseArrayList.get(i).getCourse().getLng()),
                                        courseArrayList.get(i).getLikeCount(),
                                        courseArrayList.get(i).getLiked(),
                                        courseArrayList.get(i).getCourse().getBoardIdx()
                                ));
                            }
                        }
                        // 리스트 출력
                        if(searchBtnFlag){
                            setSearchListRecyclerView(mapSearchDataArrayListResult);
                            selectedPosition = 0;
                            setSelectedContents(selectedPosition);
                        }
                        else{
                            setSearchListRecyclerView(mapSearchDataArrayListResult);
                            setSelectedContents(selectedPosition);
                        }

                    } else if (response.body().getStatus() == 404) {
                        Log.v("T", "검색 결과 없.");

                    } else {
//                        Toast.makeText(getContext(), "에러", Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<SearchFeedResponse> call, Throwable t) {
                }
            });
        }

        // 태그 미사용 -> 장소 검색
        else{
            Call<SearchNotTagResponse> postsearch = networkService.postNotTagSearches("application/json", SharedPreferenceController.INSTANCE.getAuthorization(getContext()), gsonObject);

            postsearch.enqueue(new Callback<SearchNotTagResponse>() {
                @Override
                public void onResponse(Call<SearchNotTagResponse> call, Response<SearchNotTagResponse> response) {
                    Log.e("LOG::", response.body().toString());
                    //setContents();
                    if (response.body().getStatus() == 200) {
                        if(response.body().getData() == null)
                            return;
                        ArrayList<Course> courseArrayList  = response.body().getData().getSearchCourseRes().getCourses();
                        if(courseArrayList == null){
                            return;
                        }

                        mapSearchDataArrayListResult = new ArrayList<>();

                        Log.v(TAG, "지도 검색 데이터 = " + courseArrayList.toString());
                        mapSearchDataArrayList = new ArrayList<>();

                        for (int i = 0; i < courseArrayList.size(); i++) {
                            Log.v(TAG, "코스 크기 = " + courseArrayList.size());
                            tempCourse = courseArrayList.get(i).getCourse();
                            Log.v(TAG, "비교, 받아온 str = " + inputStr + ", 비교문 = " + tempCourse.getSubAddress() + "태그문 = " + tempCourse.getTagInfo().toString());

                            // 리스트 아이템 눌렀을 경우 해당 아이템만 출력
                            if(!searchBtnFlag){
                                Log.v(TAG, "하나 출력");
                                // 선택한 리스트 아이템 하나만 일단 저장
                                if(i == 0 ){
                                    mapSearchDataArrayList.add(new MapSearchData(
                                            courseArrayList.get(selectedPosition).getCourse().get_id(),
                                            courseArrayList.get(selectedPosition).getCourse().component9().get(0).getPhotoUrl(),
                                            courseArrayList.get(selectedPosition).getCourse().getMainAddress(),
                                            courseArrayList.get(selectedPosition).getCourse().getSubAddress(),
                                            Float.parseFloat(courseArrayList.get(selectedPosition).getCourse().getLat()),
                                            Float.parseFloat(courseArrayList.get(selectedPosition).getCourse().getLng()),
                                            courseArrayList.get(selectedPosition).getLikeCount(),
                                            courseArrayList.get(selectedPosition).getLiked(),
                                            courseArrayList.get(selectedPosition).getCourse().getBoardIdx()
                                    ));
                                    offsetItem = new MyItem(Double.parseDouble(courseArrayList.get(selectedPosition).getCourse().getLat()), Double.parseDouble(courseArrayList.get(selectedPosition).getCourse().getLng()), courseArrayList.get(selectedPosition).getCourse().getMainAddress(), courseArrayList.get(selectedPosition).getCourse().getSubAddress(), courseArrayList.get(selectedPosition).getCourse().getBoardIdx());
                                    mClusterManager.addItem(offsetItem);
                                }
                                // 입력한 문자열이 메인 주소에 일부라도 포함된 경우
                                if(tempCourse.getSubAddress().contains(inputStr) ){
                                    Log.v(TAG, "지도 검색 후 장소 일치 장소 = " + inputStr);
                                    mapSearchDataArrayListResult.add(new MapSearchData(
                                            courseArrayList.get(i).getCourse().get_id(),
                                            courseArrayList.get(i).getCourse().getPhotos().get(0).getPhotoUrl(),
                                            courseArrayList.get(i).getCourse().getMainAddress(),
                                            courseArrayList.get(i).getCourse().getSubAddress(),
                                            Float.parseFloat(courseArrayList.get(i).getCourse().getLat()),
                                            Float.parseFloat(courseArrayList.get(i).getCourse().getLng()),
                                            courseArrayList.get(i).getLikeCount(),
                                            courseArrayList.get(i).getLiked(),
                                            courseArrayList.get(i).getCourse().getBoardIdx()
                                    ));
                                    //addSearchMarker();
                                }
                            }
                            else{
                                Log.v(TAG, "리스트 출력2");
                                offsetItem = new MyItem(Double.parseDouble(tempCourse.getLat()), Double.parseDouble(tempCourse.getLng()), tempCourse.getMainAddress(), tempCourse.getSubAddress(), tempCourse.getBoardIdx());
                                Log.v(TAG, "마커 메인주소 = " + offsetItem.getTitle() + ", 상세 주소 = " + offsetItem.getSnippet());
                                mClusterManager.addItem(offsetItem);

                                mapSearchDataArrayListResult.add(new MapSearchData(
                                        courseArrayList.get(i).getCourse().get_id(),
                                        courseArrayList.get(i).getCourse().component9().get(0).getPhotoUrl(),
                                        courseArrayList.get(i).getCourse().getMainAddress(),
                                        courseArrayList.get(i).getCourse().getSubAddress(),
                                        Float.parseFloat(courseArrayList.get(i).getCourse().getLat()),
                                        Float.parseFloat(courseArrayList.get(i).getCourse().getLng()),
                                        courseArrayList.get(i).getLikeCount(),
                                        courseArrayList.get(i).getLiked(),
                                        courseArrayList.get(i).getCourse().getBoardIdx()
                                ));
                            }
                        }
                        // 리스트 출력
                        if(searchBtnFlag){
                            setSearchListRecyclerView(mapSearchDataArrayListResult);
                            selectedPosition = 0;
                            setSelectedContents(selectedPosition);
                        }
                        else{
                            setSearchListRecyclerView(mapSearchDataArrayListResult);
                            setSelectedContents(selectedPosition);
                        }

                    } else if (response.body().getStatus() == 404) {
                        Log.v("T", "검색 결과 없.");

                    } else {
//                        Toast.makeText(getContext(), "에러", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SearchNotTagResponse> call, Throwable t) {
                }
            });
        }

    }

    public void addSearchMarker(){

        double tempLat = Double.parseDouble(tempCourse.getLat());
        double tempLng = Double.parseDouble(tempCourse.getLng());

        searchMarker = new MarkerOptions();
        // 마커 타이틀
        searchMarker.title("선택 마커 = " + tempCourse.getMainAddress());
        Double latitude = tempLat; // 위도
        Double longitude = tempLng; // 경도
        // 마커의 스니펫(간단한 텍스트) 설정
        searchMarker.snippet(tempCourse.getMainAddress());
        // LatLng: 위도 경도 쌍을 나타냄
        searchMarker.position(new LatLng(latitude, longitude));
        // 마커(핀) 추가
        mMap.addMarker(searchMarker);

        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11); // 범위 높을수록 확대가 커집니다.
        mMap.moveCamera(center);
        mMap.animateCamera(zoom); //해당위치로 카메라
    }

    public class MarkerClusterRenderer extends DefaultClusterRenderer<MyItem> {

        public MarkerClusterRenderer(Context context, GoogleMap map,
                                     ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
            // use this to make your change to the marker option
            // for the marker before it gets render on the map
            markerOptions.icon(BitmapDescriptorFactory.
                    fromResource(R.drawable.map_marker1));
            Log.v(TAG, "마커 하나 찍기");
            markerOptions.snippet(item.getSnippet());
            markerOptions.title(item.getTitle());

            super.onBeforeClusterItemRendered(item, markerOptions);

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(11); // 범위 높을수록 확대가 커집니다.
            mMap.animateCamera(zoom);

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<MyItem> cluster) {
            //start clustering if at least 2 items overlap
            return cluster.getSize() > 1 && shouldCluster_zoom;
        }
        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {

            final IconGenerator mClusterIconGenerator;
            mClusterIconGenerator = new IconGenerator(getContext().getApplicationContext());

            if(cluster.getSize() < 10){
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.clustering_under_10));
            }
            else if(cluster.getSize() < 20 && cluster.getSize() >= 10){
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.clustering_up_10));
            }
            else if(cluster.getSize() < 50 && cluster.getSize() >= 20){
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.clustering_up_50));
            }
            else{
                mClusterIconGenerator.setBackground(
                        ContextCompat.getDrawable(getContext(), R.drawable.clustering_up_100));
            }

            final Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
            getMarker(clusterItem).showInfoWindow();
        }
    }









/*
0.
    public void TagSearchFromHomePost(final String keword) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("keyword", keword);
            Log.e("ㅎㅎ","keyword"+" search 통신 시작");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Gson 라이브러리의 Json Parser을 통해 객체를 Json으로!
        JsonObject gsonObject = (JsonObject) new JsonParser().parse(jsonObject.toString());
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        // 태그 검색인 경우
            Call<SearchFeedResponse> postsearch = networkService.postSearches("application/json", SharedPreferenceController.INSTANCE.getAuthorization(getContext()), gsonObject);

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
                            setContents(courseArrayList,keword);


                        } else if (response.body().getStatus() == 404) {
                            Log.v("T", "검색 결과 없.");
                            setContents(null,keword);

                        } else {
//                            Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SearchFeedResponse> call, Throwable t) {
                }
            });



    }

    public void setContents(ArrayList<Course> coursesArrayList,String keword) {

        if (locationDataArrayList != null) {
            locationDataArrayList.clear();
        }
        String str;
        // 태그 검색일 경우
            str = keword;

        if(coursesArrayList != null){
            //        Log.e("setContents",coursesArrayList.toString());
            for (int i = 0; i < coursesArrayList.size(); i++) {

                Log.e("add item :",coursesArrayList.get(i).toString()+"아아디:"+i);
                // 태그 검색
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
        }



        inputStr = keword;
        searchBtnCheck = 0;
        receivedLat = data.getDoubleExtra("lat", 0.0);
        receivedLng = data.getDoubleExtra("lng", 0.0);
                getIntent().putExtra("inputStr", binding.etMapSearchActSearchLocation.getText().toString());
                getIntent().putExtra("searchBtnCheck", 0);
                getIntent().putExtra("lat", locationDataArrayList.get(position).lat);
                getIntent().putExtra("lng", locationDataArrayList.get(position).lng);
                Log.v(TAG, "보내는 lat=" + locationDataArrayList.get(position).lat + ", lng = " + locationDataArrayList.get(position).lng);
                getIntent().putExtra("position", position);
                setResult(MAP_SEARCH, getIntent());

                //Activity로 돌아감
                //position얘만 있어도됨


    }
*/





}
