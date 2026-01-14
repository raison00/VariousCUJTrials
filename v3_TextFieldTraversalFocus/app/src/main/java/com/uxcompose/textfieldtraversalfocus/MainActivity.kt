@file:OptIn(ExperimentalMaterial3Api::class)

package com.uxcompose.textfieldtraversalfocus

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uxcompose.textfieldtraversalfocus.ui.theme.TextFieldTraversalFocusTheme
import kotlinx.coroutines.delay
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

@Preview(showBackground = true, name = "Light Mode")
@Composable
fun TraversalFocusScreen() {
    val scrollState = rememberScrollState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = spacedBy(32.dp)
        ) {
            WorkingFocusTraversalSection()
            StuckTextFieldTraversal()
            InputActionsSection()
            InputTrappingActions()
            TextFieldFocusTrap()
            FocusableTextField("TextField \$index")
            //FocusTraversalSectionOption()

        }
    }
}

@Composable
//WorkingFocusTraversalSection
fun WorkingFocusTraversalSection() {
    val focusManager = LocalFocusManager.current
    Column(verticalArrangement = spacedBy(16.dp)) {
        Text(
            text = "Working Focus Traversal",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Use the 'TAB' key or Keyboard 'Next' to move between fields.  This focus traversal is working as expected by setting the value to 1.",
            style = MaterialTheme.typography.bodyMedium
        )

        val focusManager = LocalFocusManager.current

        Column(verticalArrangement = spacedBy(16.dp)) {
            repeat(4) { index ->
                FocusableTextField(label = "TextField $index")
            }
        }
    }
}

//Stuck TextField Traversal
@Composable
fun StuckTextFieldTraversal() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var dialogName by remember { mutableStateOf("") }
    var dialogEmail by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.FlashOn, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Traversal Stuck TextField within Dialog", style = MaterialTheme.typography.titleLarge)
            }

            Text("Click to see the first field gain focus immediately and allow the next field to accept keyboard input.In this example, the TAB key is trapped within the TextField. "
                , style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Text("WHAT'S WORKING: Focus Input in both TextFields" +
                    " ewer", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Text("WHAT'S NOT WORKING: Tabbing is stuck within TextField and keyboard navigation does not work" +
                    " ewer", style = MaterialTheme.typography.bodyMedium)
            Button(onClick = { showDialogWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Open TextField-Focus Dialog")
            }
        }
    }

    if (showDialogWithTextField) {
        Dialog(onDismissRequest = { showDialogWithTextField = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = spacedBy(16.dp)) {
                    Text("Tabbed Traversal Stuck in TextField", style = MaterialTheme.typography.headlineSmall)
                    Text("Active Focus Available, but tabbed navigation does not work")
                    OutlinedTextField(
                        value = dialogName,
                        onValueChange = { dialogName = it },
                        label = { Text("First Name") },
                        modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    OutlinedTextField(
                        value = dialogEmail,
                        onValueChange = { dialogEmail = it },
                        label = { Text("Email Address") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )

                    Button(onClick = { showDialogWithTextField = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("Finish")
                    }
                }
            }
        }

        LaunchedEffect(Unit) {
            delay(50)
            focusRequester.requestFocus()
        }
    }
}








@Composable
fun FocusTraversalSectionOption() {
    Column(verticalArrangement = spacedBy(16.dp), modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)) {
        Text(
            text = "Issues with Focus Traversal within TextFields",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Expected behavior: 'TAB' key on the keyboard to move sequentially to the next the TextFields.",
            style = MaterialTheme.typography.bodyMedium
        )

        // Using a simple Column instead of a Grid for better mobile row behavior
        Column(verticalArrangement = spacedBy(16.dp)) {
            repeat(4) { index ->
                FocusableTextField(label = "TextField $index")
            }
        }
    }
}
//FocusableTextField
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun FocusableTextField(
    label: String,
    imeAction: ImeAction = ImeAction.Next,
    onNext: () -> Unit = {}
) {
    // State for the text input
    var text by remember { mutableStateOf("") }

    // State to track if this specific field has focus
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            // Updates isFocused whenever the focus state changes
            .onFocusChanged { state ->
                isFocused = state.isFocused
            },
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            // Show clear button only if there's text AND the field is active
            if (text.isNotEmpty() && isFocused) {
                IconButton(onClick = { text = "" }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear text"
                    )
                }
            }
        },
        //  KeyboardOptions only contains IME and Type info
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction
        ),
        // Triggers the onNext callback for various keyboard "Enter" actions
        keyboardActions = KeyboardActions(
            onNext = { onNext() },
            onDone = { onNext() },
            onGo = { onNext() }
        ),
        // singleLine and maxLines do not belong in KeyboardOptions
        singleLine = true,
        maxLines = 1,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}

