package org.dreamfly.positionsystem.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import org.dreamfly.positionsystem.Activity.ManagerActivity;
import org.dreamfly.positionsystem.Activity.RegulatorActivity;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;


/**
 * Created by asus on 2015/3/4.
 * 轮训模块的组件service
 */
public class QueryService extends Service {


    private QuerySerivcesBinder mQueryBind;
    private DefinedShared mdata;
    private static String TAG = "zylservice";
    private NotificationManager manager;
    private BaiduLocationService mLocation;
    private MsgSender mMessageSender;

    public IBinder onBind(Intent intent) {
        return (mQueryBind);
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "[SERVICE]onDestroy");
        //记录service状态
        mdata.putString(ComParameter.LOADING_STATE, ComParameter.SERVICE_STATE,
                ComParameter.STATE_FIRST);
        manager.cancelAll();

    }

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            Bundle bd=msg.getData();
            if(bd.getInt("ACTION")== ComParameter.ACTION_CALLPHONE){
                  callPhone(bd.getString("callNum"));
            }
            else if(bd.getInt("ACTION")==ComParameter.ACTION_LOCATION){
                mLocation.locationSave();
                //存储地理位置到share
            }else if(bd.get("ACTION")==ComParameter.USER_LOCATION){
                showlocationToActivity(bd.getString("userlocation"));

            }else if(bd.getInt("STATE_ERROR")==ComParameter.STATE_ERROR){
                sendMsgErrorToActivity(ComParameter.STATE_ERROR);
                //向activity的handler发送错误的消息
            }

        }


        private void callPhone(String phoneNum){
               Intent callIn=new Intent();
               callIn.setAction(Intent.ACTION_CALL);
               callIn.setData(Uri.parse("tel:"+phoneNum));
               QueryService.this.startActivity(callIn);
        }



        private void sendMsgErrorToActivity(int state){
              if(mMessageSender==null){
                    mMessageSender=mQueryBind.getMsgSeneder();
              }
              mMessageSender.sendMsgError(state);
        }

    };

    private void showlocationToActivity(String userLocation){
        if(mMessageSender==null){
            mMessageSender=this.getmMessageSender();
        }

        if(userLocation!=null){
            if(mMessageSender==null){
                Log.i("zyl","实例仍然为空");
            }
            mMessageSender.sendMsgLocationToShow(userLocation);
        }else{
            mMessageSender.sendMsgLocationToShow("null");
        }
    }

    public void onCreate(){
         super.onCreate();
         initial();

    }

    private void initial() {
        mdata = new DefinedShared(this);
        //记录service状态
        mdata.putString(ComParameter.LOADING_STATE, ComParameter.SERVICE_STATE,
                ComParameter.STATE_SECOND);
        manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);

        this.showNotification();
         mLocation = new BaiduLocationService(this);
        this.mQueryBind=new QuerySerivcesBinder(this,mHandler,this);

    }

    /**
     * 通知栏显示
     */
    private void showNotification() {
        CharSequence text = "亲子安全卫士";

        Notification notification = new Notification();
        notification.icon = R.drawable.positionsystemlogo;
        Intent i = new Intent();
        if (mdata.getString(ComParameter.LOADING_STATE, ComParameter.IDENTITY_STATE).equals("manager")) {
            i.setComponent(new ComponentName(this, ManagerActivity.class));

        } else {
            i.setComponent(new ComponentName(this, RegulatorActivity.class));
        }
        PendingIntent mPendingIntent = PendingIntent.getActivity(QueryService.this, 0, i, 0);
        notification.setLatestEventInfo(this, text, "正在为您的亲人提供您的地理位置", mPendingIntent);
        manager.notify(0, notification);
    }

    public interface MsgSender {
            public void sendMsgLocationToShow(String userLcation);
            public void sendMsgError(int state);
    }

    public void setMsgSender(MsgSender msgSender){
        this.mMessageSender=msgSender;
    }

    public MsgSender getmMessageSender(){
        return this.mMessageSender;
    }



}
