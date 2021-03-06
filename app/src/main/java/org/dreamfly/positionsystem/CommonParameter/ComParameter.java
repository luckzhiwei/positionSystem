package org.dreamfly.positionsystem.CommonParameter;

import java.text.StringCharacterIterator;

/**
 * Created by asus on 2015/1/15.
 */
public class ComParameter {

    public final static int STATE_RIGHT = 5;

    public final static int STATE_ERROR = 3;

    public final static int STATE_ERROR_NETWORK = 6;

    public final static String TABLENAME = "regulatoritems";

    public final static String MANTABLENAME = "manageritems";

    public final static String DEVICE = "deviceinformation";

    public final static String HOST = "http://locationapp.dreamfly.org/";
    //请求主机的地址

    public final static String ENCODE_GBK = "gbk";

    public final static String ENCODE_UTF_8 = "utf-8";

    public final static int DEAL_SPILT = 1;

    public final static int DEAL_JSON = 2;

    public final static String ERRORINFO =
            "连接超时，请稍后再试";

    public final static String PACKAGE_NAME = "org.dreamfly.positionsystem";

    public final static String LOADING_STATE = "isfirstconnect";

    public final static String SERVICE_STATE = "servicestate";

    public final static String LOGIN_STATE = "loginstate";

    public final static String IDENTITY_STATE = "identitystate";

    public final static String REQUESTLOCATION_STATE="requestlocationstate";

    public final static String LOADING_STATE_REG = "regfirstconnect";

    public final static String CLICKING_STATE = "isfirstclick";

    public final static String STATE_FIRST = "0";

    public final static String STATE_SECOND = "1";

    public final static String STATE_THIRD = "2";

    public final static String STATE_LOADINGDATA="3";

    public final static String STATE_NORMAL="4";

    public final static String USER_LOCATION = "userlocation";

    public final static int ACTION_CALLPHONE = 6;

    public final static int ACTION_LOCATION = 7;

    public final static int LOCK=0;

    public final static int UNLOCK=1;


}