// --- Preview for the individual component ---

@Composable
fun PreviewFocusableTextField() {
    MaterialTheme {
        FocusableTextField(
            label = "Sample Input",
            imeAction = ImeAction.Next,
            onNext = { /* No-op for preview */ }
        )
    }
}



//InputActionsSection
@Composable
fun InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var textValue1 by remember { mutableStateOf("") }
    var textValue2 by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Standard Overlays with Input TextFields", style = MaterialTheme.typography.titleLarge)
            }
            Text("These utilize standard focus management.")
            Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Open Dialog")
            }
            ElevatedButton(onClick = { showBottomSheet = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Open Bottom Sheet")
            }
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = spacedBy(12.dp)) {
                    Text("Dialog Input", style = MaterialTheme.typography.titleLarge)
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = textValue1,
                        onValueChange = { textValue1 = it },
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = { showDialog = false }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
                }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp)) {
                OutlinedTextField(value = textValue2, onValueChange = { textValue2 = it },  label = { Text("Quick Note") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(16.dp))
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}






//InputTrappingActions
@Composable
fun InputTrappingActions() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }
    var showBottomlWithTextField by remember { mutableStateOf(false) }
    var dialogTextValue by remember { mutableStateOf("") }
    var modalTextValue by remember { mutableStateOf("") }
    var bottomTextValue by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.tertiary

                )
                Text(
                    //Section 3:
                    text = "Issue: Focus & Input with Trapping ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )
            }

            Text(
                text = "Issue:  Dialogs and modals with elevated text fields for user input lose focus and get trapped in first TextField.  ",
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
                    contentDescription = "focus issue example",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Bottom Sheet with TextField")
            }
            ElevatedButton(onClick = { showBottomlWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("TBA: Additional Issue")
            }

            // Action Buttons Row
            Row(
                horizontalArrangement = spacedBy(8.dp),
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
                    verticalArrangement = spacedBy(16.dp)
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
                verticalArrangement = spacedBy(16.dp)
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
                    horizontalArrangement = spacedBy(8.dp),
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


//TextField Focus Trap
@Composable
fun TextFieldFocusTrap() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var textValue1 by remember { mutableStateOf("") }
    var textValue2 by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryFixed)
                Spacer(Modifier.width(8.dp))
                Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
            }

            Text("Try the dialogs and modals below to see TextField focus trapping issues somewhat resolved. The issue is the complexity of the TextField focus trap within a common modal trap across Android form factors.")
            Spacer(Modifier.width(8.dp))
            Text("WHAT'S WORKING: Focus Input in both TextFields" +
                    " ewer", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.width(8.dp))
            Text("WHAT'S NOT WORKING: Tabbing is stuck within TextField and keyboard navigation does not work" +
                    " ewer", style = MaterialTheme.typography.bodyMedium)

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
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = spacedBy(12.dp)) {
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
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                Text("Using the keyboard to tab or return to navigate through the UI is not working here. The focus remains within the TextField.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(value = textValue2, onValueChange = { textValue2 = it }, label = { Text("This TextField will allow for multi-line entries.") }, modifier = Modifier.fillMaxWidth())
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}



// --- Preview Section ---


@Preview
@Composable
fun TraversalFocusPreview() {
    TextFieldTraversalFocusTheme {
        TraversalFocusScreen()
    }
}

