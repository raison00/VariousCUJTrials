package com.homehub.auto.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.car.app.model.CarColor
import androidx.car.app.model.CarIcon
import androidx.core.graphics.drawable.IconCompat
import com.homehub.auto.data.DeviceType

/**
 * Utility for creating themed CarIcons with purple and turquoise colors
 */
object CarIcons {

    // Purple and Turquoise theme colors (matching our theme)
    val PURPLE_PRIMARY = CarColor.createCustom(0xFF9C27B0.toInt(), 0xFF9C27B0.toInt())
    val TURQUOISE_SECONDARY = CarColor.createCustom(0xFF00BCD4.toInt(), 0xFF00BCD4.toInt())
    val TURQUOISE_ACCENT = CarColor.createCustom(0xFF4DD0E1.toInt(), 0xFF4DD0E1.toInt())
    val PURPLE_DARK = CarColor.createCustom(0xFF6A1B9A.toInt(), 0xFF6A1B9A.toInt())
    val STATUS_ON_COLOR = CarColor.createCustom(0xFF26C6DA.toInt(), 0xFF26C6DA.toInt())
    val STATUS_OFF_COLOR = CarColor.createCustom(0xFF757575.toInt(), 0xFF757575.toInt())

    /**
     * Create a simple colored icon for a device
     */
    fun createDeviceIcon(deviceType: DeviceType, isActive: Boolean): CarIcon {
        val iconType = when (deviceType) {
            DeviceType.LIGHT -> CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME
            DeviceType.GARAGE_DOOR -> CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME
            DeviceType.DOOR_LOCK -> CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME
            DeviceType.THERMOSTAT -> CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME
            DeviceType.SECURITY -> CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME
        }

        return CarIcon.Builder(iconType)
            .setTint(if (isActive) STATUS_ON_COLOR else STATUS_OFF_COLOR)
            .build()
    }

    /**
     * Create a scene icon
     */
    fun createSceneIcon(): CarIcon {
        return CarIcon.Builder(CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME)
            .setTint(TURQUOISE_ACCENT)
            .build()
    }

    /**
     * Create app icon
     */
    fun createAppIcon(): CarIcon {
        return CarIcon.Builder(CarIcon.Builder.DefaultCarIconType.ICON_TYPE_HOME)
            .setTint(PURPLE_PRIMARY)
            .build()
    }

    /**
     * Create a custom bitmap icon with gradient background
     */
    fun createCustomIcon(size: Int = 128): Bitmap {
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Draw purple circle background
        paint.color = 0xFF9C27B0.toInt()
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Draw turquoise accent
        paint.color = 0xFF00BCD4.toInt()
        val rect = RectF(size * 0.3f, size * 0.3f, size * 0.7f, size * 0.7f)
        canvas.drawOval(rect, paint)

        return bitmap
    }

    /**
     * Create an IconCompat from bitmap for use in CarIcon
     */
    fun createCarIconFromBitmap(bitmap: Bitmap): CarIcon {
        return CarIcon.Builder(IconCompat.createWithBitmap(bitmap)).build()
    }
}
