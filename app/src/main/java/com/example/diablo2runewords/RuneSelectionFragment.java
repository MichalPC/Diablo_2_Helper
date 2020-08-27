package com.example.diablo2runewords;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.diablo2runewords.databinding.RuneSelectionFragmentBinding;
import java.util.ArrayList;
import java.util.Collections;

public class RuneSelectionFragment extends Fragment
                                    implements RuneSelectionDialog.RuneSelectionDialogListener {

  private String runeStashKey;
  private SharedPreferences sharedPreferences;
  private ArrayList<Rune> runeList;
  private ArrayList<RuneWord> runeWordList;
  private ArrayList<Integer> favRuneWords;
  private RuneSelectionFragmentBinding binding;
  private RuneSelectionListAdaptor listAdaptor;

  public static RuneSelectionFragment newInstance(ArrayList<Rune> runeList,
                                                  ArrayList<RuneWord> runeWordList,
                                                  ArrayList<Integer> favRuneWords) {

    Bundle args = new Bundle();

    args.putParcelableArrayList("runeList", runeList);
    args.putParcelableArrayList("runeWordList", runeWordList);
    args.putIntegerArrayList("favRuneWords", favRuneWords);

    RuneSelectionFragment fragment = new RuneSelectionFragment();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void setRuneWordSelection(boolean showComplete) {
    RuneWordFragment tempFragment = RuneWordFragment
                                      .newInstance(runeWordList,
                                                    listAdaptor.getAvailableRunes(),
                                                    showComplete);

    FragmentTransaction changeFr = getFragmentManager()
                                      .beginTransaction()
                                      .addToBackStack(null)
                                      .replace(R.id.fragment_container,
                                                tempFragment,
                                            "RUNE_WORD_FRAGMENT");

    changeFr.commit();
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    runeStashKey = "Runes";
    runeList = getArguments().getParcelableArrayList("runeList");
    runeWordList = getArguments().getParcelableArrayList("runeWordList");
    favRuneWords = getArguments().getIntegerArrayList("favRuneWords");
    sharedPreferences = getActivity().getSharedPreferences("USER", Activity.MODE_PRIVATE);

    listAdaptor = new RuneSelectionListAdaptor(runeList, getActivity());
    listAdaptor.setAvailableRunes(DataUtil.loadArrayList(sharedPreferences, runeStashKey));
  }

  @Override
  @Nullable
  public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    binding = RuneSelectionFragmentBinding.inflate(inflater, container, false);
    View v = binding.getRoot();

    binding.selectionList.setLayoutManager(new LinearLayoutManager(v.getContext()));
    binding.selectionList.setAdapter(listAdaptor);

    binding.selectionButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openRuneSelectionDialog();
      }
    });

    binding.clearRunesButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listAdaptor.setAvailableRunes(new ArrayList<Integer>(Collections.nCopies(33, 0)));
        listAdaptor.notifyDataSetChanged();
      }
    });

    return v;
  }

  public void openRuneSelectionDialog() {
    RuneSelectionDialog runeSelectionDialog = new RuneSelectionDialog();
    runeSelectionDialog.setTargetFragment(this, 0);
    runeSelectionDialog.show(getFragmentManager(), "Rune Selection Dialog");
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  @Override
  public void onStop() {
    super.onStop();
    DataUtil.saveArrayList(sharedPreferences, runeStashKey, listAdaptor.getAvailableRunes());
  }

  @Override
  public void onResume() {
    super.onResume();
    listAdaptor.setAvailableRunes(DataUtil.loadArrayList(sharedPreferences, runeStashKey));
  }
}

