package org.dreamfly.positionsystem.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
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
import org.dreamfly.positionsystem.Utils.LocationUtils;

/**
 * Created by zhengyl on 15-2-21.
 * 百度定位服务类
 */
public class BaiduLocationService implements OnGetGeoCoderResultListener {

    private Context context;
    protected LocationUtils mLocationUtils;
    protected LocationClient locationClient;
    protected String lat;
    protected String lon;
    protected DefinedShared mdata;
    protected DataBase mDataBase;
    private boolean isClear = true;
    protected com.baidu.mapapi.search.geocode.GeoCoder mcoder;

    /**
     * 构造函数
     * @param context
     */
    public BaiduLocationService(Context context){
        this.context=context;
        mdata=new DefinedShared(context);
        mDataBase = new DataBase(context);
        mLocationUtils=new LocationUtils(context);
        mLocationUtils.LocationInfo();
        mcoder = GeoCoder.newInstance();

    }
    /**
     * 调用百度定位Sdk,并存储定位数据
     */
    public void locationSave() {

        locationClient = mLocationUtils.getLocationClient();
        locationClient.start();
        locationClient.requestLocation();
        BDListener bdListener = new BDListener();
        locationClient.registerLocationListener(bdListener);
        mcoder.setOnGetGeoCodeResultListener(this);
        Log.i("lzw", "locationSDKwork");
    }

    /**
     * 百度定位接口
     */
    public class BDListener implements com.baidu.location.BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null) {
                return;
            }

            lat = location.getLatitude() + "";

            lon = location.getLongitude() + "";
            mDataBase.items_changeValue(ComParameter.DEVICE,"latitude",lat,0);
            mDataBase.items_changeValue(ComParameter.DEVICE,"longitude",lon,0);
            reverseCode(lat, lon);
            Log.i("lzw",lat+"");
            Log.i("lzw",lon+"");
            locationClient.stop();
            Log.i("lzw","unlink");

        }

        @Override
        public void onReceivePoi(BDLocation bdLocation) {

        }
    }
    /**
     * 调用经纬度编码转换函数并用Sharepreference保存
     *
     * @param lat
     * @param lon
     */
    public void reverseCode(String lat, String lon) {
        LatLng ptCenter = new LatLng(
                (Float.valueOf(lat)),
                Float.valueOf(lon));
        mcoder.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));

    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(context, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String s = result.getAddress();
        mDataBase.items_changeValue(ComParameter.DEVICE,"location",s,0);
        //将获得的地址保存
        SharedPreferences mpreference = context.getSharedPreferences("address", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mpreference.edit();
        editor.putString("address", s);
        editor.commit();
        if (isClear) {
            editor.clear();
            editor.putString("address", s);
            editor.commit();
        }
        Log.i("thislzw", "您的当前位置" + s + "已被保存");

    }


}
