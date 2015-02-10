package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by asus on 2015/1/28.
 */
public class FirstLoginRequestThread extends BaseThread {

    public FirstLoginRequestThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    protected void dealReponseString(String responseString) {
        this.resultMap = new HashMap<String, String>();
//        String tmpArrStr[] = responseString.split(":");
//        if (tmpArrStr[1].equals("login")) {
//            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
//            this.resultMap.put("dataBaseId", tmpArrStr[3]);
//            this.resultMap.put("type", tmpArrStr[4]);
//
//        } else if (tmpArrStr.equals("unlogin")) {
//            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
//            this.resultMap.put("failReason", tmpArrStr[3]);
//        }
        Log.i("lzw","返回字符(firslogin)"+responseString);
       }
}
