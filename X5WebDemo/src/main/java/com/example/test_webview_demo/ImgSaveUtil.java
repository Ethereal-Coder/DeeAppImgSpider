package com.example.test_webview_demo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;

/**
 * Created by 孙应恒 on 2017/10/24.
 * Description:
 */

public class ImgSaveUtil {
  public static boolean saveImageToGallery(Context context, ResponseBody responseBody,String storePath,String fileName) {
    InputStream is = responseBody.byteStream();
    Bitmap bmp = BitmapFactory.decodeStream(is);
    // 首先保存图片
    File appDir = new File(storePath);
    if (!appDir.exists()) {
      appDir.mkdir();
    }
    File file = new File(appDir, fileName);
    try {
      FileOutputStream fos = new FileOutputStream(file);
      //通过io流的方式来压缩保存图片
      boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
      fos.flush();
      fos.close();

      //把文件插入到系统图库
      //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

      //保存图片后发送广播通知更新数据库
      Uri uri = Uri.fromFile(file);
      context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
      if (isSuccess) {
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return false;
  }
}
