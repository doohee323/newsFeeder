package com.tz.newsfeeder.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;

import com.tz.newsfeeder.R;
import com.tz.newsfeeder.dao.StoryDao;
import com.tz.newsfeeder.service.WebViewService;
import com.tz.newsfeeder.view.ObservableWebView;

public class MainActivity extends Activity {

  private ObservableWebView myWebView = null;
  Context mContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
        .detectDiskWrites().detectNetwork().penaltyLog().build());
    StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    super.onCreate(savedInstanceState);

    mContext = getBaseContext();

    setContentView(R.layout.activity_main);
    try {
      StoryDao dao = new StoryDao(mContext);
      dao.drop();
      dao.open();

      myWebView = (ObservableWebView) findViewById(R.id.scorllableWebview);
      new WebViewService(mContext).launchWebView(myWebView);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  public void onBackPressed() {
    if (myWebView.canGoBack()) {
      myWebView.goBack();
    } else {
      finish();
    }
  }

}
