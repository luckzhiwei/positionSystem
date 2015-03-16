package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhengyl on 15-2-12.
 */
public class ManagerListThread extends BaseThread {

    public ManagerListThread(Handler mHandler, String stateID) {
        super(mHandler, stateID);
    }

    @Override
    protected void dealReponseString(String responseString) throws Exception {

        this.resultMap = new HashMap<String, String>();
        if (responseString.equals("]")) {
            resultMap.put("connectedstate", "n");

        } else {
            this.dealJsonArray(responseString, resultMap);
        }
        resultMap.put("test", responseString);
        Log.i("zylresponse", responseString);

    }

    /**
     * 解析json数据
     *
     * @param responseString
     * @param resultMap
     * @throws Exception
     */
    private void dealJsonArray(String responseString, Map<String, String> resultMap) throws Exception {

        String dealedString = "\"" + "{" + "\"" + "\"" + "A" + "\"" + ":" + responseString + "\"" + "}" + "\"";
        String responseString1 = "[{\"id\":\"24\",\"subname\":\"xiaomi\",\"isconnect\":\"y\"},{\"id\":\"25\",\"subname\":\"iPhone 6plus\",\"isconnect\":\"y\"},{\"id\":\"26\",\"subname\":\"vertu\",\"isconnect\":\"y\"}]";
        //JSONObject json=new JSONObject(responseString1);
        JSONArray jsonArray = new JSONArray(responseString);
        resultMap.put("connectedstate", "y");
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject obj = (JSONObject) jsonArray.get(i);
            String cast = i + "";
            resultMap.put("idname" + cast, (String) obj.get("id"));
            Log.i("zyl", resultMap.get("idname" + cast));
            resultMap.put("subname" + cast, (String) obj.get("subname"));
            Log.i("zyl", resultMap.get("subname" + cast));
            resultMap.put("isconnect" + cast + "", (String) obj.get("isconnect"));
            Log.i("zyl", resultMap.get("isconnect" + cast));

        }
        resultMap.put("length", "" + jsonArray.length());
        Log.i("zyl", resultMap.get("length"));

    }
}
