package org.dreamfly.positionsystem.Activity;


import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.KeyEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;

import com.baidu.mapapi.model.LatLng;


import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.LocationUtils;

/**
 * Created by zhengyl on 15-1-13.
 * 定位界面Activity类
 */
public class PositionActivity extends Activity implements OnGetGeoCoderResultListener {

    private TextView txtPositionLatitute, txtPositionLongitute, txtPositionLocation;
    private Button btnPositionActivityGeo;
    private LocationClient locationClient;
    private int position = 0;
    private DataBase mDataBase = new DataBase(this);
    private DefinedShared mdata = new DefinedShared(this);
    private LocationUtils mLocationUtils;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private String sb, sb1;
    private boolean isFirstLoc = true;
    com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    private MyLocationConfiguration.LocationMode mCurrentMode =
            MyLocationConfiguration.LocationMode.NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        String libName = "BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);

        this.setContentView(R.layout.position_layout);
        this.initial();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        //locationClient.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(PositionActivity.this, ManagerActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initial() {
        this.bindID();
        dealIntent();
        mcoder = GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);
        if (dealIntent()) {
            Log.i("zyl", "mapsuccess");
            MapInfo(isFirstLoc);
            codeChanging();
        }
    }


    /**
     * 绑定控件ID
     */
    private void bindID() {
        txtPositionLocation = (TextView) this.findViewById(R.id.txt_position_location);

        mMapView = (MapView) this.findViewById(R.id.bmapView);


    }

    private boolean dealIntent() {
        String location = this.getIntent().getStringExtra("userlocation");
        if (location != null && location != "") {
            String str[] = location.split(" ");
            sb = str[0];
            sb1 = str[1];
            return true;
        } else {
            return false;
        }
    }

    /**
     * 初始化定位服务信息
     */
    private void locationInfo() {
        locationClient = mLocationUtils.getLocationClient();
        locationClient.start();
        locationClient.requestLocation();

    }


    /**
     * 转换编码
     */
    private void codeChanging() {

        LatLng ptCenter = new LatLng(
                (Float.valueOf(sb)),
                Float.valueOf(sb1));
        mcoder.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));

    }

    /**
     * 百度地图服务
     */
    protected void MapInfo(boolean isFirstLoc) {
        //初始化百度地图
        mBaiduMap = mMapView.getMap();
        //设置类型:普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置交通图
        mBaiduMap.setTrafficEnabled(true);
        //设置标记地点
        mBaiduMap.setMyLocationEnabled(true);
        //从经纬度中得到地理位置
        MyLocationData locData = new MyLocationData.Builder().accuracy(0).direction(100)
                .latitude(Float.valueOf(sb)).
                        longitude(Float.valueOf(sb1)).build();

        mBaiduMap.setMyLocationData(locData);
        //设置定位标记标记图案
        MyLocationConfiguration config = new MyLocationConfiguration
                (mCurrentMode, true, BitmapDescriptorFactory.fromResource(R.drawable.icon_geo));
        mBaiduMap.setMyLocationConfigeration(config);
        Log.i("zyl", "184success");
        //如果是第一次定位,定位到指定地点
        if (isFirstLoc) {
            this.isFirstLoc = false;

            LatLng ll = new LatLng
                    (Float.valueOf(sb), Float.valueOf(sb1));
            //设置默认比例尺
            float f = mBaiduMap.getMaxZoomLevel();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 3);
            mBaiduMap.animateMapStatus(u);
            Log.i("zyl", "194success");
        }


    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        Log.i("lzw", "trigger");
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PositionActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        txtPositionLocation.setText(result.getAddress());
        mdata.putString("locationback", "locationback", result.getAddress());
        Log.i("lzw", result.getAddress() + "");

    }


}
