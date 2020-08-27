package com.example.diablo2runewords;

import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class DataUtil {
  public static void saveArrayList(SharedPreferences prefFile,
                                   String key,
                                   ArrayList<Integer> value) {
    Gson gson = new Gson();

    String arrayToJson = gson.toJson(value);

    SharedPreferences.Editor editor = prefFile.edit();
    editor.putString(key, arrayToJson);

    editor.apply();
  }

  public static ArrayList<Integer> loadArrayList(SharedPreferences prefFile, String key) {
    Gson gson = new Gson();
    String jsonToArray = prefFile.getString(key, "");
    ArrayList<Integer> favouriteList;

    if (!jsonToArray.isEmpty()) {
      Type type = new TypeToken<ArrayList<Integer>>() {
      }.getType();
      favouriteList = gson.fromJson(jsonToArray, type);

    } else {
      favouriteList = null;
    }

    return favouriteList;
  }
}
