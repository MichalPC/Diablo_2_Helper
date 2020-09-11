package com.example.diablo2runewords;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RuneListAdaptor extends RecyclerView.Adapter<RuneListAdaptor.ViewHolder>
                             implements Filterable {

  private ArrayList<Rune> runeList;
  private ArrayList<Rune> runeListFull;
  private Context context;

  public RuneListAdaptor(ArrayList<Rune> runeList, Context context) {
    this.runeList = new ArrayList<>(runeList);
    this.runeListFull = new ArrayList<>(runeList);
    this.context = context;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.rune_item, parent, false);

    return new ViewHolder(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Rune curRune = runeList.get(position);

    holder.runeWordNameView.setText(curRune.getRuneName());
    holder.runeWordStatsView.setText(curRune.getRuneStats().toString());
  }

  @Override
  public int getItemCount() {
    return runeList.size();
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  public Filter filter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      ArrayList<Rune> filteredRunes = new ArrayList<>();

      if (constraint == null || constraint.length() == 0) {
        filteredRunes.addAll(runeListFull);
      } else {
        String filteredRuneName = constraint.toString().toLowerCase().trim();
        for (Rune rune : runeListFull) {
          if (rune.getRuneName().toLowerCase().trim().contains(filteredRuneName)) {
            filteredRunes.add(rune);
          }
        }
      }
      FilterResults results = new FilterResults();
      results.values = filteredRunes;

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      runeList.clear();
      runeList.addAll((ArrayList) results.values);
      notifyDataSetChanged();
    }
  };

  public static class ViewHolder extends RecyclerView.ViewHolder {

    public TextView runeWordNameView;
    public TextView runeWordStatsView;
    public ImageView runeStatsBtn;
    public ConstraintLayout runeStatsSection;
    public ToggleButton runeFavouriteToggle;
    public ConstraintLayout runeLayout;
    Drawable greyStar = ResourcesCompat.getDrawable(itemView.getResources(),
                                                    R.drawable.star_grey,
                                                    null);
    Drawable goldStar = ResourcesCompat.getDrawable(itemView.getResources(),
                                                    R.drawable.star_gold,
                                                    null);

    public ViewHolder(@NonNull final View itemView) {
      super(itemView);

      runeWordNameView = itemView.findViewById(R.id.rune_word_name);
      runeWordStatsView = itemView.findViewById(R.id.rune_stats);
      runeStatsBtn = itemView.findViewById(R.id.rune_stats_section_button);
      runeStatsSection = itemView.findViewById(R.id.rune_stats_section);
      runeFavouriteToggle = itemView.findViewById(R.id.favourite_rune_toggle);
      runeLayout = itemView.findViewById(R.id.rune_layout);

      runeStatsSection.setVisibility(View.GONE);
      runeFavouriteToggle.setChecked(false);

      runeFavouriteToggle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!runeFavouriteToggle.isChecked()) {
            runeFavouriteToggle.setBackgroundDrawable(greyStar);

            Toast.makeText(itemView.getContext(),
                          "Removed from Favourites",
                          Toast.LENGTH_SHORT).show();
          } else {
            runeFavouriteToggle.setBackgroundDrawable(goldStar);

            Toast.makeText(itemView.getContext(),
                          "Added to Favourites",
                          Toast.LENGTH_SHORT).show();
          }
        }
      });

      runeLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (runeStatsSection.getVisibility() == View.GONE) {
            ExpansionAnimation.expand(runeStatsSection,
                                      Integer.parseInt(AnimationSpeed.DROPDOWN_OPEN.toString()));

            RotateAnimation ra = new RotateAnimation(0, 360,
                    RotateAnimation.RELATIVE_TO_SELF, .5f,
                    RotateAnimation.RELATIVE_TO_SELF, .5f);

            ra.setDuration(500);
            runeStatsBtn.setAnimation(ra);
          } else {
            ExpansionAnimation.collapse(runeStatsSection,
                                        Integer.parseInt(AnimationSpeed.DROPDOWN_CLOSE.toString()));

            RotateAnimation ra = new RotateAnimation(360, 0,
                    RotateAnimation.RELATIVE_TO_SELF, .5f,
                    RotateAnimation.RELATIVE_TO_SELF, .5f);

            ra.setDuration(500);
            runeStatsBtn.setAnimation(ra);
          }
        }
      });
    }
  }
}
