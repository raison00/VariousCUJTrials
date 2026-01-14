package com.example.inputoptionsapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api // Added for OptIn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import android.view.View


// Main Composable function that demonstrates different input and focus needs.
@OptIn(ExperimentalMaterial3Api::class) // Added for TopAppBar
@Composable
fun InputFocusScreen() {
    // 1. Focus Management Setup
    // LocalFocusManager is used to request global focus changes (like moving to the next field).
    val focusManager = LocalFocusManager.current

    // FocusRequesters are used to programmatically move focus to specific fields.
    val (emailFocus, passwordFocus, numberFocus) = FocusRequester.createRefs()

    // 2. State Management for Inputs
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var number by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("Enter your details above.") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compose Input & Focus Demo") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // --- Input 1: Email (ImeAction.Next to move focus) ---
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(emailFocus), // Attach the FocusRequester
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next // 'Next' button on keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) } // Explicitly move down
                    ),
                    singleLine = true
                )

                // --- Input 2: Password (VisualTransformation and Custom KeyboardAction) ---
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility")
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(passwordFocus),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next // 'Next' button on keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) } // Explicitly move down
                    ),
                    singleLine = true
                )

                // --- Input 3: Number (ImeAction.Done to dismiss keyboard) ---
                OutlinedTextField(
                    value = number,
                    onValueChange = { if (it.length <= 5) number = it.filter { char -> char.isDigit() } },
                    label = { Text("5-Digit Code") },
                    leadingIcon = { Text("#") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(numberFocus),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done // 'Done' button on keyboard
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            focusManager.clearFocus() // Clear focus, which typically dismisses the keyboard
                            resultText = "Data entered:\nEmail: $email\nPassword: ${"•".repeat(password.length)}\nCode: $number"
                        }
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Action Button ---
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        resultText = "Data submitted via button:\nEmail: $email\nPassword: ${"•".repeat(password.length)}\nCode: $number"
                    },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    enabled = email.isNotBlank() && password.isNotBlank() && number.length == 5
                ) {
                    Text("Submit Data")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- Result Display ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Text(
                        text = resultText,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    )

    // 3. Initial Focus Request (Side Effect)
    // Use LaunchedEffect to request focus on the first field once the composable is first launched.
    // The key 'Unit' ensures this block runs only once.
    LaunchedEffect(Unit) {
        emailFocus.requestFocus()
    }
}

// Mockup of a simple Theme structure for preview purposes
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFF3700B3),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun InputFocusScreenPreview() {
    AppTheme {
        // Apply a dark background for better contrast in the preview
        Box(Modifier.background(Color(0xFF121212)).fillMaxSize()) {
            InputFocusScreen()
        }
    }
}

// Note: In a real Android app, InputFocusScreen() would be called from the MainActivity's onCreate
// Example: setContent { AppTheme { InputFocusScreen() } }
