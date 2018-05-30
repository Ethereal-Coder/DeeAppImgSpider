package com.syh.deeis;

import android.app.Application;
import com.syh.deeis.log.Logger;

/**
 * Created by 孙应恒 on 2018/5/30.
 * Description:
 */
public class DeeApp extends Application {
  @Override public void onCreate() {
    super.onCreate();
    Logger.init();
  }
}
