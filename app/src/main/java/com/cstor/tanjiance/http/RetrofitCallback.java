package com.cstor.tanjiance.http;

/**
 * author : li
 * date   : 2020/3/27
 * desc   :
 */
public abstract class RetrofitCallback {
    //用于进度的回调
    public abstract void onLoading(long total, long progress);
}
