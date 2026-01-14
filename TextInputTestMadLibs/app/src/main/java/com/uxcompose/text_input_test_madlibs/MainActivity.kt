package com.uxcompose.text_input_test_madlibs

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
//import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
//import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- Data Models ---

data class Blank(val id: String, val label: String)

data class Story(
    val id: Int,
    val title: String,
    val segments: List<String>,
    val blanks: List<Blank>
)

data class SavedStory(
    val timestamp: Long,
    val storyId: Int,
    val answers: Map<String, String>
)

// Hardcoded Data
val STORIES = listOf(
    Story(
        id = 1,
        title = "The Interview",
        segments = listOf(
            "I walked into the room and saw a ",
            " sitting in the easy chair. The interviewer asked me if ",
            " I wanted a beverage. I said YES and then felt very ",
            ", but I knew I would be ok, since it is a ",
            " role."
        ),
        blanks = listOf(
            Blank("noun1", "Noun (Object)"),
            Blank("verb1", "Verb (Action)"),
            Blank("adj1", "Adjective (Feeling)"),
            Blank("adj2", "Adjective (Descriptive)")
        )
    ),
    Story(
        id = 2,
        title = "The Drop",
        segments = listOf(
            "It was a dark and ",
            " night. The wind was ",
            " through the trees. Suddenly, a giant ",
            " jumped out and shouted '",
            "!' I dropped my ",
            " and ran away."
        ),
        blanks = listOf(
            Blank("adj1", "Adjective"),
            Blank("verbIng", "Verb ending in -ing"),
            Blank("noun1", "Noun (Creature)"),
            Blank("exclamation", "Silly Word/Sound"),
            Blank("noun2", "Noun (Object)")
        )
    ),
    Story(
        id = 3,
        title = "The Poet",
        segments = listOf(
            "so much depends ",
            " a red wheel ",
            " glazed with  ",
            " water '",
            "beside the ",
            " chickens."
        ),
        blanks = listOf(
            Blank("adj1", "Formal Preposition"),
            Blank("noun1", "Noun (Object)"),
            Blank("noun2", "Noun (Thing)"),
            Blank("verbIng", "Verb ending in -ing"),
            Blank("noun3", "Noun (Object)")
        )
    )
)

// --- Simple Repository for Persistence ---
object StoryRepository {
    private const val PREFS_NAME = "mad_libs_prefs"
    private const val KEY_SAVED_STORIES = "saved_stories_json"

    fun saveStory(context: Context, storyId: Int, answers: Map<String, String>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existingList = getSavedStories(context).toMutableList()

        // Add new story at the top
        existingList.add(0, SavedStory(System.currentTimeMillis(), storyId, answers))

        // Serialize to JSON
        val jsonArray = JSONArray()
        existingList.forEach { story ->
            val jsonObject = JSONObject()
            jsonObject.put("timestamp", story.timestamp)
            jsonObject.put("storyId", story.storyId)

            val answersJson = JSONObject()
            story.answers.forEach { (k, v) -> answersJson.put(k, v) }
            jsonObject.put("answers", answersJson)

            jsonArray.put(jsonObject)
        }

        prefs.edit { putString(KEY_SAVED_STORIES, jsonArray.toString()) }
    }

