# Quick Reference Guide

## TV Leanback Components

### TVLeanbackButton
```kotlin
TVLeanbackButton(
    text = "Home",
    onClick = { },
    glowStyle = GlowStyle.PULSE,      // PULSE, SHIMMER, BREATHE, STATIC
    glowColor = TVFocusStyles.GlowBlue,
    contentDescription = "Home button"
)
```

### TVFocusableCard
```kotlin
TVFocusableCard(
    title = "Movie Title",
    subtitle = "2024",
    onClick = { },
    glowColor = TVFocusStyles.GlowPurple,
    imageContent = { /* Image composable */ }
)
```

### TVMenuButton
```kotlin
TVMenuButton(
    text = "Settings",
    icon = { Icon(Icons.Default.Settings, null) },
    onClick = { },
    isSelected = currentTab == "settings"
)
```

---

## Focus Groups

### Setup
```kotlin
val manager = remember { FocusGroupManager() }
```

### Create Group
```kotlin
FocusGroupContainer(
    groupId = "sidebar",
    groupName = "Sidebar Navigation",
    manager = manager,
    isTrapped = false,           // true for dialogs
    showBorder = true,            // show group border
    onGroupFocused = { }
) {
    // Content
}
```

### Add Items
```kotlin
FocusGroupItem { focusRequester, isFocused ->
    Button(
        onClick = { },
        modifier = Modifier.focusable()
    ) {
        Text("Item")
    }
}
```

### Navigation
```kotlin
// Keyboard shortcuts
FocusGroupKeyHandler(
    manager = manager,
    groupNavigationKeys = mapOf(
        Key.F1 to "sidebar",
        Key.F2 to "content"
    )
) { /* Layout */ }

// Programmatic
manager.moveFocusToGroup("content")
```

---

## Accessibility Components

### Accessible Button
```kotlin
AccessibleFocusableButton(
    text = "Submit",
    onClick = { },
    contentDescription = "Submit form",
    stateDescription = "Ready",
    enabled = true,
    isLoading = false,
    errorMessage = null,
    customActions = listOf(
        CustomAccessibilityAction("Save draft") {
            saveDraft()
            true
        }
    )
)
```

### Accessible Card
```kotlin
AccessibleFocusableCard(
    title = "Card Title",
    subtitle = "Subtitle",
    description = "Description text",
    onClick = { },
    isSelected = false,
    badge = "NEW",
    customActions = listOf(/* ... */)
)
```

### Live Region
```kotlin
AccessibleLiveRegion(
    text = statusMessage,
    politeness = LiveRegionMode.Polite  // or Assertive
)
```

### Heading
```kotlin
AccessibleHeading(
    text = "Section Title",
    level = 1  // 1, 2, or 3
)
```

### Text Field
```kotlin
AccessibleTextField(
    value = text,
    onValueChange = { text = it },
    label = "Email",
    placeholder = "you@example.com",
    isRequired = true,
    helperText = "Help text",
    errorMessage = "Error message"
)
```

---

## Custom Focus Effects

### Radial Glow
```kotlin
.drawBehind {
    if (isFocused) {
        val brush = Brush.radialGradient(
            colors = listOf(
                Color.Cyan.copy(alpha = 0.6f),
                Color.Cyan.copy(alpha = 0.3f),
                Color.Transparent
            )
        )
        drawRect(brush = brush)
    }
}
```

### Pulsing Border
```kotlin
val infiniteTransition = rememberInfiniteTransition()
val alpha by infiniteTransition.animateFloat(
    initialValue = 0.4f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
        animation = tween(1000),
        repeatMode = RepeatMode.Reverse
    )
)

.border(4.dp, Color.Cyan.copy(alpha = alpha))
```

### Scale Animation
```kotlin
val scale by animateFloatAsState(
    targetValue = if (isFocused) 1.1f else 1f,
    animationSpec = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy
    )
)

.scale(scale)
```

### Colored Shadow
```kotlin
.shadow(
    elevation = if (isFocused) 16.dp else 4.dp,
    spotColor = if (isFocused) Color.Cyan.copy(0.5f) else Color.Black
)
```

---

## Semantic Properties

### Basic Semantics
```kotlin
.semantics {
    contentDescription = "Play button"
    role = Role.Button
    stateDescription = "Playing"
}
```

### States
```kotlin
.semantics {
    selected = isSelected
    disabled = !enabled
    error("Error message")
}
```

### Collections
```kotlin
.semantics {
    collectionInfo = CollectionInfo(
        rowCount = items.size,
        columnCount = 1
    )
}

// Item
.semantics {
    collectionItemInfo = CollectionItemInfo(
        rowIndex = index,
        rowSpan = 1,
        columnIndex = 0,
        columnSpan = 1
    )
}
```

### Custom Actions
```kotlin
.semantics {
    customActions = listOf(
        CustomAccessibilityAction("Delete") {
            delete()
            true
        }
    )
}
```

### Navigation
```kotlin
.semantics {
    heading()                    // Mark as heading
    isTraversalGroup = true      // Focus group
    invisibleToUser()            // Hide from TalkBack
}
```

---

## TalkBack Utilities

### Check if Enabled
```kotlin
val isTalkBackEnabled = rememberIsTalkBackEnabled()
```

