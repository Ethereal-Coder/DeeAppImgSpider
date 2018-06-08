package com.example.test_webview_demo.detail;

import android.os.Parcel;
import android.os.Parcelable;
import io.reactivex.annotations.Nullable;

/**
 * Created by 孙应恒 on 2018/6/5.
 * Description:
 */
public class MutilType implements Parcelable {
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(this.type);
    dest.writeParcelable(this.deeImgEntity, flags);
  }

  protected MutilType(Parcel in) {
    this.type = in.readInt();
    this.deeImgEntity = in.readParcelable(DeeImgEntity.class.getClassLoader());
  }

  public static final Parcelable.Creator<MutilType> CREATOR = new Parcelable.Creator<MutilType>() {
    @Override public MutilType createFromParcel(Parcel source) {
      return new MutilType(source);
    }

    @Override public MutilType[] newArray(int size) {
      return new MutilType[size];
    }
  };
}
