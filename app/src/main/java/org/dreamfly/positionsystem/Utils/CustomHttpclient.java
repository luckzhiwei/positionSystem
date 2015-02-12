package org.dreamfly.positionsystem.Utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

/**
 * Created by asus on 2015/1/21.
 */
public class CustomHttpclient {

    private static volatile HttpClient mHttpClient = null;
    private static int  MAXREQUEST_TIME=200*1000;
    private static int  MAXCON_TIME=200*1000;
    /**
     * singleTon模式,节省内存
     * @return
     */
    public static HttpClient getSigleTonInstance() {
        if (mHttpClient == null) {
            synchronized (CustomHttpclient.class) {
                if (mHttpClient == null) {
                    BasicHttpParams params=new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(params,MAXREQUEST_TIME);
                    //三次握手的请求时间最大设置
                    HttpConnectionParams.setSoTimeout(params,MAXCON_TIME);
                    //三次握手的返回时间最大设置
                    mHttpClient = new DefaultHttpClient(params);
                }
            }
        }
        return (mHttpClient);
    }
}
