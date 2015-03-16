package org.dreamfly.positionsystem.Thread;

import android.os.Handler;

import java.util.HashMap;

/**
 * Created by lzw on 2015/3/4.
 * admin申请向服务器反拨出电话的线程
 */
public class CallPhoneThread extends BaseThread {

    public CallPhoneThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    protected void dealReponseString(String responseString) {
        this.resultMap = new HashMap<String, String>();
        String[] strArr = responseString.split(":");
        if (strArr.length == 2) {
            resultMap.put(strArr[0], strArr[1]);
        } else if (strArr.length == 3) {
            resultMap.put(strArr[0], strArr[1]);
            resultMap.put("failReason", strArr[2]);
        }
    }
}
