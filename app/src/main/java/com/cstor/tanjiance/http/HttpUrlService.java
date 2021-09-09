package com.cstor.tanjiance.http;


import com.cstor.tanjiance.bean.LatLngBean;
import com.cstor.tanjiance.bean.LatLngListBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * retrofit 请求接口
 */
public interface HttpUrlService {
    /**
     * get请求
     */
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, String> map);

    /**
     * 带Header的get请求
     */
    @GET
    Call<String> getHeader(@HeaderMap Map<String, String> mapHeader, @Url String url, @QueryMap Map<String, String> map);
    /**
     * POST请求
     */
    @POST
    @FormUrlEncoded
    Call<String>   post(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 带Header的POST请求
     */
    @POST
    @FormUrlEncoded
    Call<String>   postHeader(@HeaderMap Map<String, String> mapHeader, @Url String url, @FieldMap Map<String, String> map);

    /**
     * 文件下载请求
     * 文件下载需要额外添加@Streaming注解。
     */
    @GET
    @Streaming
    Call<String> postDownloadFile(@HeaderMap Map<String, String> mapHeader,@Url String url);
    /**
     * 文件上传请求
     * File file=new File("/sdcard/img.jpg");
     * RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
     * Map<String, RequestBody> params=new HashMap<>() ;
     * params.put("file\"; filename=\""+ file.getName(), requestBody);
     * 文件上传需要额外添加@Multipart、@PartMap注解；
     */
    @POST
    @Multipart
    Call<String>  postMapFile(@HeaderMap Map<String, String> mapHeader, @Url String url, @PartMap Map<String, RequestBody> params);

    @POST
    @Multipart
    Call<String>  postMapFile(@Url String url, @PartMap Map<String, RequestBody> params);

    @POST
    @Multipart
    Call<String>  postMapFile2(@Url String url, @PartMap Map<String, String> params,@Part List<MultipartBody.Part> file);

    //第二种方式：@Field
    @Headers("Accept:application/json")
    @POST(Constans.faceRecognize)
    Call<String> postBean(@Body LatLngListBean latLngListBean);
    //发送json数据形式的post请求，把网络请求接口的后半部分app/receiveEq写在里面
    //Get是请求数据实体类，Take接受数据实体类
    @POST//Post请求发送数据
    //@body即非表单请求体，被@Body注解的str将会被Gson转换成json发送到服务器，返回到Take。
    //其中返回类型为Call<*>，*是接收数据的类
    Call<String> request(@Url String url, @Body LatLngBean latLngBean);

//    //其中返回类型为Call<*>，*是接收数据的类
//    Call<String> request(@Url String url,@Body QuickReportDialogBean quickReportDialogBean);
}
