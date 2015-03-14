package org.dreamfly.positionsystem.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lzw on 2015/3/10.
 */
public class NetWorkInfoUtils {
            /**
             * 判断网络是否可行
             * @param mContext 上下文参数
             * @return
             */
            public static boolean isNetWorkAlive(Context mContext){
                boolean isAlive=false;
                ConnectivityManager cm=(ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeInfo=cm.getActiveNetworkInfo();
                if(activeInfo!=null){
                     int conType=activeInfo.getType();
                     switch (conType){
                         case ConnectivityManager.TYPE_MOBILE:
                              isAlive=true;
                              break;
                         case ConnectivityManager.TYPE_WIFI:
                              isAlive=true;
                              break;
                         case ConnectivityManager.TYPE_WIMAX:
                              isAlive=true;
                              break;
                         default:
                               isAlive=false;
                               break;
                     }
                }
                return(isAlive);
            }



}
