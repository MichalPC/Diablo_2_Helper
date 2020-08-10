package com.example.diablo2runewords;

import android.os.Parcel;
import android.os.Parcelable;

public class Rune implements Parcelable {
  private int runeID;
  private String runeName;
  private RuneStats runeStats;

  /**
   * Constructor for Rune.
   *
   * @param runeID    The ID of the rune.
   * @param runeName  The name of the rune.
   * @param runeStats The stats the rune applies.
   */
  public Rune(int runeID, String runeName, RuneStats runeStats) {
    this.runeID = runeID;
    this.runeName = runeName;
    this.runeStats = runeStats;
  }

  protected Rune(Parcel in) {
    runeID = in.readInt();
    runeName = in.readString();
  }

  public static final Creator<Rune> CREATOR = new Creator<Rune>() {
    @Override
    public Rune createFromParcel(Parcel in) {
      return new Rune(in);
    }

    @Override
    public Rune[] newArray(int size) {
      return new Rune[size];
    }
  };

  /**
   * Returns the ID of the rune.
   *
   * @return The ID of the rune.
   */
  public int getRuneID() {
    return this.runeID;
  }

  /**
   * Returns the name of the rune.
   *
   * @return The name of the rune.
   */
  public String getRuneName() {
    return this.runeName;
  }

  /**
   * Returns the stats the rune applies.
   *
   * @return The stats the rune applies.
   */
  public RuneStats getRuneStats() {
    return this.runeStats;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(runeID);
    dest.writeString(runeName);
    dest.writeParcelable(runeStats, 0);
  }
}
