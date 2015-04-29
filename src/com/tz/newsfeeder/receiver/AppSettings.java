package com.tz.newsfeeder.receiver;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

  /**
   */
  public final class PrefsKeys {

    public static final String PREFS = "com.tz.intro";

    private PrefsKeys() {
    }
  }

  private static Context context;
  private static SharedPreferences prefs;

  public static void init(Context pContext) {
    context = pContext;
    prefs = context.getSharedPreferences(PrefsKeys.PREFS, Context.MODE_PRIVATE);
  }

  public static void clear() {
    prefs.edit().clear().commit();
  }

}