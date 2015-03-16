package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by asus on 2015/1/28.
 */
public class RegisterRequestThread extends BaseThread {

    public RegisterRequestThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    protected void dealReponseString(String responseString) throws Exception {
        this.resultMap = new HashMap<String, String>();
        String tmpStrArr[] = responseString.split(":");
        if (tmpStrArr.length == 2) {
            this.resultMap.put(tmpStrArr[0], tmpStrArr[1]);
        } else if (tmpStrArr.length == 3) {
            this.resultMap.put(tmpStrArr[0], tmpStrArr[1]);
            this.resultMap.put("failReason", tmpStrArr[2]);
        }
        Log.i("lzw", "返回字符" + responseString);

    }

}
