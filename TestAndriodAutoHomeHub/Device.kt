package com.homehub.auto.data

/**
 * Represents a smart home device
 */
data class Device(
    val id: String,
    val name: String,
    val type: DeviceType,
    val room: String,
    var state: DeviceState,
    val isControllable: Boolean = true
) {
    /**
     * Get a human-readable status string
     */
    fun getStatusText(): String {
        return when (type) {
            DeviceType.LIGHT -> if (state.isOn) "On - ${state.brightness}%" else "Off"
            DeviceType.GARAGE_DOOR -> if (state.isOpen) "Open" else "Closed"
            DeviceType.DOOR_LOCK -> if (state.isLocked) "Locked" else "Unlocked"
            DeviceType.THERMOSTAT -> "${state.temperature}°F"
            DeviceType.SECURITY -> if (state.isArmed) "Armed" else "Disarmed"
        }
    }

    /**
     * Get the primary action text for this device
     */
    fun getPrimaryActionText(): String {
        return when (type) {
            DeviceType.LIGHT -> if (state.isOn) "Turn Off" else "Turn On"
            DeviceType.GARAGE_DOOR -> if (state.isOpen) "Close" else "Open"
            DeviceType.DOOR_LOCK -> if (state.isLocked) "Unlock" else "Lock"
            DeviceType.THERMOSTAT -> "Adjust"
            DeviceType.SECURITY -> if (state.isArmed) "Disarm" else "Arm"
        }
    }

    /**
     * Toggle the device state
     */
    fun toggle() {
        state = when (type) {
            DeviceType.LIGHT -> state.copy(isOn = !state.isOn)
            DeviceType.GARAGE_DOOR -> state.copy(isOpen = !state.isOpen)
            DeviceType.DOOR_LOCK -> state.copy(isLocked = !state.isLocked)
            DeviceType.SECURITY -> state.copy(isArmed = !state.isArmed)
            DeviceType.THERMOSTAT -> state // Thermostat doesn't toggle
        }
    }
}

/**
 * Device state properties
 */
data class DeviceState(
    val isOn: Boolean = false,
    val isOpen: Boolean = false,
    val isLocked: Boolean = true,
    val isArmed: Boolean = false,
    val brightness: Int = 100,
    val temperature: Int = 72
)

/**
 * Types of smart home devices
 */
enum class DeviceType {
    LIGHT,
    GARAGE_DOOR,
    DOOR_LOCK,
    THERMOSTAT,
    SECURITY
}
