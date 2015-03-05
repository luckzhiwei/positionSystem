package org.dreamfly.positionsystem.Services;

import android.content.Context;
import android.os.Binder;
import android.os.Handler;

import org.dreamfly.positionsystem.Thread.QueryThread;

/**
 * Created by asus on 2015/3/5.
 */
public class QuerySerivcesBinder extends Binder {


    private Context mContext;
    private Handler mHandler;

    private QueryThread queryThread;

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
}
