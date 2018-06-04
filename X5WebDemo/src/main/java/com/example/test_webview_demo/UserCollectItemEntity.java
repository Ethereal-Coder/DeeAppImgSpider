package com.example.test_webview_demo;

import android.net.Uri;

/**
 * Created by 孙应恒 on 2018/5/31.
 * Description:
 */
class UserCollectItemEntity {
  private boolean isChecked = false;
  private String imgUrl;
  private Uri path;

  public Uri getPath() {
    return path;
  }

  public void setPath(Uri path) {
    this.path = path;
  }

  public boolean isChecked() {
    return isChecked;
  }

  public void setChecked(boolean checked) {
    isChecked = checked;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public UserCollectItemEntity(String imgUrl) {
    this.imgUrl = imgUrl;
  }
}
