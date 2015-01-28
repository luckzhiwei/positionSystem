package org.dreamfly.positionsystem.Thread;

import android.os.Handler;

import java.util.HashMap;

/**
 * Created by asus on 2015/1/28.
 */
public class FirstLoginRequestThread extends  BaseThread {

     public FirstLoginRequestThread(Handler mHandler,String stateId)
     {
         super(mHandler,stateId);
     }

    protected  void dealReponseString(String responseString)
    {
        this.resultMap=new HashMap<String,String>();
        String tmpArrStr[]=responseString.split(":");
        if(tmpArrStr[1].equals("login"))
        {
            this.resultMap.put(tmpArrStr[0],tmpArrStr[1]);
            this.resultMap.put("dataBaseId",tmpArrStr[2]);

        }else if(tmpArrStr.equals("unlogin"))
        {
            this.resultMap.put(tmpArrStr[0],tmpArrStr[1]);
            this.resultMap.put("failReason",tmpArrStr[2]);
        }
    }
}
