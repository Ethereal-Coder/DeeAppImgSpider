package com.example.test_webview_demo.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

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
  private ArrayList<DeeImgEntity> imgSelectData;
  private DetailEditAdapter adapter_detail;
  private DetailEditAdapter adapter_banner;
  private DetailEditAdapter adapter_scale;
  private DetailEditAdapter adapter_main;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_edit);
    ButterKnife.bind(this);
    imgSelectData = getIntent().getParcelableArrayListExtra("spider_img_list");

    initMain();
    initScale();
    initBanner();
    initDetail();

    initData();
  }

  private void initData() {
    String spider_title = getIntent().getStringExtra("spider_title");
    etDetailEditName.setText(spider_title);
    etDetailEditName.setSelection(etDetailEditName.getText().length());
  }

  private void initMain() {
    GridLayoutManager manager_main = new GridLayoutManager(this,3);
    List<MutilType> list = new ArrayList<>();
    list.add(new MutilType(MutilType.T_ADD,null));
    adapter_main = new DetailEditAdapter(list);
    adapter_main.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //adapter_main.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this,ImgSelectActivity.class);
        intent.putExtra("img_type",ImgType.MAIN);
        intent.putParcelableArrayListExtra("img_list",imgSelectData);
        startActivityForResult(intent,ImgType.MAIN);
      }
    });
    rvDetailEditMain.setLayoutManager(manager_main);
    rvDetailEditMain.setAdapter(adapter_main);
  }

  private void initScale() {
    GridLayoutManager manager_scale = new GridLayoutManager(this,3);
    List<MutilType> list = new ArrayList<>();
    list.add(new MutilType(MutilType.T_ADD,null));
    adapter_scale = new DetailEditAdapter(list);
    adapter_scale.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //adapter_scale.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this,ImgSelectActivity.class);
        intent.putExtra("img_type",ImgType.SCALE);
        intent.putParcelableArrayListExtra("img_list",imgSelectData);
        startActivityForResult(intent,ImgType.SCALE);
      }
    });
    rvDetailEditScale.setLayoutManager(manager_scale);
    rvDetailEditScale.setAdapter(adapter_scale);
  }

  private void initBanner() {
    GridLayoutManager manager_banner = new GridLayoutManager(this,3);
    List<MutilType> list = new ArrayList<>();
    list.add(new MutilType(MutilType.T_ADD,null));
    adapter_banner = new DetailEditAdapter(list);
    adapter_banner.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //adapter_banner.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this,ImgSelectActivity.class);
        intent.putExtra("img_type",ImgType.BANNER);
        intent.putParcelableArrayListExtra("img_list",imgSelectData);
        startActivityForResult(intent,ImgType.BANNER);
      }
    });
    rvDetailEditBanner.setLayoutManager(manager_banner);
    rvDetailEditBanner.setAdapter(adapter_banner);
  }

  private void initDetail() {
    GridLayoutManager manager_detail = new GridLayoutManager(this,3);
    List<MutilType> list = new ArrayList<>();
    list.add(new MutilType(MutilType.T_ADD,null));
    adapter_detail = new DetailEditAdapter(list);
    adapter_detail.setOnEditClickListener(new DetailEditAdapter.OnEditClickListener() {
      @Override public void onRemove(int position) {
        //adapter_detail.remove(position);
      }

      @Override public void onAdd() {
        Intent intent = new Intent(DetailEditActivity.this,ImgSelectActivity.class);
        intent.putExtra("img_type",ImgType.DETAIL);
        intent.putParcelableArrayListExtra("img_list",imgSelectData);
        startActivityForResult(intent,ImgType.DETAIL);
      }
    });
    rvDetailEditDetail.setLayoutManager(manager_detail);
    rvDetailEditDetail.setAdapter(adapter_detail);
  }

  @OnClick({R.id.btn_select_shop ,R.id.detail_edit_back,R.id.tv_scan})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.detail_edit_back:
        finish();
        break;
      case R.id.btn_select_shop:
        break;
      case R.id.tv_scan:
        //UpWare upWare = new UpWare();
        //upWare.setShopId((long) 3);
        //upWare.setProduct_title(etDetailEditName.getText().toString());
        //upWare.setMarket_price(etDetailEditPrice.getText().toString());

        break;
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK){
      imgSelectData = data.getParcelableArrayListExtra("img_select_data");
      freshData(imgSelectData,requestCode);
    }
  }

  private void freshData(ArrayList<DeeImgEntity> imgSelectData, int requestCode) {
    if (requestCode == ImgType.MAIN){
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isMain()){
          list.add(new MutilType(MutilType.T_NORMAL,imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD,null));
      adapter_main.setNewData(list);
    }else if (requestCode == ImgType.SCALE){
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isScale()){
          list.add(new MutilType(MutilType.T_NORMAL,imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD,null));
      adapter_scale.setNewData(list);
    }else if (requestCode == ImgType.BANNER){
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isBanner()){
          list.add(new MutilType(MutilType.T_NORMAL,imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD,null));
      adapter_banner.setNewData(list);
    }else if (requestCode == ImgType.DETAIL){
      List<MutilType> list = new ArrayList<>();
      for (int i = 0; i < imgSelectData.size(); i++) {
        if (imgSelectData.get(i).isDetail()){
          list.add(new MutilType(MutilType.T_NORMAL,imgSelectData.get(i)));
        }
      }
      list.add(new MutilType(MutilType.T_ADD,null));
      adapter_detail.setNewData(list);
    }
  }
}
