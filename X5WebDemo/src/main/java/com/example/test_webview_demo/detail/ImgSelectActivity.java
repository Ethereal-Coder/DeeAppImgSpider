package com.example.test_webview_demo.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.test_webview_demo.R;
import java.util.ArrayList;

public class ImgSelectActivity extends AppCompatActivity {

  @BindView(R.id.tv_img_select_cancel) TextView tvImgSelectCancel;
  @BindView(R.id.tv_img_select_complete) TextView tvImgSelectComplete;
  @BindView(R.id.rv_img_select) RecyclerView rvImgSelect;
  private int imgType;
  private ArrayList<DeeImgEntity> imgList;
  private ImgSelectAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_img_select);
    ButterKnife.bind(this);
    imgList = getIntent().getParcelableArrayListExtra("img_list");
    GridLayoutManager layoutManager = new GridLayoutManager(this,2);
    imgType = getIntent().getIntExtra("img_type", 0);
    adapter = new ImgSelectAdapter(imgList,imgType);
    rvImgSelect.setLayoutManager(layoutManager);
    rvImgSelect.setAdapter(adapter);
  }

  @OnClick({ R.id.tv_img_select_cancel, R.id.tv_img_select_complete })
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.tv_img_select_cancel:
        finish();
        break;
      case R.id.tv_img_select_complete:
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("img_select_data",adapter.getData());
        setResult(RESULT_OK,intent);
        finish();
        break;
    }
  }
}