### Manual Announcement
```kotlin
val context = LocalContext.current
context.announceForAccessibility("Item added to cart")
```

### Announce on State Change
```kotlin
LaunchedEffect(isFocused) {
    if (isTalkBackEnabled && isFocused) {
        context.announceForAccessibility("Button focused")
    }
}
```

---

## Color Specifications

### TV Colors
```kotlin
TVFocusStyles.FocusBorder       // Color(0xFFFFD600) - Amber
TVFocusStyles.FocusBackground   // Color(0xFF2C2C2C) - Dark gray
TVFocusStyles.GlowBlue          // Color(0xFF00D9FF) - Cyan
TVFocusStyles.GlowPurple        // Color(0xFFBB86FC) - Purple
TVFocusStyles.GlowGreen         // Color(0xFF03DAC6) - Teal
```

### Mobile Colors
```kotlin
FocusColors.FocusBorder         // Color(0xFF00D9FF) - Cyan
FocusColors.FocusBackground     // Color(0xFF1A4D5C) - Dark teal
FocusColors.DefaultBackground   // Color(0xFF2196F3) - Blue
```

---

## Contrast Requirements (WCAG 2.1 AA)

| Type | Minimum Ratio | Example |
|------|---------------|---------|
| Normal Text | 4.5:1 | White on Blue |
| Large Text | 3:1 | White on Dark Teal |
| UI Components | 3:1 | Cyan on Dark Teal |
| Focus Indicator | 3:1 | Cyan border |

---

## Touch Target Sizes

| Platform | Minimum Size |
|----------|--------------|
| Mobile | 48x48 dp |
| Tablet | 48x48 dp |
| TV | 64x64 dp |

```kotlin
// Mobile/Tablet
.heightIn(min = 48.dp)

// TV
.height(64.dp)
```

---

## Animation Durations

| Effect | Duration | Easing |
|--------|----------|--------|
| Border fade | 200ms | Linear |
| Elevation change | 300ms | FastOutSlowIn |
| Scale | 300ms | Spring (Medium Bouncy) |
| Pulse | 1000ms | FastOutSlowIn |
| Shimmer | 2000ms | Linear |
| Breathe | 2000ms | FastOutSlowIn |

---

## Input Detection

```kotlin
var isFocused by remember { mutableStateOf(false) }

Button(
    onClick = {
        // Touch or Mouse click
        handleClick()
    },
    modifier = Modifier
        .onFocusChanged { focusState ->
            if (focusState.isFocused && !isFocused) {
                // Keyboard or D-Pad navigation
                handleKeyboardFocus()
            }
            isFocused = focusState.isFocused
        }
        .focusable()
)
```

---

## Common Patterns

### Simple Focusable Button
```kotlin
var isFocused by remember { mutableStateOf(false) }

Button(
    onClick = onClick,
    modifier = Modifier
        .onFocusChanged { isFocused = it.isFocused }
        .focusable()
        .border(
            width = if (isFocused) 4.dp else 0.dp,
            color = Color.Cyan,
            shape = RoundedCornerShape(8.dp)
        )
)
```

### TV Button with Glow
```kotlin
TVLeanbackButton(
    text = "Action",
    onClick = onClick,
    glowStyle = GlowStyle.PULSE,
    glowColor = TVFocusStyles.GlowBlue
)
```

### Accessible Button
```kotlin
AccessibleFocusableButton(
    text = "Submit",
    onClick = onClick,
    contentDescription = "Submit form"
)
```

### Dialog with Focus Trap
```kotlin
FocusGroupContainer(
    groupId = "dialog",
    groupName = "Dialog",
    manager = manager,
    isTrapped = true
) {
    // Dialog content
}
```

### Card with Custom Actions
```kotlin
AccessibleFocusableCard(
    title = "Item",
    onClick = { },
    customActions = listOf(
        CustomAccessibilityAction("Delete") { 
            delete()
            true 
        }
    )
)
```

---

## Keyboard Shortcuts

| Key | Action |
|-----|--------|
| Tab | Move focus forward |
| Shift+Tab | Move focus backward |
| Enter | Activate focused element |
| Space | Activate focused element |
| Arrow Keys | Navigate in grids |
| F1-F12 | Custom group navigation |
| Escape | Close dialog |

---

## Testing Commands

### Check Contrast
```
https://webaim.org/resources/contrastchecker/
```

### Screen Size Testing
```bash
# Mobile
adb shell wm size 480x800

# Tablet
adb shell wm size 1280x800

# TV
adb shell wm size 1920x1080

# Reset
adb shell wm size reset
```

### Enable TalkBack
```
Settings → Accessibility → TalkBack → On
```

---

## Dependencies

```kotlin
// build.gradle.kts
dependencies {
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
}
```

---

## Resources

- [Complete Implementation](MainActivity.kt)
- [TV Leanback Code](TVLeanbackButton.kt)
- [Focus Groups Code](FocusGroups.kt)
- [Accessibility Code](Accessibility.kt)
- [Advanced Features Guide](ADVANCED_FEATURES_GUIDE.md)
- [WCAG Compliance Guide](WCAG_COMPLIANCE_GUIDE.md)
- [Implementation Guide](IMPLEMENTATION_GUIDE.md)