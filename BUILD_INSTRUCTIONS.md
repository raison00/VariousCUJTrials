# Build Instructions for HomeHub Auto

## Prerequisites

Before building this Android Auto app, ensure you have the following installed:

### Required Software
1. **Android Studio Ladybug (2024.2.1) or later**
   - Download from: https://developer.android.com/studio
   - Includes Android SDK and build tools

2. **Java Development Kit (JDK) 17**
   - Included with Android Studio
   - Or download from: https://adoptium.net/

3. **Android SDK Components**
   - Android SDK Platform 35
   - Android SDK Build-Tools 35.0.0
   - Android Auto Desktop Head Unit (for testing)

### Optional Tools
- **Git** (for version control)
- **ADB** (Android Debug Bridge - included with Android Studio)
- **Physical Android device** with Android 9+ (for real device testing)

## Build Steps

### 1. Import Project

```bash
# Clone or copy the project to your local machine
cd /path/to/HomeHubAuto

# Open Android Studio
# File > Open > Select HomeHubAuto folder
```

### 2. Sync Gradle Dependencies

Android Studio will automatically prompt you to sync Gradle files. If not:

```
File > Sync Project with Gradle Files
```

Wait for the sync to complete. This will download all required dependencies:
- Jetpack Compose 1.9.4
- Car App Library 1.7.0
- Kotlin 2.0.21
- AndroidX libraries

### 3. Configure Build Variants

The project supports two build types:
- **Debug**: For development and testing
- **Release**: For production (requires signing configuration)

Select build variant:
```
Build > Select Build Variant > debug
```

### 4. Build the Project

#### Option A: Using Android Studio
```
Build > Make Project (Ctrl+F9 / Cmd+F9)
```

#### Option B: Using Gradle Command Line
```bash
# On macOS/Linux
./gradlew assembleDebug

# On Windows
gradlew.bat assembleDebug
```

The APK will be generated at:
```
app/build/outputs/apk/debug/app-debug.apk
```

### 5. Install on Device

#### Option A: Using Android Studio
1. Connect your Android device via USB
2. Enable USB debugging on the device
3. Click the "Run" button (green play icon)
4. Select your device from the list

#### Option B: Using ADB
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Testing the App

### Method 1: Desktop Head Unit (Recommended for Development)

The Desktop Head Unit (DHU) simulates an Android Auto display on your computer.

#### Setup DHU

1. **Install DHU via SDK Manager**
   ```
   Android Studio > Tools > SDK Manager > SDK Tools
   Check "Android Auto Desktop Head Unit"
   Click "Apply" to install
   ```

2. **Enable Developer Mode on Phone**
   - Install Android Auto app on your phone
   - Open Android Auto app
   - Tap the hamburger menu (≡)
   - Scroll to "About"
   - Tap "Version" 10 times
   - Developer settings will be enabled

3. **Enable Developer Settings**
   - Go back to main Android Auto menu
   - Tap the hamburger menu (≡)
   - Tap "Developer settings"
   - Enable "Unknown sources"
   - Enable "Developer mode"

4. **Connect Phone to Computer**
   ```bash
   # Connect phone via USB
   # Enable USB debugging on phone
   
   # Forward DHU port
   adb forward tcp:5277 tcp:5277
   ```

5. **Launch DHU**
   ```bash
   cd $ANDROID_SDK_ROOT/extras/google/auto/
   ./desktop-head-unit
   
   # On Windows:
   # cd %ANDROID_SDK_ROOT%\extras\google\auto\
   # desktop-head-unit.exe
   ```

6. **Launch HomeHub Auto**
   - DHU window will show Android Auto interface
   - Tap the app launcher icon
   - Find "HomeHub Auto" in the list
   - Tap to launch

### Method 2: Real Car Testing

1. **Prepare Phone**
   - Install HomeHub Auto APK on phone
   - Install Android Auto app (if not pre-installed)
   - Enable developer settings (see DHU setup above)
   - Enable "Unknown sources" in developer settings

2. **Connect to Car**
   - Connect phone to car via USB cable
   - Or use wireless Android Auto (if supported)
   - Android Auto should launch automatically

