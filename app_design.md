# Smart Home Controller for Android Auto - Design Document

## App Concept

**Name**: HomeHub Auto

**Category**: IoT (Internet of Things)

**Purpose**: A driver-optimized smart home controller that allows users to manage their connected home devices directly from their car's display. Perfect for controlling lights, garage doors, thermostats, and security systems as they approach or leave home.

## Target Use Cases

1. **Arriving Home**: Open garage door, turn on lights, adjust thermostat
2. **Leaving Home**: Close garage, turn off lights, arm security system
3. **Quick Actions**: Toggle specific devices while parked or at traffic lights
4. **Status Monitoring**: Check device states (is garage closed? are lights on?)

## App Architecture

### Technology Stack
- **UI Framework**: Jetpack Compose 1.9.4 (latest stable)
- **Car App Library**: androidx.car.app:app:1.7.0
- **Platform**: Android Auto (phone-based projection)
- **Minimum SDK**: API 28 (Android 9)
- **Language**: Kotlin

### Core Components

1. **CarAppService**: Main service that Android Auto connects to
2. **Session**: Manages the app lifecycle and screen navigation
3. **Screens**: Template-based UI screens using Car App Library
   - Home Screen: Quick actions grid
   - Devices List Screen: Browse all devices by room
   - Device Control Screen: Control individual device
   - Scenes Screen: Predefined automation scenes

### Screen Templates

Using Car App Library templates for driver safety:
- **GridTemplate**: For quick action buttons (Home screen)
- **ListTemplate**: For browsing devices by category/room
- **PaneTemplate**: For device details and controls
- **MessageTemplate**: For confirmations and status messages

## Feature Set

### Core Features (MVP)
1. **Quick Actions Dashboard**: 6-8 most common actions in a grid
2. **Device Categories**: 
   - Lights (on/off, brightness)
   - Garage Door (open/close)
   - Thermostat (temperature up/down)
   - Security (arm/disarm)
3. **Preset Scenes**:
   - "Arriving Home"
   - "Leaving Home"
   - "Good Night"
4. **Device Status**: Real-time status indicators

### Simulated Smart Home Backend
Since this is a demonstration app, we'll simulate smart home devices with:
- In-memory device state management
- Mock device types (lights, garage, thermostat, locks)
- Simulated response delays for realism
- Persistent preferences using SharedPreferences

## User Interface Design

### Color Scheme
- **Primary Color**: Purple (#9C27B0 - Material Purple 500)
- **Secondary Color**: Turquoise (#00BCD4 - Material Cyan 500)
- **Accent**: Light Turquoise (#4DD0E1 - Material Cyan 300)
- **Background**: Dark Purple (#4A148C - Material Purple 900)
- **Surface**: Medium Purple (#6A1B9A - Material Purple 800)
- **Text**: White (#FFFFFF)
- **Icons**: Turquoise with purple backgrounds

### Visual Style
- Modern, clean interface optimized for glanceability
- Large touch targets (minimum 48dp) for driver safety
- High contrast for visibility in various lighting conditions
- Consistent iconography using Material Icons
- Purple gradient backgrounds with turquoise accents

## Screen Flow

```
Launch
  ↓
Home Screen (Quick Actions Grid)
  ├→ All Devices (List by Room)
  │   └→ Device Control
  ├→ Scenes (Preset Automations)
  └→ Settings (Preferences)
```

## Device Types to Simulate

1. **Living Room Lights** (Dimmable)
2. **Bedroom Lights** (On/Off)
3. **Garage Door** (Open/Close with status)
4. **Front Door Lock** (Lock/Unlock)
5. **Thermostat** (Temperature control)
6. **Security System** (Arm/Disarm)
7. **Porch Lights** (On/Off)
8. **Kitchen Lights** (On/Off)

## Technical Implementation Notes

### Car App Library Integration
- Use `Screen` class for each view
- Implement `onGetTemplate()` to return appropriate templates
- Handle user actions through `OnClickListener` callbacks
- Use `ScreenManager` for navigation
- Implement proper lifecycle management

### Compose Integration
While Car App Library uses templates (not full Compose), we'll use Compose for:
- Any companion phone app screens (settings, setup)
- Custom UI elements where permitted
- Modern Kotlin coroutines and Flow for state management

### Safety Considerations
- Limit interactions while driving (use templates)
- Provide voice feedback for actions
- Quick confirmations without requiring extended attention
- Large, easy-to-tap buttons
- Clear visual feedback for all actions

## Project Structure

```
com.homehub.auto/
├── service/
│   ├── HomeHubCarAppService.kt
│   └── HomeHubSession.kt
├── screens/
│   ├── HomeScreen.kt
│   ├── DevicesListScreen.kt
│   ├── DeviceControlScreen.kt
│   └── ScenesScreen.kt
├── data/
│   ├── Device.kt
│   ├── DeviceType.kt
│   ├── DeviceRepository.kt
│   └── Scene.kt
├── ui/
│   └── theme/
│       ├── Color.kt
│       └── Theme.kt
└── utils/
    ├── Icons.kt
    └── Constants.kt
```

## Testing Strategy

- Use Desktop Head Unit (DHU) for testing
- Test in both light and dark ambient modes
- Verify touch target sizes
- Test navigation flow
- Validate color contrast ratios
- Test with simulated driving restrictions

## Future Enhancements (Beyond MVP)

1. Integration with real smart home APIs (Google Home, HomeKit, SmartThings)
2. Location-based automation triggers
3. Voice command integration with Google Assistant
4. Energy usage monitoring
5. Camera feed viewing (when parked)
6. Custom scene creation
7. Multi-home support
