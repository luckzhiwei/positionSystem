package org.dreamfly.positionsystem.Activity;

import android.content.SharedPreferences;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

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
    com.baidu.mapapi.search.geocode.GeoCoder mcoder;

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
        locationClient.stop();
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

    }

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
            StringBuffer sb=new StringBuffer(256);
            sb.append(location.getLatitude());

            StringBuffer sb1=new StringBuffer(256);
            sb1.append(location.getLongitude());

            txtPositionLatitute.setText(sb);
            txtPositionLongitute.setText(sb1);

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
