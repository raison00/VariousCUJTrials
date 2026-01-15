# HomeHub Auto - Architecture Documentation

## Overview

HomeHub Auto is an Android Auto IoT application built using the Car App Library and Jetpack Compose. This document describes the architectural decisions, design patterns, and implementation details.

## Architecture Pattern

The app follows a **simplified MVVM (Model-View-ViewModel)** pattern adapted for Android Auto's template-based UI system.

```
┌─────────────────────────────────────────────────────┐
│                 Android Auto System                  │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              HomeHubCarAppService                    │
│  (Entry point - extends CarAppService)               │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                HomeHubSession                        │
│  (Manages app lifecycle and initial screen)          │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                   Screens                            │
│  - HomeScreen (Quick Actions)                        │
│  - DevicesListScreen (All Devices)                   │
│  - DeviceControlScreen (Individual Control)          │
│  - ScenesScreen (Automation Scenes)                  │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│              DeviceRepository                        │
│  (Business logic and state management)               │
└────────────────────┬────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────┐
│                Data Models                           │
│  - Device, DeviceState, DeviceType                   │
│  - Scene, SceneAction                                │
└─────────────────────────────────────────────────────┘
```

## Layer Breakdown

### 1. Service Layer

#### HomeHubCarAppService
- **Purpose**: Entry point for Android Auto
- **Responsibilities**:
  - Validate host (car system)
  - Create app session
  - Lifecycle management
- **Key Methods**:
  - `createHostValidator()`: Security validation
  - `onCreateSession()`: Session initialization

#### HomeHubSession
- **Purpose**: Manages app session lifecycle
- **Responsibilities**:
  - Create initial screen
  - Handle deep links (future)
  - Manage session state
- **Key Methods**:
  - `onCreateScreen()`: Returns HomeScreen

### 2. Presentation Layer (Screens)

All screens extend `androidx.car.app.Screen` and use Car App Library templates.

#### HomeScreen
- **Template**: GridTemplate
- **Purpose**: Quick access to most-used devices
- **Features**:
  - 6 device quick action buttons
  - Real-time status updates
  - Navigation to other screens
- **State Management**: 
  - Observes `DeviceRepository.devices` StateFlow
  - Auto-invalidates on state changes

#### DevicesListScreen
- **Template**: ListTemplate
- **Purpose**: Browse all devices organized by room
- **Features**:
  - Grouped by room
  - Tap to open device control
  - Real-time status in list
- **Navigation**: Pushes DeviceControlScreen on item tap

#### DeviceControlScreen
- **Template**: PaneTemplate
- **Purpose**: Control individual device
- **Features**:
  - Device-specific controls
  - Brightness adjustment (lights)
  - Temperature control (thermostat)
  - Toggle actions (all devices)
- **Dynamic UI**: Actions change based on device type

#### ScenesScreen
- **Template**: ListTemplate
- **Purpose**: Activate preset automation scenes
- **Features**:
  - List of available scenes
  - Scene descriptions
  - Confirmation after activation
- **Flow**: Scene selection → Activation → Confirmation screen

### 3. Data Layer

#### DeviceRepository (Singleton)
- **Purpose**: Central data management and business logic
- **Pattern**: Singleton with StateFlow for reactive updates
- **Responsibilities**:
  - Maintain device state
  - Provide device CRUD operations
  - Handle scene activation
  - Simulate backend delays
- **State Management**:
  - `StateFlow<List<Device>>` for devices
  - `StateFlow<List<Scene>>` for scenes
  - Coroutine-based async operations

#### Data Models

**Device**
```kotlin
data class Device(
    val id: String,
    val name: String,
    val type: DeviceType,
    val room: String,
    var state: DeviceState,
    val isControllable: Boolean = true
)
```

**DeviceState**
```kotlin
data class DeviceState(
    val isOn: Boolean = false,
    val isOpen: Boolean = false,
    val isLocked: Boolean = true,
    val isArmed: Boolean = false,
    val brightness: Int = 100,
    val temperature: Int = 72
)
```

**DeviceType**
```kotlin
enum class DeviceType {
    LIGHT, GARAGE_DOOR, DOOR_LOCK, THERMOSTAT, SECURITY
}
```

**Scene**
```kotlin
data class Scene(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val actions: List<SceneAction>
)
```

### 4. UI/Theme Layer

