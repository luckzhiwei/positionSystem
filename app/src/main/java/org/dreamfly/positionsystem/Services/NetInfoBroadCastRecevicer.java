package org.dreamfly.positionsystem.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;

/**
 * Created by asus on 2015/1/22.
 */
public class NetInfoBroadCastRecevicer extends BroadcastReceiver {

    private int conType;
    private Handler mHandler;

    public NetInfoBroadCastRecevicer(Handler mHandler) {
        this.mHandler = mHandler;
    }


    //网络连接类型
    public void onReceive(Context mContext, Intent intent) {
        String recevieAction = intent.getAction();
        if (recevieAction.equals(ConnectivityManager.CONNECTIVITY_ACTION))
        //广播消息中如果是系统网络状态变化了
        {
            ConnectivityManager mConectivityManager =
                    (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (this.dealNetworkInfoChanged(mConectivityManager)) {
                this.sendNetInfoServiceMsg(ComParameter.STATE_RIGHT);
            } else {
                this.sendNetInfoServiceMsg(ComParameter.STATE_ERROR);
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

    /**
     * 向service发送挽网络状态
     *
     * @param state
     */
    private void sendNetInfoServiceMsg(int state) {
        Message msg = new Message();
        Bundle bd = new Bundle();
        bd.putInt("netState", state);
        msg.setData(bd);
        this.mHandler.sendMessage(msg);
    }


}
