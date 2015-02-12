package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by zhengyl on 15-2-12.
 */
public class ManagerListThread extends BaseThread {

    public ManagerListThread(Handler mHandler,String stateID){
        super(mHandler,stateID);
    }
    @Override
    protected void dealReponseString(String responseString) {
        this.resultMap = new HashMap<String, String>();
        resultMap.put("test",responseString);
        Log.i("zylresponse",responseString);
    }
}
