package org.dreamfly.positionsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;


/**
 * Created by asus on 2015/3/4.
 * 轮训模块的组件service
 */
public class QueryService extends Service {


    private QuerySerivcesBinder mQueryBiind;


    public IBinder onBind(Intent intent) {
        return (mQueryBiind);
    }

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            Bundle bd=msg.getData();
            if(bd.getInt("ACTION")== ComParameter.ACTION_CALLPHONE){
                  callPhone(bd.getString("callNum"));
            }
        }

        private void callPhone(String phoneNum){
               Intent callIn=new Intent();
               callIn.setAction(Intent.ACTION_CALL);
               callIn.setData(Uri.parse("tel:"+phoneNum));
               QueryService.this.startActivity(callIn);
        }
    };

    public void onCreate(){
         super.onCreate();
         this.mQueryBiind=new QuerySerivcesBinder(this,mHandler);

    }

}
