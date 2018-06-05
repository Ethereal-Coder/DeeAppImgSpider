package com.example.test_webview_demo.mainpf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.test_webview_demo.R;
import java.util.ArrayList;
import java.util.List;

public class MainPlateformActivity extends AppCompatActivity {

  @BindView(R.id.rv_main_plateform) RecyclerView rvMainPlateform;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_plateform);
    ButterKnife.bind(this);
    List<MainPfEntity> list = new ArrayList<>();
    list.add(new MainPfEntity("1688","https://m.1688.com"));
    list.add(new MainPfEntity("淘宝","https://h5.m.taobao.com"));
    LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    MainPlateformAdapter adapter = new MainPlateformAdapter(list);
    rvMainPlateform.setLayoutManager(layoutManager);
    rvMainPlateform.setAdapter(adapter);
  }
}
