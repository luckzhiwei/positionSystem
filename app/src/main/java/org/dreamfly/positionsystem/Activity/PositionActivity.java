package org.dreamfly.positionsystem.Activity;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
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

import org.dreamfly.positionsystem.Database.DataBase;
import org.dreamfly.positionsystem.R;
import org.dreamfly.positionsystem.Utils.LocationUtils;

/**
 * Created by zhengyl on 15-1-13.
 * 定位界面Activity类
 */
public class PositionActivity extends ActionBarActivity implements OnGetGeoCoderResultListener {

    private TextView txtPositionLatitute,txtPositionLongitute,txtPositionLocation;
    private Button btnPositionActivityGeo;
    private LocationClient locationClient = null;

    private DataBase mDataBase = new DataBase(this);
    private LocationUtils mLocationUtils;
    private MapView mMapView=null;
    private BaiduMap mBaiduMap;
    private String sb,sb1;
    private boolean isFirstLoc=true;
    com.baidu.mapapi.search.geocode.GeoCoder mcoder;
    private MyLocationConfiguration.LocationMode mCurrentMode =
            MyLocationConfiguration.LocationMode.NORMAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        String libName="BaiduMapSDK_v3_2_0_11";
        System.loadLibrary(libName);

        this.setContentView(R.layout.position_layout);
        this.initial();
        this.bindListener();
      }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
        locationClient.stop();
    }
    @Override
    protected void onResume(){
        super.onResume();
        mMapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mMapView.onPause();
    }

    private void initial(){
        this.bindID();
        mLocationUtils=new LocationUtils(this);
        mLocationUtils.LocationInfo();
        this.locationInfo();
        mcoder=GeoCoder.newInstance();
        mcoder.setOnGetGeoCodeResultListener(this);


    }

    /**
     * 显示系统得到的经纬度
     * @param txt
     * @param txt1
     * @param txt2
     */

    /**
     * 绑定控件ID
     */
    private void bindID(){
       txtPositionLatitute=(TextView)this.findViewById(R.id.txt_position_latitute);
       txtPositionLongitute=(TextView)this.findViewById(R.id.txt_position_longitute);
       txtPositionLocation=(TextView)this.findViewById(R.id.txt_position_location);
       btnPositionActivityGeo=(Button)this.findViewById(R.id.btn_positionactivity_geo);
       mMapView=(MapView)this.findViewById(R.id.bmapView);


    }

    /**
     * 初始化定位服务信息
     */
    private void locationInfo(){
        locationClient=mLocationUtils.getLocationClient();
        BDListener bdListener=new BDListener();
        locationClient.registerLocationListener(bdListener);

        locationClient.start();
        locationClient.requestLocation();


    }
    public class BDListener implements com.baidu.location.BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location == null){
                return;
            }

            sb=location.getLatitude()+"";

            sb1=location.getLongitude()+"";

            txtPositionLatitute.setText(sb);
            txtPositionLongitute.setText(sb1);
            MapInfo(txtPositionLatitute, txtPositionLongitute,isFirstLoc);
        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    }


    /**
     * 绑定按钮监听
     */
    private void bindListener() {
        this.btnPositionActivityGeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng ptCenter=new LatLng(
                        (Float.valueOf(txtPositionLatitute.getText().toString())),
                        Float.valueOf(txtPositionLongitute.getText().toString()));
                mcoder.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
            }
        });
    }

    /**
     * 百度地图服务
     */
    protected void MapInfo(TextView txt1,TextView txt2,boolean isFirstLoc){
        //初始化百度地图
        mBaiduMap=mMapView.getMap();
        //设置类型:普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //设置交通图
        mBaiduMap.setTrafficEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        //设置标记地点
        MyLocationData locData=new MyLocationData.Builder().accuracy(0).direction(100)
                .latitude(Float.valueOf(txt1.getText().toString())).
                        longitude(Float.valueOf(txt2.getText().toString())).build();
        mBaiduMap.setMyLocationData(locData);
        //设置定位标记标记图案
        MyLocationConfiguration config = new MyLocationConfiguration
                (mCurrentMode, true, BitmapDescriptorFactory.fromResource(R.drawable.icon_marka));
       mBaiduMap.setMyLocationConfigeration(config);
        //如果是第一次定位,定位到指定地点
        if(isFirstLoc){
            this.isFirstLoc=false;

            LatLng ll=new LatLng
                    (Float.valueOf(txt1.getText().toString()),Float.valueOf(txt2.getText().toString()));
            //设置默认比例尺
            float f=mBaiduMap.getMaxZoomLevel();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll,f-3);
            mBaiduMap.animateMapStatus(u);
        }



    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(PositionActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        txtPositionLocation.setText(result.getAddress());

    }

}