    fun getSavedStories(context: Context): List<SavedStory> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_SAVED_STORIES, null) ?: return emptyList()

        val list = mutableListOf<SavedStory>()
        try {
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val map = mutableMapOf<String, String>()
                val answersObj = obj.getJSONObject("answers")
                answersObj.keys().forEach { key ->
                    map[key] = answersObj.getString(key)
                }

                list.add(SavedStory(
                    timestamp = obj.getLong("timestamp"),
                    storyId = obj.getInt("storyId"),
                    answers = map
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun deleteStory(context: Context, timestamp: Long) {
        val currentList = getSavedStories(context).filter { it.timestamp != timestamp }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonArray = JSONArray()
        currentList.forEach { story ->
            val jsonObject = JSONObject()
            jsonObject.put("timestamp", story.timestamp)
            jsonObject.put("storyId", story.storyId)
            val answersJson = JSONObject()
            story.answers.forEach { (k, v) -> answersJson.put(k, v) }
            jsonObject.put("answers", answersJson)
            jsonArray.put(jsonObject)
        }
        prefs.edit { putString(KEY_SAVED_STORIES, jsonArray.toString()) }
    }
}

// --- Navigation States ---
enum class Screen { Menu, Input, Result, Gallery }

// --- Main Activity ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF4F46E5), // Indigo 600
                    secondary = Color(0xFF818CF8), // Indigo 400
                    tertiary = Color(0xFFC7D2FE), // Indigo 200
                    background = Color(0xFFFDFCF5), // Creamy white
                    surface = Color.White,
                    onPrimary = Color.White
                )
            ) {
                MadLibsApp()
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MadLibsApp() {
    var currentScreen by remember { mutableStateOf(Screen.Menu) }
    var selectedStory by remember { mutableStateOf<Story?>(null) }
    var answers by remember { mutableStateOf(mapOf<String, String>()) }
    var isViewingSaved by remember { mutableStateOf(false) } // Track if we are in read-only mode

    // Navigation Helper
    fun navigateToInput(story: Story) {
        selectedStory = story
        answers = emptyMap() // Reset answers
        isViewingSaved = false
        currentScreen = Screen.Input
    }

    fun navigateToResult(newAnswers: Map<String, String>) {
        answers = newAnswers
        currentScreen = Screen.Result
    }

    fun navigateToGallery() {
        currentScreen = Screen.Gallery
    }

    fun navigateToSavedResult(story: Story, savedAnswers: Map<String, String>) {
        selectedStory = story
        answers = savedAnswers
        isViewingSaved = true
        currentScreen = Screen.Result
    }

    fun navigateHome() {
        currentScreen = Screen.Menu
        selectedStory = null
        answers = emptyMap()
        isViewingSaved = false
    }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        // Animation Wrapper for smooth transitions
        AnimatedContent(
            targetState = currentScreen,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> -width } + fadeOut())
                } else {
                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                        slideOutHorizontally { width -> width } + fadeOut())
                }
            },
            label = "ScreenTransition"
        ) { screen ->
            when (screen) {
                Screen.Menu -> MenuScreen(
                    onStorySelected = { navigateToInput(it) },
                    onGalleryClick = { navigateToGallery() }
                )
                Screen.Input -> InputScreen(
                    story = selectedStory!!,
                    initialAnswers = answers,
                    onFinished = { newAnswers -> navigateToResult(newAnswers) },
                    onBack = { navigateHome() }
                )
                Screen.Result -> ResultScreen(
                    story = selectedStory!!,
                    answers = answers,
                    isReadOnly = isViewingSaved,
                    onPlayAgain = { navigateHome() },
                    onBack = { if(isViewingSaved) navigateToGallery() else navigateToInput(selectedStory!!) }
                )
                Screen.Gallery -> GalleryScreen(
                    onStoryClick = { story, savedAnswers -> navigateToSavedResult(story, savedAnswers) },
                    onBack = { navigateHome() }
                )
            }
        }
    }
}

// --- Screen 1: Story Selection ---
@Composable
fun MenuScreen(
    onStorySelected: (Story) -> Unit,
    onGalleryClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(content = {
            Spacer(modifier = Modifier.height(54.dp),
            )
        })
        // Logo / Header
        Box(
            modifier = Modifier
                .size(64.dp)
                .rotate(3f)
                .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Focus, Input,  TextField, Trapping Example",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937) // Dark Gray
        )

        Text(
            text = "Choose a story template to begin. Note any issues with text input, focus, trapping.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF6B7280) // Light Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // List of Stories
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(56.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(STORIES) { story ->
                StoryCard(story = story, onClick = { onStorySelected(story) })
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Gallery Button
                OutlinedButton(
                    onClick = onGalleryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.History, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("View Saved Stories")
                }
            }
        }
    }
}

