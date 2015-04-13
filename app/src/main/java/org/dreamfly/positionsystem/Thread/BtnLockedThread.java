package org.dreamfly.positionsystem.Thread;

import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import org.dreamfly.positionsystem.CommonParameter.ComParameter;

/**
 * Created by zhengyl on 15-3-20.
 * 该线程用于防止用户误操作
 */
public class BtnLockedThread extends Thread{

    private int stateCode=0;
    private int waitCode=0;
    private Handler mHandler;

    public BtnLockedThread(Handler mHandler){
        this.mHandler=mHandler;
    }

    public void run(){
        while (stateCode<2){
            try {
                if(stateCode==0){

                    Thread.sleep(200);
                    stateCode=1;
                    Log.i("zylThread","线程启动");
                }
                else {
                    Thread.sleep(100);
                    lockBtns();
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void lockBtns(){
        waitCode+=10;

        if(waitCode>=1000){
            mHandler.sendMessage(mHandler.obtainMessage(ComParameter.UNLOCK));
            stateCode=2;
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(ComParameter.LOCK));
    }
}
