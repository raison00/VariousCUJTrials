@file:OptIn(ExperimentalMaterial3Api::class)

package com.uxcompose.a12192025_textfield

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

class MainActivity : ComponentActivity() {
    private lateinit var content: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            Material3TemplateTheme(darkTheme = darkTheme) {
                TemplateApp(
                    darkTheme = darkTheme,
                    onThemeToggle = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateApp(darkTheme: Boolean, onThemeToggle: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Input and Focus Demos") },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = if (darkTheme) Icons.Default.Star else Icons.Default.Settings,
                            contentDescription = "Theme icon",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Switch(
                            checked = darkTheme,
                            onCheckedChange = { onThemeToggle() }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Section1FoundationalButtons()
            Section2FoundationalInput()
            Section3InputActions()
        }
    }
}

@Composable
fun Section1FoundationalButtons() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    //Section 1:
                    text = "Traversing Foundational Buttons",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Various M3 Compose button types with icons and styles.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Standard Button
            Button(
                onClick = { /* Action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Standard Button")
            }

            // Outlined Button
            OutlinedButton(
                onClick = { /* Action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Outlined Button")
            }

            // Filled Tonal Button
            FilledTonalButton(
                onClick = { /* Action */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Filled Tonal Button")
            }

            // Elevated Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                ElevatedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Share")
                }

                ElevatedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text("Edit")
                }
            }

            // Floating Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                FloatingActionButton(
                    onClick = { /* Action */ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }
}

@Composable
fun Section2FoundationalInput() {
    var textFieldValue by remember { mutableStateOf("") }
    var checkedState by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showModalBottomSheet by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    //Section 2:
                    text = "Focus and Input",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = "Input components including checkboxes, text fields, dialogs and modals.  TODO: add more here",
                style = MaterialTheme.typography.bodyMedium
            )

            // Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it }
                )
                Text("Enable notifications")
            }

            // TextField
            OutlinedTextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Enter text") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Dialog Button
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Show Dialog")
            }

            // Modal Bottom Sheet Button
            FilledTonalButton(
                onClick = { showModalBottomSheet = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Show Modal Bottom Sheet")
            }
        }
    }

    // Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            icon = {
                Icon(Icons.Default.Info, contentDescription = null)
            },
            title = {
                Text("Dialog Title")
            },
            text = {
                Text("This is a Material3 dialog with standard content and actions.")
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal Bottom Sheet
    if (showModalBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showModalBottomSheet = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Modal Bottom Sheet",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "This is a Material3 modal bottom sheet with content that slides up from the bottom.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = { showModalBottomSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun Section3InputActions() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }
    var dialogTextValue by remember { mutableStateOf("") }
    var modalTextValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    //Section 3:
                    text = "Focus & Input with Trapping",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }

            Text(
                text = "Dialogs and modals with elevated text fields for user input. Issue: TextField Focus Trapping.",
                style = MaterialTheme.typography.bodyMedium
            )

            // Dialog with TextField Button
            ElevatedButton(
                onClick = { showDialogWithTextField = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Dialog with TextField")
            }

            // Modal with TextField Button
            ElevatedButton(
                onClick = { showModalWithTextField = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Modal with TextField")
            }

            // Action Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }

                OutlinedButton(
                    onClick = { /* Action */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }

    // Dialog with TextField
    if (showDialogWithTextField) {
        Dialog(onDismissRequest = { showDialogWithTextField = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Enter Information",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "The issue here is the Focus Trapping of the TextField prevents the text to be entered into the email field. ",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    OutlinedTextField(
                        value = dialogTextValue,
                        onValueChange = { dialogTextValue = it },
                        label = { Text("Name") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = "",
                        onValueChange = { },
                        label = { Text("Email") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = { showDialogWithTextField = false }) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { showDialogWithTextField = false }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet with TextField
    if (showModalWithTextField) {
        ModalBottomSheet(
            onDismissRequest = { showModalWithTextField = false }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Quick Entry Form",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "The issue here is the Focus Trapping of the TextField prevents the text to be entered into the description field. ",
                    style = MaterialTheme.typography.bodyMedium
                )
                OutlinedTextField(
                    value = modalTextValue,
                    onValueChange = { modalTextValue = it },
                    label = { Text("Title") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    label = { Text("Description") },
                    leadingIcon = {
                        Icon(Icons.Default.List, contentDescription = null)
                    },
                    minLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = { showModalWithTextField = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = { showModalWithTextField = false },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Submit")
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun Material3TemplateTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme()
    } else {
        lightColorScheme()
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}