package com.example.test_webview_demo.detail;

import io.reactivex.annotations.Nullable;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class MutilType {
  public static int T_ADD = 1;
  public static int T_NORMAL = 2;
  private int type;
  private DeeImgEntity deeImgEntity;

  public MutilType(int type,@Nullable DeeImgEntity deeImgEntity) {
    this.type = type;
    this.deeImgEntity = deeImgEntity;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public DeeImgEntity getDeeImgEntity() {
    return deeImgEntity;
  }

  public void setDeeImgEntity(DeeImgEntity deeImgEntity) {
    this.deeImgEntity = deeImgEntity;
  }
}
