package com.tz.newsfeeder.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import org.json.JSONObject;
import org.json.JSONTokener;

public class AppUtil {

  public static JSONObject getjson(String url) {
    try {
      url = URLDecoder.decode(url, "UTF-8");
      HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setDoInput(true);
      urlConnection.connect();
      String response = streamToString(urlConnection.getInputStream());
      response = response.replaceAll("\'", "`");
      return (JSONObject) new JSONTokener(response).nextValue();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static String streamToString(InputStream is) throws IOException {
    String str = "";
    if (is != null) {
      StringBuilder sb = new StringBuilder();
      String line;
      try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
          sb.append(line);
        }
        reader.close();
      } finally {
        is.close();
      }
      str = sb.toString();
    }
    return str;
  }

}
