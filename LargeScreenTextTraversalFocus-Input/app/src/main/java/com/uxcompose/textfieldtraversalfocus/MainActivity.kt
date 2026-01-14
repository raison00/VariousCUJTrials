@file:OptIn(ExperimentalMaterial3Api::class)

package com.uxcompose.textfieldtraversalfocus


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults.indicatorLine
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.uxcompose.textfieldtraversalfocus.ui.theme.TextFieldTraversalFocusTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Proper Extension Property (Must be top-level or in an object)
val TextFieldState.isEmpty: Boolean get() = this.text.isEmpty()


@Composable
fun EnhancedFieldTest(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onSearchTriggered: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current // Added for Samsung focus fix
    val shakeOffset = remember { Animatable(0f) }
    val maxChars = 15

    // --- 1. SHAKE ANIMATION DEFINITION ---
    // This must be defined so the inputTransformation can call it
    val triggerShake: () -> Unit = {
        scope.launch {
            val shakeValues = listOf(-10f, 10f, -10f, 10f, 0f)
            shakeValues.forEach { target ->
                shakeOffset.animateTo(target, spring(stiffness = Spring.StiffnessHigh))
            }
        }
    }

    // --- 2. DEBOUNCED SEARCH (Side Effect) ---
    LaunchedEffect(state) {
        snapshotFlow { state.text }.collectLatest { currentText ->
            if (currentText.isNotEmpty()) {
                delay(500)
                onSearchTriggered(currentText.toString())
            }
        }
    }

    // --- 3. UI COMPONENT ---
    OutlinedTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .offset(x = shakeOffset.value.dp),
        label = { Text("Type something here for the demo") },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Clear, null)
                }
            }
        },
        // --- 4. INPUT TRANSFORMATION (The Gatekeeper) ---
        inputTransformation = InputTransformation.maxLength(maxChars).then {
            // asCharSequence() is the internal buffer of the TextField
            if (asCharSequence().length >= maxChars) {
                triggerShake()
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = {
            onSearchTriggered(state.text.toString())
            // Explicitly clear focus to hide keyboard on physical devices
            focusManager.clearFocus()
        },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun TextFieldContrastTest(label: String) {
    val backgroundColor = MaterialTheme.colorScheme.surface // or your card color
    val textColor = MaterialTheme.colorScheme.onSurface

    val ratio = textColor.contrastAgainst(backgroundColor)
    val isAccessible = ratio >= 4.5f

    Column(modifier = Modifier.padding(16.dp)) {
        FocusableTextField(
            state = rememberTextFieldState(),
            label = "Contrast Ratio Test Field"
        )

        Spacer(Modifier.height(8.dp))

        Surface(
            color = if (isAccessible) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "Contrast Ratio of TextField vs Text: ${"%.2f".format(ratio)} : 1 (${if (isAccessible) "PASS" else "FAIL"})",
                modifier = Modifier.padding(8.dp),
                color = if (isAccessible) Color(0xFF2E7D32) else Color(0xFFC62828),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
@Composable
fun FocusableTextField(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,    imeAction: ImeAction = ImeAction.Next,
    onAction: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current // Needed for action handling

    OutlinedTextField(
        state = state,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = if (isFocused) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        // 3. Pass the imeAction to KeyboardOptions
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        // 4. Handle the keyboard action
        onKeyboardAction = {
            if (imeAction == ImeAction.Next) {
                focusManager.moveFocus(FocusDirection.Next)
            } else {
                focusManager.clearFocus()
            }
            onAction()
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )
    )
}






//does work in emulator, gets trapped on devices
@Composable
fun FocusableTextFieldOLD(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onAction: () -> Unit = {}
) {
    OutlinedTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = { Icon(Icons.Default.Search, null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) { Icon(Icons.Default.Close, null) }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        onKeyboardAction = { onAction() },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}
@Composable
fun MyForm() {
    val nameState = rememberTextFieldState()
    val emailState = rememberTextFieldState()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "WORKING: Focus Traversal ",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Using updated Composable to ensure TAB key navigation works.",
            style = MaterialTheme.typography.bodyMedium
        )

    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Registration Form Input", style = MaterialTheme.typography.titleLarge)
        FocusableTextField26(state = nameState, label = "Full Name")
        FocusableTextField26(
            state = emailState,
            label = "Email Address here",
            imeAction = ImeAction.Done
        )
    }
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TextFieldTraversalFocusTheme {
                val mainSearchState = rememberTextFieldState()
                val standaloneState = rememberTextFieldState()
                val searchState = rememberTextFieldState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(24.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        AccessibilityCheck2()
                        TextFieldContrastTest("TextField Contrast Test")
                        FocusTraversalSection()
                        FocusTraversalSectionUpdate()
                        Section3InputActions()
                        InputActionsSection()
                        Focus26InputActionsSection()
                        AccessibilityCheck()
                        EnhancedFieldTest(
                            state = searchState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            onSearchTriggered = { query ->
                                // This code runs when the user hits 'Search' on the keyboard
                                // or after the 500ms debounce delay you set in the LaunchedEffect
                                println("Logic triggered for: $query")

                                // Example: myViewModel.performSearch(query)
                            }
                        )
                        Text("Stuck Traverse within the TextField", style = MaterialTheme.typography.titleLarge)

                        FocusableTextField(state = standaloneState, label = "Standalone Field")
                        EnhancedSearchField(state = mainSearchState)
                        MyForm()
                        Text("Search Input Field.  Note this functionality is not built out, interest is in the Focus and Input ecosystem for 2026.", style = MaterialTheme.typography.titleLarge)
                        EnhancedFieldTest(
                            state = searchState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            onSearchTriggered = { query ->
                                println("Logic triggered for: $query")

                                //  myViewModel.performSearch(query)
                            }
                        )

                    }
                }
            }
        }
    }
}



@Composable
fun FocusTraversalSection() {
    val states = remember { List(4) { TextFieldState() } }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "UPDATED: Focus Traversal ( with TextFieldState)",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Using TextFieldState to TAB key navigation compared to the legacy String-based API. (1/9/26)" + " Works on Device and NOT on Emulator",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Using TextFieldState with updated FocusableTextField Composable to ensure TAB key navigation works. (1/9/26)" + " Works on Device and NOT on Emulator",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            states.forEachIndexed { index, state ->
                FocusableTextField(
                    state = state,
                    label = "TextField $index",
                    imeAction = if (index == 3) ImeAction.Done else ImeAction.Next
                )
            }
        }
    }
}
@Composable
fun FocusTraversalSectionUpdate() {
    val states = remember { List(4) { TextFieldState() } }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "WORKING: Focus Traversal ( with TextFieldState)",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Using TextFieldState to TAB key navigation compared to the legacy String-based API.",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Using TextFieldState with updated FocusableTextField26 Composable to ensure TAB key navigation works." ,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = " Works on Device AND on Emulator" + " Has a character limit - this will help visually see the difference in Composable FocusableTextFields ",
            style = MaterialTheme.typography.bodyMedium
        )
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            states.forEachIndexed { index, state ->
                val isTooLong by remember { derivedStateOf { state.text.length > 5 } }
                FocusableTextField26(
                    state = state,
                    label = if (isTooLong) "Too Long! Make it shorter." else "TextField $index",
                    imeAction = if (index == 3) ImeAction.Done else ImeAction.Next
                )
            }
        }
    }
}

