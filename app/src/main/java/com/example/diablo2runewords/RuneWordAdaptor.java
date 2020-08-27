package com.example.diablo2runewords;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class RuneWordAdaptor extends RecyclerView.Adapter<RuneWordAdaptor.ViewHolder>
  implements Filterable {

  private List<RuneWord> runeWordListFull;
  private List<RuneWord> runeWordList;
  private ArrayList<Integer> favouriteRuneWords;
  private Context context;
  Drawable greyStar;
  Drawable goldStar;

  RuneWordAdaptor(ArrayList<RuneWord> runeWordList, Context context) {
    this.context = context;
    this.runeWordList = new ArrayList<>(runeWordList);
    this.runeWordListFull = new ArrayList<>(runeWordList);

    greyStar = context.getResources().getDrawable(R.drawable.star_grey);
    goldStar = context.getResources().getDrawable(R.drawable.star_gold);

    favouriteRuneWords = new ArrayList<>();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext())
      .inflate(R.layout.rune_word_item, parent, false);

    return new ViewHolder(v);
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    StringBuilder str = new StringBuilder();
    RuneWord curWord = runeWordList.get(position);

    SpannableStringBuilder spannable = new SpannableStringBuilder();
    if (curWord.isLadder()) {
      holder.wordNameView.setText(curWord.getRuneWordName().concat("(Ladder Only)"));
    } else {
      holder.wordNameView.setText(curWord.getRuneWordName());
    }

    if (favouriteRuneWords.contains(curWord.getId())) {
      holder.favouriteToggle.setChecked(true);
      holder.favouriteToggle.setBackgroundDrawable(goldStar);
    } else {
      holder.favouriteToggle.setChecked(false);
      holder.favouriteToggle.setBackgroundDrawable(greyStar);
    }

    //Load Word Stats
    for (String stat : curWord.getStats()) {
      str.append(stat).append("\n");
    }

    holder.wordStatsView.setText(str);

    str.setLength(0);

    //Load Required Level
    str.append("Required Character Level: ");
    str.append(curWord.getCharacterLevel());

    holder.requiredLevel.setText(str);

    str.setLength(0);

    //Load Item Types
    str.append("Item Types: ");

    for (RuneWordCategory type : curWord.getRuneWordCategories()) {
      str.append(type.toString()).append(", ");
    }
    str.delete(str.length() - 2, str.length());

    holder.itemTypes.setText(str);

    //Load Required Runes
    spannable.append("Required Runes: ");
    int start = spannable.length() - 1;

    for (Rune rune : curWord.getRequiredRunes()) {
      spannable.append(rune.getRuneName().toUpperCase());
      spannable.setSpan(new StyleSpan(Typeface.BOLD),
                        start,
                        spannable.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      spannable.append(", ");
      start = spannable.length();
    }
    spannable.delete(spannable.length() - 2, spannable.length());

    holder.requiredRunes.setText(spannable, TextView.BufferType.SPANNABLE);
  }

  @Override
  public int getItemCount() {
    return runeWordList.size();
  }

  public ArrayList<Integer> getFavouriteRuneWords() {
    return favouriteRuneWords;
  }

  public void setFavouriteRuneWords(ArrayList<Integer> favouriteRuneWords) {
    if (favouriteRuneWords != null) {
      this.favouriteRuneWords = favouriteRuneWords;
    }
  }

  @Override
  public Filter getFilter() {
    return filter;
  }

  private Filter filter = new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      ArrayList<RuneWord> filteredRuneWords = new ArrayList<>();

      if (constraint == null || constraint.length() == 0) {
        filteredRuneWords.addAll(runeWordListFull);
      } else {
        String filteredPattern = constraint.toString().toLowerCase().trim();

        for (RuneWord runeWord : runeWordList) {
          if (runeWord.getRuneWordName().toLowerCase().contains(filteredPattern)) {
            filteredRuneWords.add(runeWord);
          }
        }
      }
      FilterResults results = new FilterResults();
      results.values = filteredRuneWords;

      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
      runeWordList.clear();
      runeWordList.addAll((List) results.values);
      notifyDataSetChanged();

    }
  };

  class ViewHolder extends RecyclerView.ViewHolder {
    TextView wordNameView;
    TextView wordStatsView;
    TextView requiredRunes;
    ImageView statsExpandBtn;
    ConstraintLayout statsSection;
    ToggleButton favouriteToggle;
    TextView requiredLevel;
    TextView itemTypes;
    ConstraintLayout runeWordLayout;

    ViewHolder(@NonNull final View itemView) {
      super(itemView);

      wordNameView = itemView.findViewById(R.id.word_name);
      wordStatsView = itemView.findViewById(R.id.word_stats);
      requiredRunes = itemView.findViewById(R.id.required_runes);
      statsExpandBtn = itemView.findViewById(R.id.stats_button);
      statsSection = itemView.findViewById(R.id.stats_section);
      favouriteToggle = itemView.findViewById(R.id.favourite_toggle);
      requiredLevel = itemView.findViewById(R.id.required_level);
      itemTypes = itemView.findViewById(R.id.item_types);
      runeWordLayout = itemView.findViewById(R.id.rune_word_layout);

      statsSection.setVisibility(View.GONE);
      favouriteToggle.setChecked(false);

      favouriteToggle.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!favouriteToggle.isChecked()) {
            favouriteToggle.setBackgroundDrawable(greyStar);

            int tempIndex = favouriteRuneWords
                              .indexOf(runeWordList.get((getAdapterPosition())).getId());

            System.out.println(tempIndex);

            favouriteRuneWords.remove(tempIndex);

            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show();
          } else {
            favouriteToggle.setBackgroundDrawable(goldStar);

            int tempId = runeWordList.get((getAdapterPosition())).getId();

            if (!favouriteRuneWords.contains(tempId)) {
              favouriteRuneWords.add(runeWordList.get((getAdapterPosition())).getId());
            }

            Toast.makeText(context, "Added to Favourites", Toast.LENGTH_SHORT).show();
          }
        }
      });

      runeWordLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (statsSection.getVisibility() == View.GONE) {
            ExpansionAnimation.expand(statsSection,
                                      Integer.parseInt(AnimationSpeed.BUTTON.toString()));

            RotateAnimation ra = new RotateAnimation(0, 360,
              RotateAnimation.RELATIVE_TO_SELF, .5f,
              RotateAnimation.RELATIVE_TO_SELF, .5f);

            ra.setDuration(500);
            statsExpandBtn.setAnimation(ra);
          } else {
            ExpansionAnimation.collapse(statsSection,
                                        Integer.parseInt(AnimationSpeed.BUTTON.toString()));

            RotateAnimation ra = new RotateAnimation(360, 0,
              RotateAnimation.RELATIVE_TO_SELF, .5f,
              RotateAnimation.RELATIVE_TO_SELF, .5f);

            ra.setDuration(500);
            statsExpandBtn.setAnimation(ra);
          }
        }
      });
    }
  }
}