3. **Launch App**
   - Tap the app launcher in Android Auto
   - Find "HomeHub Auto"
   - Tap to open

### Method 3: Android Automotive OS Emulator

For testing on Android Automotive OS (built-in car system):

1. **Create Automotive Emulator**
   ```
   Tools > Device Manager > Create Device
   Select "Automotive" category
   Choose a system image (API 28+)
   Finish setup
   ```

2. **Run on Emulator**
   - Select the automotive emulator
   - Click "Run"
   - HomeHub Auto will install and launch

## Troubleshooting

### Common Issues

#### 1. Gradle Sync Failed
**Problem**: Dependencies cannot be downloaded
**Solution**: 
```bash
# Clear Gradle cache
./gradlew clean
rm -rf ~/.gradle/caches/

# Sync again
./gradlew build --refresh-dependencies
```

#### 2. App Not Showing in Android Auto
**Problem**: HomeHub Auto doesn't appear in the app list
**Solutions**:
- Enable "Unknown sources" in Android Auto developer settings
- Verify AndroidManifest.xml has correct category:
  ```xml
  <category android:name="androidx.car.app.category.IOT" />
  ```
- Restart Android Auto app
- Reconnect phone to DHU or car

#### 3. Build Error: SDK Not Found
**Problem**: Android SDK path not configured
**Solution**:
```bash
# Create or edit local.properties
echo "sdk.dir=/path/to/Android/Sdk" > local.properties
```

#### 4. Kotlin Compiler Error
**Problem**: Kotlin version mismatch
**Solution**:
- Verify Kotlin version in build.gradle.kts matches:
  ```kotlin
  id("org.jetbrains.kotlin.android") version "2.0.21"
  ```
- File > Invalidate Caches / Restart

#### 5. Car App Library Not Found
**Problem**: androidx.car.app dependencies not resolving
**Solution**:
- Verify Google Maven repository in settings.gradle.kts:
  ```kotlin
  repositories {
      google()
      mavenCentral()
  }
  ```
- Sync Gradle files again

### Debugging

#### Enable Verbose Logging
Add to `HomeHubCarAppService.kt`:
```kotlin
import android.util.Log

private const val TAG = "HomeHubAuto"

override fun onCreateSession(): Session {
    Log.d(TAG, "Creating new session")
    return HomeHubSession()
}
```

#### View Logcat
```bash
# Filter for HomeHub Auto logs
adb logcat | grep HomeHub

# Or in Android Studio:
# View > Tool Windows > Logcat
# Filter: package:com.homehub.auto
```

#### Debug on DHU
```bash
# Launch DHU with verbose logging
./desktop-head-unit --verbose
```

## Building for Release

### 1. Generate Signing Key

```bash
keytool -genkey -v -keystore homehub-release.keystore \
  -alias homehub -keyalg RSA -keysize 2048 -validity 10000
```

### 2. Configure Signing in build.gradle.kts

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("homehub-release.keystore")
            storePassword = "your_store_password"
            keyAlias = "homehub"
            keyPassword = "your_key_password"
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(...)
        }
    }
}
```

### 3. Build Release APK

```bash
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

## Performance Optimization

### ProGuard/R8 Configuration

The project includes ProGuard rules in `proguard-rules.pro`:
- Keeps Car App Library classes
- Keeps Compose classes
- Optimizes APK size

### APK Size Reduction

```bash
# Enable resource shrinking
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}
```

## Next Steps

After successful build:
1. Test all features in DHU
2. Test on real car if available
3. Verify color scheme (purple/turquoise) displays correctly
4. Test device controls and scene activation
5. Check performance and responsiveness
6. Review Android Auto design guidelines compliance

## Additional Resources

- [Android Auto Developer Guide](https://developer.android.com/training/cars)
- [Car App Library Documentation](https://developer.android.com/training/cars/apps)
- [Jetpack Compose Guide](https://developer.android.com/jetpack/compose)
- [Android Auto Design Guidelines](https://developers.google.com/cars/design)

## Support

For build issues specific to this project, refer to:
- README.md for project overview
- GitHub Issues (if repository is public)
- Android Auto community forums
