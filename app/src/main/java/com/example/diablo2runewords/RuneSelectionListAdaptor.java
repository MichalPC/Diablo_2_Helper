package com.example.diablo2runewords;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class RuneSelectionListAdaptor<Exercise>
  extends RecyclerView.Adapter<RuneSelectionListAdaptor.ViewHolder> {

  private ArrayList<Integer> availableRunes;
  private ArrayList<Rune> runeList;

  public RuneSelectionListAdaptor(ArrayList<Rune> runeList, Context context) {
    this.runeList = runeList;
    this.availableRunes = new ArrayList<>();

    for (int i = 0; i < 33; i++) {
      availableRunes.add(0);
    }
  }

  @NonNull
  @Override
  public RuneSelectionListAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.rune_selection_item, parent, false);

    return new RuneSelectionListAdaptor.ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull RuneSelectionListAdaptor.ViewHolder holder, final int position) {
    Rune curWord = runeList.get(position);

    holder.number.setText(availableRunes.get(position));

    holder.name.setText(curWord.getRuneName());
  }

  @Override
  public int getItemCount() {
    return runeList.size();
  }

  public ArrayList<Integer> getAvailableRunes() {
    return availableRunes;
  }

  /**
   * Changes contents of availableRunes to contents of newAvailableRunes
   *
   * @param newAvailableRunes List of available runes
   */
  public void setAvailableRunes(ArrayList<Integer> newAvailableRunes) {
    if (newAvailableRunes != null) {
      this.availableRunes.clear();
      this.availableRunes.addAll(newAvailableRunes);
    }
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    public Button plus;
    public Button minus;
    public TextView number;
    public TextView name;

    public ViewHolder(@NonNull final View itemView) {
      super(itemView);

      plus = itemView.findViewById(R.id.plus);
      minus = itemView.findViewById(R.id.minus);
      number = itemView.findViewById(R.id.number);
      name = itemView.findViewById(R.id.name);

      minus.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          int temp = Integer.parseInt(number.getText().toString());

          if (temp > 0) {
            temp--;

            availableRunes.set(getAdapterPosition(), temp);
            number.setText(String.format("%d", temp));
          }
        }
      });

      plus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int temp = Integer.parseInt(number.getText().toString());

          temp++;

          availableRunes.set(getAdapterPosition(), temp);
          number.setText(String.format("%d", temp));
        }
      });
    }
  }


}
