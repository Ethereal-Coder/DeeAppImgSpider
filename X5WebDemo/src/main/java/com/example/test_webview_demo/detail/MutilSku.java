package com.example.test_webview_demo.detail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 孙应恒 on 2018/6/7.
 * Description:
 */
class MutilSku implements Parcelable {
  public static int T_ADD = 1;
  public static int T_NORMAL = 2;
  private int type;
  private Sku sku;

  public MutilSku(int type, Sku sku) {
    this.type = type;
    this.sku = sku;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Sku getSku() {
    return sku;
  }

  public void setSku(Sku sku) {
    this.sku = sku;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.type);
    dest.writeParcelable(this.sku, flags);
  }

  protected MutilSku(Parcel in) {
    this.type = in.readInt();
    this.sku = in.readParcelable(Sku.class.getClassLoader());
  }

  public static final Parcelable.Creator<MutilSku> CREATOR = new Parcelable.Creator<MutilSku>() {
    @Override public MutilSku createFromParcel(Parcel source) {
      return new MutilSku(source);
    }

    @Override public MutilSku[] newArray(int size) {
      return new MutilSku[size];
    }
  };
}
