package com.cstor.tanjiance.http;


/**
 * 网络请求结果
 */
public interface HttpRequestCallback {
    void onSuccess(String result);
    void onFailure(String error);
}
