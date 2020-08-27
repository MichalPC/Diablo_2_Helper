package com.example.diablo2runewords;

import android.os.Parcel;
import android.os.Parcelable;

public class RuneWord implements Comparable<RuneWord>, Parcelable {
  private int id;
  private String runeWordName;
  private int cLevel;
  private Rune[] reqRunes;
  private RuneWordCategory[] runeWordCat;
  private String[] stats;
  private double version;
  private boolean isLadder;

  public RuneWord(int id,
                  String runeWordName,
                  int cLevel, boolean isLadder,
                  Rune[] reqRunes,
                  RuneWordCategory[] runeWordCat,
                  String[] stats,
                  double version) {

    this.id = id;
    this.runeWordName = runeWordName;
    this.cLevel = cLevel;
    this.isLadder = isLadder;
    this.reqRunes = reqRunes;
    this.runeWordCat = runeWordCat;
    this.stats = stats;
    this.version = version;
  }

  protected RuneWord(Parcel in) {
    id = in.readInt();
    runeWordName = in.readString();
    cLevel = in.readInt();
    stats = in.createStringArray();
    version = in.readDouble();
    isLadder = in.readByte() != 0;
  }

  public static final Creator<RuneWord> CREATOR = new Creator<RuneWord>() {
    @Override
    public RuneWord createFromParcel(Parcel in) {
      return new RuneWord(in);
    }

    @Override
    public RuneWord[] newArray(int size) {
      return new RuneWord[size];
    }
  };

  public int getId() {
    return id;
  }

  public String getRuneWordName() {
    return runeWordName;
  }

  public int getCharacterLevel() {
    return cLevel;
  }

  public Rune[] getRequiredRunes() {
    return reqRunes;
  }

  public RuneWordCategory[] getRuneWordCategories() {
    return runeWordCat;
  }

  public String[] getStats() {
    return stats;
  }

  public double getVersion() {
    return version;
  }

  public boolean isLadder() {
    return isLadder;
  }

  @Override
  public int compareTo(RuneWord o) {
    return runeWordName.compareTo(o.getRuneWordName());
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(id);
    dest.writeString(runeWordName);
    dest.writeInt(cLevel);
    dest.writeStringArray(stats);
    dest.writeDouble(version);
    dest.writeByte((byte) (isLadder ? 1 : 0));
  }
}
