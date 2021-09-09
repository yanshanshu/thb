package com.cstor.tanjiance.http;

import android.content.Context;


import com.cstor.tanjiance.bean.LatLngBean;
import com.cstor.tanjiance.bean.LatLngListBean;
import com.cstor.tanjiance.utils.LogUtils;
import com.cstor.tanjiance.utils.SPutils;
import com.cstor.tanjiance.utils.SpKey;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求对外的工具类
 */
public class AppHttpUtils {

    private static HashMap<String, String> headerMap;
    private static Context context;

    /**
     * get请求
     */
    public static void get(Context mContext, String url, Map<String, String> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        AppHttp.getInstence().createDefault().get(url, map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * get请求加Header
     */
    public static void getHeader(Context mContext, String url, Map<String, String> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        AppHttp.getInstence().createDefault().getHeader(getHeaderMap(), url, map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * post请求
     */
    public static void post(Context mContext, String url, Map<String, String> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        AppHttp.getInstence().createDefault().post(url, map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * post请求加Header
     */
    public static void postHeader(Context mContext, String url, Map<String, String> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        AppHttp.getInstence().createDefault().postHeader(getHeaderMap(), url, map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * post请求
     */
    public static void postFile(Context mContext, String url, Map<String, RequestBody> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        AppHttp.getInstence().createDefault().postMapFile(url, map).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * post请求  单独测试的上传人脸的临时接口
     */
    public static void postFile2(Context mContext, String url,ArrayList<File> files, Map<String, String> map, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);//表单类型
        for (int i=0; i < files.size();i++){
            LogUtils.e("获取图片文件：" + files.get(i).length());
            File file =  files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("files", file.getName(), photoRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        AppHttp.getInstence().createDefault().postMapFile2(url, map,parts).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * Post 请求上传bean
     * @param mContext
     * @param latLngListBean latLngListBean
     */
    public static void postBean(Context mContext, LatLngListBean latLngListBean, HttpRequestCallback mHttpRequestCallback){
        context = mContext;
        AppThreadHttp.getInstence().createDefault().postBean(latLngListBean).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                LogUtils.e("获取识别结果:" + response.toString());
                mHttpRequestCallback.onSuccess(response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * post发送json数据  地震预警
     */
    public static void postJson(Context mContext, String url, LatLngBean latLngBean, HttpRequestCallback mHttpRequestCallback) {
        context = mContext;
        //receiveEqBean在前面赋予了各个字段的数据，在接口request中转成了json格式的数据，发送请求
        AppHttp.getInstence().createDefault().request(url, latLngBean).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                mHttpRequestCallback.onSuccess(response.body().toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                mHttpRequestCallback.onFailure(t.toString());
            }
        });
    }

    /**
     * 获取HeaderMap
     */
    private static HashMap<String, String> getHeaderMap() {
        if (headerMap == null) {
            headerMap = new HashMap<String, String>();
            headerMap.put("cookie", SPutils.getInstans(context).getStringValue(SpKey.COOKIE));
        }
        return headerMap;
    }
}