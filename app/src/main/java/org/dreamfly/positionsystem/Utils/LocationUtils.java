package org.dreamfly.positionsystem.Utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by zhengyl on 15-1-25.
 */
public class LocationUtils {
    private Location location;
    private Context context;
    public LocationUtils(Context mcontext){
        context=mcontext;
    }
    public static LocationManager getLocationManager(Context context)
    {
        return (LocationManager)context.getSystemService(context.LOCATION_SERVICE);
    }

    /**
     * 初始化Locationmanager
     * @param context
     * @return
     */
    public Location getLocation(Context context)
    {
        LocationManager locationManager=getLocationManager(context);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            /*android.provider.Settings.Secure.setLocationProviderEnabled
                    (context.getContentResolver(), LocationManager.GPS_PROVIDER, false);*/
            Toast.makeText(context,"gps初始化错误",Toast.LENGTH_LONG);
        }
        return setPosition(context);
    }

    /**
     * 获取最好的定位方式
     * @param context
     * @return
     */
    public Location setPosition(Context context){
        LocationManager locationManager=getLocationManager(context);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider=locationManager.getBestProvider(criteria,true);
        //一直在监听事件里面获取location
        while(location==null)
        {
            location =locationManager.getLastKnownLocation(provider);
        }
        LocationListener locationListener = new LocationListener()
        {

            @Override
            public void onLocationChanged(Location location)
            {

                LocationUtils.this.location=location;
            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

        };
        locationManager.requestLocationUpdates(provider, 1000, 10, locationListener);
        return location;

    }
}
