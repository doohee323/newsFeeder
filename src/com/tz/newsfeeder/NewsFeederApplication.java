package com.tz.newsfeeder;

import android.app.Application;
import android.content.Context;

import com.tz.newsfeeder.receiver.AppSettings;

public class NewsFeederApplication extends Application {

  private static Context context;

  @Override
  public void onCreate() {
    super.onCreate();
    context = getApplicationContext();
    AppSettings.init(context);
  }
}
