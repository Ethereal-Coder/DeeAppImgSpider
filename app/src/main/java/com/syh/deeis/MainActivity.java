package com.syh.deeis;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.syh.deeis.webview.GcsMarkdownViewClient;
import com.syh.deeis.webview.WebImageListener;

public class MainActivity extends AppCompatActivity {

  private FrameLayout container;
  private WebView webView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    container = findViewById(R.id.fl_container);
    webView = new WebView(this);
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setDatabaseEnabled(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      settings.setAllowUniversalAccessFromFileURLs(true);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    webView.setWebChromeClient(new WebChromeClient() {
      @SuppressLint("JavascriptInterface")
      @Override
      public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        //if (newProgress == 100) {
        //  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
        //    webView.loadUrl(mPreviewText);
        //  } else {
        //    webView.evaluateJavascript(mPreviewText, null);
        //  }
        //}
      }
    });

    WebImageListener listener = new WebImageListener(this);
    webView.addJavascriptInterface(listener, "listener");
    GcsMarkdownViewClient mWebViewClient = new GcsMarkdownViewClient(this);
    webView.setWebViewClient(mWebViewClient);
    webView.loadUrl("http://m.jd.com");
    container.addView(webView);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    clearWebViewResource();
  }

  private void clearWebViewResource() {
    if (webView != null) {
      webView.clearHistory();
      ((ViewGroup) webView.getParent()).removeView(webView);
      webView.setTag(null);
      webView.loadUrl("about:blank");
      webView.stopLoading();
      webView.setWebViewClient(null);
      webView.setWebChromeClient(null);
      webView.removeAllViews();
      webView.destroy();
      webView = null;
    }
  }
}
