package com.example.test_webview_demo.net;

import io.reactivex.Observable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 孙应恒 on 2018/5/31.
 * Description:
 */
public class VisitorNets {
  public static final String BASE_MOBILE_URL = "http://192.168.1.94:8081/mobile/";
  private static final int DEFAULT_TIMEOUT = 10;
  private Retrofit retrofit;
  private VisitorApi visitorApi;

  private VisitorNets() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    builder.addInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = original.newBuilder()
            .addHeader("token","125AA9CD290C")
            .method(original.method(), original.body()).build();
        return chain.proceed(request);
      }
    });
    retrofit = new Retrofit.Builder()
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_MOBILE_URL)
        .build();
    visitorApi = retrofit.create(VisitorApi.class);
  }

  private static class SingletonHolder {
    private static final VisitorNets INSTANCE = new VisitorNets();
  }

  public static VisitorNets getInstance() {
    return SingletonHolder.INSTANCE;
  }

  public Observable downloadImg(String url) {
    return visitorApi.downloadImg(url);
  }

  public Observable loadHtml(String url) {
    return visitorApi.loadHtml(url);
  }

  //public Observable postMsg( RequestBody msg){
  //  return visitorApi.postMsg(msg);
  //}
}
