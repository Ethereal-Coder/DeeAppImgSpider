package com.example.test_webview_demo.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.test_webview_demo.R;
import com.example.test_webview_demo.net.DeeResponse;
import com.example.test_webview_demo.net.VisitorApi;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScanActivity extends AppCompatActivity {

  @BindView(R.id.banner_scan) ConvenientBanner bannerScan;
  @BindView(R.id.title_scan) TextView titleScan;
  @BindView(R.id.price_scan) TextView priceScan;
  @BindView(R.id.rv_scan) RecyclerView rvScan;
  private String title;
  private String price;
  private ArrayList<MutilType> banner;
  private ArrayList<MutilType> detail;
  private ArrayList<MutilType> main;
  private ArrayList<MutilType> scale;
  private ArrayList<SkuProp> sku;
  private UpWare upWare;
  private long shopId;
  private ScanDetailAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan);
    ButterKnife.bind(this);
    title = getIntent().getStringExtra("title");
    price = getIntent().getStringExtra("price");
    shopId = getIntent().getLongExtra("shopId", 3);
    banner = getIntent().getParcelableArrayListExtra("banner");
    detail = getIntent().getParcelableArrayListExtra("detail");
    main = getIntent().getParcelableArrayListExtra("main");
    scale = getIntent().getParcelableArrayListExtra("scale");
    sku = getIntent().getParcelableArrayListExtra("sku");
    titleScan.setText(title);
    priceScan.setText("￥" + price);
    ArrayList<DeeImgEntity> bannerEntities = new ArrayList<>();
    ArrayList<DeeImgEntity> detailEntities = new ArrayList<>();
    for (int i = 0; i < banner.size(); i++) {
      if (banner.get(i).getType() == MutilType.T_NORMAL) {
        bannerEntities.add(banner.get(i).getDeeImgEntity());
      }
    }
    for (int i = 0; i < detail.size(); i++) {
      if (detail.get(i).getType() == MutilType.T_NORMAL) {
        detailEntities.add(detail.get(i).getDeeImgEntity());
      }
    }

    bannerScan.setPages(new CBViewHolderCreator<LocalImageHolderView>() {

      @Override public LocalImageHolderView createHolder() {
        return new LocalImageHolderView();
      }
    }, bannerEntities).setOnItemClickListener(new OnItemClickListener() {
      @Override public void onItemClick(int position) {

      }
    });

    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    adapter = new ScanDetailAdapter(detailEntities);
    rvScan.setNestedScrollingEnabled(false);
    rvScan.setLayoutManager(layoutManager);
    rvScan.setAdapter(adapter);
  }

  @OnClick({ R.id.btn_scan_back, R.id.btn_scan_commit }) public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_scan_back:
        finish();
        break;
      case R.id.btn_scan_commit:
        scan();
        break;
    }
  }

  private void scan() {
    //if (upWare == null) {
      upWare = new UpWare();
      upWare.setShopId(shopId);
      upWare.setProduct_title(title);
      upWare.setMarket_price(price);
      upWare.setSkuProps(sku);
      if (main != null && main.size()>0){
        String large = main.get(0).getDeeImgEntity().getImgUrl();
        upWare.setLarge(large);
        upWare.setThumbnail_hd(large);
        upWare.setThumbnail(large);
      }
      if (scale != null && scale.size()>0){
        String thumbnail = scale.get(0).getDeeImgEntity().getImgUrl();
        upWare.setThumbnail(thumbnail);

        //主图就是高清缩略图
        //if (scale.size()>1){
        //  String thumbnail_hd = scale.get(1).getDeeImgEntity().getImgUrl();
        //  upWare.setThumbnail_hd(thumbnail_hd);
        //}
      }
      String carousep = getStrxx(banner);
      String detailp = getStrDetail(adapter.getmData());

      upWare.setCarouselPhoto(carousep);
      upWare.setDetailPhoto(detailp);
    //}
    Gson gson = new Gson();
    String upWareJson = gson.toJson(upWare);
    RequestBody body =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), upWareJson);
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.94:8081/mobile/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    VisitorApi visitorApi = retrofit.create(VisitorApi.class);
    Call<DeeResponse> call = visitorApi.postMsg(body);
    call.enqueue(new Callback<DeeResponse>() {
      @Override public void onResponse(Call<DeeResponse> call, Response<DeeResponse> response) {
        Log.e("s_post", "--------------" + new Gson().toJson(response.body()));
        Toast.makeText(ScanActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
      }

      @Override public void onFailure(Call<DeeResponse> call, Throwable t) {
        Log.e("s_post", t.getMessage());
        Toast.makeText(ScanActivity.this,"提交失败",Toast.LENGTH_SHORT).show();
      }
    });
  }

  private String getStrDetail(List<DeeImgEntity> deeImgEntities) {
    String lx = "";
    if (deeImgEntities != null && deeImgEntities.size()>0){
      for (int i = 0; i < deeImgEntities.size(); i++) {
        lx += deeImgEntities.get(i).getImgUrl() + ";";
      }
    }
    return lx;
  }

  private String getStrxx(ArrayList<MutilType> xm) {
    String lx = "";
    for (int i = 0; i < xm.size(); i++) {
      if (xm.get(i).getType() != MutilType.T_ADD) {
        lx += xm.get(i).getDeeImgEntity().getImgUrl() + ";";
      }
    }
    return lx;
  }
}
