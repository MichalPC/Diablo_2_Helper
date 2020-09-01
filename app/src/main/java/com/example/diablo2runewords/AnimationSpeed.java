package com.example.diablo2runewords;

public enum AnimationSpeed {
  DROPDOWN_OPEN(100), DROPDOWN_CLOSE(100), BUTTON(100);

  Integer speed;

  AnimationSpeed(int speed) {
    this.speed = speed;
  }

  public String toString() {
    return this.speed.toString();
  }
}
