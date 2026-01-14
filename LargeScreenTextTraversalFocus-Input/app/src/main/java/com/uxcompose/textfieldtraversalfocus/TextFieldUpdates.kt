package com.uxcompose.textfieldtraversalfocus


import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        MainScreenContent(innerPadding = innerPadding)
    }
}

@Composable
fun MainScreenContent(innerPadding: PaddingValues) {
    // We initialize the state here to pass it down to the components
    val searchFieldState = rememberTextFieldState()
    val standaloneFieldState = rememberTextFieldState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .consumeWindowInsets(innerPadding)
            .padding(innerPadding)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Now passing the required 'state' parameter
        EnhancedSearchField(state = searchFieldState)

        MyForm2()

        FocusableTextField2(
            state = standaloneFieldState,
            label = "Standalone Modern Field"
        )
    }
}

@Composable
fun EnhancedSearchField2(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    onSearchTriggered: (String) -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val shakeOffset = remember { Animatable(0f) }
    val maxChars = 15

    LaunchedEffect(state) {
        snapshotFlow { state.text }
            .collectLatest { currentText ->
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

    OutlinedTextField(
        state = state,
        modifier = modifier
            .fillMaxWidth()
            .offset(x = shakeOffset.value.dp),
        label = { Text("Search Products") },
        placeholder = { Text("Try 'Modern Compose'...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        inputTransformation = InputTransformation.maxLength(maxChars).then {
            if (asCharSequence().length >= maxChars) triggerShake()
            if (asCharSequence().any { it.isUpperCase() }) {
                val lowered = asCharSequence().toString().lowercase()
                replace(0, length, lowered)
            }
        },
        outputTransformation = {
            if (length > 0 && !asCharSequence().startsWith("#")) {
                insert(0, "#")
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        onKeyboardAction = { onSearchTriggered(state.text.toString()) },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun FocusableTextField2(
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
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (state.text.isNotEmpty()) {
                IconButton(onClick = { state.clearText() }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear text")
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        onKeyboardAction = { onAction() },
        lineLimits = TextFieldLineLimits.SingleLine
    )
}

@Composable
fun MyForm2() {
    val nameState = rememberTextFieldState()
    val emailState = rememberTextFieldState()

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Registration Form", style = MaterialTheme.typography.titleMedium)
        FocusableTextField(
            state = nameState,
            label = "Full Name"
        )

        FocusableTextField(
            state = emailState,
            label = "Email Address",
            imeAction = ImeAction.Done
        )
    }
}