@Composable
fun FocusableTextFieldMultiline(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable () -> Unit)? = null,
    // 1. Change default ImeAction to Default for multiline support
    imeAction: ImeAction = ImeAction.Default,
    onAction: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        onKeyboardAction = {
            if (imeAction == ImeAction.Next) {
                focusManager.moveFocus(FocusDirection.Next)
            } else {
                focusManager.clearFocus()
            }
            onAction()
        },
        // MultiLine with a minimum of 3 lines
        lineLimits = TextFieldLineLimits.MultiLine(
            minHeightInLines = 3,
            maxHeightInLines = 5 // stops the field from growing infinitely
        ),
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        }
    )
}
@Composable
fun FocusableTextField26(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    // Add this parameter: default it to null
    leadingIcon: (@Composable () -> Unit)? = null,
    imeAction: ImeAction = ImeAction.Next,
    onAction: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        // Pass the parameter here
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        onKeyboardAction = {
            if (imeAction == ImeAction.Next) {
                focusManager.moveFocus(FocusDirection.Next)
            } else {
                focusManager.clearFocus()
            }
            onAction()
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        }
    )
}



@Composable
fun FocusableTextField2226(
    state: TextFieldState,
    label: String,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Next,
    onAction: () -> Unit = {}
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        state = state,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        //  Explicitly handle the action for Samsung compatibility
        onKeyboardAction = {
            if (imeAction == ImeAction.Next) {
                //focusManager.moveFocus(ViewCompat.FocusDirection.Next)
                focusManager.moveFocus(FocusDirection.Next)
            } else {
                focusManager.clearFocus()
            }
            onAction()
        },
        lineLimits = TextFieldLineLimits.SingleLine,
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        }
    )
}
@Composable
fun InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    // Modern state containers
    val nameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val descriptionState = rememberTextFieldState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Error, null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                Spacer(Modifier.width(8.dp))
                //Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "TextField Focus Trap Information Entry",
                    // Use onTertiaryContainer for the Text
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    // Increase the size using a larger Typography style
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                text = "Using FocusableTextField to TAB key navigation with imeAction = ImeAction.Next.",
                style = MaterialTheme.typography.bodyMedium
            )

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

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.titleLarge)
                    FocusableTextField(state = nameState, label = "Name")
                    FocusableTextField(state = passwordState, label = "Password", imeAction = ImeAction.Done)
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
                FocusableTextFieldMultiline(state = descriptionState, label = "Multi-line entries supported")
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}

