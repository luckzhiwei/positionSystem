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

    protected void dealReponseString(String responseString) throws Exception {
        this.resultMap = new HashMap<String, String>();
        String tmpArrStr[] = responseString.split(":");
        if (tmpArrStr[1].equals("login")) {
            String tmpArrStr1[] = tmpArrStr[2].split("[+]");

            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
            this.resultMap.put("type", tmpArrStr1[0]);
            this.resultMap.put("dataBaseId", tmpArrStr[3]);


        } else if (tmpArrStr[1].equals("unlogin")) {
            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
            this.resultMap.put("failReason", tmpArrStr[2]);
        }

        Log.i("lzw", responseString);


    }
}
