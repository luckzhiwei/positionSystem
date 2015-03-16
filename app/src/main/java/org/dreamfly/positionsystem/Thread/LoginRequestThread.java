package org.dreamfly.positionsystem.Thread;

import android.os.Handler;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzw on 2015/1/28.
 * 二次登录的Thread的子类
 */
public class LoginRequestThread extends BaseThread {


    public LoginRequestThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    protected void dealReponseString(String responseString) throws Exception {
        this.resultMap = new HashMap<String, String>();
        String tmpArrStr[] = responseString.split(":");
        if (tmpArrStr[1].equals("login")) {
            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
            this.resultMap.put("type", tmpArrStr[2]);

        } else if (tmpArrStr.equals("unlogin")) {
            this.resultMap.put(tmpArrStr[0], tmpArrStr[1]);
            this.resultMap.put("failReason", tmpArrStr[2]);
        }

    }


}
