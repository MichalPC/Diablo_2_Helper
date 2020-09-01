package com.example.diablo2runewords;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * THIS IS A WRAPPER CLASS FOR CODE BY Tom Esterez
 * https://stackoverflow.com/questions/4946295/android-expand-collapse-animation
 */
public class ExpansionAnimation {

  public static void expand(final View v, int miliSecs) {
    int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(
      ((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
    int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
    v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
    final int targetHeight = v.getMeasuredHeight();

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    v.getLayoutParams().height = 1;
    v.setVisibility(View.VISIBLE);
    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {
        v.getLayoutParams().height = interpolatedTime == 1
          ? ConstraintLayout.LayoutParams.WRAP_CONTENT
          : (int) (targetHeight * interpolatedTime);
        v.requestLayout();
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };

    a.setDuration(miliSecs * 5);
    v.startAnimation(a);
  }

  public static void collapse(final View v, int miliSecs) {
    final int initialHeight = v.getMeasuredHeight();

    Animation a = new Animation() {
      @Override
      protected void applyTransformation(float interpolatedTime, Transformation t) {

        if (interpolatedTime == 1) {
          v.setVisibility(View.GONE);
        } else {
          v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
          v.requestLayout();
        }
      }

      @Override
      public boolean willChangeBounds() {
        return true;
      }
    };

    a.setDuration(miliSecs * 5);
    v.startAnimation(a);
  }
}
