package com.cstor.tanjiance.http;


import com.cstor.tanjiance.utils.LogUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * retrofit log请求日志适配器
 */
public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //拿到request实例在此对请求做需要的设置
        Request request = chain.request();
        long t1 = System.nanoTime();
//        LogUtils.i("接口--"+ String.format("Sending request %s on %s%n%s",
//                request.url(), chain.connection(), request.headers()));
        //发送request请求
        Response response = chain.proceed(request);
        //得到请求后的response实例，做相应操作
        long t2 = System.nanoTime();
        LogUtils.i(String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        return response;
    }
}