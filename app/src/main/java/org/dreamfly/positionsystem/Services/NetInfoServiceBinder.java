package org.dreamfly.positionsystem.Services;

import android.os.Binder;

/**
 * Created by asus on 2015/3/13.
 */
public class NetInfoServiceBinder extends Binder {

    private NetWorkInfoService.NetWorkInfoMsgSender mSender;

    public NetInfoServiceBinder() {

    }

    public void setNetWorkInfoMsgSender(NetWorkInfoService.NetWorkInfoMsgSender mSender) {
        this.mSender = mSender;
    }

    public NetWorkInfoService.NetWorkInfoMsgSender getNetWorkInfoMsgSender() {
        return (this.mSender);
    }


}
