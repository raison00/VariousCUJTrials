package com.uxcompose.dropdowntest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.uxcompose.dropdowntest.ui.theme.DropdownTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DropdownTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pass the innerPadding to the Greeting composable
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    // This list holds all possible options
    val allOptions = remember { listOf("Option 1", "Option 2", "Option 3", "Option 4", "Option 5") }
    var expanded by remember { mutableStateOf(false) }

    // This state will hold the user's input text
    var selectedOptionText by remember { mutableStateOf("") }

    // --- 1. Center the Dropdown Menu ---
    // Use a Column with Arrangement.Center and Alignment.CenterHorizontally
    // The modifier now fills the whole screen to enable centering.
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedOptionText,
                // --- 2. Allow User Input ---
                // Update the state as the user types
                onValueChange = { selectedOptionText = it },
                label = { Text("Search Options") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                // Make the field editable by removing readOnly = true
            )

            // --- 3. Filter the Dropdown Based on Input ---
            // Filter options based on what the user has typed
            val filteringOptions = allOptions.filter { it.contains(selectedOptionText, ignoreCase = true) }

            if (filteringOptions.isNotEmpty()) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    filteringOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DropdownTestTheme {
        Greeting("Android")
    }
}
