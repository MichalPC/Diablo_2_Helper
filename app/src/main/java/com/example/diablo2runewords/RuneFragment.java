package com.example.diablo2runewords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.diablo2runewords.databinding.RuneFragmentBinding;
import java.util.ArrayList;

public class RuneFragment extends Fragment {
  private RuneFragmentBinding binding;
  private RuneListAdaptor runeAdaptor;
  private ArrayList<Rune> runeList;

  public static RuneFragment newInstance(ArrayList<Rune> runeList) {
    Bundle args = new Bundle(1);

    RuneFragment fragment = new RuneFragment();
    args.putParcelableArrayList("runeList", runeList);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
    assert getArguments() != null;
    this.runeList = getArguments().getParcelableArrayList("runeList");
  }

  @Override
  @Nullable
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    binding = RuneFragmentBinding.inflate(inflater, container, false);
    View v = binding.getRoot();

    if (savedInstanceState == null) {
      binding.runeList.setHasFixedSize(true);
      binding.runeList.setLayoutManager(new LinearLayoutManager(v.getContext()));
      runeAdaptor = new RuneListAdaptor(runeList, v.getContext());
    }

    binding.runeList.setAdapter(runeAdaptor);
    return v;
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
    MenuItem searchMenuItem = menu.findItem(R.id.app_bar_search);
    SearchView searchView = (SearchView) searchMenuItem.getActionView();

    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
      @Override
      public boolean onQueryTextSubmit(String query) {
        return false;
      }

      @Override
      public boolean onQueryTextChange(String newText) {
        runeAdaptor.getFilter().filter(newText);
        return false;
      }
    });
  }
}
