package com.hucw.coolweather.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具
 * @author  hucw
 * @version 1.0.0
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";

    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    Log.d(TAG, "发送请求 : " + address);
                    URL url = new URL(address);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response.append(line);
                    }

                    Log.d(TAG, "响应数据 : " + response.toString());

                    if(listener != null)
                        listener.onFinish(response.toString());
                }catch (Exception e){
                    if(listener != null)
                        listener.onError(e);
                }finally {
                    if(null != connection)
                        connection.disconnect();
                }
            }
        }).start();
    }
}
