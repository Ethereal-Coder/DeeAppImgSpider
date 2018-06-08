package com.example.test_webview_demo.detail;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.test_webview_demo.R;
import com.example.test_webview_demo.mainpf.MainPfEntity;
import com.example.test_webview_demo.net.DeeResponse;
import com.example.test_webview_demo.net.VisitorApi;
import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailEditActivity extends AppCompatActivity {

  @BindView(R.id.tv_detail_edit_toolbar_title) TextView tvDetailEditToolbarTitle;
  @BindView(R.id.btn_select_shop) Button btnSelectShop;
  @BindView(R.id.et_detail_edit_name) EditText etDetailEditName;
  @BindView(R.id.et_detail_edit_price) EditText etDetailEditPrice;
  @BindView(R.id.rv_detail_edit_main) RecyclerView rvDetailEditMain;
  @BindView(R.id.rv_detail_edit_scale) RecyclerView rvDetailEditScale;
  @BindView(R.id.rv_detail_edit_banner) RecyclerView rvDetailEditBanner;
  @BindView(R.id.rv_detail_edit_detail) RecyclerView rvDetailEditDetail;
  @BindView(R.id.rv_detail_edit_sku) RecyclerView rvDetailEditSku;
  @BindView(R.id.ll_edit_sku) LinearLayout llEditSku;
  @BindView(R.id.ll_edit_scale) LinearLayout llEditScale;
  @BindView(R.id.ll_1688_edit_sku) LinearLayout ll1688EditSku;
  @BindView(R.id.rv_detail_1688_edit_sku) RecyclerView rvDetail1688EditSku;
  private ArrayList<DeeImgEntity> imgSelectData;
  private DetailEditAdapter adapter_detail;
  private DetailEditAdapter adapter_banner;
  private DetailEditAdapter adapter_scale;
  private DetailEditAdapter adapter_main;
  private MainPfEntity mainPfEntity;
  private String deeFilter;
  private DetailEditSkuAdapter adapter_sku;
  private int tempSkuPosition;
  private Result1688 result1688;
  private Disposable disposable;
  private String come_from;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_edit);
    ButterKnife.bind(this);

    initData();
  }

  @SuppressLint("CheckResult") private void getDetailImg(String detailUrl) {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
    Observable.just(detailUrl)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.io())
        .map(new Function<String, List<MutilType>>() {
          @Override public List<MutilType> apply(String url) throws Exception {
            Document doc = Jsoup.connect(url).timeout(5000).get();
            //List<String> detailImgs = new ArrayList<>();
            Elements select = doc.select("img[src]");
            for (int i = 0; i < select.size(); i++) {
              String detailImg = select.get(i).attr("src").replace("\\\"", "");
              if (!detailImg.endsWith("360x360.jpg")) {
                //detailImgs.add(detailImg);
                DeeImgEntity imgEntity = new DeeImgEntity(detailImg);
                imgEntity.setDetail(true);
                imgSelectData.add(imgEntity);
                //System.out.println(detailImg);
              }
            }

            List<MutilType> list = new ArrayList<>();
            if (imgSelectData.size() > 0) {
              for (int i = 0; i < imgSelectData.size(); i++) {
                if (imgSelectData.get(i).isDetail()) {
                  list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
                }
              }
            }
            return list;
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<MutilType>>() {
          @Override public void accept(List<MutilType> mutilTypes) throws Exception {
            adapter_detail.setNewData(mutilTypes);
          }
        });

  }

  private void initData() {
    come_from = getIntent().getStringExtra("come_from");
    if (come_from.equals("1688_detail")) {
      String jsonStr = getIntent().getStringExtra("1688_detail_result");
      ll1688EditSku.setVisibility(View.GONE);
      llEditScale.setVisibility(View.GONE);
      llEditSku.setVisibility(View.VISIBLE);
      imgSelectData = new ArrayList<>();
      result1688 = new Gson().fromJson(jsonStr, Result1688.class);
      etDetailEditName.setText(result1688.getSubject());
      etDetailEditName.setSelection(etDetailEditName.getText().length());
      List<Result1688.ImageListBean> imageList = result1688.getImageList();
      for (int i = 0; i < imageList.size(); i++) {
        //System.err.println(imageList.get(i).getSize310x310URL());
        DeeImgEntity imgEntity = new DeeImgEntity(imageList.get(i).getOriginalImageURI());
        if (i == 1) {
          imgEntity.setMain(true);
          //imgEntity.setScale(true);
        }
        imgEntity.setBanner(true);
        imgSelectData.add(imgEntity);
      }

      String detailUrl = "http:" + result1688.getDetailUrl();

      getDetailImg(detailUrl);

      init1688Main();
      init1688Banner();
      inti1688Detail();
      init1688Sku();

    } else {
      imgSelectData = getIntent().getParcelableArrayListExtra("spider_img_list");
      deeFilter = getIntent().getStringExtra("spider_dee_filter");
      String spider_title = getIntent().getStringExtra("spider_title");
      etDetailEditName.setText(spider_title);
      etDetailEditName.setSelection(etDetailEditName.getText().length());
      ll1688EditSku.setVisibility(View.GONE);
      llEditScale.setVisibility(View.VISIBLE);
      llEditSku.setVisibility(View.VISIBLE);
      initMain();
      initScale();
      initBanner();
      initDetail();
      initSku();
    }
  }

  private void init1688Sku() {
    LinearLayoutManager layoutManager_sku = new LinearLayoutManager(this);
    adapter_sku = new DetailEditSkuAdapter(null);
    adapter_sku.setOnEditClickListener(new DetailEditSkuAdapter.OnEditClickListener() {
      @Override public void onAdd(int position) {
        tempSkuPosition = position;
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        //intent.putExtra("img_type", ImgType.DETAIL);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, 888);
      }
    });
    rvDetailEditSku.setLayoutManager(layoutManager_sku);
    rvDetailEditSku.setAdapter(adapter_sku);
    List<SkuProp> skuProps = result1688.getSkuProps();
    if (skuProps != null && skuProps.size()>0){
      ArrayList<Sku> skus = new ArrayList<>();
      for (int i = 0; i < skuProps.size(); i++) {
        if (i>1) break;
        SkuProp skuProp = skuProps.get(i);
        String prop = skuProp.getProp();
        List<SkuProp.ValueBean> values = skuProp.getValue();
        for (int j = 0; j <values.size(); j++) {
          skus.add(new Sku(prop,values.get(j).getName(),values.get(j).getImageUrl()));
        }
      }
      adapter_sku.setNewData(skus);
    }

  }

  private void inti1688Detail() {
    GridLayoutManager manager_detail = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_detail = new DetailEditAdapter(list);
    adapter_detail.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //DeeImgEntity deeImgEntity = adapter_detail.getmData().get(position).getDeeImgEntity();
        //int index = imgSelectData.indexOf(deeImgEntity);
        //imgSelectData.get(index).setDetail(false);
        //adapter_detail.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.DETAIL);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.DETAIL);
      }
    });
    rvDetailEditDetail.setLayoutManager(manager_detail);
    rvDetailEditDetail.setAdapter(adapter_detail);
  }

  private void init1688Banner() {
    GridLayoutManager manager_banner = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0) {
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isBanner()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_banner = new DetailEditAdapter(list);
    adapter_banner.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //DeeImgEntity deeImgEntity = adapter_banner.getmData().get(position).getDeeImgEntity();
        //int index = imgSelectData.indexOf(deeImgEntity);
        //imgSelectData.get(index).setBanner(false);
        //adapter_banner.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.BANNER);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.BANNER);
      }
    });
    rvDetailEditBanner.setLayoutManager(manager_banner);
    rvDetailEditBanner.setAdapter(adapter_banner);
  }

  private void init1688Main() {
    GridLayoutManager manager_main = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0) {
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isMain()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_main = new DetailEditAdapter(list);
    adapter_main.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //DeeImgEntity deeImgEntity = adapter_main.getmData().get(position).getDeeImgEntity();
        //int index = imgSelectData.indexOf(deeImgEntity);
        //imgSelectData.get(index).setMain(false);
        //adapter_main.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.MAIN);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.MAIN);
      }
    });
    rvDetailEditMain.setLayoutManager(manager_main);
    rvDetailEditMain.setAdapter(adapter_main);
  }

  private void initMain() {
    GridLayoutManager manager_main = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0 && deeFilter.equals("1688")) {
      //imgSelectData.get(1).setMain(true);
      //list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(1)));
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).getImgUrl().contains("460x460")) {
          imgSelectData.get(i).setMain(true);
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
          break;
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_main = new DetailEditAdapter(list);
    adapter_main.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        DeeImgEntity deeImgEntity = adapter_main.getmData().get(position).getDeeImgEntity();
        int index = imgSelectData.indexOf(deeImgEntity);
        imgSelectData.get(index).setMain(false);
        adapter_main.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.MAIN);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.MAIN);
      }
    });
    rvDetailEditMain.setLayoutManager(manager_main);
    rvDetailEditMain.setAdapter(adapter_main);
  }

  private void initScale() {
    GridLayoutManager manager_scale = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0 && deeFilter.equals("1688")) {
      //imgSelectData.get(0).setScale(true);
      //list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(0)));
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).getImgUrl().contains("200x200")) {
          imgSelectData.get(i).setScale(true);
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
          break;
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_scale = new DetailEditAdapter(list);
    adapter_scale.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        DeeImgEntity deeImgEntity = adapter_scale.getmData().get(position).getDeeImgEntity();
        int index = imgSelectData.indexOf(deeImgEntity);
        imgSelectData.get(index).setScale(false);
        adapter_scale.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.SCALE);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.SCALE);
      }
    });
    rvDetailEditScale.setLayoutManager(manager_scale);
    rvDetailEditScale.setAdapter(adapter_scale);
  }

  private void initBanner() {
    GridLayoutManager manager_banner = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0 && deeFilter.equals("1688")) {
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).getImgUrl().contains("460x460")) {
          imgSelectData.get(i).setBanner(true);
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_banner = new DetailEditAdapter(list);
    adapter_banner.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        DeeImgEntity deeImgEntity = adapter_banner.getmData().get(position).getDeeImgEntity();
        int index = imgSelectData.indexOf(deeImgEntity);
        imgSelectData.get(index).setBanner(false);
        adapter_banner.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.BANNER);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.BANNER);
      }
    });
    rvDetailEditBanner.setLayoutManager(manager_banner);
    rvDetailEditBanner.setAdapter(adapter_banner);
  }

  private void initDetail() {
    GridLayoutManager manager_detail = new GridLayoutManager(this, 3);
    List<MutilType> list = new ArrayList<>();
    if (imgSelectData.size() > 0 && deeFilter.equals("1688")) {
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).getImgUrl().contains("460x460") || imgSelectData.get(i)
            .getImgUrl()
            .contains("200x200")) {

        } else {
          imgSelectData.get(i).setDetail(true);
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
    }
    list.add(new MutilType(MutilType.T_ADD, null));
    adapter_detail = new DetailEditAdapter(list);
    adapter_detail.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        DeeImgEntity deeImgEntity = adapter_detail.getmData().get(position).getDeeImgEntity();
        int index = imgSelectData.indexOf(deeImgEntity);
        imgSelectData.get(index).setDetail(false);
        adapter_detail.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        intent.putExtra("img_type", ImgType.DETAIL);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, ImgType.DETAIL);
      }
    });
    rvDetailEditDetail.setLayoutManager(manager_detail);
    rvDetailEditDetail.setAdapter(adapter_detail);
  }

  private void initSku() {
    LinearLayoutManager layoutManager_sku = new LinearLayoutManager(this);
    adapter_sku = new DetailEditSkuAdapter(null);
    adapter_sku.setOnEditClickListener(new DetailEditSkuAdapter.OnEditClickListener() {
      @Override public void onAdd(int position) {
        tempSkuPosition = position;
        Intent intent = new Intent(DetailEditActivity.this, ImgSelectActivity.class);
        //intent.putExtra("img_type", ImgType.DETAIL);
        intent.putParcelableArrayListExtra("img_list", imgSelectData);
        startActivityForResult(intent, 888);
      }
    });
    rvDetailEditSku.setLayoutManager(layoutManager_sku);
    rvDetailEditSku.setAdapter(adapter_sku);
  }

  @OnClick({ R.id.btn_select_shop, R.id.detail_edit_back, R.id.tv_scan, R.id.tv_edit_sku })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.detail_edit_back:
        finish();
        break;
      case R.id.btn_select_shop:
        xxShop();
        break;
      case R.id.tv_scan:
        xxScan();
        //scan();
        break;
      case R.id.tv_edit_sku:
        if (!come_from.equals("1688_detail")){
          startActivityForResult(new Intent(DetailEditActivity.this, SkuEditActivity.class), 1314);
        }
        break;
    }
  }

  private void xxShop() {
    Intent intent = new Intent(DetailEditActivity.this, ShopSelectActivity.class);
    startActivityForResult(intent, 3344);
  }

  private void xxScan() {
    Intent intent = new Intent(DetailEditActivity.this, ScanActivity.class);
    intent.putExtra("title", etDetailEditName.getText().toString());
    intent.putExtra("shopId", mainPfEntity == null ? 3 : mainPfEntity.getId());
    intent.putExtra("price", etDetailEditPrice.getText().toString());
    intent.putParcelableArrayListExtra("sku",
        (ArrayList<? extends Parcelable>) result1688.getSkuProps());
    intent.putParcelableArrayListExtra("banner",
        (ArrayList<? extends Parcelable>) adapter_banner.getmData());
    intent.putParcelableArrayListExtra("detail",
        (ArrayList<? extends Parcelable>) adapter_detail.getmData());
    intent.putParcelableArrayListExtra("main",
        (ArrayList<? extends Parcelable>) adapter_main.getmData());
    //intent.putParcelableArrayListExtra("scale",
    //    (ArrayList<? extends Parcelable>) adapter_scale.getmData());
    startActivity(intent);
  }

  private void scan() {
    UpWare upWare = new UpWare();
    upWare.setShopId((long) 3);
    upWare.setProduct_title(etDetailEditName.getText().toString());
    upWare.setMarket_price(etDetailEditPrice.getText().toString());
    upWare.setLarge("");
    upWare.setThumbnail("");
    upWare.setCarouselPhoto("");
    upWare.setDetailPhoto("");
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
      }

      @Override public void onFailure(Call<DeeResponse> call, Throwable t) {
        Log.e("s_post", t.getMessage());
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK) {
      if (requestCode == 3344) {
        mainPfEntity = data.getParcelableExtra("shop_msg");
        tvDetailEditToolbarTitle.setText(mainPfEntity.getName());
      } else if (requestCode == 1314) {
        ArrayList<Sku> editSkus = data.getParcelableArrayListExtra("edit_skus");
        adapter_sku.setNewData(editSkus);
      } else if (requestCode == 888) {
        String img_select_sku = data.getStringExtra("img_select_sku");
        adapter_sku.changeData(tempSkuPosition, img_select_sku);
      } else {
        imgSelectData = data.getParcelableArrayListExtra("img_select_data");
        freshData(imgSelectData, requestCode);
      }
    }
  }

  private void freshData(ArrayList<DeeImgEntity> imgSelectData, int requestCode) {
    if (requestCode == ImgType.MAIN) {
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isMain()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD, null));
      adapter_main.setNewData(list);
    } else if (requestCode == ImgType.SCALE) {
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isScale()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD, null));
      adapter_scale.setNewData(list);
    } else if (requestCode == ImgType.BANNER) {
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isBanner()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD, null));
      adapter_banner.setNewData(list);
    } else if (requestCode == ImgType.DETAIL) {
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isDetail()) {
          list.add(new MutilType(MutilType.T_NORMAL, imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD, null));
      adapter_detail.setNewData(list);
    }
  }
}
