package org.dreamfly.positionsystem.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by asus on 2015/1/22.
 */
public class NetInfoBroadCastRecevicer extends BroadcastReceiver {

    private int conType;

    //网络连接类型
    public void onReceive(Context mContext, Intent intent) {
        String recevieAction = intent.getAction();
        if (recevieAction.equals(ConnectivityManager.CONNECTIVITY_ACTION))
        //广播消息中如果是系统网络状态变化了
        {
            ConnectivityManager mConectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (this.dealNetworkInfoChanged(mConectivityManager)) {
                //网络是正常的
            } else {

                Toast.makeText(mContext, "网络连接不可用", Toast.LENGTH_SHORT).show();
                //提示用户网络不正常
            }
        }
    }

    /**
     * 只针对了移动网络和wifi网络
     *
     * @param mConectivityManager
     * @return
     */
    private boolean dealNetworkInfoChanged(ConnectivityManager mConectivityManager) {
        boolean isCon = false;
        NetworkInfo activeInfo = mConectivityManager.getActiveNetworkInfo();
        if (activeInfo != null) {
            int conType = activeInfo.getType();
            switch (conType) {
                case ConnectivityManager.TYPE_WIFI:
                    isCon = this.dealWifiWork(mConectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI));
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    isCon = this.dealMoblieInfo(mConectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE));
                    break;
                default:
                    isCon = false;
                    break;
            }
        }
        return (isCon);
    }

    /**
     * 处理wifi的情况
     *
     * @param wifiNetInfo
     * @return
     */
    private boolean dealWifiWork(NetworkInfo wifiNetInfo) {
        if (wifiNetInfo.isConnected()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * 处理移动网络的情况
     *
     * @param moblieNetInfo
     * @return
     */
    private boolean dealMoblieInfo(NetworkInfo moblieNetInfo) {
        if (moblieNetInfo.isAvailable()) {
            if (moblieNetInfo.isConnected()) {
                return (true);
            } else {
                return (false);
            }
        } else {
            return (false);
        }

    }


}
