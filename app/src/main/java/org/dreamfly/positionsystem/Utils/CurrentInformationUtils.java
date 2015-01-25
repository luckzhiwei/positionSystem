package org.dreamfly.positionsystem.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * 该类用于加载一些初始化信息
 * 系统时间，地理位置
 * Created by zhengyl on 15-1-18.
 */
public class CurrentInformationUtils {

    private String getCurrentTime;
    private int month, day, hour, minute;
    Calendar c = Calendar.getInstance();
    private Context context;



    public CurrentInformationUtils(Context context){
        this.context=context;
    }
    /**
     * 获取系统时间
     * @return
     */
    public String getCurrentTime() {

        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        if (minute < 10) {
            getCurrentTime = month + "-" + day + "  " + hour + ":" + "0" + minute;
        } else {
            getCurrentTime = month + "-" + day + "  " + hour + ":" + minute;
        }

        return getCurrentTime;

    }

    /**
     * 用于初始化地理位置的字符串数组
     * @param i 位于listview的条目位置
     * @return
     */
    public String setFirstLocation(int i) {

        String[] position = new String[]{"上次的位置:北京天安门", "上次的位置:长江三峡大坝",
                "上次的位置:电子科技大学", "上次的位置:西安长安街", "上次的位置:山东青岛", "上次的位置:天津廊坊",
                "上次的位置:夏威夷群岛"};
        return position[i];

    }

    /**
     * 得到用于初始化设备名称的字符串数组
     * @param i 位于listview的条目位置
     * @return
     */
    public String setFirstDeviceName(int i) {

        String[] device = new String[]{"Samsung i9260", "Samsung s4",
                "iPhone6 plus", "Mi2S_note", "LG G3 D859", "Lenovo S2", "Meizu MX4"};
        return device[i];
    }

    /**
     *获取本机的设备名称
     * @return
     */
    public String getCurrentDeviceName(){
        Build bd=new Build();
        String model=bd.MODEL;
        Log.i("position",model);
        return  model;
    }

    /**
     * 获取地理位置信息方法
     * @return
     */
    public Location getUserLocation(Context context){


        MLocationListener locationListener=new MLocationListener();
        LocationManager locationManager=(LocationManager)context.
                getSystemService(Context.LOCATION_SERVICE);

         Location  location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            if (location == null) {
                location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
            }

        //Log.v("mposition",""+location.getLongitude());
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        if(location == null) {
            locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        return location;

    }



    /**
     * 自定义地理位置改变的监听事件
     */
    private class MLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
            // log it when the location changes

           Log.v("myposition",""+location.getLatitude());
            SharedPreferences sp = context.getSharedPreferences("listener", 0);
            SharedPreferences.Editor se = sp.edit();
            se.putString("location", "" + location.getLatitude());
            se.putString("location1", "" + location.getLongitude());
           sendLocation(location);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Provider被disable时触发此函数，比如GPS被关闭
        }

        @Override
        public void onProviderEnabled(String provider) {
            //  Provider被enable时触发此函数，比如GPS被打开

        }

        @Override
        public void onProviderDisabled(String provider) {
            // Provider的转态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        }

    }

    public Location sendLocation(Location location){
        return location;
    }

}
