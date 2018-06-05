package com.example.test_webview_demo.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class DeeImgEntity implements Parcelable {
  private String imgUrl;
  private boolean isMain;
  private boolean isScale;
  private boolean isBanner;
  private boolean isDetail;

  public boolean isMain() {
    return isMain;
  }

  public void setMain(boolean main) {
    isMain = main;
  }

  public boolean isScale() {
    return isScale;
  }

  public void setScale(boolean scale) {
    isScale = scale;
  }

  public boolean isBanner() {
    return isBanner;
  }

  public void setBanner(boolean banner) {
    isBanner = banner;
  }

  public boolean isDetail() {
    return isDetail;
  }

  public void setDetail(boolean detail) {
    isDetail = detail;
  }

  public String getImgUrl() {
    return imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public DeeImgEntity(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.imgUrl);
    dest.writeByte(this.isMain ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isScale ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isBanner ? (byte) 1 : (byte) 0);
    dest.writeByte(this.isDetail ? (byte) 1 : (byte) 0);
  }

  protected DeeImgEntity(Parcel in) {
    this.imgUrl = in.readString();
    this.isMain = in.readByte() != 0;
    this.isScale = in.readByte() != 0;
    this.isBanner = in.readByte() != 0;
    this.isDetail = in.readByte() != 0;
  }

  public static final Parcelable.Creator<DeeImgEntity> CREATOR =
      new Parcelable.Creator<DeeImgEntity>() {
        @Override public DeeImgEntity createFromParcel(Parcel source) {
          return new DeeImgEntity(source);
        }

        @Override public DeeImgEntity[] newArray(int size) {
          return new DeeImgEntity[size];
        }
      };
}
