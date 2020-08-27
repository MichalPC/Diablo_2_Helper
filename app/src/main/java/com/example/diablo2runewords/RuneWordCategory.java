package com.example.diablo2runewords;

import androidx.annotation.NonNull;

public enum RuneWordCategory {
  AXES("Axes"), BODY_ARMOR("Body Armor"), CLAWS("Claws"),
  CLUBS("Clubs"), HAMMERS("Hammers"), HELMS("Helms"), MACES("Maces"),
  MELEE_WEAPONS("Melee Weapons"), PALADIN_SHIELDS("Paladin Shields"), POLEARMS("Polearms"),
  RANGED_WEAPONS("Ranged Weapons"), SCEPTERS("Scepters"), SHIELDS("Shields"),
  STAVES("Staves"), SWORDS("Swords"), WANDS("Wands"), WEAPONS("Weapons"), KATARS("Katars");

  String value;

  RuneWordCategory(String value) {
    this.value = value;
  }

  @NonNull
  @Override
  public String toString() {
    return value;
  }
}
