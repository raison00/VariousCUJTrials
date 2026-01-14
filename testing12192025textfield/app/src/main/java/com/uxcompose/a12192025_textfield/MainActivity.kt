@file:OptIn(ExperimentalMaterial3Api::class)

package com.uxcompose.a12192025_textfield

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode

import android.R.attr.padding
import android.graphics.PointF.length
import android.os.Bundle
import android.provider.SyncStateContract.Helpers.insert
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.*
//import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.maxLength
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.then

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.PopupPositionProvider
import androidx.core.text.isDigitsOnly


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            //Material3TemplateTheme(darkTheme = darkTheme) {
                TemplateApp(
                    darkTheme = darkTheme,
                    onThemeToggle = { darkTheme = !darkTheme }
                )
           // }
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
                            // 1. Use more intuitive icons for the theme state.
                            imageVector = if (darkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,

                            // 2. Provide a clear, action-oriented content description for accessibility.
                            contentDescription = "Toggle theme",

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
    ) { innerPadding ->
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Section1FoundationalButtons()
            //Section2FoundationalInput()
           // Section3InputActions()
        }
    }
}

@Composable
fun Section1FoundationalButtons() {
    var showDialogWithTextField by remember { mutableStateOf(false) }
    var showModalWithTextField by remember { mutableStateOf(false) }
    var dialogTextValue by remember { mutableStateOf("") }
    var modalTextValue by remember { mutableStateOf("") }

    private fun ColumnScope.asCharSequence() {
        TODO("Not yet implemented")
    }

    private fun TooltipDefaults.rememberTooltipPositionProvider(above: Any): PopupPositionProvider {}
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
                    imageVector = Icons.Default.Star,
                    contentDescription = "Star",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    //Section 1:
                    text = "Traversing TextFields",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = "Expectation is you can use the Tab key to go from one TextField to another and be able to enter text into the field.",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Focus Traversal Test (maxLines = 1)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 24.dp)
            )
            Text(
                text = "This uses maxLines = 1 to resolve the focus trapping issue in TextFields.  Try it.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            // Grid layout matching your image
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(4) { index ->
                    FocusableTextField(label = "TextField $index")
                }
            }
            // Add content here

            @Composable
            fun SimpleTextFieldSample() {
                TextField(
                    state = rememberTextFieldState(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                )
            }


            @Composable
            fun SimpleOutlinedTextFieldSample() {
                OutlinedTextField(
                    state = rememberTextFieldState(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                )
            }


            @Composable
            fun TextFieldWithTransformations() {
                TextField(
                    state = rememberTextFieldState(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Phone number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    // Input transformation to limit user input to 10 digits
                    inputTransformation =
                        InputTransformation.maxLength(10).then {
                            if (!this.asCharSequence().isDigitsOnly()) {
                                revertAllChanges()
                            }
                        },
                    outputTransformation = {
                        // Output transformation to format as a phone number: (XXX) XXX-XXXX
                        if (length > 0) insert(0, "(")
                        if (length > 4) insert(4, ") ")
                        if (length > 9) insert(9, "-")
                    },
                )
            }


            @Composable
            fun TextFieldWithIcons() {
                val state = rememberTextFieldState()

                TextField(
                    state = state,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                    leadingIcon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    trailingIcon = {
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                            tooltip = { PlainTooltip { Text("Clear text") } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = { state.clearText() }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear text")
                            }
                        }
                    },
                )
            }


            @Composable
            fun TextFieldWithPlaceholder() {
                var alwaysMinimizeLabel by remember { mutableStateOf(false) }
                Column {
                    Row {
                        Checkbox(checked = alwaysMinimizeLabel, onCheckedChange = { alwaysMinimizeLabel = it })
                        Text("Show placeholder even when unfocused")
                    }
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        state = rememberTextFieldState(),
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text("Email") },
                        labelPosition = TextFieldLabelPosition.Attached(alwaysMinimize = alwaysMinimizeLabel),
                        placeholder = { Text("example@gmail.com") },
                    )
                }
            }


            @Sampled
            @Composable
            fun TextFieldWithPrefixAndSuffix() {
                var alwaysMinimizeLabel by remember { mutableStateOf(false) }
                Column {
                    Row {
                        Checkbox(checked = alwaysMinimizeLabel, onCheckedChange = { alwaysMinimizeLabel = it })
                        Text("Show placeholder even when unfocused")
                    }
                    Spacer(Modifier.height(16.dp))
                    TextField(
                        state = rememberTextFieldState(),
                        lineLimits = TextFieldLineLimits.SingleLine,
                        label = { Text("Label") },
                        labelPosition = TextFieldLabelPosition.Attached(alwaysMinimize = alwaysMinimizeLabel),
                        prefix = { Text("www.") },
                        suffix = { Text(".com") },
                        placeholder = { Text("google") },
                    )
                }
            }

            @Sampled
            @Composable
            fun TextFieldWithErrorState() {
                val errorMessage = "Text input too long"
                val state = rememberTextFieldState()
                var isError by rememberSaveable { mutableStateOf(false) }
                val charLimit = 10

                fun validate(text: CharSequence) {
                    isError = text.length > charLimit
                }

                LaunchedEffect(Unit) {
                    // Run validation whenever text value changes
                    snapshotFlow { state.text }.collect { validate(it) }
                }
                TextField(
                    state = state,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text(if (isError) "Username*" else "Username") },
                    supportingText = {
                        Row {
                            Text(if (isError) errorMessage else "", Modifier.clearAndSetSemantics {})
                            Spacer(Modifier.weight(1f))
                            Text("Limit: ${state.text.length}/$charLimit")
                        }
                    },
                    isError = isError,
                    onKeyboardAction = { validate(state.text) },
                    modifier =
                        Modifier.semantics {
                            maxTextLength = charLimit
                            // Provide localized description of the error
                            if (isError) error(errorMessage)
                        },
                )
            }


            @Composable
            fun TextFieldWithSupportingText() {
                TextField(
                    state = rememberTextFieldState(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                    supportingText = {
                        Text("Supporting text that is long and perhaps goes onto another line.")
                    },
                )
            }


            @Composable
            fun PasswordTextField() {
                var passwordHidden by rememberSaveable { mutableStateOf(true) }
                SecureTextField(
                    state = rememberTextFieldState(),
                    label = { Text("Enter password") },
                    textObfuscationMode =
                        if (passwordHidden) TextObfuscationMode.RevealLastTyped
                        else TextObfuscationMode.Visible,
                    trailingIcon = {
                        // Provide localized description for accessibility services
                        val description = if (passwordHidden) "Show password" else "Hide password"
                        TooltipBox(
                            positionProvider =
                                TooltipDefaults.rememberTooltipPositionProvider(TooltipAnchorPosition.Above),
                            tooltip = { PlainTooltip { Text(description) } },
                            state = rememberTooltipState(),
                        ) {
                            IconButton(onClick = { passwordHidden = !passwordHidden }) {
                                val visibilityIcon =
                                    if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                Icon(imageVector = visibilityIcon, contentDescription = description)
                            }
                        }
                    },
                )
            }


            @Composable
            fun TextFieldWithInitialValueAndSelection() {
                val state = rememberTextFieldState("Initial text", TextRange(0, 12))
                TextField(state = state, lineLimits = TextFieldLineLimits.SingleLine, label = { Text("Label") })
            }

            @Sampled
            @Composable
            fun OutlinedTextFieldWithInitialValueAndSelection() {
                val state = rememberTextFieldState("Initial text", TextRange(0, 12))
                OutlinedTextField(
                    state = state,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                )
            }

            @Sampled
            @Composable
            fun DenseTextFieldContentPadding() {
                TextField(
                    state = rememberTextFieldState(),
                    lineLimits = TextFieldLineLimits.SingleLine,
                    label = { Text("Label") },
                    // Need to set a min height using `heightIn` to override the default
                    modifier = Modifier.heightIn(min = 48.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 4.dp, start = 12.dp, end = 12.dp),
                )
            }


            @Composable
            fun TextFieldWithHideKeyboardOnImeAction() {
                val keyboardController = LocalSoftwareKeyboardController.current
                TextField(
                    state = rememberTextFieldState(),
                    label = { Text("Label") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    onKeyboardAction = { keyboardController?.hide() },
                )
            }

            @Composable
            fun TextArea() {
                val state =
                    rememberTextFieldState(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                                "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quisque " +
                                "nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                                "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
                                "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non  proident, sunt in " +
                                "culpa qui officia deserunt mollit anim id est laborum."
                    )
                TextField(state = state, modifier = Modifier.height(120.dp), label = { Text("Label") })
            }


            @Composable
            fun CustomTextFieldUsingDecorator() {
                val state = rememberTextFieldState()
                val interactionSource = remember { MutableInteractionSource() }
                val customColors =
                    TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        cursorColor = Color.Green,
                    )
                val enabled = true
                val isError = false
                val lineLimits = TextFieldLineLimits.SingleLine

                val textColor =
                    LocalTextStyle.current.color.takeOrElse {
                        customColors.textColor(
                            enabled = enabled,
                            isError = isError,
                            focused = interactionSource.collectIsFocusedAsState().value,
                        )
                    }

                BasicTextField(
                    state = state,
                    modifier = Modifier,
                    interactionSource = interactionSource,
                    enabled = enabled,
                    lineLimits = lineLimits,
                    // Colors of non-decorator elements (such as text color or cursor color)
                    // must be passed to BasicTextField
                    textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                    cursorBrush = SolidColor(customColors.cursorColor),
                    decorator =
                        TextFieldDefaults.decorator(
                            state = state,
                            outputTransformation = null,
                            lineLimits = lineLimits,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            container = {
                                TextFieldDefaults.Container(
                                    enabled = enabled,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = customColors,
                                    // Update indicator line thickness
                                    unfocusedIndicatorLineThickness = 2.dp,
                                    focusedIndicatorLineThickness = 4.dp,
                                )
                            },
                        ),
                )
            }


            @Composable
            fun CustomOutlinedTextFieldUsingDecorator() {
                val state = rememberTextFieldState()
                val interactionSource = remember { MutableInteractionSource() }
                val customColors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.Gray,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Green,
                    )
                val enabled = true
                val isError = false
                val lineLimits = TextFieldLineLimits.SingleLine

                val textColor =
                    LocalTextStyle.current.color.takeOrElse {
                        customColors.textColor(
                            enabled = enabled,
                            isError = isError,
                            focused = interactionSource.collectIsFocusedAsState().value,
                        )
                    }

                BasicTextField(
                    state = state,
                    modifier = Modifier,
                    interactionSource = interactionSource,
                    enabled = enabled,
                    lineLimits = lineLimits,
                    // Colors of non-decorator elements (such as text color or cursor color)
                    // must be passed to BasicTextField
                    textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                    cursorBrush = SolidColor(customColors.cursorColor),
                    decorator =
                        OutlinedTextFieldDefaults.decorator(
                            state = state,
                            outputTransformation = null,
                            lineLimits = lineLimits,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            container = {
                                OutlinedTextFieldDefaults.Container(
                                    enabled = enabled,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = customColors,
                                    // Update border thickness and shape
                                    shape = RectangleShape,
                                    unfocusedBorderThickness = 2.dp,
                                    focusedBorderThickness = 4.dp,
                                )
                            },
                        ),
                )
            }


            @Composable
            fun CustomTextFieldBasedOnDecorationBox() {
                var text by remember { mutableStateOf("") }
                val interactionSource = remember { MutableInteractionSource() }
                val customColors =
                    TextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.Gray,
                        focusedIndicatorColor = Color.Blue,
                        cursorColor = Color.Green,
                    )
                val enabled = true
                val isError = false
                val singleLine = true

                val textColor =
                    LocalTextStyle.current.color.takeOrElse {
                        customColors.textColor(
                            enabled = enabled,
                            isError = isError,
                            focused = interactionSource.collectIsFocusedAsState().value,
                        )
                    }

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier,
                    interactionSource = interactionSource,
                    enabled = enabled,
                    singleLine = singleLine,
                    // Colors of non-decoration-box elements (such as text color or cursor color)
                    // must be passed to BasicTextField
                    textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                    cursorBrush = SolidColor(customColors.cursorColor),
                    decorationBox = { innerTextField ->
                        TextFieldDefaults.DecorationBox(
                            value = text,
                            innerTextField = innerTextField,
                            visualTransformation = VisualTransformation.None,
                            singleLine = singleLine,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            container = {
                                TextFieldDefaults.Container(
                                    enabled = enabled,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = customColors,
                                    // Update indicator line thickness
                                    unfocusedIndicatorLineThickness = 2.dp,
                                    focusedIndicatorLineThickness = 4.dp,
                                )
                            },
                        )
                    },
                )
            }


            @Composable
            fun CustomOutlinedTextFieldBasedOnDecorationBox() {
                var text by remember { mutableStateOf("") }
                val interactionSource = remember { MutableInteractionSource() }
                val customColors =
                    OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.DarkGray,
                        unfocusedTextColor = Color.Gray,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Green,
                    )
                val enabled = true
                val isError = false
                val singleLine = true

                val textColor =
                    LocalTextStyle.current.color.takeOrElse {
                        val textColor: Color = customColors.textColor(enabled = enabled, isError = isError, focused = interactionSource.collectIsFocusedAsState.value)
                        textColor
                    }

                BasicTextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier,
                    interactionSource = interactionSource,
                    enabled = enabled,
                    singleLine = singleLine,
                    // Colors of non-decoration-box elements (such as text color or cursor color)
                    // must be passed to BasicTextField
                    textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor)),
                    cursorBrush = SolidColor(customColors.cursorColor),
                    decorationBox = { innerTextField ->
                        OutlinedTextFieldDefaults.DecorationBox(
                            value = text,
                            innerTextField = innerTextField,
                            visualTransformation = VisualTransformation.None,
                            singleLine = singleLine,
                            enabled = enabled,
                            isError = isError,
                            interactionSource = interactionSource,
                            container = {
                                OutlinedTextFieldDefaults.Container(
                                    enabled = enabled,
                                    isError = isError,
                                    interactionSource = interactionSource,
                                    colors = customColors,
                                    // Update border thickness and shape
                                    shape = RectangleShape,
                                    unfocusedBorderThickness = 2.dp,
                                    focusedBorderThickness = 4.dp,
                                )
                            },
                        )
                    },
                )
            }

                FloatingActionButton(
                    onClick = { /* Action */ }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }

annotation class Sampled

@Composable
fun FocusableTextField(label: String) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            // Monitor the focus state of the entire component
            .onFocusChanged { isFocused = it.isFocused },
        label = { Text(label) },
        leadingIcon = {
            // Focusable leading icon
            IconButton(onClick = { /* Action */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search Icon")
            }
        },
        trailingIcon = {
            // The "Close" button mentioned in your prompt
            if (text.isNotEmpty() || isFocused) {
                IconButton(onClick = { text = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        },
        // CRITICAL: maxLines = 1 allows Tab key to trigger focus move
        maxLines = 1,
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (isFocused) MaterialTheme.colorScheme.primary else Color.Gray
        )
    )

//}

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
}