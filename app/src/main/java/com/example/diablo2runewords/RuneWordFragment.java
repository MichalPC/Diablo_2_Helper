package com.example.diablo2runewords;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.diablo2runewords.databinding.RuneWordsFragmentBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.zip.Inflater;

public class RuneWordFragment extends Fragment {
  private String favRuneWordsKey;
  private RuneWordsFragmentBinding binding;
  private boolean getComplete;
  private boolean favOnly;
  private ArrayList<Integer> availableRunes;
  private ArrayList<Integer> favRuneWords;
  private ArrayList<RuneWord> runeWordList;
  private ArrayList<RuneWord> currentSelectedRuneWords;
  private ArrayList<RuneWord> originalSelectedRuneWords;
  private SharedPreferences sharedPreferences;
  private RuneWordAdaptor runeWordAdaptor;

  public static RuneWordFragment newInstance(ArrayList<RuneWord> runeWordList,
                                             ArrayList<Integer> availableRunes,
                                             boolean getComplete) {
    Bundle args = new Bundle(4);

    RuneWordFragment fragment = new RuneWordFragment();
    args.putParcelableArrayList("runeWords", runeWordList);
    args.putIntegerArrayList("availableRunes", availableRunes);
    args.putBoolean("getComplete", getComplete);
    args.putBoolean("favOnly", false);
    fragment.setArguments(args);
    return fragment;
  }

  public static RuneWordFragment newInstance(ArrayList<RuneWord> runeWordList,
                                             ArrayList<Integer> favRuneWords) {
    Bundle args = new Bundle(3);

    RuneWordFragment fragment = new RuneWordFragment();
    args.putParcelableArrayList("runeWords", runeWordList);
    args.putIntegerArrayList("favRuneWords", favRuneWords);
    args.putBoolean("favOnly", true);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);

