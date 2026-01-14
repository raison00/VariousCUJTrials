# HomeHub Auto - Smart Home Controller for Android Auto

A beautiful and functional Android Auto app for controlling smart home devices from your car's display. Built with the latest Jetpack Compose (1.9.4) and featuring a stunning purple and turquoise color scheme.

## Features

### Quick Actions Dashboard
Access your most frequently used devices instantly with a grid-based quick actions interface optimized for driver safety.

### Device Categories
- **Lights**: Control brightness and on/off state
- **Garage Door**: Open and close with status monitoring
- **Door Locks**: Lock and unlock your home
- **Thermostat**: Adjust temperature up or down
- **Security System**: Arm and disarm your home security

### Preset Scenes
Activate multiple devices with a single tap:
- **Arriving Home**: Opens garage, turns on lights, adjusts temperature
- **Leaving Home**: Closes garage, turns off lights, arms security
- **Good Night**: Locks doors, turns off lights, sets night mode
- **Movie Time**: Dims lights, sets ambiance

### All Devices View
Browse all your smart home devices organized by room for easy access.

## Technology Stack

### Core Technologies
- **Android Auto**: Car App Library 1.7.0
- **Jetpack Compose**: 1.9.4 (latest stable)
- **Kotlin**: Modern Android development
- **Coroutines**: Asynchronous programming
- **StateFlow**: Reactive state management

### UI Framework
- **Car App Library Templates**: Driver-optimized UI components
  - GridTemplate for quick actions
  - ListTemplate for device browsing
  - PaneTemplate for device control
  - MessageTemplate for confirmations

### Design System
- **Color Scheme**: Purple and Turquoise
  - Primary: Purple 500 (#9C27B0)
  - Secondary: Turquoise 500 (#00BCD4)
  - Accent: Turquoise 300 (#4DD0E1)
  - Background: Purple 900 (#4A148C)
  - Surface: Purple 800 (#6A1B9A)

## Project Structure

```
com.homehub.auto/
├── service/
│   ├── HomeHubCarAppService.kt    # Main Car App Service
│   └── HomeHubSession.kt          # App session management
├── screens/
│   ├── HomeScreen.kt              # Quick actions grid
│   ├── DevicesListScreen.kt       # All devices by room
│   ├── DeviceControlScreen.kt     # Individual device control
│   └── ScenesScreen.kt            # Preset automation scenes
├── data/
│   ├── Device.kt                  # Device data model
│   ├── DeviceType.kt              # Device type enum
│   ├── DeviceState.kt             # Device state model
│   ├── Scene.kt                   # Scene data model
│   └── DeviceRepository.kt        # Device management logic
├── ui/
│   └── theme/
│       ├── Color.kt               # Color definitions
│       └── Theme.kt               # Material Theme
└── utils/
    ├── CarIcons.kt                # Themed icon utilities
    └── Constants.kt               # App constants
```

## Building the Project

### Requirements
- Android Studio Ladybug or later
- Android SDK 35
- Minimum SDK: API 28 (Android 9)
- Kotlin 2.0.21

### Build Steps

1. **Open the project in Android Studio**
   ```bash
   cd HomeHubAuto
   # Open in Android Studio
   ```

2. **Sync Gradle**
   - Android Studio will automatically sync dependencies
   - Wait for the sync to complete

3. **Build the APK**
   ```bash
   ./gradlew assembleDebug
   ```

4. **Install on device**
   ```bash
   ./gradlew installDebug
   ```

## Testing on Android Auto

### Desktop Head Unit (DHU)

The easiest way to test Android Auto apps is using the Desktop Head Unit:

1. **Install DHU**
   ```bash
   # Download from Android SDK Manager
   # Tools > SDK Manager > SDK Tools > Android Auto Desktop Head Unit
   ```

2. **Enable Developer Mode on Phone**
   - Open Android Auto app on phone
   - Tap version number 10 times
   - Enable developer settings

3. **Connect Phone via USB**
   ```bash
   adb forward tcp:5277 tcp:5277
   ```

4. **Launch DHU**
   ```bash
   cd $ANDROID_SDK_ROOT/extras/google/auto/
   ./desktop-head-unit
   ```

5. **Launch the App**
   - HomeHub Auto should appear in the DHU launcher
   - Tap to open and test functionality

### Testing on Real Car

1. Install the app on your Android phone
2. Connect phone to car via USB or wireless Android Auto
3. HomeHub Auto will appear in the car's app launcher
4. Tap to launch and control your simulated smart home

## Simulated Smart Home Backend

This demo app includes a simulated smart home backend for demonstration purposes. All devices are stored in memory and state changes are simulated with realistic delays.

### Default Devices
- Living Room Lights (dimmable)
- Bedroom Lights
- Kitchen Lights
- Porch Lights
- Garage Door
- Front Door Lock
- Home Thermostat
- Security System

### Integrating Real Smart Home APIs

To connect to real smart home systems, modify `DeviceRepository.kt` to integrate with:
- **Google Home API**: For Google Home devices
- **HomeKit**: For Apple HomeKit devices
- **SmartThings**: For Samsung SmartThings
- **Home Assistant**: For open-source home automation
- **Custom APIs**: For proprietary smart home systems

## Safety Considerations

This app follows Android Auto's driver distraction guidelines:
- Large touch targets (minimum 48dp)
- High contrast colors for visibility
- Limited text and simple interactions
- Template-based UI prevents custom distracting elements
- Quick actions require minimal attention

## Future Enhancements

- Integration with real smart home APIs
- Location-based automation triggers
- Voice command integration with Google Assistant
- Energy usage monitoring
- Camera feed viewing (when parked)
- Custom scene creation
- Multi-home support
- Weather-based automations

## License

This is a demonstration project created for educational purposes.

## Credits

Built with:
- Android Auto Car App Library
- Jetpack Compose
- Material Design 3
- Kotlin Coroutines

Design inspired by Material Design principles with a custom purple and turquoise color scheme.

## Support

For questions or issues, please refer to the official Android Auto documentation:
- [Android for Cars Overview](https://developer.android.com/training/cars)
- [Car App Library Guide](https://developer.android.com/training/cars/apps)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
