package com.example.diablo2runewords;

import android.os.Parcel;
import android.os.Parcelable;

public class RuneStats implements Parcelable {

  private String[] shieldStats;
  private String[] armourStats;
  private String[] weaponStats;

  /**
   * Constructor for RuneStats.
   *
   * @param weaponStats The stats the rune applies to weapons.
   * @param armourStats The stats the rune applies to a armors/helms.
   * @param shieldStats The stats the rune applies to a shields.
   */
  public RuneStats(String[] weaponStats, String[] armourStats, String[] shieldStats) {
    this.weaponStats = weaponStats;
    this.armourStats = armourStats;
    this.shieldStats = shieldStats;
  }

  /**
   * Constructor for RuneStats.
   *
   * @param weaponStats The stats the rune applies to weapons.
   * @param armourStats The stats the rune applies to a armors/helms/shields.
   */
  public RuneStats(String[] weaponStats, String[] armourStats) {
    this.weaponStats = weaponStats;
    this.armourStats = armourStats;
  }

  protected RuneStats(Parcel in) {
    shieldStats = in.createStringArray();
    armourStats = in.createStringArray();
    weaponStats = in.createStringArray();
  }

  public static final Creator<RuneStats> CREATOR = new Creator<RuneStats>() {
    @Override
    public RuneStats createFromParcel(Parcel in) {
      return new RuneStats(in);
    }

    @Override
    public RuneStats[] newArray(int size) {
      return new RuneStats[size];
    }
  };

  /**
   * Returns the weaponStats variable.
   *
   * @return The stats the rune applies to weapons.
   */
  public String[] getWeaponStats() {
    return weaponStats;
  }

  /**
   * Returns the armorStats variable.
   *
   * @return The stats the rune applies to a armors/helms.
   */
  public String[] getArmourStats() {
    return armourStats;
  }

  /**
   * Returns the shieldStats variable.
   *
   * @return The stats the rune applies to a shields.
   */
  public String[] getShieldStats() {
    return shieldStats;
  }

  @Override
  public String toString() {
    StringBuilder allStats = new StringBuilder();

    if (weaponStats != null) {
      allStats.append("Weapons\n");

      for (String weaponStat : weaponStats) {
        allStats.append(weaponStat).append("\n");
      }
    }

    if (armourStats != null) {
      allStats.append("\nBody Armour/Helm\n");

      for (String armorStat : armourStats) {
        allStats.append(armorStat).append("\n");
      }
    }

    if (shieldStats != null) {
      allStats.append("\nShield\n");

      for (String shieldStat : shieldStats) {
        allStats.append(shieldStat).append("\n");
      }
    }

    return allStats.toString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeStringArray(shieldStats);
    dest.writeStringArray(armourStats);
    dest.writeStringArray(weaponStats);
  }
}
