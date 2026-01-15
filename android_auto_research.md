# Android Auto App Development Research

## Key Findings

### Android Auto Overview
- Android Auto provides a driver-optimized app experience for users with Android phones
- Apps run on the phone but display on the car's screen
- Requires Android 9 (API level 28) or higher

### Supported App Categories
The following categories are supported for Android Auto:

1. **Media - Audio** (While driving or parked)
   - Built using: MediaBrowserService and MediaSession
   - Can also use Car App Library templates (Early Access Program)
   
2. **Communication - Messaging** (While driving or parked)
   - Built using: MessagingStyle notifications
   
3. **Navigation** (While driving or parked)
   - Built using: Android for Cars App Library
   
4. **Point of Interest (POI)** (While driving or parked)
   - Built using: Android for Cars App Library
   - Examples: parking, charging, fuel apps
   
5. **Internet of Things (IOT)** (While driving or parked)
   - Built using: Android for Cars App Library
   - Examples: garage door, home lights, security
   
6. **Weather** (While driving or parked)
   - Built using: Android for Cars App Library

### Development Approach
- Use **Android for Cars App Library** for templated apps
- One app architecture works for both Android Auto and Android Automotive OS
- Testing available via Desktop Head Unit

## Next Steps
- Research Car App Library and Jetpack Compose integration
- Determine which app category to build
- Check latest Compose version


## Jetpack Compose Latest Versions (as of November 5, 2025)

### Stable Versions
- **Compose UI**: 1.9.4
- **Compose Foundation**: 1.9.4
- **Compose Material**: 1.9.4
- **Compose Material3**: 1.4.0
- **Compose Runtime**: 1.9.4
- **Compose Animation**: 1.9.4
- **Compose Compiler**: 1.5.15

### Beta Versions
- Compose 1.10.0-beta02 available for UI, Foundation, Material, Runtime, Animation

### Key Features in Recent Releases
- **Compose 1.9** (August 2025): Advanced shadows, 2D scrolling APIs, better list performance
- **Compose 1.8** (April 2025): Autofill, text improvements, visibility tracking

## Car App Library Version
- **Latest Stable**: 1.7.0 (July 16, 2025)
- **Latest Alpha**: 1.8.0-alpha02
- Dependencies:
  - androidx.car.app:app:1.7.0
  - androidx.car.app:app-projected:1.7.0 (for Android Auto)
  - androidx.car.app:app-automotive:1.7.0 (for Android Automotive OS)
