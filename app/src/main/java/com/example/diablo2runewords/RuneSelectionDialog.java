package com.example.diablo2runewords;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class RuneSelectionDialog extends AppCompatDialogFragment {
  private RadioButton completeRuneWords;
  private RuneSelectionDialogListener listener;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      listener = (RuneSelectionDialogListener) getTargetFragment();
    } catch (ClassCastException e) {
      throw new ClassCastException("Calling Fragment must implement RuneSelectionDialogListener");
    }
  }

  public Dialog onCreateDialog(Bundle savedInstanceState) {
    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

    LayoutInflater inflater = getActivity().getLayoutInflater();
    View view = inflater.inflate(R.layout.rune_selection_dialog, null);

    builder.setView(view)
            .setTitle("Set Mode")
            .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {

              }
            })
            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                boolean isComplete = false;

                if (completeRuneWords.isChecked()) {
                  isComplete = true;
                }

                listener.setRuneWordSelection(isComplete);
              }
            });

    completeRuneWords = view.findViewById(R.id.complete_radio1);
    return builder.create();
  }

  public interface RuneSelectionDialogListener {
    void setRuneWordSelection(boolean showComplete);
  }
}