@Composable
fun StoryCard(story: Story, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = story.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Start Story",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

// --- Screen 2: Input Form ---
@Composable
fun InputScreen(
    story: Story,
    initialAnswers: Map<String, String>,
    onFinished: (Map<String, String>) -> Unit,
    onBack: () -> Unit
) {
    var answers by remember { mutableStateOf(initialAnswers) }
    val scrollState = rememberScrollState()
    val isComplete = story.blanks.all { answers[it.id]?.isNotBlank() == true }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        // Back Button
        IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back", color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = story.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Fill in the blanks to reveal the story.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Form Fields
        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                story.blanks.forEach { blank ->
                    val value = answers[blank.id] ?: ""

                    OutlinedTextField(
                        value = value,
                        onValueChange = { answers = answers + (blank.id to it) },
                        label = { Text(blank.label) },
                        placeholder = { Text("Enter a word...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = {
                            Icon(Icons.Default.Edit, contentDescription = null, tint = Color.LightGray)
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onFinished(answers)
            },
            enabled = isComplete,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(100.dp) // Fully rounded
        ) {
            Text("Reveal Story", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(300.dp)) // Extra space for keyboard
    }
}

// --- Screen 3: Result ---
@Composable
fun ResultScreen(
    story: Story,
    answers: Map<String, String>,
    isReadOnly: Boolean,
    onPlayAgain: () -> Unit,
    onBack: () -> Unit
) {
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current
    var isSaved by remember { mutableStateOf(isReadOnly) } // If readonly, it's already saved

    // Build the story string
    val fullStoryText = buildAnnotatedString {
        story.segments.forEachIndexed { index, segment ->
            append(segment)
            val blank = story.blanks.getOrNull(index)
            if (blank != null) {
                val answer = answers[blank.id] ?: ""
                withStyle(SpanStyle(
                    background = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )) {
                    append(" $answer ")
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Back Button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isReadOnly) "Gallery" else "Edit", color = Color.Gray)
                }
            }
        }

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(32.dp)) {
                // Header line decoration
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = story.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = fullStoryText,
                    style = MaterialTheme.typography.bodyLarge,
                    lineHeight = 32.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Save Button (Only show if not already saved/read-only)
        if (!isReadOnly) {
            FilledTonalButton(
                onClick = {
                    StoryRepository.saveStory(context, story.id, answers)
                    isSaved = true
                    Toast.makeText(context, "Story saved to Gallery!", Toast.LENGTH_SHORT).show()
                },
                enabled = !isSaved,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Icon(if (isSaved) Icons.Default.BookmarkAdded else Icons.Default.BookmarkBorder, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isSaved) "Saved" else "Save to Gallery")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .height(50.dp)
                    .weight(1f)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Play Again")
            }

//            TextButton(
//                onClick = {
//                    clipboardManager.setText(AnnotatedString(fullStoryText.toString()))
//                    Toast.makeText(context, "Copied!", Toast.LENGTH_SHORT).show()
//                },
//                modifier = Modifier.height(50.dp)
//            ) {
//                Icon(Icons.Default.ContentCopy, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text("Copy")
//            }


        }
    }
}

// --- Screen 4: Gallery ---
@Composable
fun GalleryScreen(
    onStoryClick: (Story, Map<String, String>) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var savedStories by remember { mutableStateOf(StoryRepository.getSavedStories(context)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack, modifier = Modifier.offset(x = (-12).dp)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Saved Stories",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (savedStories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Book,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No saved stories yet.", color = Color.Gray)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(savedStories) { savedStory ->
                    val originalStory = STORIES.find { it.id == savedStory.storyId }
                    if (originalStory != null) {
                        SavedStoryCard(
                            savedStory = savedStory,
                            title = originalStory.title,
                            onClick = { onStoryClick(originalStory, savedStory.answers) },
                            onDelete = {
                                StoryRepository.deleteStory(context, savedStory.timestamp)
                                savedStories = StoryRepository.getSavedStories(context)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedStoryCard(savedStory: SavedStory, title: String, onClick: () -> Unit, onDelete: () -> Unit) {
    val dateStr = remember(savedStory.timestamp) {
        SimpleDateFormat("MMM dd, yyyy • h:mm a", Locale.getDefault()).format(Date(savedStory.timestamp))
    }

    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = dateStr, color = Color.Gray, fontSize = 12.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.LightGray)
            }
        }
    }
}