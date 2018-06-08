package com.example.test_webview_demo.detail;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

/**
 * Created by 孙应恒 on 2018/6/6.
 * Description:
 */
class LocalImageHolderView implements Holder<DeeImgEntity>{
  private ImageView imageView;
  @Override public View createView(Context context) {
    imageView = new ImageView(context);
    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    return imageView;
  }

  @Override public void UpdateUI(Context context, int position, DeeImgEntity data) {
    Glide.with(context).load(data.getImgUrl()).into(imageView);
  }
}
