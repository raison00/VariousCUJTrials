@file:OptIn(ExperimentalMaterial3Api::class)
package com.uxcompose.textfieldtraversalfocus
import androidx.compose.material.icons.Icons
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uxcompose.textfieldtraversalfocus.ui.theme.TextFieldTraversalFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TextFieldTraversalFocusTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Main Container: 24.dp padding and vertical scrolling
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(24.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        FocusTraversalSection()
                        InputActionsSection()
                    }
                }
            }
        }
    }
}

@Composable
fun FocusTraversalSection() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Working Focus Traversal within TextFields",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = " Use the 'TAB' key on the keyboard to move between the TextFields.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Using a simple Column instead of a Grid for better mobile row behavior
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index ->
                FocusableTextField(label = "TextField $index")
            }
        }
    }
}

@Composable
fun FocusableTextField(label: String) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        label = { Text(label) },
        leadingIcon = {
            IconButton(onClick = { /* Search Action */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            }
        },
        trailingIcon = {
            if (text.isNotEmpty() || isFocused) {
                IconButton(onClick = { text = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        },
        maxLines = 1,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.Gray
        )
    )
}

@Composable
fun InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var textValue1 by remember { mutableStateOf("") }
    var textValue2 by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryFixed)
                Spacer(Modifier.width(8.dp))
                Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
            }

            Text("Try the dialogs and modals below to see TextField focus trapping issues. This is one way to view the complexity of the TextField focus trap within a common modal trap.")

            Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Create, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Dialog")
            }

            ElevatedButton(onClick = { showBottomSheet = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Bottom Sheet")
            }
        }
    }

    // --- Overlays ---

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.titleLarge)
                    Text("Using the keyboard to tab or return to navigate through the UI is not working here. The focus remains within the TextField.", style = MaterialTheme.typography.bodyMedium)
                    OutlinedTextField(value = textValue, onValueChange = { textValue = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = textValue1, onValueChange = { textValue1 = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        Button(onClick = { showDialog = false }) { Text("Save") }
                    }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                Text("Using the keyboard to tab or return to navigate through the UI is not working here. The focus remains within the TextField.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(value = textValue2, onValueChange = { textValue2 = it }, label = { Text("This TextField will allow for multi-line entries.") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}