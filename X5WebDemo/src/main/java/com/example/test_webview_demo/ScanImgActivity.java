package com.example.test_webview_demo;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.shizhefei.view.largeimage.LargeImageView;
import com.shizhefei.view.largeimage.factory.FileBitmapDecoderFactory;

public class ScanImgActivity extends AppCompatActivity {

  @BindView(R.id.toolbar_scan_img) Toolbar toolbarScanImg;
  @BindView(R.id.liv_scan_img) LargeImageView imageView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_scan_img);
    ButterKnife.bind(this);
    toolbarScanImg.setTitle("");
    setSupportActionBar(toolbarScanImg);
    toolbarScanImg.setNavigationIcon(R.mipmap.activity_back);
    toolbarScanImg.setNavigationOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onBackPressed();
      }
    });
    int position = getIntent().getIntExtra("scan_img_position",0);
    String scanImg = getIntent().getStringExtra("scan_img");
    ViewCompat.setTransitionName(imageView, "smart_ware_img"+position);
    imageView.setImage(new FileBitmapDecoderFactory(scanImg));
    imageView.setEnabled(true);
  }
}
