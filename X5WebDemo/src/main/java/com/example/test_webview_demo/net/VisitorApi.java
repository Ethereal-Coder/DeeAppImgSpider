package com.example.test_webview_demo.net;

import com.example.test_webview_demo.detail.XxShops;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by 孙应恒 on 2018/5/31.
 * Description:
 */
public interface VisitorApi {
  @GET Observable<ResponseBody> downloadImg(@Url String imgUrl);
  @GET Observable<ResponseBody> loadHtml(@Url String imgUrl);

  @Headers({"Content-Type: application/json","Accept: application/json","token: 125AA9CD290C"})
  @POST("saveProductFromMobile") Call<DeeResponse> postMsg(@Body RequestBody msg);

  @GET("getShops")Call<XxShops>getShops();

}
