package com.mojilab.moji.ui.main.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.mojilab.moji.R;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class TMapFragment extends Fragment implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean bTrackingMode = true;
    TMapView tMapView = null;
    private TMapGpsManager tmapGps = null;
    final String appKey = "39e3c1c8-26b9-4afe-96e3-d68fe892aa84";
    private static int markerID;

    ArrayList<TMapPoint> tmapPoints = new ArrayList<TMapPoint>();
    ArrayList<String> arrayMarkerID = new ArrayList<String>();
    ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();

    private TMapFragment() {
    }

    private static TMapFragment tmapFragment = null;

    public static TMapFragment getMapFragment(){
        if(tmapFragment == null) tmapFragment = new TMapFragment();
        return tmapFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tmap, container, false);
        mContext = getContext();

        LinearLayout linearLayoutTmap = (LinearLayout)v.findViewById(R.id.linearLayoutTmap);

        tMapView = new TMapView(mContext);
        tMapView.setSKTMapApiKey(appKey);
        linearLayoutTmap.addView(tMapView);
        tMapView.setIconVisibility(true);//현재위치로 표시될 아이콘을 표시할지 여부를 설정합니다.


        ImageButton currentBtn = (ImageButton) v.findViewById(R.id.btn_gps_map);
        currentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setGps();
            }
        });
//        addPoint();
//        showMarkerPoint();

/*        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tmapGps = new TMapGpsManager(getActivity());
        tmapGps.setMinTime(1000);
        tmapGps.setMinDistance(5);
        // 연결된 인터넷으로 현위치를 받아옴.
        tmapGps.setProvider(tmapGps.NETWORK_PROVIDER);
        // 실내일 때 유용
//        tmapGps.setProvider(tmapGps.GPS_PROVIDER);*/

