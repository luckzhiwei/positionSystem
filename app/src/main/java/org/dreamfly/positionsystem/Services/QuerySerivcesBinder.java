package org.dreamfly.positionsystem.Services;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;

import org.dreamfly.positionsystem.Thread.QueryThread;

/**
 * Created by lzw on 2015/3/5.
 */
public class QuerySerivcesBinder extends Binder {


    private Context mContext;
    private Handler mHandler;

    private QueryThread queryThread;
    private QueryService queryService;
    private QueryService.MsgSender msgSeneder;

    public QuerySerivcesBinder(Context mContext,Handler mHandler,QueryService queryService) {
        this.mContext = mContext;
        this.mHandler=mHandler;
        this.queryService=queryService;
    }

    /**
     * 启动轮询线程
     */
    public void startQuery() {
          this.queryThread=new QueryThread(mContext,mHandler);
          this.queryThread.start();
    }

    public  void setMsgSender(QueryService.MsgSender msgSender){
             this.msgSeneder=msgSender;
    }

    public QueryService.MsgSender getMsgSeneder(){
        return(this.msgSeneder);
    }

    public QueryService getService(){
        return queryService ;
    }


}
