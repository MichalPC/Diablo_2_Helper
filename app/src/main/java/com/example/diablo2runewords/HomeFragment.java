package com.example.diablo2runewords;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.diablo2runewords.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
  private HomeFragmentBinding binding;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    binding = HomeFragmentBinding.inflate(inflater, container, false);

    return binding.getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();

    binding = null;
  }
}
