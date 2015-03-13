package org.dreamfly.positionsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;

/**
 * 监听网络状态的serivce
 * Created by lzw on 2015/3/13.
 */
public class NetWorkInfoService  extends Service {

    private NetInfoServiceBinder mBinder;
    private NetWorkInfoMsgSender mSender;
    private NetInfoBroadCastRecevicer mNetInfoBroadCastReceiver;

    public IBinder onBind(Intent intent) {
        return (mBinder);
    }


    public void onCreate(){
         super.onCreate();
         this.mBinder=new NetInfoServiceBinder();
         this.mNetInfoBroadCastReceiver=new NetInfoBroadCastRecevicer(mHandler);

    }

    public interface NetWorkInfoMsgSender{
        public void sendNetWorkInfo(int NetWorkState);
    }

    /**
     * 将BroadCastReceiver中发送的网络状态发送给activity,使得前端视图变化
     */
    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            int state=msg.getData().getInt("netSate");
            sendInfoMsg(state);
        }

        private void sendInfoMsg(int netInfoState){
             if(mSender==null){
                   mSender=mBinder.getNetWorkInfoMsgSender();
                   mSender.sendNetWorkInfo(netInfoState);
             }
        }
    };
}
