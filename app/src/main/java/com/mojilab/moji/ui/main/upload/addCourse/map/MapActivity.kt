package com.mojilab.moji.ui.main.upload.addCourse.map

import android.os.Bundle
import android.widget.Toast

import androidx.fragment.app.FragmentActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mojilab.moji.R


class MapActivity : FragmentActivity(), OnMapReadyCallback {

    //구글맵참조변수
    lateinit var mMap: GoogleMap

    //마커정보창 클릭리스너는 다작동하나, 마커클릭리스너는 snippet정보가 있으면 중복되어 이벤트처리가 안되는거같다.
    // oneMarker(); 는 동작하지않으나 manyMarker(); 는 snippet정보가 없어 동작이가능하다.

    //정보창 클릭 리스너
    internal var infoWindowClickListener: GoogleMap.OnInfoWindowClickListener =
        GoogleMap.OnInfoWindowClickListener { marker ->
            val markerId = marker.id
            Toast.makeText(this@MapActivity, "정보창 클릭 Marker ID : $markerId", Toast.LENGTH_SHORT)
                .show()
        }

    //마커 클릭 리스너
    internal var markerClickListener: GoogleMap.OnMarkerClickListener =
        GoogleMap.OnMarkerClickListener { marker ->
            val markerId = marker.id
            //선택한 타겟위치
            val location = marker.position
            Toast.makeText(
                this@MapActivity,
                "마커 클릭 Marker ID : " + markerId + "(" + location.latitude + " " + location.longitude + ")",
                Toast.LENGTH_SHORT
            ).show()
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mojilab.moji.R.layout.activity_map)

        // SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출한다.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.actvity_content_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this) //getMapAsync must be called on the main thread.
    }

    override//구글맵을 띄울준비가 됬으면 자동호출된다.
    fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //지도타입 - 일반
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        oneMarker()


        // 맵 터치 이벤트 구현 //
        mMap.setOnMapClickListener { point ->
            val mOptions = MarkerOptions()
            // 마커 타이틀
            mOptions.title("마커 좌표")
            val latitude = point.latitude // 위도
            val longitude = point.longitude // 경도
            // 마커의 스니펫(간단한 텍스트) 설정
            mOptions.snippet("$latitude, $longitude")
            // LatLng: 위도 경도 쌍을 나타냄
            mOptions.position(LatLng(latitude, longitude))
            // 마커(핀) 추가
            googleMap.addMarker(mOptions)
        }
        ////////////////////

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }

    //마커하나찍는 기본 예제
    fun oneMarker() {
        // 서울 여의도에 대한 위치 설정
        val seoul = LatLng(37.52487, 126.92723)

        // 구글 맵에 표시할 마커에 대한 옵션 설정  (알파는 좌표의 투명도이다.)
        val makerOptions = MarkerOptions()
        makerOptions
            .position(seoul)
            .title("원하는 위치(위도, 경도)에 마커를 표시했습니다.")
            .snippet("여기는 여의도인거같네여!!")
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            .alpha(0.5f)

        // 마커를 생성한다. showInfoWindow를 쓰면 처음부터 마커에 상세정보가 뜨게한다. (안쓰면 마커눌러야뜸)
        mMap.addMarker(makerOptions) //.showInfoWindow();

        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener)

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener)

        //카메라를 여의도 위치로 옮긴다.
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        //처음 줌 레벨 설정 (해당좌표=>서울, 줌레벨(16)을 매개변수로 넣으면 된다.) (위에 코드대신 사용가능)(중첩되면 이걸 우선시하는듯)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 16f))

        mMap.setOnMarkerClickListener {
            Toast.makeText(this@MapActivity, "눌렀습니다!!", Toast.LENGTH_LONG)
            false
        }

    }

    ////////////////////////  구글맵 마커 여러개생성 및 띄우기 //////////////////////////
    fun manyMarker() {
        // for loop를 통한 n개의 마커 생성
        for (idx in 0..9) {
            // 1. 마커 옵션 설정 (만드는 과정)
            val makerOptions = MarkerOptions()
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                .position(LatLng(37.52487 + idx, 126.92723))
                .title("마커$idx") // 타이틀.

            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions)
        }
        //정보창 클릭 리스너
        mMap.setOnInfoWindowClickListener(infoWindowClickListener)

        //마커 클릭 리스너
        mMap.setOnMarkerClickListener(markerClickListener)

        // 카메라를 위치로 옮긴다.
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.52487, 126.92723)))
    }

}