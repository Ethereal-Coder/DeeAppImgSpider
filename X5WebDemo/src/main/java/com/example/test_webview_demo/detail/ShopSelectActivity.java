package com.example.test_webview_demo.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.test_webview_demo.R;
import com.example.test_webview_demo.mainpf.MainPfEntity;
import com.example.test_webview_demo.mainpf.MainPlateformAdapter;
import com.example.test_webview_demo.net.VisitorApi;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShopSelectActivity extends AppCompatActivity {

  @BindView(R.id.btn_shop_select_back) Button btnShopSelectBack;
  @BindView(R.id.rv_shop_select) RecyclerView rvShopSelect;
  private MainPlateformAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_shop_select);
    ButterKnife.bind(this);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    adapter = new MainPlateformAdapter(null);
    adapter.setSelectListener(new MainPlateformAdapter.OnSelectListener() {
      @Override public void onSelect(int position) {
        MainPfEntity mainPfEntity = adapter.getmData().get(position);
        Intent intent = new Intent();
        intent.putExtra("shop_msg",mainPfEntity);
        setResult(RESULT_OK,intent);
        finish();
      }
    });
    rvShopSelect.setLayoutManager(layoutManager);
    rvShopSelect.setAdapter(adapter);

    initData();
  }

  private void initData() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.1.94:8081/mobile/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    VisitorApi visitorApi = retrofit.create(VisitorApi.class);
    Call<XxShops> call = visitorApi.getShops();
    call.enqueue(new Callback<XxShops>() {
      @Override public void onResponse(Call<XxShops> call, Response<XxShops> response) {
        Log.e("s_post", "-------success-------" + new Gson().toJson(response.body()));
        List<MainPfEntity> shops = response.body().getShops();
        adapter.addData(shops);
      }

      @Override public void onFailure(Call<XxShops> call, Throwable t) {
        Log.e("s_post", t.getMessage());
      }
    });
  }

  @OnClick({ R.id.btn_shop_select_back, R.id.rv_shop_select })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.btn_shop_select_back:
        finish();
        break;
      case R.id.rv_shop_select:
        break;
    }
  }
}
