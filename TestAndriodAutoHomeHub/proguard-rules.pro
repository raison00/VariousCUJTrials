# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Car App Library classes
-keep class androidx.car.app.** { *; }

# Keep Compose classes
-keep class androidx.compose.** { *; }

# Keep data classes
-keep class com.homehub.auto.data.** { *; }
