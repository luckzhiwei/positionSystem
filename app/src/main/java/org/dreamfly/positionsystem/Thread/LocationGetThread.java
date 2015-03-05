package org.dreamfly.positionsystem.Thread;

import android.os.Handler;

import java.util.HashMap;

/**
 * Created by asus on 2015/3/4.
 * admin向服务器所要地理位置的线程
 */
public class LocationGetThread extends BaseThread {

    public LocationGetThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    protected void dealReponseString(String responseString) {
          this.resultMap=new HashMap<String,String>();
          String[] strArr=responseString.split(":");
          if(strArr.length==2){
                 resultMap.put(strArr[0],strArr[1]);
          }else if(strArr.length==3){
                 resultMap.put(strArr[0],strArr[1]);
                 resultMap.put("failReason",strArr[2]);
          }
    }

}
