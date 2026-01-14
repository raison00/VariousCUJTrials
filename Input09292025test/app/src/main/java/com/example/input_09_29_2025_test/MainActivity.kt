import android.os.Bundle
package com.example.Input-09-29-2025-test
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// Assuming you have a MyAppTheme composable defined elsewhere for your theme.

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // NOTE: The 'MyAppTheme' is assumed to be defined in your actual project.
            // If it's not, replace it with 'YourAppNameTheme' or similar from your project's theme file.
            MyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UserNameInput() // ✅ Now calls the correctly named function
                }
            }
        }
    }
}
// Define a simple light color scheme
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),    // A nice purple
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)

// Define a simple dark color scheme
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    secondary = Color(0xFFCCC2DC),
    tertiary = Color(0xFFEFB8C8),
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onPrimary = Color(0xFF381E72),
    onSecondary = Color(0xFF332D41),
    onTertiary = Color(0xFF492532),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5),
)

@Composable
fun MyAppTheme(
    // A simple way to toggle between dark and light themes (though better logic uses system settings)
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        // You can define typography and shapes here if you have them
        typography = MaterialTheme.typography,
        content = content
    )
}
@Composable
// RENAMED: Changed from 'UserNameInputPrototype' to 'UserNameInput'
fun UserNameInput() {
    // 1. State Hoisting: 'remember' ensures the state is retained across recompositions.
    // 'mutableStateOf' holds the current value of the text field.
    var userName by remember { mutableStateOf("") } //

    // Use a Column to stack the text field and the output text vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(36.dp)
    ) {
        // 2. OutlinedTextField: A Material 3 text field with an outline style.
        OutlinedTextField(
            // The current text displayed in the input field
            value = userName,

            // Lambda that gets called every time the input text changes
            onValueChange = { newText ->
                // Update the state with the new text
                userName = newText
            },

            // Optional: The hint/placeholder text inside the field
            label = {
                Text("Enter Your Name")
            },

            // Optional: Helper text below the field
            supportingText = {
                Text("Your name is required for profile creation.")
            },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp)) // Add vertical space

        // 3. Output Text: Displays the user's input dynamically
        Surface(color = Color.White) {
            Text(
                text = if (userName.isBlank()) {
                    "Hello there! Please enter your name above."
                } else {
                    "Hello, $userName! Ready to proceed."
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


// --- Preview Function for Android Studio ---
@Preview(showBackground = true)
@Composable
fun UserNameInputPreview() {
    MyAppTheme {
        UserNameInput() // ✅ Now calls the correctly named function
    }
}