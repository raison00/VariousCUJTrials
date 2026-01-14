@file:OptIn(ExperimentalMaterial3Api::class)
package com.uxcompose.focusissuesamples

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MainContent()
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(vertical = 24.dp)
        ) {
            item { TanContainer() }
            item { RegistrationFlowSection(snackbarHostState) }

            item { WorkingFocusTraversalSection() }
            item { Section3InputActions() }
            item { InputActionsSection() }
            item { FocusTraversalSection() }
        }
    }
}

@Composable
fun TanContainer() {
    var textInput by remember { mutableStateOf("") }
    var secondInput by remember { mutableStateOf("") }
    var focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF6495ED)) // cornflowerBlue
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Space inside the column
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "The Focus Trap",
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "This shows a typical frustration of the TextField focus trap.",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Type something in this TextField") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.5f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
                    focusedBorderColor = Color(0xFF5D4037), // Darker brown
                    unfocusedBorderColor = Color(0xFF8D6E63)
                )
            )
            // Second Text Field
            OutlinedTextField(
                value = secondInput,
                onValueChange = { secondInput = it },
                label = { Text("A TextField to type in and tab out of") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = outlinedFieldColors()
            )
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "For testing Focus Traversal:",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(16.dp)
    )
// Action Buttons Row
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(16.dp)

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
    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "These are not active buttons; the CTAs are here to test the focus traversal. Tabbing should allow for keyboard traversal of these buttons where seen on this test app.",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
        }




@Composable
fun outlinedFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedContainerColor = Color.White.copy(alpha = 0.5f),
    unfocusedContainerColor = Color.White.copy(alpha = 0.3f),
    focusedBorderColor = Color(0xFF5D4037),
    unfocusedBorderColor = Color(0xFF8D6E63)
)


@Composable
fun RegistrationFlowSection(snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    var isLoaderVisible by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var zip by remember { mutableStateOf("") }

    val isFormValid = username.isNotBlank() && password.length >= 6 &&
            address.isNotBlank() && city.isNotBlank() && zip.length == 5

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {




        AnimatedContent(
            targetState = when {
                isLoaderVisible -> "loading"
                isSubmitted -> "success"
                else -> "form"
            },
            transitionSpec = { fadeIn(tween(400)) togetherWith fadeOut(tween(400)) },
            label = "ScreenTransition"
        ) { targetState ->
            when (targetState) {
                "form" -> {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Create Account", style = MaterialTheme.typography.headlineMedium)

                        // Pass ImeAction.Next to each field to enable the "Next" button on mobile
                        CustomIconTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = "Username",
                            icon = Icons.Default.Person,
                            imeAction = ImeAction.Next
                        )
                        CustomIconTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = "Password",
                            icon = Icons.Default.Lock,
                            isPassword = true,
                            imeAction = ImeAction.Next
                        )
                        CustomIconTextField(
                            value = address,
                            onValueChange = { address = it },
                            label = "Street Address",
                            icon = Icons.Default.Home,
                            imeAction = ImeAction.Next
                        )
                        CustomIconTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = "City",
                            icon = Icons.Default.LocationCity,
                            imeAction = ImeAction.Next
                        )
                        CustomIconTextField(
                            value = zip,
                            onValueChange = { if (it.length <= 5) zip = it },
                            label = "Zip Code",
                            icon = Icons.Default.Place,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                            onDone = { focusManager.clearFocus() } // Hide keyboard on last field
                        )

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                scope.launch {
                                    isLoaderVisible = true
                                    delay(1500)
                                    isLoaderVisible = false
                                    isSubmitted = true
                                    snackbarHostState.showSnackbar("Welcome, $username!")
                                }
                            },
                            enabled = isFormValid,
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Submit")
                        }
                    }
                }
                "loading" -> {
                    Box(Modifier.height(300.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                "success" -> {
                    SuccessView(onReset = {
                        isSubmitted = false
                        username = ""; password = ""; address = ""; city = ""; zip = ""
                    })
                }
            }
        }
    }
}

@Composable
fun CustomIconTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Next,
    keyboardOptions: KeyboardOptions? = null,
    onDone: (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(icon, null) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        // If specific options aren't passed, use the default with the provided imeAction
        keyboardOptions = keyboardOptions ?: KeyboardOptions(
            imeAction = imeAction,
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down) },
            onDone = { onDone?.invoke() ?: focusManager.clearFocus() }
        ),
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(Icons.Default.Clear, "Clear")
                }
            }
        },
        singleLine = true
    )
}

@Composable
fun WorkingFocusTraversalSection() {
    val focusManager = LocalFocusManager.current
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Standard Input Focus", style = MaterialTheme.typography.headlineSmall)
        repeat(3) { index ->
            var text by remember { mutableStateOf("") }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Text Entry Field $index") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true
            )
        }
    }
}

@Composable
fun SuccessView(onReset: () -> Unit) {
    Column(
        Modifier.fillMaxWidth().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(80.dp))
        Text("Success!", style = MaterialTheme.typography.headlineMedium)
        TextButton(onClick = onReset) { Text("Reset Form") }
    }
}

