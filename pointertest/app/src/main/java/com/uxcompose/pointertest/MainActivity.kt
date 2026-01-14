package com.uxcompose.pointertest

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Creating a Simple Scaffold
            // Layout updates needed to add boundaries, will revisit later
            // https://developer.android.com/develop/ui/compose/system/setup-e2e
            Scaffold(

                // Creating a Top Bar - to do- add some color so the bar is obvious
                //notice how the top and bottom bar have different methods for the UI
                //using https://developer.android.com/develop/ui/compose/designsystems/material3
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Pointer Input Test",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold
                                //titleContentColor = androidx.material3.MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        },
                        // Set the colors for the entire app bar
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.DarkGray,
                            titleContentColor = Color.Black
                        ),
                        // Optional: Add a Modifier if you need padding or other adjustments for the TopAppBar itself
                        // modifier = Modifier.padding(horizontal = 8.dp)
                    )
                },


                //Creating a bottom bar as a test too why do they have different methods to get the UI in?
                bottomBar = {
                    BottomAppBar(containerColor = Color.DarkGray) {
                        Text(
                            text = "Bottom Bar",
                            color = Color.Black,
                            modifier = Modifier.padding(start = 16.dp),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.ExtraBold,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },

// Creating Content
                content = { innerPadding ->

                    //    Apply the Scaffold's padding here.
                    Surface(
                        modifier = Modifier
                            .padding(innerPadding) // <-- Use the padding from Scaffold
                            .fillMaxSize(),
                        color = Color.Black // Set the background color on the Surface
                    ) { // 2. Provide the content for the Surface in its trailing lambda.

                        // 3. Place the Column *inside* the Surface. It should have boundaries so it doesn't get lost or run off the screen.
                        //do this next- restrict viewport to a constrained area for this exercise.
                        Column(
                            modifier = Modifier.fillMaxSize(), // No need for extra padding or background here
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            // Offset Values for X and Y
                            var offsetX by remember { mutableFloatStateOf(0f) }
                            var offsetY by remember { mutableFloatStateOf(0f) }

                            // click and drag Pointer
                            Box(
                                Modifier
                                    .offset {
                                        IntOffset(
                                            offsetX.roundToInt(),
                                            offsetY.roundToInt()
                                        )
                                    }
                                    .background(Color.Magenta)
                                    .size(40.dp)
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            offsetX += dragAmount.x
                                            offsetY += dragAmount.y
                                        }
                                    }
                            )
                        }
                    }
                }    )
        }
    }

}