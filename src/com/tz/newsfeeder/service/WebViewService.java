package com.tz.newsfeeder.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.os.StrictMode;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.tz.newsfeeder.dao.StoryDao;
import com.tz.newsfeeder.view.ObservableWebView;
import com.tz.newsfeeder.view.ObservableWebView.OnScrollChangedCallback;

public class WebViewService extends Service {
  private ObservableWebView myWebView;
  private Context mContext;

  public WebViewService(Context mContext) {
    this.mContext = mContext;
  }
  
  @SuppressLint("SetJavaScriptEnabled")
  public void launchWebView(ObservableWebView aWebView) {
    try {
      myWebView = aWebView;

      StrictMode.enableDefaults();
      WebSettings webSettings = myWebView.getSettings();
      webSettings.setJavaScriptEnabled(true);
      webSettings.setDomStorageEnabled(true);
      webSettings.setDatabaseEnabled(true);
      webSettings.setBuiltInZoomControls(true);
      webSettings.setDisplayZoomControls(false);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        WebView.setWebContentsDebuggingEnabled(true);
      }
      if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
        webSettings.setAllowUniversalAccessFromFileURLs(true);
      }
      myWebView.addJavascriptInterface(new FromWebService(mContext), "Android");

      myWebView.setOnScrollChangedCallback(new OnScrollChangedCallback() {
        public void onScroll(int l, int t) {
          int height = (int) Math.floor(myWebView.getContentHeight() * myWebView.getScaleY());
          int cutoff = (height * (StoryDao.currPage + 1)) - 10;
          if (t >= cutoff) {
            String rslt = new StoryDao(mContext).getStoryList().toString();
            if (!rslt.equals("")) {
              myWebView.loadUrl("javascript:gfNextPage(" + rslt + ")");
            }
          }
        }
      });

      myWebView.loadUrl("file:///android_asset/www/index.html");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}
