package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by zhengyl on 15-2-24.
 */
public class RenameThread extends BaseThread {

    public RenameThread(Handler mHandler, String stateId) {
        super(mHandler, stateId);
    }

    @Override
    protected void dealReponseString(String responseString) throws Exception {
        this.resultMap = new HashMap<String, String>();
        String tmpArrStr[] = responseString.split(":");
        if (tmpArrStr[1].equals("success")) {
            resultMap.put(tmpArrStr[0], tmpArrStr[1]);
        } else if (tmpArrStr[1].equals("fail")) {
            resultMap.put("failreson", tmpArrStr[2]);
        }
        Log.i("lzw", responseString);
    }
}
