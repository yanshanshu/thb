package com.cstor.tanjiance.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by shixin on 16/8/23.
 * bug反馈QQ:1438734562
 */
public class MyApp extends Application {
    public static Context mContext;
    public static final String UPDATE_PHOTO = "com.cstor.photoamplifier";
    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * setApiKey是静态方法,内部引用了Context，建议放在Application中
         * 如果你在meta-data中配置了key，那么以meta-data中的为准，此行代码
         * 可以忽略，这个方法主要是为那些不想在xml里配置key的用户使用。
         * **/
//        AMapNavi.setApiKey(this, "你的KEY");

        mContext = this;
    }


    public static Context getmContext() {
        return mContext;
    }
}
