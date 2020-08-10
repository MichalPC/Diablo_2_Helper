package com.example.diablo2runewords;

import android.content.Context;
import android.content.res.AssetManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RunesParser {
  private JSONObject runesFile;
  private ArrayList<Rune> runeList;

  public RunesParser(Context appContext, String jsonName) throws IOException {
    AssetManager assManager = appContext.getApplicationContext().getAssets();

    InputStream input = assManager.open(jsonName);
    InputStreamReader inputReader = new InputStreamReader(input);

    BufferedReader bufferedInput = new BufferedReader(inputReader);

    String temp;
    StringBuilder jsonInString = new StringBuilder();

    while ((temp = bufferedInput.readLine()) != null) {
      jsonInString.append(temp);
    }

    try {
      JSONArray allRunes = runesFile.getJSONArray("runes");
      runesFile = new JSONObject(jsonInString.toString());
      runeList = new ArrayList<>();

      //Rune Array
      for (int i = 0; i < allRunes.length(); i++) {
        JSONObject curRune = allRunes.getJSONObject(i);

        int runeID = curRune.getInt("id");
        String runeName = curRune.getString("name");

        JSONArray allRuneStats = curRune.getJSONArray("rune_stats");

        RuneStats runeStats = null;

        String[][] stat = new String[allRuneStats.length()][];

        for (int k = 0; k < allRuneStats.length(); k++) {
          JSONObject curRuneStats = allRuneStats.getJSONObject(k);
          JSONArray curStat = curRuneStats.getJSONArray("stats");

          stat[k] = new String[curStat.length()];

          for (int l = 0; l < curStat.length(); l++) {
            stat[k][l] = curStat.getString(l);
          }

          if (k == 3) {
            runeStats = new RuneStats(stat[0], stat[1], stat[2]);
          } else {
            runeStats = new RuneStats(stat[0], stat[1]);
          }
        }
        runeList.add(new Rune(runeID, runeName, runeStats));
      }
    } catch (JSONException ex) {
      ex.printStackTrace();
    }
  }

  public ArrayList<Rune> getRuneLists() {
    return runeList;
  }
}
