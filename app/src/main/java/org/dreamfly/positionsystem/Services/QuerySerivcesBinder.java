package org.dreamfly.positionsystem.Services;

import android.app.DownloadManager;
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
    private  QueryService.MsgSeneder msgSeneder;

    public QuerySerivcesBinder(Context mContext,Handler mHandler) {
        this.mContext = mContext;
        this.mHandler=mHandler;
    }


    /**
     * 启动轮询线程
     */
    public void startQuery() {
          this.queryThread=new QueryThread(mContext,mHandler);
          this.queryThread.start();
    }

    public  void setMsgSender(QueryService.MsgSeneder msgSender){
             this.msgSeneder=msgSender;
    }

    public QueryService.MsgSeneder getMsgSeneder(){
        return(this.msgSeneder);
    }


}
