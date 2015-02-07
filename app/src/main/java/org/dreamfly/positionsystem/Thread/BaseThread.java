package org.dreamfly.positionsystem.Thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;
import org.dreamfly.positionsystem.Utils.HttpUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzw on 2015/1/28.
 * 使用模版模式来写这个继承体系
 */
public abstract class BaseThread extends Thread {

    protected String requestURL;
    protected Map resultMap;
    protected Map requestParams;
    protected List resultList;
    protected Handler mHandler;
    protected Message msg;
    protected String stateId;

    /**
     * handler是关于主线程的交互类
     * stateId是与主线程的message的标识号
     *
     * @param mHandler
     * @param stateId
     */
    public BaseThread(Handler mHandler, String stateId) {
        this.mHandler = mHandler;
        this.stateId = stateId;
    }

    /**
     * 设置POST表单的参数和请求主机的位置
     *
     * @param requestURL
     * @param requestParams
     */
    public void setRequestPrepare(String requestURL, Map requestParams) {
        this.requestURL = requestURL;
        this.requestParams = requestParams;
    }

    /**
     * 获取返回结果，结果为Map形式的
     *
     * @return
     */
    public Map getResultMap() {
        if (this.resultMap != null) {
            return (this.resultMap);
        } else {
            this.resultMap = new HashMap();
            return (this.resultMap);
        }

    }

    /**
     * 获取返回结果，结果是List形式的
     *
     * @return
     */
    public List getResultList() {
        if (this.resultList != null) {
            return (this.resultList);
        } else {
            this.resultList = new ArrayList();
            return (this.resultList);
        }
    }

    /**
     * 主要的线程体方法,先是请求服务器，
     * 将服务器的返回的结果（字符）交给子类的实例化的方法来
     */
    public void run() {
        try {
            String responseStr =
                    HttpUtils.requestHttpServer(this.requestURL, this.requestParams,
                            ComParameter.ENCODE_UTF_8, ComParameter.ENCODE_UTF_8);
            if (!this.dealRequestState(responseStr)) {
                this.setLoadState(this.stateId, ComParameter.STATE_ERROR);
                this.mHandler.sendMessage(this.msg);

                return;
            }
            this.dealReponseString(responseStr);
            //这个方法放在子类中进行
            this.setLoadState(this.stateId, ComParameter.STATE_RIGHT);
            this.mHandler.sendMessage(this.msg);
        } catch (Exception e) {
            this.setLoadState(this.stateId, ComParameter.STATE_ERROR);
            this.mHandler.sendMessage(this.msg);
            e.printStackTrace();
        }
    }

    protected abstract void dealReponseString(String responseString);

    /**
     * 通知主线程加载的状态
     *
     * @param key
     * @param state
     */
    protected void setLoadState(String key, int state) {
        this.msg = new Message();
        Bundle bd = new Bundle();
        bd.putInt(key, state);
        this.msg.setData(bd);
    }

    /**
     * 根据httpUtils类返回的字符串来确定请求成功与否
     * @param str
     * @return
     */
    protected boolean dealRequestState(String str) {

        if (str != null) {
            if (str.equals("paramsException")) {
                return (false);
            } else if (str.equals("singleTonException")) {
                return (false);
            } else if (str.equals("InterNetException")) {
                return (false);
            } else if (str.equals("switchException")) {
                return (false);
            } else {
                return (true);
            }
        } else {
            Log.i("lzw","null");
            return (false);
        }
    }
}
