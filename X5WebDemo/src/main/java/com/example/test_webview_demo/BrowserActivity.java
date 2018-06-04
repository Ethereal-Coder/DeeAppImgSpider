package com.example.test_webview_demo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.test_webview_demo.net.VisitorNets;
import com.example.test_webview_demo.utils.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient.CustomViewCallback;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.ResponseBody;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class BrowserActivity extends Activity {
  private static final String TAG_FINISH = "Dee_finish";
  private X5WebView mWebView;
  private ViewGroup mViewParent;
  private ImageButton mBack;
  private ImageButton mForward;
  private ImageButton mHome;
  private Button mMore;
  private Button mGo;
  private EditText mUrl;

  private static final String mHomeUrl = "http://m.taobao.com";
  private static final String TAG = "Dee_log";
  private static final String TAG_IMG = "Dee_img";
  private static final String TAG_HOST = "Dee_host";
  private static final String TAG_TITLE = "Dee_title";
  private static final int MAX_LENGTH = 14;
  private boolean mNeedTestPage = false;

  private final int disable = 120;
  private final int enable = 255;

  private ProgressBar mPageLoadingProgressBar = null;

  private ValueCallback<Uri> uploadFile;

  private URL mIntentUrl;

  private ArrayList<String> imgUrls = new ArrayList<>();
  private String dee_filter;
  private String title_x = "";
  private String title_j = "";
  private Disposable disposable;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setFormat(PixelFormat.TRANSLUCENT);

    Intent intent = getIntent();
    if (intent != null) {
      try {
        mIntentUrl = new URL(intent.getData().toString());
      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {

      } catch (Exception e) {
      }
    }
    //
    try {
      if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
      }
    } catch (Exception e) {
    }

    /*
     * getWindow().addFlags(
     * android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
     */
    setContentView(R.layout.activity_main);
    mViewParent = (ViewGroup) findViewById(R.id.webView1);

    initBtnListenser();

    mTestHandler.sendEmptyMessageDelayed(MSG_INIT_UI, 10);
  }

  private void changGoForwardButton(WebView view) {
    if (view.canGoBack()) {
      mBack.setAlpha(enable);
    } else {
      mBack.setAlpha(disable);
    }
    if (view.canGoForward()) {
      mForward.setAlpha(enable);
    } else {
      mForward.setAlpha(disable);
    }
    if (view.getUrl() != null && view.getUrl().equalsIgnoreCase(mHomeUrl)) {
      mHome.setAlpha(disable);
      mHome.setEnabled(false);
    } else {
      mHome.setAlpha(enable);
      mHome.setEnabled(true);
    }
  }

  private void initProgressBar() {
    mPageLoadingProgressBar = (ProgressBar) findViewById(R.id.progressBar1);// new
    // ProgressBar(getApplicationContext(),
    // null,
    // android.R.attr.progressBarStyleHorizontal);
    mPageLoadingProgressBar.setMax(100);
    mPageLoadingProgressBar.setProgressDrawable(
        this.getResources().getDrawable(R.drawable.color_progressbar));
  }

  @SuppressLint("SetJavaScriptEnabled") private void init() {

    mWebView = new X5WebView(this);

    mViewParent.addView(mWebView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
        FrameLayout.LayoutParams.FILL_PARENT));

    initProgressBar();

    mWebView.setWebViewClient(new WebViewClient() {
      @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return false;
      }

      @Override public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
        super.onPageStarted(webView, s, bitmap);
        String host = getHost(s);
        Log.d(TAG_HOST, "url: " + host);
        if (host.contains("taobao") || host.contains("tmall")) {
          dee_filter = "tb";
        } else if (host.contains("jd")) {
          dee_filter = "jd";
        } else {
          dee_filter = "normal";
        }
        imgUrls.clear();

        title_j = "";
        if (host.contains("taobao") && host.contains("detail")) {
          getWebTitleByJsoup(s, "tb");
        } else if (host.contains("tmall") && host.contains("detail")) {
          getWebTitleByJsoup(s, "tm");
        }
        //getWebTitleByRetrofit(s);
        Log.e(TAG_FINISH, "pageStarted：" + s);
      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.e(TAG_FINISH, "pageFinish：" + url);

        //mTestHandler.sendEmptyMessage(MSG_OPEN_TEST_URL);
        mTestHandler.sendEmptyMessageDelayed(MSG_OPEN_TEST_URL, 5000);// 5s?
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) changGoForwardButton(view);
        /* mWebView.showLog("test Log"); */
      }

      @Override public void onLoadResource(WebView webView, String url) {
        //super.onLoadResource(webView, s);
        if (isImageSuffix(url)) {
          if (dee_filter.equals("tb")) {
            Log.e(TAG_IMG, "加载img：" + url);
            if ((url.contains("/tps"))
                ||(url.contains("imgextra"))
                ||(url.contains("tfscom"))
                ||(url.contains("uploaded"))){
              imgUrls.add(url);
              Log.i(TAG_IMG, "加载img：" + url);
            }else {
              Log.w(TAG_IMG, "加载img：" + url);
            }
          } else if (dee_filter.equals("jd")) {
            if (url.contains("da") || url.contains("mobilecms") || url.contains("jdphoto")) {
              imgUrls.add(url);
            }
          } else {
            imgUrls.add(url);
          }
          //Log.e(TAG,"img资源：" + url);
          //Glide.with(mContext).load(url).asBitmap().into(new SimpleTarget<Bitmap>() {
          //    @Override
          //    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
          //        mCache.saveBitmap(url, resource);
          //    }
          //});
        }
      }
    });

    mWebView.setWebChromeClient(new WebChromeClient() {

      @Override public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
        return super.onJsConfirm(arg0, arg1, arg2, arg3);
      }

      View myVideoView;
      View myNormalView;
      CustomViewCallback callback;

      // /////////////////////////////////////////////////////////
      //

      /**
       * 全屏播放配置
       */
      @Override public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
        FrameLayout normalView = (FrameLayout) findViewById(R.id.web_filechooser);
        ViewGroup viewGroup = (ViewGroup) normalView.getParent();
        viewGroup.removeView(normalView);
        viewGroup.addView(view);
        myVideoView = view;
        myNormalView = normalView;
        callback = customViewCallback;
      }

      @Override public void onHideCustomView() {
        if (callback != null) {
          callback.onCustomViewHidden();
          callback = null;
        }
        if (myVideoView != null) {
          ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
          viewGroup.removeView(myVideoView);
          viewGroup.addView(myNormalView);
        }
      }

      @Override public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
        /**
         * 这里写入你自定义的window alert
         */
        return super.onJsAlert(null, arg1, arg2, arg3);
      }

      @Override public void onReceivedTitle(WebView webView, String s) {
        Log.e(TAG_TITLE, "title：" + s);
        title_x = s.trim();
        super.onReceivedTitle(webView, s);
      }
    });

    mWebView.setDownloadListener(new DownloadListener() {

      @Override
      public void onDownloadStart(String arg0, String arg1, String arg2, String arg3, long arg4) {
        TbsLog.d(TAG, "url: " + arg0);
        new AlertDialog.Builder(BrowserActivity.this).setTitle("allow to download？")
            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
              @SuppressLint("WrongConstant") @Override
              public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BrowserActivity.this, "fake message: i'll download...",
                    Toast.LENGTH_SHORT).show();
              }
            })
            .setNegativeButton("no", new DialogInterface.OnClickListener() {

              @Override public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BrowserActivity.this, "fake message: refuse download...",
                    Toast.LENGTH_SHORT).show();
              }
            })
            .setOnCancelListener(new DialogInterface.OnCancelListener() {

              @Override public void onCancel(DialogInterface dialog) {
                Toast.makeText(BrowserActivity.this, "fake message: refuse download...",
                    Toast.LENGTH_SHORT).show();
              }
            })
            .show();
      }
    });

    WebSettings webSetting = mWebView.getSettings();
    webSetting.setAllowFileAccess(true);
    webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
    webSetting.setSupportZoom(true);
    webSetting.setBuiltInZoomControls(true);
    webSetting.setUseWideViewPort(true);
    webSetting.setSupportMultipleWindows(false);
    // webSetting.setLoadWithOverviewMode(true);
    webSetting.setAppCacheEnabled(true);
    // webSetting.setDatabaseEnabled(true);
    webSetting.setDomStorageEnabled(true);
    webSetting.setJavaScriptEnabled(true);
    webSetting.setGeolocationEnabled(true);
    webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
    webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
    webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
    webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
    // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
    webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
    // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
    // webSetting.setPreFectch(true);
    long time = System.currentTimeMillis();
    if (mIntentUrl == null) {
      mWebView.loadUrl(mHomeUrl);
    } else {
      mWebView.loadUrl(mIntentUrl.toString());
    }
    Log.d("time-cost", "cost time: " + (System.currentTimeMillis() - time));
    CookieSyncManager.createInstance(this);
    CookieSyncManager.getInstance().sync();
  }

  private void getWebTitleByRetrofit(String url) {
    Observable.just(url)
        .subscribeOn(Schedulers.io())
        .flatMap(new Function<String, ObservableSource<ResponseBody>>() {
          @Override public ObservableSource<ResponseBody> apply(String s) throws Exception {
            return VisitorNets.getInstance().downloadImg(s);
          }
        })
        .map(new Function<ResponseBody, String>() {
          @Override public String apply(ResponseBody responseBody) throws Exception {
            return responseBody.string();
          }
        })
        .observeOn(Schedulers.io())
        .subscribe(new Observer<String>() {
          @Override public void onSubscribe(Disposable d) {

          }

          @Override public void onNext(String s) {
            Document doc = Jsoup.parse(s);
            Log.e("Dee_content", doc.toString());
            Elements metas = doc.select("meta[name]");
            for (Element meta : metas) {
              //Log.e("Dee_content",meta.toString());
              if (meta.attr("name").equals("keywords")) {
                String content = meta.attr("content");
                //Log.e("Dee_content",content);
                title_j = content;
              }
            }
          }

          @Override public void onError(Throwable e) {

          }

          @Override public void onComplete() {

          }
        });
  }

  private void getWebTitleByJsoup(String url, final String platform) {
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
    io.reactivex.Observable.just(url)
        .observeOn(Schedulers.io())
        .subscribeOn(Schedulers.io())
        .subscribe(new Observer<String>() {
          @Override public void onSubscribe(Disposable d) {
            disposable = d;
          }

          @Override public void onNext(String url) {
            try {
              Document doc = Jsoup.connect(url).timeout(5000).get();
              Log.e("Dee_content", doc.toString());
              if (platform.equals("tb")) {
                Elements metas = doc.select("meta[name]");
                for (Element meta : metas) {
                  //Log.e("Dee_content",meta.toString());
                  if (meta.attr("name").equals("keywords")) {
                    String content = meta.attr("content");
                    //Log.e("Dee_content",content);
                    title_j = content;
                  }
                }
              } else if (platform.equals("tm")) {
                Elements metas = doc.select("meta[property]");
                for (Element meta : metas) {
                  //Log.e("Dee_content",meta.toString());
                  if (meta.attr("property").equals("og:title")) {
                    String content = meta.attr("content");
                    //Log.e("Dee_content",content);
                    title_j = content;
                  }
                }
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }

          @Override public void onError(Throwable e) {
          }

          @Override public void onComplete() {

          }
        });
  }

  /**
   * webp --> tps, imgextra,tfscom,uploaded
   * https://gw.alicdn.com/imgextra/i4/T1vsbrXgdgXXcK5RZ8_101517.jpg_970x970q50s150.jpg_.webp
   * https://gw.alicdn.com/tps/i4/TB1JnU9QXXXXXaEXXXXqa6X9pXX_720x720q75.jpg_.webp
   *
   * jd
   * png -- mobilecms --
   */
  private boolean isImageSuffix(String url) {
    return url.endsWith(".png")
        || url.endsWith(".PNG")
        || url.endsWith(".jpg")
        || url.endsWith(".JPG")
        || url.endsWith(".jpeg")
        || url.endsWith(".JPEG")
        || url.endsWith(".webp")
        || url.endsWith(".WEBP");
  }

  /**
   *
   * @param url
   * @return
   */
  public String getHost(String url) {
    if (url == null || url.trim().equals("")) {
      return "";
    }
    String host = "";
    Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+");
    Matcher matcher = p.matcher(url);
    if (matcher.find()) {
      host = matcher.group();
    }
    return host;
  }

  private boolean checkImgSize(String xx){
    return checkImgSize(xx,300);
  }

  /**
   * 合适返回true
   */
  private boolean checkImgSize(String xx,int imgWidth){
    int index_j = xx.indexOf(".jpg_");
    int index_p = xx.indexOf(".png_");
    if (index_p == -1) {
      if (index_j == -1) {

      } else {
        String xs = xx.substring(index_j + 5);
        int index_x = xs.indexOf("x");
        try {
          Integer integer = Integer.getInteger(xs.substring(0, index_x));
          if (integer < imgWidth) {
            return false;
          }
        }catch (Exception e){

        }

      }
    } else {
      if (index_j == -1) {
        String xs = xx.substring(index_p + 5);
        int index_x = xs.indexOf("x");
        try {
          Integer integer = Integer.getInteger(xs.substring(0, index_x));
          if (integer < imgWidth) {
            return false;
          }
        }catch (Exception e){

        }
      } else {
        int temp_index = index_j > index_p ? index_p : index_j;
        String xs = xx.substring(temp_index + 5);
        int index_x = xs.indexOf("x");
        try {
          Integer integer = Integer.getInteger(xs.substring(0, index_x));
          if (integer < imgWidth) {
            return false;
          }
        }catch (Exception e){

        }
      }
    }
    return true;
  }

  private ArrayList<String> getOpImgUrls(ArrayList<String> imgUrls) {
    ArrayList<String> list = new ArrayList<>();
    for (String img : imgUrls) {
      int index_p = img.indexOf(".png");
      int index_j = img.indexOf(".jpg");
      if (index_p == -1) {
        if (index_j == -1) {
          list.add(img);
        } else {
          list.add(img.substring(0, index_j + 4));
        }
      } else {
        if (index_j == -1) {
          list.add(img.substring(0, index_p + 4));
        } else {
          int temp_index = index_j > index_p ? index_p : index_j;
          list.add(img.substring(0, temp_index + 4));
        }
      }
    }
    return list;
  }

  private void initBtnListenser() {
    mBack = (ImageButton) findViewById(R.id.btnBack1);
    mForward = (ImageButton) findViewById(R.id.btnForward1);
    //mExit = (ImageButton) findViewById(R.id.btnExit1);
    mHome = (ImageButton) findViewById(R.id.btnHome1);
    mGo = (Button) findViewById(R.id.btnGo1);
    mUrl = (EditText) findViewById(R.id.editUrl1);
    mMore = (Button) findViewById(R.id.btnMore);
    if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) {
      mBack.setAlpha(disable);
      mForward.setAlpha(disable);
      mHome.setAlpha(disable);
    }
    mHome.setEnabled(false);

    mBack.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (mWebView != null && mWebView.canGoBack()) mWebView.goBack();
      }
    });

    mForward.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (mWebView != null && mWebView.canGoForward()) mWebView.goForward();
      }
    });

    mGo.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        String url = mUrl.getText().toString();
        mWebView.loadUrl(url);
        mWebView.requestFocus();
      }
    });

    mMore.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (imgUrls.size() == 0) {
          Toast.makeText(BrowserActivity.this, "请换个网页尝试", Toast.LENGTH_LONG).show();
        } else {
          ArrayList<String> list = new ArrayList<>();
          for (int i = 0; i < imgUrls.size(); i++) {
            if (checkImgSize(imgUrls.get(i))){
              //Log.e("Dee_remove", imgUrls.get(i));
              //imgUrls.remove(i);
              list.add(imgUrls.get(i));
            }
          }
          Intent intent = new Intent(BrowserActivity.this, ShareActivity.class);
          intent.putStringArrayListExtra("share_imgs", imgUrls);
          intent.putExtra("share_title", title_j.isEmpty() ? title_x : title_j);
          startActivity(intent);
        }
      }
    });

    mUrl.setOnFocusChangeListener(new OnFocusChangeListener() {

      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          //mGo.setVisibility(View.VISIBLE);
          if (null == mWebView.getUrl()) return;
          if (mWebView.getUrl().equalsIgnoreCase(mHomeUrl)) {
            mUrl.setText("");
            mGo.setText("首页");
            mGo.setTextColor(0X6F0F0F0F);
          } else {
            mUrl.setText(mWebView.getUrl());
            mGo.setText("进入");
            mGo.setTextColor(0X6F0000CD);
          }
        } else {
          //mGo.setVisibility(View.GONE);
          String title = mWebView.getTitle();
          if (title != null && title.length() > MAX_LENGTH) {
            mUrl.setText(title.subSequence(0, MAX_LENGTH) + "...");
          } else {
            mUrl.setText(title);
          }
          InputMethodManager imm =
              (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
      }
    });

    mUrl.addTextChangedListener(new TextWatcher() {

      @Override public void afterTextChanged(Editable s) {
        String url = null;
        if (mUrl.getText() != null) {
          url = mUrl.getText().toString();
        }

        if (url == null || mUrl.getText().toString().equalsIgnoreCase("")) {
          mGo.setText("请输入网址");
          mGo.setTextColor(0X6F0F0F0F);
        } else {
          mGo.setText("进入");
          mGo.setTextColor(0X6F0000CD);
        }
      }

      @Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

      }

      @Override public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

      }
    });

    mHome.setOnClickListener(new OnClickListener() {

      @Override public void onClick(View v) {
        if (mWebView != null) mWebView.loadUrl(mHomeUrl);
      }
    });

    //mExit.setOnClickListener(new OnClickListener() {
    //  @Override public void onClick(View v) {
    //    Process.killProcess(Process.myPid());
    //  }
    //});
  }

  boolean[] m_selected = new boolean[] {
      true, true, true, true, false, false, true
  };

  @Override public boolean onKeyDown(int keyCode, KeyEvent event) {

    if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (mWebView != null && mWebView.canGoBack()) {
        mWebView.goBack();
        if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16) changGoForwardButton(mWebView);
        return true;
      } else {
        return super.onKeyDown(keyCode, event);
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    TbsLog.d(TAG, "onActivityResult, requestCode:" + requestCode + ",resultCode:" + resultCode);

    if (resultCode == RESULT_OK) {
      switch (requestCode) {
        case 0:
          if (null != uploadFile) {
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            uploadFile.onReceiveValue(result);
            uploadFile = null;
          }
          break;
        default:
          break;
      }
    } else if (resultCode == RESULT_CANCELED) {
      if (null != uploadFile) {
        uploadFile.onReceiveValue(null);
        uploadFile = null;
      }
    }
  }

  @Override protected void onNewIntent(Intent intent) {
    if (intent == null || mWebView == null || intent.getData() == null) return;
    mWebView.loadUrl(intent.getData().toString());
  }

  @Override protected void onDestroy() {
    if (mTestHandler != null) mTestHandler.removeCallbacksAndMessages(null);
    if (mWebView != null) mWebView.destroy();
    super.onDestroy();
  }

  public static final int MSG_OPEN_TEST_URL = 0;
  public static final int MSG_INIT_UI = 1;
  private final int mUrlStartNum = 0;
  private int mCurrentUrl = mUrlStartNum;
  private Handler mTestHandler = new Handler() {
    @Override public void handleMessage(Message msg) {
      switch (msg.what) {
        case MSG_OPEN_TEST_URL:
          if (!mNeedTestPage) {
            return;
          }

          String testUrl =
              "file:///sdcard/outputHtml/html/" + Integer.toString(mCurrentUrl) + ".html";
          if (mWebView != null) {
            mWebView.loadUrl(testUrl);
          }

          mCurrentUrl++;
          break;
        case MSG_INIT_UI:
          init();
          break;
      }
      super.handleMessage(msg);
    }
  };
}