@Composable
fun Focus26InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }

    // Modern state containers
    val nameState = rememberTextFieldState()
    val passwordState = rememberTextFieldState()
    val descriptionState = rememberTextFieldState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                Spacer(Modifier.width(8.dp))
                //Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "Alternate Solution for TextField Focus Trap within Overlay",
                    // Use onTertiaryContainer for the Text
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    // Increase the size using a larger Typography style
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Text(
                text = "Using FocusableTextFieldMultiline for TAB traversal with imeAction = ImeAction.Next.",
                style = MaterialTheme.typography.bodyMedium
            )

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

    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.titleLarge)
                    FocusableTextField26(state = nameState, label = "Name")
                    FocusableTextField26(state = passwordState, label = "Password", imeAction = ImeAction.Done)
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
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(32.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                FocusableTextFieldMultiline(state = descriptionState, label = "Multi-line entries supported")
                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}
@Composable
fun Section3InputActions() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }

    // Correct TextFieldState usage (Stable in late 2025)
    val dialogNameState = rememberTextFieldState()
    val dialogEmailState = rememberTextFieldState()
    val modalTitleState = rememberTextFieldState()
    val modalDescState = rememberTextFieldState()

    // UI Structure of trapped example
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiary)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Check, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onTertiaryContainer)
                Text("Fixed(ish): Focus Trapping", style = MaterialTheme.typography.headlineSmall)
            }
            Text(
                text = "Using TextFieldState for Focus Traversal. TextField to TAB key navigation. Currently finding this works on Device and not in Emulator.",
                style = MaterialTheme.typography.bodyMedium
            )
            ElevatedButton(onClick = { showDialogWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Open Dialog")
            }
            ElevatedButton(onClick = { showModalWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Open Modal")
            }
        }
    }

    // DIALOG FIX
    if (showDialogWithTextField) {
        Dialog(
            onDismissRequest = { showDialogWithTextField = false },
            // Ensure the dialog properties allow focus trapping
            properties = DialogProperties(usePlatformDefaultWidth = true)
        ) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.headlineSmall)
                    Text(
                        text = "Using TextField to TAB key navigation with imeAction = ImeAction.Next.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    // Use standard BasicTextField2/TextField with the state
                    TextField(
                        state = dialogNameState,
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        state = dialogEmailState,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDialogWithTextField = false }) { Text("Cancel") }
                        Button(onClick = { showDialogWithTextField = false }) { Text("Save") }
                    }
                }
            }
        }
    }

    // MODAL BOTTOM SHEET FIX
    if (showModalWithTextField) {
        ModalBottomSheet(
            onDismissRequest = { showModalWithTextField = false },
            // FocusRestorer helps focus return to the button that opened the sheet
            modifier = Modifier.focusRestorer()
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.headlineSmall)

                TextField(
                    state = modalTitleState,
                    label = { Text("Title") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    state = modalDescState,
                    label = { Text("Description") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(onClick = { showModalWithTextField = false }, modifier = Modifier.fillMaxWidth()) {
                    Text("Submit")
                }
            }
        }
    }
}
@Composable
fun Section3InputActionsOLD() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }

    //updated from mutableStateOf to rememberTextFieldState
    val dialogNameState = rememberTextFieldState()
    val dialogEmailState = rememberTextFieldState()
    val modalTitleState = rememberTextFieldState()
    val modalDescState = rememberTextFieldState()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Error, null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.error)
                Text("Issue: Focus & Trapping", style = MaterialTheme.typography.headlineSmall)
            }
            ElevatedButton(onClick = { showDialogWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Dialog with TextField")
            }

            ElevatedButton(onClick = { showModalWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Text("Modal with TextField")
            }
        }
    }

    if (showDialogWithTextField) {
        Dialog(onDismissRequest = { showDialogWithTextField = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.headlineSmall)
                    FocusableTextField(state = dialogNameState, label = "Name")
                    FocusableTextField(state = dialogEmailState, label = "Email", imeAction = ImeAction.Done)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDialogWithTextField = false }) { Text("Cancel") }
                        Button(onClick = { showDialogWithTextField = false }) { Text("Save") }
                    }
                }
            }
        }
    }

    if (showModalWithTextField) {
        ModalBottomSheet(onDismissRequest = { showModalWithTextField = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.headlineSmall)
                FocusableTextField(state = modalTitleState, label = "Title")
                FocusableTextField(state = modalDescState, label = "Description", imeAction = ImeAction.Done)
                Button(onClick = { showModalWithTextField = false }, modifier = Modifier.fillMaxWidth()) { Text("Submit") }
            }
        }
    }
}

