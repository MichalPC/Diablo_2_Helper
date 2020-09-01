package com.example.diablo2runewords;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import com.example.diablo2runewords.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
                          implements NavigationView.OnNavigationItemSelectedListener {

  private ActivityMainBinding binding;
  private ArrayList<Rune> runeList;
  private ArrayList<RuneWord> runeWordList;
  private ArrayList<Integer> favRunes;
  private ArrayList<Integer> favRuneWords;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    View v = binding.getRoot();
    setContentView(v);

    variableInit();

    setSupportActionBar(binding.toolbar);
    Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

    binding.navView.setNavigationItemSelectedListener(this);

    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                                                              binding.mainActivityLayout,
                                                              binding.toolbar,
                                                              R.string.navigation_drawer_open,
                                                              R.string.navigation_drawer_close);
    binding.mainActivityLayout.addDrawerListener(toggle);
    toggle.syncState();

    if (savedInstanceState == null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
              new HomeFragment()).commit();
      binding.navView.setCheckedItem(R.id.nav_home);
    }
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
    switch (menuItem.getItemId()) {
      case R.id.nav_runes:
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(
          R.id.fragment_container,
          RuneFragment.newInstance(runeList),
          "RUNE_FRAGMENT").commit();
        break;
      case R.id.nav_home:
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(
          R.id.fragment_container, new HomeFragment(), "HOME_FRAGMENT").commit();
        break;
      case R.id.nav_rune_selection:
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(
          R.id.fragment_container,
          RuneSelectionFragment.newInstance(runeList, runeWordList, favRuneWords),
          "RUNE_SELECTION_FRAGMENT").commit();
        break;
      case R.id.nav_fav_rune_words:
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(
          R.id.fragment_container,
          RuneWordFragment.newInstance(runeWordList, favRuneWords),
          "FAV_RUNE_WORDS_FRAGMENT").commit();
        break;
      case R.id.nav_share:
        Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
        break;
      default:
        break;
    }
    binding.mainActivityLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private void variableInit() {
    SharedPreferences sharedPreferences = getSharedPreferences("USER", Activity.MODE_PRIVATE);
    favRunes = DataUtil.loadArrayList(sharedPreferences, "Runes");
    favRuneWords = DataUtil.loadArrayList(sharedPreferences, "Favourite Rune Words");
    runeList = loadRunesFromJson(runesFileName);
    runeWordList = loadRuneWordsFromJson(runeWordsFileName);
  }

  private ArrayList<Rune> loadRunesFromJson(String jsonName) {
    ArrayList<Rune> tempRuneList = new ArrayList<>();
    try {
      RunesParser p = new RunesParser(this, jsonName);
      tempRuneList = p.getRuneLists();

    } catch (IOException e) {
      System.out.println("Could not find file '" + jsonName + "'");
    }
    return tempRuneList;
  }

  private ArrayList<RuneWord> loadRuneWordsFromJson(String jsonName) {
    ArrayList<RuneWord> tempRuneWordList = new ArrayList<>();

    try {
      RuneWordParser p = new RuneWordParser(this, jsonName);
      tempRuneWordList = p.getAllRuneWords();
    } catch (IOException e) {
      System.out.println("Could not find file '" + jsonName + "'");
    }
    return tempRuneWordList;
  }

  @Override
  public void onBackPressed() {
    FragmentManager fm = getFragmentManager();

    if (fm.getBackStackEntryCount() > 0) {
      fm.popBackStack();
    } else {
      super.onBackPressed();
    }
  }
}
