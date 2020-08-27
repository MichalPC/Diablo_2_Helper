package com.example.diablo2runewords;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RuneWordParser {
  private ArrayList<RuneWord> runeWordList;

  public RuneWordParser(Context appContext, String jsonName) throws IOException {
    AssetManager assManager = appContext.getApplicationContext().getAssets();

    InputStream input = assManager.open(jsonName);
    InputStreamReader inputReader = new InputStreamReader(input);
    ArrayList<Rune> runeList = new RunesParser(appContext, "runes.json").getRuneLists();

    BufferedReader bufferedInput = new BufferedReader(inputReader);

    String temp;
    StringBuilder jsonInString = new StringBuilder();

    while ((temp = bufferedInput.readLine()) != null) {
      jsonInString.append(temp);
    }

    try {
      JSONObject runeWordsFile = new JSONObject(jsonInString.toString());
      JSONArray allWords = runeWordsFile.getJSONArray("rune_words");
      runeWordList = new ArrayList<>();

      //RuneWords
      for (int i = 0; i < allWords.length(); i++) {
        JSONObject curWords = allWords.getJSONObject(i);
        JSONArray jsonRuneList = curWords.getJSONArray("runes");
        JSONArray jsonClassList = curWords.getJSONArray("categories");
        JSONArray jsonStatsList = curWords.getJSONArray("stats");

        Rune[] runesArray = new Rune[jsonRuneList.length()];
        for (int j = 0; j < jsonRuneList.length(); j++) {
          runesArray[j] = runeList.get(jsonRuneList.getInt(j) - 1);
        }

        RuneWordCategory[] classArray = new RuneWordCategory[jsonClassList.length()];
        for (int j = 0; j < jsonClassList.length(); j++) {
          classArray[j] = RuneWordCategory.valueOf(jsonClassList.getString(j));
        }

        String[] statsArray = new String[jsonStatsList.length()];
        for (int j = 0; j < jsonStatsList.length(); j++) {
          statsArray[j] = jsonStatsList.getString(j);
        }

        int runeWordId = curWords.getInt("id");
        String runeWordName = curWords.getString("name");
        int charLevel = curWords.getInt("c_level");
        double version = curWords.getDouble("version");
        boolean isLadder = curWords.getBoolean("is_ladder");

        runeWordList.add( new RuneWord(runeWordId,
                                        runeWordName,
                                        charLevel,
                                        isLadder,
                                        runesArray,
                                        classArray,
                                        statsArray,
                                        version));
      }
      System.out.println("SUCCESS " + runeWordList.size());
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<RuneWord> getAllRuneWords() {
    return runeWordList;
  }
}
