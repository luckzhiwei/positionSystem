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

import org.dreamfly.positionsystem.R;

/**
 * Created by zhengyl on 15-1-13.
 * 定位界面Activity类
 */
public class PositionActivity extends ActionBarActivity implements OnGetGeoCoderResultListener {

    private TextView txtPositionLatitute,txtPositionLongitute,txtPositionLocation;
    private Button btnPositionActivityGeo,btnPositionStart;
    private LocationClient locationClient = null;
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
    /*private void positionInfo(TextView txt,TextView txt1,TextView txt2){
        SharedPreferences sp=this.getSharedPreferences("position",0);
        String location=sp.getString("location","");
        String location1=sp.getString("location1",""packa);
        txt.setText(location+"");
        txt1.setText(location1+"");
    }*/

    /**
     * 绑定控件ID
     */
    private void bindID(){
       txtPositionLatitute=(TextView)this.findViewById(R.id.txt_position_latitute);
       txtPositionLongitute=(TextView)this.findViewById(R.id.txt_position_longitute);
       txtPositionLocation=(TextView)this.findViewById(R.id.txt_position_location);
       btnPositionActivityGeo=(Button)this.findViewById(R.id.btn_positionactivity_geo);
       btnPositionStart=(Button)this.findViewById(R.id.btn_position_start);
    }

    private void locationInfo(){
        this.locationClient=new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("dreamflyLocationDemo"); // 设置产品线名称
        option.setScanSpan(1000);// 设置定时定位的时间间隔。单位毫秒
        locationClient.setLocOption(option);
        locationClient.registerLocationListener(new BDLocationListener() {
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
        });


    }

    /**
     * 通过按钮控制是否获取定位服务
     * @param view
     */
    public void start_btn(View view) {

        if (locationClient == null) {
            return;
        }
        if (locationClient.isStarted()) {
            btnPositionStart.setText("Start");
            locationClient.stop();
        } else {
            btnPositionStart.setText("Stop");
            locationClient.start();
			/*
			 * 当所设的整数值大于等于1000（ms）时，定位SDK内部使用定时定位模式。调用requestLocation(
			 * )后，每隔设定的时间，定位SDK就会进行一次定位。如果定位SDK根据定位依据发现位置没有发生变化，就不会发起网络请求，
			 * 返回上一次定位的结果；如果发现位置改变，就进行网络请求进行定位，得到新的定位结果。
			 * 定时定位时，调用一次requestLocation，会定时监听到定位结果。
			 */
            locationClient.requestLocation();
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
