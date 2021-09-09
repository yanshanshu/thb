package com.cstor.tanjiance.activity;

import android.app.Application;
import android.content.Context;


public class app extends Application {
    public static Context mContext;
    public static final String UPDATE_PHOTO = "com.cstor.photoamplifier";
    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }


    public static Context getmContext() {
        return mContext;
    }
}