    runeWordList = getArguments().getParcelableArrayList("runeWords");
    availableRunes = getArguments().getIntegerArrayList("availableRunes");
    getComplete = getArguments().getBoolean("getComplete");
    favRuneWords = getArguments().getIntegerArrayList("favRuneWords");
    favOnly = getArguments().getBoolean("favOnly");
    favRuneWordsKey = "Favourite Rune Words";
    sharedPreferences = getActivity().getSharedPreferences("USER", Activity.MODE_PRIVATE);
  }

  public View onCreateView(LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    binding = RuneWordsFragmentBinding.inflate(inflater, container, false);
    View v = binding.getRoot();

    setAllListeners();

    //Load data into socketNumberSpinner
    ArrayAdapter<String> socketNumberSpinnerAdaptor = new ArrayAdapter<>(this.getActivity(),
            android.R.layout.simple_list_item_1,
            getResources().getStringArray(R.array.number_of_sockets));
    socketNumberSpinnerAdaptor.setDropDownViewResource(
            R.layout.support_simple_spinner_dropdown_item);
    binding.socketNumberSpinner.setAdapter(socketNumberSpinnerAdaptor);

    //Load data into itemTypeSpinner
    ArrayAdapter<RuneWordCategory> itemTypeSpinnerAdaptor = new ArrayAdapter<>(this.getActivity(),
            android.R.layout.simple_spinner_item, RuneWordCategory.values());
    itemTypeSpinnerAdaptor.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
    binding.itemTypeSpinner.setAdapter(itemTypeSpinnerAdaptor);

    if (savedInstanceState == null) {
      binding.runeWordsList.setHasFixedSize(true);
      binding.runeWordsList.setLayoutManager(new LinearLayoutManager(v.getContext()));

      if (favOnly) {
        currentSelectedRuneWords = filterByFavourite(runeWordList);
      } else {
        if (availableRunes != null) {
          if (getComplete) {
            currentSelectedRuneWords = getCompleteRuneWords();
          } else {
            currentSelectedRuneWords = getPartialRunewords();
          }
          if (currentSelectedRuneWords.size() == 0) {
            currentSelectedRuneWords = new ArrayList<>(runeWordList);
          }
        } else {
          currentSelectedRuneWords = new ArrayList<>(runeWordList);
        }
      }
      originalSelectedRuneWords = new ArrayList<>(currentSelectedRuneWords);

      runeWordAdaptor = new RuneWordAdaptor(currentSelectedRuneWords, v.getContext());
      runeWordAdaptor.setFavouriteRuneWords(
              DataUtil.loadArrayList(sharedPreferences, favRuneWordsKey));
    }
    binding.runeWordsList.setAdapter(runeWordAdaptor);
    return v;
  }

  private ArrayList<RuneWord> getCompleteRuneWords() {
    ArrayList<RuneWord> temp = new ArrayList<>();
    boolean isAvailable = true;
    ArrayList<Integer> tempAvailableRunes = new ArrayList<>();

    for (RuneWord runeWord : runeWordList) {
      tempAvailableRunes.addAll(availableRunes);
      for (Rune rune : runeWord.getRequiredRunes()) {
        if (tempAvailableRunes.get(rune.getRuneID() - 1) > 0) {
          tempAvailableRunes.set(rune.getRuneID() - 1,
                  tempAvailableRunes.get(rune.getRuneID() - 1) - 1);
        } else {
          isAvailable = false;
          break;
        }
      }
      if (isAvailable) {
        temp.add(runeWord);
      }
      isAvailable = true;
    }
    return temp;
  }

  private ArrayList<RuneWord> getPartialRunewords() {
    ArrayList<RuneWord> tempList = new ArrayList<>();
    boolean isAvailable = false;

    for (RuneWord rw : runeWordList) {
      for (Rune r : rw.getRequiredRunes()) {
        if (availableRunes.get(r.getRuneID() - 1) > 0) {
          isAvailable = true;
          break;
        }
      }
      if (isAvailable) {
        tempList.add(rw);
      }
      isAvailable = false;
    }
    return tempList;
  }

  private ArrayList<RuneWord> filterBySockets(ArrayList<RuneWord> runeWordList, int socketNumber) {
    ArrayList<RuneWord> tempList = new ArrayList<>();

    for (RuneWord rw : runeWordList) {
      if (rw.getRequiredRunes().length == socketNumber) {
        tempList.add(rw);
      }
    }
    return tempList;
  }

  private ArrayList<RuneWord> filterByItemType(ArrayList<RuneWord> runeWordList,
                                               RuneWordCategory itemType) {

    ArrayList<RuneWord> tempList = new ArrayList<>();

    for (RuneWord rw : runeWordList) {
      for (RuneWordCategory category : rw.getRuneWordCategories()) {
        if (category == itemType) {
          tempList.add(rw);
        }
      }
    }
    return tempList;
  }

  private ArrayList<RuneWord> filterByCharacterLevel(ArrayList<RuneWord> runeWordList, int minCharLevel, int maxCharLevel) {
    ArrayList<RuneWord> tempList = new ArrayList<>();

    if (minCharLevel > maxCharLevel) {
      int temp = minCharLevel;
      minCharLevel = maxCharLevel;
      maxCharLevel = temp;
    }
    for (RuneWord rw : runeWordList) {
      if (minCharLevel <= rw.getCharacterLevel() && maxCharLevel >= rw.getCharacterLevel()) {
        tempList.add(rw);
      }
    }
    return tempList;
  }

  private ArrayList<RuneWord> filterByFavourite(ArrayList<RuneWord> runeWordList) {
    ArrayList<RuneWord> tempList = new ArrayList<>();

    for (RuneWord rw : runeWordList) {
      if (favRuneWords == null) {
        return tempList;
      }
      if (favRuneWords.contains(rw.getId())) {
        tempList.add(rw);
      }
    }

    return tempList;
  }

  private void applyAllFilters() {
    ArrayList<RuneWord> tempOriginalList = new ArrayList<>(originalSelectedRuneWords);
    ArrayList<RuneWord> tempList = new ArrayList<>();

    if (binding.itemTypeFilterToggle.isChecked()) {
      tempList.addAll(filterByItemType(tempOriginalList, (RuneWordCategory) binding.itemTypeSpinner.getSelectedItem()));
      tempOriginalList.clear();
      tempOriginalList.addAll(tempList);
      tempList.clear();
    }
    if (binding.socketFilterToggle.isChecked()) {
      tempList.addAll(filterBySockets(tempOriginalList, Integer.parseInt((String) binding.socketNumberSpinner.getSelectedItem())));
      tempOriginalList.clear();
      tempOriginalList.addAll(tempList);
      tempList.clear();
    }
    currentSelectedRuneWords.clear();
    currentSelectedRuneWords.addAll(tempOriginalList);
    runeWordAdaptor.notifyDataSetChanged();
  }

  private void setAllListeners() {
    binding.socketFilterToggle.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        applyAllFilters();
      }
    });
    binding.itemTypeFilterToggle.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        applyAllFilters();
      }
    });
    binding.sortButton1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Collections.sort(currentSelectedRuneWords, new Comparator<RuneWord>() {
          @Override
          public int compare(RuneWord o1, RuneWord o2) {
            return o1.getRequiredRunes().length - o2.getRequiredRunes().length;
          }
        });
        ExpansionAnimation.collapse(binding.sortArea,
          Integer.parseInt(AnimationSpeed.DROPDOWN_OPEN.toString()));

        runeWordAdaptor.notifyDataSetChanged();
      }
    });
    binding.sortButton2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Collections.sort(currentSelectedRuneWords, new Comparator<RuneWord>() {
          @Override
          public int compare(RuneWord o1, RuneWord o2) {
            return o1.getRuneWordName().compareTo(o2.getRuneWordName());
          }
        });
        ExpansionAnimation.collapse(binding.sortArea,
          Integer.parseInt(AnimationSpeed.DROPDOWN_CLOSE.toString()));

        runeWordAdaptor.notifyDataSetChanged();
      }
    });
    binding.sortButtonLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (binding.sortArea.getVisibility() == View.GONE) {
          binding.filterArea.setVisibility(View.GONE);
          ExpansionAnimation.expand(binding.sortArea,
            Integer.parseInt(AnimationSpeed.DROPDOWN_OPEN.toString()));
        } else {
          ExpansionAnimation.collapse(binding.sortArea,
            Integer.parseInt(AnimationSpeed.DROPDOWN_CLOSE.toString()));
        }

        runeWordAdaptor.notifyDataSetChanged();
      }
    });
    binding.filterButtonLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (binding.filterArea.getVisibility() == View.GONE) {
          binding.sortArea.setVisibility(View.GONE);
          ExpansionAnimation.expand(binding.filterArea,
            Integer.parseInt(AnimationSpeed.DROPDOWN_OPEN.toString()));
        } else {
          ExpansionAnimation.collapse(binding.filterArea,
            Integer.parseInt(AnimationSpeed.DROPDOWN_CLOSE.toString()));
        }

        runeWordAdaptor.notifyDataSetChanged();
      }
    });
    binding.socketNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (binding.socketFilterToggle.isChecked()) {
          applyAllFilters();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });

    binding.itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (binding.itemTypeFilterToggle.isChecked()) {
          applyAllFilters();
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  @Override
  public void onStop() {
    super.onStop();

    DataUtil.saveArrayList(sharedPreferences,
      favRuneWordsKey,
      runeWordAdaptor.getFavouriteRuneWords());
  }

  @Override
  public void onResume() {
    super.onResume();

    runeWordAdaptor.setFavouriteRuneWords(DataUtil.loadArrayList(sharedPreferences, favRuneWordsKey));
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    binding = null;
  }

  @Override
  public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    inflater.inflate(R.menu.search_bar, menu);
    MenuItem mSearchMenuItem = menu.findItem(R.id.app_bar_search);
    SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        runeWordAdaptor.getFilter().filter(newText);
        return false;
      }
    });
  }
}