//        tmapGps.OpenGps();

        setGps();

        // 화면 중심을 단말의 현재위치로
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        TMapMarkerItem markerItem1 = new TMapMarkerItem();
        TMapMarkerItem markerItem2= new TMapMarkerItem();
        TMapMarkerItem markerItem3 = new TMapMarkerItem();
        TMapMarkerItem markerItem4 = new TMapMarkerItem();
        TMapMarkerItem markerItem5 = new TMapMarkerItem();
        TMapMarkerItem markerItem6 = new TMapMarkerItem();
        TMapMarkerItem markerItem7 = new TMapMarkerItem();
        TMapMarkerItem markerItem8 = new TMapMarkerItem();
        TMapMarkerItem markerItem9 = new TMapMarkerItem();
        TMapMarkerItem markerItem10 = new TMapMarkerItem();
        TMapMarkerItem markerItem11 = new TMapMarkerItem();
        TMapMarkerItem markerItem12 = new TMapMarkerItem();
        TMapMarkerItem markerItem13 = new TMapMarkerItem();

        TMapPoint tMapPoint1 = new TMapPoint(37.570841, 126.985302); // SKT타워
        TMapPoint tMapPoint2 = new TMapPoint(37.370841, 126.985302); // SKT타워
        TMapPoint tMapPoint3 = new TMapPoint(37.270841, 126.985302); // SKT타워
        TMapPoint tMapPoint4 = new TMapPoint(37.470841, 126.985302); // SKT타워
        TMapPoint tMapPoint5 = new TMapPoint(37.870841, 126.985302); // SKT타워
        TMapPoint tMapPoint6 = new TMapPoint(37.579841, 126.985302); // SKT타워
        TMapPoint tMapPoint7= new TMapPoint(37.520841, 126.985302); // SKT타워
        TMapPoint tMapPoint8 = new TMapPoint(37.5710841, 126.985302); // SKT타워
        TMapPoint tMapPoint9 = new TMapPoint(37.573841, 126.985302); // SKT타워
        TMapPoint tMapPoint10 = new TMapPoint(37.670841, 126.985302); // SKT타워
        TMapPoint tMapPoint11 = new TMapPoint(37.579851, 126.985202); // SKT타워
        TMapPoint tMapPoint12 = new TMapPoint(37.57982, 126.985502); // SKT타워
        TMapPoint tMapPoint13 = new TMapPoint(37.579841, 126.985302); // SKT타워


        // 마커 아이콘
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.write_location_active);

        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem1.setTMapPoint( tMapPoint1 ); // 마커의 좌표 지정
        markerItem1.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem1.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

        markerItem2.setIcon(bitmap); // 마커 아이콘 지정
        markerItem2.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem2.setTMapPoint( tMapPoint2 ); // 마커의 좌표 지정
        markerItem2.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem2.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem2", markerItem2); // 지도에 마커 추가

        markerItem3.setIcon(bitmap); // 마커 아이콘 지정
        markerItem3.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem3.setTMapPoint( tMapPoint3 ); // 마커의 좌표 지정
        markerItem3.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem3.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem3", markerItem3); // 지도에 마커 추가

        markerItem4.setIcon(bitmap); // 마커 아이콘 지정
        markerItem4.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem4.setTMapPoint( tMapPoint4 ); // 마커의 좌표 지정
        markerItem4.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem4.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem4", markerItem4); // 지도에 마커 추가

        markerItem5.setIcon(bitmap); // 마커 아이콘 지정
        markerItem5.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem5.setTMapPoint( tMapPoint5 ); // 마커의 좌표 지정
        markerItem5.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem5.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem5", markerItem5); // 지도에 마커 추가

        markerItem6.setIcon(bitmap); // 마커 아이콘 지정
        markerItem6.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem6.setTMapPoint( tMapPoint6 ); // 마커의 좌표 지정
        markerItem6.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem6.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem6", markerItem6); // 지도에 마커 추가

        markerItem7.setIcon(bitmap); // 마커 아이콘 지정
        markerItem7.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem7.setTMapPoint( tMapPoint7 ); // 마커의 좌표 지정
        markerItem7.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem7.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem7", markerItem7); // 지도에 마커 추가

        markerItem8.setIcon(bitmap); // 마커 아이콘 지정
        markerItem8.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem8.setTMapPoint( tMapPoint8 ); // 마커의 좌표 지정
        markerItem8.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem8.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem8", markerItem8); // 지도에 마커 추가4


        markerItem9.setIcon(bitmap); // 마커 아이콘 지정4
        markerItem9.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem9.setTMapPoint( tMapPoint9 ); // 마커의 좌표 지정
        markerItem9.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem9.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem9", markerItem9); // 지도에 마커 추가

        markerItem10.setIcon(bitmap); // 마커 아이콘 지정
        markerItem10.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem10.setTMapPoint( tMapPoint10 ); // 마커의 좌표 지정
        markerItem10.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem10.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem10", markerItem10); // 지도에 마커 추가

        markerItem11.setIcon(bitmap); // 마커 아이콘 지정
        markerItem11.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem11.setTMapPoint( tMapPoint11 ); // 마커의 좌표 지정
        markerItem11.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem11.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem11", markerItem11); // 지도에 마커 추가

        markerItem12.setIcon(bitmap); // 마커 아이콘 지정
        markerItem12.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem12.setTMapPoint( tMapPoint12 ); // 마커의 좌표 지정
        markerItem12.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem12.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem12", markerItem12); // 지도에 마커 추가

        markerItem13.setIcon(bitmap); // 마커 아이콘 지정
        markerItem13.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem13.setTMapPoint( tMapPoint13 ); // 마커의 좌표 지정
        markerItem13.setName("SKT타워"); // 마커의 타이틀 지정
        markerItem13.setEnableClustering(true);
        tMapView.addMarkerItem("markerItem13", markerItem13); // 지도에 마커 추가

//        tMapView.setCenterPoint( 126.985302, 37.570841 );
//
        tMapView.setEnableClustering(true);

//        tMapView.setEnableClustering(true);
        return v;
    }

    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                tMapView.setLocationPoint(longitude, latitude);
                tMapView.setCenterPoint(longitude, latitude);
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    public void setGps() {
        final LocationManager lm = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // 등록할 위치제공자(실내에선 NETWORK_PROVIDER 권장)
                1000, // 통지사이의 최소 시간간격 (miliSecond)
                1, // 통지사이의 최소 변경거리 (m)
                mLocationListener);
    }

    @Override
    public void onLocationChange(Location location) {
        if(bTrackingMode){
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
        }
    }
}