@Composable
fun Section3InputActions() {
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error

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
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Modal with TextField")
            }
            ElevatedButton(onClick = { showBottomlWithTextField = true }, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Build, null)
                Spacer(Modifier.width(8.dp))
                Text("Open Bottom Sheet")
            }
            Text("Testing Focus Traversal", style = MaterialTheme.typography.titleMedium)
            Text("Tabbing should allow for keyboard traversal of these buttons. No actions expected.", style = MaterialTheme.typography.bodyMedium)
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
    if (showBottomlWithTextField) {
        ModalBottomSheet(onDismissRequest = { showBottomlWithTextField = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                Text("Focus Trapping within the TextField apparent on the Next or Tab navigation." , style = MaterialTheme.typography.bodyMedium)
                Text("See below in next section for more details to somewhat fix this. This approach keeps the focus within the TextField and does not allow for multi-line entries. " + "This is a common issue in Android.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = bottomTextValue,
                    onValueChange = { bottomTextValue = it },
                    label = { Text("Quick Note") },
                    modifier = Modifier.fillMaxWidth(),
                    // 1. Set singleLine to false to allow the Return key to create new lines
                    singleLine = false,
                    // 2. Set minLines to make it look like a note area
                    minLines = 3,
                    // 3. Set ImeAction.Default to ensure the Return key appears
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    )
                )
                Button(
                    onClick = { showBottomlWithTextField = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}


@Composable
fun OLDInputActionsSection() {
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(8.dp))
                Text("TextField Focus Trap", style = MaterialTheme.typography.titleLarge)
            }

            Text("Try the dialogs and modals below to see TextField focus trapping issues somewhat resolved. The issue is the complexity of the TextField focus trap within a common modal trap across Android form factors.")

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
@Composable
fun InputActionsSection() {
    var showDialog by remember { mutableStateOf(false) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf("") }
    var textValue1 by remember { mutableStateOf("") }
    var textValue2 by remember { mutableStateOf("") }

    //  FocusManager to control where focus goes
    val focusManager = LocalFocusManager.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Error",
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(8.dp))
                Text("Almost Fixed Focus Traversal", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            }

            Text("The fields below now support 'Next' and 'Tab' navigation to move focus sequentially but still have issues with focus trapping.")
            Text("What's Fixed: User can input text into the fields.")
            Text("What's Not Fixed: Focus Trapping within the TextField apparent on the Next or Tab navigation.")
            Spacer(Modifier.width(8.dp))

            Spacer(Modifier.width(8.dp))
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

    // --- Dialog with Working Traversal ---
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Enter Information", style = MaterialTheme.typography.titleLarge)

                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true, // 2. Important for 'Next' action
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    OutlinedTextField(
                        value = textValue1,
                        onValueChange = { textValue1 = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        // Moves focus to the 'Save' button
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showDialog = false }) { Text("Cancel") }
                        Button(onClick = { showDialog = false }) { Text("Save") }
                    }
                }
            }
        }
    }

    // --- Bottom Sheet with Working Traversal ---
    if (showBottomSheet) {
        ModalBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 32.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Quick Entry Form", style = MaterialTheme.typography.titleLarge)
                Text("Focus Trapping within the TextField apparent on the Next or Tab navigation." , style = MaterialTheme.typography.bodyMedium)
                Text("To somewhat fix this: Set ImeAction.Default to ensure the Return key appears.  This keeps the focus within the TextField, which allows for multi-line entries, but remains within this texField and cannot tab to the next UI element.", style = MaterialTheme.typography.bodyMedium)
                OutlinedTextField(
                    value = textValue2,
                    onValueChange = { textValue2 = it },
                    label = { Text("Quick Note") },
                    modifier = Modifier.fillMaxWidth(),
                    // 1. Set singleLine to false to allow the Return key to create new lines
                    singleLine = false,
                    // 2. Set minLines to make it look like a note area
                    minLines = 3,
                    // 3. Set ImeAction.Default to ensure the Return key appears
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Default,
                        keyboardType = KeyboardType.Text
                    )
                )
                Button(
                    onClick = { showBottomSheet = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}


@Composable
fun FocusTraversalSection() {
    // 1. Initialize the FocusManager to handle movement
    val focusManager = LocalFocusManager.current

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Working Focus Traversal within TextFields. Enter text and traverse to the next field with the 'Next' button. Tabbing should allow for keyboard traversal of these buttons.",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Use the 'TAB' key or the mobile 'Next' button to move between fields and buttons.",
            style = MaterialTheme.typography.bodyMedium
        )

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            val totalFields = 4
            repeat(totalFields) { index ->
                // 2. Decide if this is the last field or not
                val isLastField = index == totalFields - 1

                FocusableTextField(
                    label = "TextField $index",
                    // 3. Set the IME action based on position
                    imeAction = if (isLastField) ImeAction.Done else ImeAction.Next,
                    keyboardActions = KeyboardActions(
                        onNext = {
                            // Move focus to the next element in the focus chain
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        onDone = {
                            // Hide the keyboard when the last field is finished
                            focusManager.clearFocus()
                        }
                    )
                )
                // Action Buttons Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                    //.background(MaterialTheme.colorScheme.secondaryContainer)
                    //.padding(16.dp)

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
    }
}
@Composable
fun FocusableTextField(
    label: String,
    imeAction: ImeAction,
    keyboardActions: KeyboardActions
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        // 4. Apply the navigation options
        keyboardOptions = KeyboardOptions(
            imeAction = imeAction,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = keyboardActions,
        singleLine = true // Required for ImeAction.Next to display correctly
    )
}











