package com.example.test_webview_demo.mainpf;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class MainPfEntity implements Parcelable {
  String name;
  String url;

  public MainPfEntity(String name, String url) {
    this.name = name;
    this.url = url;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.name);
    dest.writeString(this.url);
  }

  protected MainPfEntity(Parcel in) {
    this.name = in.readString();
    this.url = in.readString();
  }

  public static final Parcelable.Creator<MainPfEntity> CREATOR =
      new Parcelable.Creator<MainPfEntity>() {
        @Override public MainPfEntity createFromParcel(Parcel source) {
          return new MainPfEntity(source);
        }

        @Override public MainPfEntity[] newArray(int size) {
          return new MainPfEntity[size];
        }
      };
}
