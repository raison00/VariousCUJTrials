package com.example.viewincomposefocussample;

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MyComposeApp()
    }
  }
}

@Composable
fun MyComposeApp() {
  val TAG = "viewdebug"
  var bg by remember { mutableStateOf(Color.Black) }
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    // [START android_compose_touchinput_focus_vertical]
    Row {
      Column {
        TextButton({ }) { Text("1 field") }
        TextButton({ }) { Text("2 field") }
        // TextButton({ }) { Text("3 field") }
        // TextButton({ }) { Text("4 field") }
        // TextButton({ }) { Text("5 field") }
      }
      Column {
        TextButton({ }) { Text("Third field") }
        AndroidView(
          modifier = Modifier
            // .padding(top = 60.dp)
            .size(150.dp)
            .background( color  = bg)
            .onKeyEvent { keyEvent ->
              Log.i(TAG, "jrjr In android view Key event received at $keyEvent")
              false
            }
            .onFocusChanged {
              if (it.isFocused) {bg = Color.Blue} else { bg = Color.Black} }
            // .focusable(true)
            .focusGroup()
          ,
          factory = { context ->
            CustomButtonView(context)
              .apply {
              // Request focus to ensure the view can receive key events
              // requestFocus()
            }
          }
        )

        // TextButton({ }) { Text("six field") }
      }
    }
  }

}
