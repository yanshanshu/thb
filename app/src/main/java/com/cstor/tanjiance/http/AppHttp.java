package com.cstor.tanjiance.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Url;

public class AppHttp {

      private HttpUrlService mHttpUrlService;
      private static AppHttp mAppHttp;
      private Retrofit mRetrofit;
      private static int TIME_OUT = 20; // 20秒超时断开连接

      private AppHttp(){
            initAppHttp();
      }

      public static AppHttp getInstence() {
            if (mAppHttp == null) {
                  synchronized (AppHttp.class) {
                        if (mAppHttp == null) {
                              mAppHttp = new AppHttp();
                        }
                  }
            }
            return mAppHttp;
      }

      /**
       * 初始化retrofit
       */
      private void initAppHttp() {
                  //不需要使用拦截器就不创建直接从if开始
                  OkHttpClient client = new OkHttpClient.Builder()
                          //添加应用拦截器
                          .addInterceptor(new LoggingInterceptor())
                          //添加网络拦截器
                          .addNetworkInterceptor(new LoggingInterceptor())
                          .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                          .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                          .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                          .build();
            ExecutorService executorService = Executors.newFixedThreadPool(1);//获取异步线程
                  mRetrofit = new Retrofit.Builder()
                          .baseUrl(Constans.BaseUrl)      //url没有用，只是为了符合框架标准
                          //增加返回值为String的支持
//                          .callbackExecutor(executorService)
                          .addConverterFactory(ScalarsConverterFactory.create())
                          //添加转换工厂，用于解析json并转化为javaBean
//                        .addConverterFactory(GsonConverterFactory.create())
                          .client(client)
                          .build();
      }

      /**
       * 创建API
       */
      public <T> T create(Class<T> clazz) {
            return mRetrofit.create(clazz);
      }

      /**
       * 创建默认的API
       */
      public HttpUrlService createDefault() {
            if(mHttpUrlService==null){
                  mHttpUrlService = mRetrofit.create(HttpUrlService.class);
            }
            return mHttpUrlService;
      }
}