package org.dreamfly.positionsystem.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

import org.dreamfly.positionsystem.Activity.ManagerActivity;
import org.dreamfly.positionsystem.Activity.RegulatorActivity;
import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Database.DefinedShared;
import org.dreamfly.positionsystem.R;

/**
 * Created by asus on 2015/1/12.
 */
public class Services  extends Service {
    private final IBinder mBinder=new MBinder();
    private static String TAG="zylservice";
    private DefinedShared mdata;

    /**
     * Binder
     */
    public class MBinder extends Binder{
        public Services getService(){
            return  Services.this;
        }
    }
    @Override
    public IBinder onBind(Intent  intent)
    {
        Log.i(TAG,"[SERVICE]onbind");
       return mBinder;
    }
    @Override
    public void onCreate(){
        super.onCreate();
        this.initial();
        Log.i(TAG,"[SERVICE]onCreate");
    }
    @Override
    public void onDestroy(){
        Log.i(TAG,"[SERVICE]onDestroy");
        //记录service状态
        mdata.putString(ComParameter.LOADING_STATE,ComParameter.SERVICE_STATE,
                ComParameter.STATE_FIRST);

    }
    private void initial(){
        mdata=new DefinedShared(this);
        //记录service状态
        mdata.putString(ComParameter.LOADING_STATE,ComParameter.SERVICE_STATE,
                ComParameter.STATE_SECOND);
        this.showNotification();
    }

    /**
     * 通知栏显示
     */
    private void showNotification(){
        CharSequence text="亲子安全卫士";
        NotificationManager manager=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification();
        notification.icon= R.drawable.positionsystemlogo;
        Intent i=new Intent();
        if(mdata.getString(ComParameter.LOADING_STATE,ComParameter.IDENTITY_STATE).equals("manager")) {
            i.setComponent(new ComponentName(this, ManagerActivity.class));
        }
        else {
            i.setComponent(new ComponentName(this, RegulatorActivity.class));
        }
        PendingIntent mPendingIntent=PendingIntent.getActivity(Services.this, 0, i, 0);
        notification.setLatestEventInfo(this,text,"正在为您的亲人提供您的地理位置",mPendingIntent);
        manager.notify(0,notification);
    }

}
