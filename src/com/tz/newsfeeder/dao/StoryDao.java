package com.tz.newsfeeder.dao;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.tz.newsfeeder.util.AppUtil;

public class StoryDao {

  final static String SERVICE_URL =
      "http://api.nytimes.com/svc/topstories/v1/home.json?api-key=201f2d9fc5a0f555ac4ad2946c51950d:7:71840120";
  final static int CNT_FOR_PAGE = 5;
  public static String last_updated = null;
  public static int currPage = 0;
  public static boolean working = false;

  private static SQLiteDatabase db;
  private MySQLiteHelper dbHelper;

  public StoryDao(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    db = dbHelper.getWritableDatabase();
    db.execSQL(MySQLiteHelper.SQL_DELETE_TABLE_NAME);
    db.execSQL(MySQLiteHelper.SQL_CREATE_TABLE_NAME);
  }

  public void drop() throws SQLException {
    // db.execSQL(MySQLiteHelper.SQL_DELETE_TABLE_NAME);
    // new File("/data/data/com.tz.newsfeeder/databases/datafile").exists();
    // new File("/data/data/com.tz.newsfeeder/databases/datafile").delete();
  }

  public void close() {
    dbHelper.close();
  }

  public void update(ContentValues contentValues) {
    try {
      String selection = " id = ?";
      String[] selectionArgs = { String.valueOf(contentValues.getAsString("id")) };
      db.update(MySQLiteHelper.TABLE_NAME, contentValues, selection, selectionArgs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void insert(ContentValues contentValues) {
    try {
      db.insert(MySQLiteHelper.TABLE_NAME, null, contentValues);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public JSONArray write(String last_updated, JSONArray array) {
    JSONArray arry = new JSONArray();
    try {
      ContentValues values = new ContentValues();
      for (int i = 0; i < array.length(); i++) {
        JSONObject json = array.getJSONObject(i);
        JSONObject item = new JSONObject();
        json.put("last_updated", last_updated);
        json.put("indx", Integer.toString(i));
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
          String key = (String) keys.next();
          if (MySQLiteHelper.SQL_CREATE_TABLE_NAME.indexOf(key + " ") > -1) {
            String val = null;
            Object obj = json.get(key);
            if (obj instanceof String) {
              val = json.getString(key);
            } else if (obj instanceof JSONArray) {
              if (key.equals("multimedia")) {
                val = ((JSONObject) json.getJSONArray(key).get(0)).getString("url");
                String normal = ((JSONObject) json.getJSONArray(key).get(2)).getString("url");
                values.put("normal", normal);
                item.put("normal", normal);
              } else {
                val = json.getJSONArray(key).getString(0);
              }
            }
            values.put(key, val);
            item.put(key, val);
          }
        }
        arry.put(item);
        if (checkIfExist(values)) {
          update(values);
        } else {
          insert(values);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return arry;
  }

  public boolean checkIfExist(ContentValues contentValues) {
    boolean alreadyExist = false;
    try {
      String indx = contentValues.getAsString("indx");
      Cursor cursor =
          db.query(MySQLiteHelper.TABLE_NAME, new String[] { "indx" }, " indx = ? ",
              new String[] { indx }, null, null, null);
      if (cursor.getCount() > 0) {
        alreadyExist = true;
      } else {
        alreadyExist = false;
      }
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return alreadyExist;
  }

  public JSONArray getFeeds(String last_updated) {
    JSONArray items = new JSONArray();
    Cursor cursor = null;
    try {
      cursor =
          db.rawQuery(
              "SELECT _id, last_updated, indx, title, url, multimedia, abstract, per_facet, org_facet, published_date FROM "
                  + MySQLiteHelper.TABLE_NAME + " WHERE last_updated = ?",
              new String[] { last_updated });

      while (cursor.moveToNext()) {
        JSONObject result = new JSONObject();
        result.put("_id", cursor.getString(0));
        result.put("last_updated", cursor.getString(1));
        result.put("indx", cursor.getString(2));
        result.put("title", cursor.getString(3));
        result.put("url", cursor.getString(4));
        result.put("multimedia", cursor.getString(5));
        result.put("abstract", cursor.getString(6));
        result.put("per_facet", cursor.getString(7));
        result.put("org_facet", cursor.getString(8));
        result.put("published_date", cursor.getString(9));
        items.put(result);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
    return items;
  }

  public JSONArray getStoryList() {
    currPage++;
    return getStoryList(last_updated, currPage);
  }

  public JSONArray getStoryList(String alast_updated, int page) {
    try {
      last_updated = alast_updated;
      JSONArray rslt = new JSONArray();
      if (!working) {
        working = true;
        currPage = page;
        int from = 0;
        int to = CNT_FOR_PAGE;
        if (page > 0) {
          from = CNT_FOR_PAGE * page;
          to = from + CNT_FOR_PAGE - 1;
        }
        JSONArray arry = getFeeds(last_updated);
        if (arry == null || arry.length() == 0) {
          arry = AppUtil.getjson(SERVICE_URL).getJSONArray("results");
          arry = write(last_updated, arry);
        }
        for (int i = 0; i < arry.length(); i++) {
          if (from <= i && i <= to) {
            rslt.put(arry.get(i));
          }
        }
        working = false;
      }
      return rslt;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
  }
}
