package org.dreamfly.positionsystem.Utils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.io.InputStream;
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


           public static  String requestHttpServer
                   (String requestPath,
                    Map<String,String> requestParams,
                    String requestEncode,String responseEncode)
           {
               ArrayList<NameValuePair> requestList=new ArrayList<NameValuePair>();
               Iterator iterator=requestParams.keySet().iterator();
               while(iterator.hasNext())
               {
                    Map.Entry<String,String> oneEntry=
                            (Map.Entry<String,String>)iterator.next();
                    requestList.add(new BasicNameValuePair(oneEntry.getKey(),
                            oneEntry.getValue()));
               }

               try {
                   UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(requestList, requestEncode);

                   HttpPost httpPost=new HttpPost(requestPath);

                   httpPost.setEntity(postEntity);

                   HttpClient customClient=new DefaultHttpClient();

                   HttpParams params=null;

                   params=customClient.getParams();

                   HttpConnectionParams.setConnectionTimeout(params,5*1000);//设置请求时间

                   HttpResponse repsonseServer=customClient.execute(httpPost);

                   if(repsonseServer.getStatusLine().getStatusCode()==200)
                   {

                   }
               }catch(UnsupportedEncodingException e){
                    e.printStackTrace();
                   return null;
               }catch(IOException e){
                   e.printStackTrace();
                   return null;
               }catch(Exception e){//超时的处理
                   e.printStackTrace();
                  return null;
               }



               return null;
           }

           private static String switchStreamToString(InputStream inpustream,String encode)
           {

               return null;
           }


}
