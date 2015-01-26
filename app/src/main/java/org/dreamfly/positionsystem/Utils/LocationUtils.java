package org.dreamfly.positionsystem.Utils;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by zhengyl on 15-1-26.
 */
public class LocationUtils {
    private Context context;
    private LocationClient mlocationClient;

    public LocationUtils(Context context){
        this.context=context;
        this.mlocationClient=new LocationClient(context);
    }

    public void LocationInfo(){
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 是否打开GPS
        option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
        option.setPriority(LocationClientOption.NetWorkFirst); // 设置定位优先级
        option.setProdName("dreamflyLocationDemo"); // 设置产品线名称
        option.setScanSpan(1000);// 设置定时定位的时间间隔。单位毫秒
        mlocationClient.setLocOption(option);
    }
    public LocationClient getLocationClient(){
        return this.mlocationClient;
    }

}