@Composable
fun EnhancedSearchField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onSearchTriggered: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }
    val maxChars = 15

    LaunchedEffect(state) {
        snapshotFlow { state.text }.collectLatest { currentText ->
            if (currentText.isNotEmpty()) {
                delay(500)
                onSearchTriggered(currentText.toString())
            }
        }
    }

    val triggerShake: () -> Unit = {
        scope.launch {
            val shakeValues = listOf(-10f, 10f, -10f, 10f, 0f)
            shakeValues.forEach { target ->
                shakeOffset.animateTo(target, spring(stiffness = Spring.StiffnessHigh))
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
                Icon(Icons.Default.Email, contentDescription = "Email Icon")
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
            // FIX: Define colors using the Theme's scheme to ensure Dark Mode support
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline, // Better than Color.Gray
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
    @Composable
    fun OLDFocusableTextField(label: String) {
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
                    Icon(Icons.Default.Email, contentDescription = "Email Icon")
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
}

@Composable
fun AccessibilityCheck() {
    // bgColor and textColor will automatically swap values in Dark Mode
    val bgColor = MaterialTheme.colorScheme.surfaceContainer
    val textColor = MaterialTheme.colorScheme.onSurface

    val ratio = textColor.contrastAgainst(bgColor)

    // WCAG Standards: 3.0 for large text/UI, 4.5 for normal text
    val isAccessible = ratio >= 4.5f

    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, if (isAccessible) Color.Transparent else MaterialTheme.colorScheme.error)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Contrast Ratio: ${"%.2f".format(ratio)}:1",
                // Using semantic colors instead of hardcoded Cyan/Yellow
                color = if (isAccessible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "Compliance Check: ${if (isAccessible) "PASS (AA)" else "FAIL"}",
                // This ensures text is ALWAYS visible against bgColor
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AccessibilityCheck2() {
    // bgColor is likely white/light gray
    val bgColor = MaterialTheme.colorScheme.surfaceContainer
    val textColor = MaterialTheme.colorScheme.onSurface

    val ratio = textColor.contrastAgainst(bgColor)
    val isAccessible = ratio >= 4.5f

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Contrast Ratio of the background to the text color: ${"%.2f".format(ratio)}",
            // If it fails, use a visible color instead of White
            color = if (isAccessible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Compliance Check for dynamic contrast ratio: ${if (isAccessible) "PASS" else "FAIL"}",
            color = if (isAccessible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun MyFormScreen() {
    // 1. Define the state at the TOP level of the screen
    val emailState = rememberTextFieldState()

    Column(modifier = Modifier.padding(16.dp)) {
        // 2. Pass it into your text field
        FocusableTextField(
            state = emailState,
            label = "Email Address"
        )

        Spacer(Modifier.height(16.dp))

        // 3. Now 'emailState' is accessible here for the button!
        Button(
            onClick = {
                // Accessing the text value safely
                println("Submitted: ${emailState.text}")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}


