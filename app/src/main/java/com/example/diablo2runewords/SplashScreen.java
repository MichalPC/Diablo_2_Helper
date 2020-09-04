package com.example.diablo2runewords;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    View easySplashScreenView = new EasySplashScreen(SplashScreen.this)
        .withFullScreen()
        .withTargetActivity(MainActivity.class)
        .withSplashTimeOut(4000)
        .withBackgroundResource(android.R.color.holo_red_light)
        .withLogo(R.drawable.fav_rune_word_icon)
        .withAfterLogoText("D2 Helper")
        .create();

    setContentView(easySplashScreenView);
  }
}