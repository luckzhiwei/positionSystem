package org.dreamfly.positionsystem.Utils;

import android.content.Entity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.io.UnsupportedEncodingException;

/**
 * Created by liaozhiwei on 2015/1/12.
 * 关于网络处理请求的工具类
 */
public class HttpUtils {
    private static HttpClient mHttpClient=null;
    private static HttpEntity mEntity=null;
    /**
     * 请求服务器的接口函数
     * @param requestPath//服务器路径
     * @param requestParams//请求参数(HashMap)
     * @param requestEncode//请求的表单的编码格式
     * @param responseEncode//请求的返回体的编码格式
     * @return  //返回体的内容
     */
    public static String requestHttpServer
            (String requestPath,
             Map<String, String> requestParams,
             String requestEncode, String responseEncode) {
        List<NameValuePair> list = getRequestParams(requestParams);
        if (list != null) {
            HttpPost post = bulidHttpPost(list, requestPath);
            mHttpClient = CustomHttpclient.getSigleTonInstance();
            if(mHttpClient!=null) {
                InputStream ServerInputStream = getServetReponse(mHttpClient, post);
                if (ServerInputStream != null)
                //请求服务器没有异常,比如网络异常，超时异常等，就把数据流转化为字符串
                {
                    return (switchStreamToString(ServerInputStream, responseEncode));
                }else{
                    return "InterNetException";
                }
            }else{
                return "singleTonException";
            }
        }else{
              return "paramsException";
        }
    }

    /**
     * 将网络中的数据流变成字符串
     * @param inputstream
     * @param encode
     * @return
     */
    private static String switchStreamToString(InputStream inputstream, String encode) {
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader mBufferReader = new BufferedReader(new InputStreamReader(inputstream, encode));
            String tmpStr=new String();
            while((tmpStr=mBufferReader.readLine())!=null)
            {
                   strBuilder.append(tmpStr);
            }
            mBufferReader.close();
            //Log.i("lzw","转换流至字符串");
            return(strBuilder.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "switchException";
        }catch(IOException e){
            e.printStackTrace();
            return "switchException";
        }
    }

    /**
     * 遍历哈希表,用键值对的方式来形成表单的内容
     * 返回一个List容器
     *
     * @param reuqestParams
     * @return
     */
    private static List<NameValuePair> getRequestParams(Map<String, String> reuqestParams) {
        List<NameValuePair> list = null;
        if (reuqestParams != null && (!reuqestParams.isEmpty())) {
            //Log.i("lzw","post预前准备");
            list = new ArrayList<NameValuePair>();
            Iterator iterator = reuqestParams.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                //依照键值对填入
            }
        }
        return (list);
    }

    /**
     * 将带有参数的List容器放入Aache表单类中
     * 按照服务器的URL形成请求表单
     *
     * @param list
     * @param requestPath
     * @return
     */
    private static HttpPost bulidHttpPost(List<NameValuePair> list, String requestPath) {
        HttpPost post = null;
        try {
            //Log.i("lzw","形成表单");
            UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(list, "utf-8");
            //请求体的编码格式
            post = new HttpPost(requestPath);
            post.setEntity(postEntity);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return (post);
    }

    /**
     * 执行POST方法，来请求服务器,返回数据连接流
     *
     * @param mHttpCLient
     * @param post
     * @return
     */
    private static InputStream getServetReponse(HttpClient mHttpCLient, HttpPost post) {
        try {
            HttpResponse mResponse = mHttpCLient.execute(post);
                //向服务器做请求连接
            mEntity = mResponse.getEntity();
            return (mEntity.getContent());
        } catch (SocketTimeoutException e) {
            //请求超时异常捕捉
            Log.i("lzw","connection_timeout");
            e.printStackTrace();
        } catch (IOException e) {
            //请求服务器异常捕捉
            e.printStackTrace();
            Log.i("lzw","IOException");
        }
        return (null);
    }

    /**
     * 断开连接,取消http请求
     */
    public static void shutDownConnection(){

        synchronized (mHttpClient){
            try {
                if(mEntity!=null)
                {mEntity.consumeContent();}
            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
        Log.i("lzw", "关闭http请求");
    }

}
