package com.example.test_webview_demo.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 孙应恒 on 2018/6/7.
 * Description:
 */
class Sku implements Parcelable {
  private String name1;
  private String name2;
  private String url = "";

  public Sku(String name1) {
    this.name1 = name1;
  }

  public Sku(String name1, String name2, String url) {
    this.name1 = name1;
    this.name2 = name2;
    this.url = url;
  }

  public String getName1() {
    return name1;
  }

  public void setName1(String name1) {
    this.name1 = name1;
  }

  public String getName2() {
    return name2;
  }

  public void setName2(String name2) {
    this.name2 = name2;
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
    dest.writeString(this.name1);
    dest.writeString(this.name2);
    dest.writeString(this.url);
  }

  protected Sku(Parcel in) {
    this.name1 = in.readString();
    this.name2 = in.readString();
    this.url = in.readString();
  }

  public static final Parcelable.Creator<Sku> CREATOR = new Parcelable.Creator<Sku>() {
    @Override public Sku createFromParcel(Parcel source) {
      return new Sku(source);
    }

    @Override public Sku[] newArray(int size) {
      return new Sku[size];
    }
  };
}
