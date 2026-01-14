package com.example.viewincomposefocussample

import org.junit.Assert.*

import android.view.KeyEvent.ACTION_UP
import android.view.KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP
import androidx.compose.foundation.focusable

import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.InputMode
import androidx.compose.ui.input.key.NativeKeyEvent
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.test.isFocusable
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performKeyPress
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleUnitTest {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun carFocusProperties_reactToKeyEvent() {
    var button2Clicked = false
    var button2Focus = false
    composeRule.setContent {
        Button(
          onClick = { button2Clicked = true },
          Modifier
            .focusable(true)
            .onKeyEvent { keyEvent ->
            button2Focus = true
            true
          },
        ) {
          Text("Button1")
        }
      }
    val keyEvent = KeyEvent(NativeKeyEvent(ACTION_UP, KEYCODE_SYSTEM_NAVIGATION_UP))
    composeRule.onNode(isFocusable()).performKeyPress(keyEvent)

   assert(button2Focus)
  }


}