#### Color System
- **Purple Theme**: Primary brand color
  - Purple 900 (#4A148C) - Background
  - Purple 800 (#6A1B9A) - Surface
  - Purple 500 (#9C27B0) - Primary
- **Turquoise Theme**: Secondary/Accent
  - Turquoise 500 (#00BCD4) - Secondary
  - Turquoise 300 (#4DD0E1) - Accent
  - Turquoise 400 (#26C6DA) - Status On

#### Theme Implementation
- Material 3 Dark Color Scheme
- Custom colors for Car App Library icons
- High contrast for automotive displays

### 5. Utilities

#### CarIcons
- **Purpose**: Create themed icons for Car App Library
- **Features**:
  - Device-specific icons
  - Status-based coloring (on/off)
  - Custom bitmap generation
- **Color Integration**: Uses theme colors for consistency

#### Constants
- **Purpose**: Centralized configuration
- **Contains**:
  - Temperature limits
  - Brightness ranges
  - Simulated delays

## Design Patterns

### 1. Singleton Pattern
**Used in**: DeviceRepository
**Reason**: Single source of truth for device state across all screens

### 2. Observer Pattern
**Used in**: Screen invalidation
**Implementation**: Kotlin StateFlow + Coroutines
**Benefit**: Automatic UI updates when data changes

### 3. Repository Pattern
**Used in**: DeviceRepository
**Reason**: Abstracts data source (currently simulated, easily replaceable with real API)

### 4. Template Method Pattern
**Used in**: Car App Library screens
**Implementation**: `onGetTemplate()` override
**Benefit**: Consistent screen lifecycle management

## State Management

### Flow of State Updates

```
User Action (Tap Button)
    ↓
Screen onClick Handler
    ↓
DeviceRepository Method (suspend fun)
    ↓
Update StateFlow
    ↓
StateFlow Emission
    ↓
Screen Observer Triggered
    ↓
Screen.invalidate()
    ↓
onGetTemplate() Called
    ↓
UI Refreshed with New State
```

### Lifecycle-Aware Observation

```kotlin
override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)
    lifecycleScope.launch {
        repository.devices.collectLatest {
            invalidate()
        }
    }
}
```

- Coroutines tied to screen lifecycle
- Automatic cleanup when screen destroyed
- No memory leaks

## Threading Model

### Main Thread
- UI rendering (Car App Library templates)
- Template building
- User interaction callbacks

### Background Thread (Coroutines)
- Device state updates
- Repository operations
- Simulated network delays

### Coroutine Scopes
- **lifecycleScope**: Tied to screen lifecycle
- **Repository operations**: Use `suspend` functions

## Navigation

### Screen Stack Management

```
HomeScreen (Root)
    ├─→ DevicesListScreen
    │       └─→ DeviceControlScreen
    └─→ ScenesScreen
            └─→ SceneConfirmationScreen
```

### Navigation Methods
- `screenManager.push(screen)`: Add to stack
- `screenManager.pop()`: Remove top screen
- `Action.BACK`: Automatic back navigation

## Car App Library Integration

### Template Selection Rationale

| Screen | Template | Reason |
|--------|----------|--------|
| HomeScreen | GridTemplate | Quick visual access to devices |
| DevicesListScreen | ListTemplate | Scrollable list with grouping |
| DeviceControlScreen | PaneTemplate | Detailed view with actions |
| ScenesScreen | ListTemplate | Simple list selection |
| Confirmation | MessageTemplate | Full-screen feedback |

### Driver Distraction Guidelines

**Compliance Measures**:
1. **Template-based UI**: No custom views while driving
2. **Large touch targets**: Minimum 48dp (enforced by templates)
3. **Limited text**: Short titles and descriptions
4. **Quick interactions**: Single tap actions
5. **High contrast**: Purple/turquoise for visibility

## Extensibility

### Adding New Device Types

1. **Add to DeviceType enum**
```kotlin
enum class DeviceType {
    LIGHT, GARAGE_DOOR, DOOR_LOCK, THERMOSTAT, SECURITY,
    CAMERA // New type
}
```

2. **Update DeviceState**
```kotlin
data class DeviceState(
    // existing fields...
    val isRecording: Boolean = false // New field
)
```

3. **Add control logic in DeviceControlScreen**
```kotlin
DeviceType.CAMERA -> {
    actions.add(
        Action.Builder()
            .setTitle("Start Recording")
            .setOnClickListener { /* ... */ }
            .build()
    )
}
```

### Integrating Real Smart Home APIs

Replace simulated repository with real implementation:

```kotlin
class RealDeviceRepository : DeviceRepository {
    private val apiClient = SmartHomeApiClient()
    
    override suspend fun updateDevice(deviceId: String, newState: DeviceState): Boolean {
        return try {
            apiClient.updateDevice(deviceId, newState)
            // Update local state
            true
        } catch (e: Exception) {
            false
        }
    }
}
```

## Testing Strategy

### Unit Tests
- **Target**: DeviceRepository logic
- **Framework**: JUnit
- **Coverage**: Device operations, scene activation

### Integration Tests
- **Target**: Screen templates
- **Framework**: Car App Library Testing
- **Coverage**: Template building, navigation

### UI Tests
- **Target**: End-to-end flows
- **Tool**: Desktop Head Unit
- **Coverage**: User interactions, state updates

## Performance Considerations

### Memory Management
- Singleton repository prevents multiple instances
- StateFlow uses conflated emission (only latest state)
- Lifecycle-aware coroutines prevent leaks

### Rendering Performance
- Template-based UI is optimized by Car App Library
- Minimal custom rendering
- Efficient state invalidation

### Network Simulation
- Artificial delays (300ms) simulate real API calls
- Prepared for async operations
- Non-blocking UI updates

## Security Considerations

### Host Validation
```kotlin
override fun createHostValidator(): HostValidator {
    return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
}
```
- Currently allows all hosts for development
- Production should use specific allowlist

### Data Privacy
- No personal data collected
- Device states stored in memory only
- No network communication (simulated)

## Future Architecture Enhancements

### 1. ViewModel Layer
Add ViewModels for complex state management:
```kotlin
class DeviceViewModel : ViewModel() {
    private val repository = DeviceRepository.getInstance()
    val devices = repository.devices.asLiveData()
}
```

### 2. Dependency Injection
Use Hilt for better testability:
```kotlin
@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val repository: DeviceRepository
) : ViewModel()
```

### 3. Local Persistence
Add Room database for offline support:
```kotlin
@Entity
data class DeviceEntity(
    @PrimaryKey val id: String,
    val name: String,
    // ...
)
```

### 4. Remote Configuration
Firebase Remote Config for dynamic scenes:
```kotlin
val remoteConfig = Firebase.remoteConfig
val scenes = remoteConfig.getString("available_scenes")
```

## Conclusion

HomeHub Auto demonstrates a clean, maintainable architecture for Android Auto apps using modern Android development practices. The separation of concerns, reactive state management, and template-based UI create a robust foundation for a production-ready smart home controller.
