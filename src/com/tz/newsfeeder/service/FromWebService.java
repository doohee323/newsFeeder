package com.tz.newsfeeder.service;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tz.newsfeeder.dao.StoryDao;

public class FromWebService {

  private Context mContext;

  FromWebService(Context c) {
    mContext = c;
  }

  @JavascriptInterface
  public String viewStory(String input) {
    try {
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  @JavascriptInterface
  public String getStoryList(String params) {
    try {
      JSONObject obj = new JSONObject(params);
      int page = obj.getInt("page");
      String last_updated = obj.getString("last_updated");
      return new StoryDao(mContext).getStoryList(last_updated, page).toString();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return null;
  }

}