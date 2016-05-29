package com.hucw.coolweather.util;

/**
 * 网络请求回调接口服务
 * @author  hucw
 * @version 1.0.0
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
