package com.example.viewincomposefocussample;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.LinearLayout;

public class CustomButtonView extends LinearLayout {

  private Button buttonOne;
  private Button buttonTwo;
  private Button buttonThree;

  public CustomButtonView(Context context) {
    super(context);
    init(context);
  }

  public CustomButtonView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CustomButtonView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    setOrientation(VERTICAL); // Set the orientation of the LinearLayout

    buttonOne = new Button(context);
    buttonTwo = new Button(context);
    buttonThree = new Button(context);

    buttonOne.setText("Button Four");
    buttonTwo.setText("Button Five");
    buttonThree.setText("Button Six");

    buttonOne.setFocusable(true);
    buttonOne.setFocusableInTouchMode(true);
    buttonTwo.setFocusable(true);
    buttonTwo.setFocusableInTouchMode(true);
    buttonThree.setFocusable(true);
    buttonThree.setFocusableInTouchMode(true);

    // Set onClick listeners if needed
    buttonOne.setOnClickListener(v -> performButtonOneClick());
    buttonTwo.setOnClickListener(v -> performButtonTwoClick());
    buttonThree.setOnClickListener(v -> performButtonTwoClick());

    // Add buttons to the LinearLayout
    addView(buttonOne);
    addView(buttonTwo);
    addView(buttonThree);
  }

  private void performButtonOneClick() {
    // Handle Button One click
  }

  private void performButtonTwoClick() {
    // Handle Button Two click
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event) {
    System.out.println("jrjr view get onKeyUp event "+event);
    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
      // Optionally handle key events for the LinearLayout
      buttonTwo.requestFocus();
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
      buttonOne.requestFocus();
      return true;
    }
    return super.onKeyUp(keyCode, event);
  }
}