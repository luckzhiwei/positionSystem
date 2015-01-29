package org.dreamfly.positionsystem.Thread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

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
public abstract class BaseThread extends Thread{

             protected  String requestURL;
             protected  Map resultMap;
             protected  Map requestParams;
             protected  List resultList;
             protected Handler mHandler;
             protected  Message msg;
             protected String stateId;


             public  BaseThread(Handler mHandler,String stateId)
             {
                 this.mHandler=mHandler;
                 this.stateId=stateId;
             }
             public void setRequestPrepare(String  requestURL,Map requestParams)
             {
                   this.requestURL=requestURL;
                   this.requestParams=requestParams;
             }

             public Map getResultMap()
             {
                 if(this.resultMap!=null)
                 {
                     return(this.resultMap);
                 }else{
                     this.resultMap=new HashMap();
                     return(this.resultMap);
                 }

             }

             public List getResultList()
             {
                 if(this.resultList!=null)
                 {
                     return(this.resultList);
                 }else{
                     this.resultList=new ArrayList();
                     return(this.resultList);
                 }
             }
             public void run()
             {
                 try {
                     String responseStr =
                             HttpUtils.requestHttpServer(this.requestURL, this.requestParams,
                                     ComParameter.ENCODE_UTF_8, ComParameter.ENCODE_UTF_8);
                     if(!this.dealRequestState(responseStr))
                     {
                         this.setLoadState(this.stateId,ComParameter.STATE_ERROR);
                         this.mHandler.sendMessage(this.msg);
                     }

                     this.dealReponseString(responseStr);
                     this.setLoadState(this.stateId,ComParameter.STATE_RIGHT);
                     this.mHandler.sendMessage(this.msg);
                 }catch (Exception e){
                     this.setLoadState(this.stateId,ComParameter.STATE_ERROR);
                     this.mHandler.sendMessage(this.msg);
                     e.printStackTrace();
                 }
             }

             protected  abstract void dealReponseString(String responseString);


             protected  void setLoadState(String key,int state)
             {
                   this.msg=new Message();
                   Bundle bd=new Bundle();
                   bd.putInt(key,state);
                   this.msg.setData(bd);
             }

             protected boolean dealRequestState(String str)
             {
                      if(str.equals("paramsException"))
                      {
                          return (false);
                      }else if(str.equals("singleTonException"))
                      {
                          return(false);
                      }else if(str.equals("InterNetException"))
                      {
                          return(false);
                      }else{
                          return(true);
                      }
             }
}
