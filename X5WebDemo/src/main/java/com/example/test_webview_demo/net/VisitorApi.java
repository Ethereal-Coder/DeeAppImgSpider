package com.example.test_webview_demo.net;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by 孙应恒 on 2018/5/31.
 * Description:
 */
interface VisitorApi {
  @GET Observable<ResponseBody> downloadImg(@Url String imgUrl);
  @GET Observable<ResponseBody> loadHtml(@Url String imgUrl);
}
