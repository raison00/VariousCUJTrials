package com.homehub.auto.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository for managing smart home devices
 * This is a simulated implementation for demonstration purposes
 */
class DeviceRepository private constructor() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _scenes = MutableStateFlow<List<Scene>>(emptyList())
    val scenes: StateFlow<List<Scene>> = _scenes.asStateFlow()

    init {
        initializeDevices()
        initializeScenes()
    }

    /**
     * Initialize sample devices
     */
    private fun initializeDevices() {
        _devices.value = listOf(
            Device(
                id = "living_room_lights",
                name = "Living Room Lights",
                type = DeviceType.LIGHT,
                room = "Living Room",
                state = DeviceState(isOn = false, brightness = 80)
            ),
            Device(
                id = "bedroom_lights",
                name = "Bedroom Lights",
                type = DeviceType.LIGHT,
                room = "Bedroom",
                state = DeviceState(isOn = false, brightness = 60)
            ),
            Device(
                id = "kitchen_lights",
                name = "Kitchen Lights",
                type = DeviceType.LIGHT,
                room = "Kitchen",
                state = DeviceState(isOn = false, brightness = 100)
            ),
            Device(
                id = "porch_lights",
                name = "Porch Lights",
                type = DeviceType.LIGHT,
                room = "Exterior",
                state = DeviceState(isOn = false, brightness = 100)
            ),
            Device(
                id = "garage_door",
                name = "Garage Door",
                type = DeviceType.GARAGE_DOOR,
                room = "Garage",
                state = DeviceState(isOpen = false)
            ),
            Device(
                id = "front_door_lock",
                name = "Front Door Lock",
                type = DeviceType.DOOR_LOCK,
                room = "Entrance",
                state = DeviceState(isLocked = true)
            ),
            Device(
                id = "thermostat",
                name = "Home Thermostat",
                type = DeviceType.THERMOSTAT,
                room = "Living Room",
                state = DeviceState(temperature = 72)
            ),
            Device(
                id = "security_system",
                name = "Security System",
                type = DeviceType.SECURITY,
                room = "Home",
                state = DeviceState(isArmed = false)
            )
        )
    }

    /**
     * Initialize preset scenes
     */
    private fun initializeScenes() {
        _scenes.value = listOf(
            Scene(
                id = "arriving_home",
                name = "Arriving Home",
                description = "Opens garage, turns on lights, adjusts temperature",
                icon = "home",
                actions = listOf(
                    SceneAction("garage_door", DeviceState(isOpen = true)),
                    SceneAction("living_room_lights", DeviceState(isOn = true, brightness = 80)),
                    SceneAction("porch_lights", DeviceState(isOn = true, brightness = 100)),
                    SceneAction("thermostat", DeviceState(temperature = 72)),
                    SceneAction("security_system", DeviceState(isArmed = false))
                )
            ),
            Scene(
                id = "leaving_home",
                name = "Leaving Home",
                description = "Closes garage, turns off lights, arms security",
                icon = "exit",
                actions = listOf(
                    SceneAction("garage_door", DeviceState(isOpen = false)),
                    SceneAction("living_room_lights", DeviceState(isOn = false)),
                    SceneAction("bedroom_lights", DeviceState(isOn = false)),
                    SceneAction("kitchen_lights", DeviceState(isOn = false)),
                    SceneAction("porch_lights", DeviceState(isOn = false)),
                    SceneAction("security_system", DeviceState(isArmed = true)),
                    SceneAction("front_door_lock", DeviceState(isLocked = true))
                )
            ),
            Scene(
                id = "good_night",
                name = "Good Night",
                description = "Locks doors, turns off lights, sets night mode",
                icon = "night",
                actions = listOf(
                    SceneAction("living_room_lights", DeviceState(isOn = false)),
                    SceneAction("bedroom_lights", DeviceState(isOn = false)),
                    SceneAction("kitchen_lights", DeviceState(isOn = false)),
                    SceneAction("porch_lights", DeviceState(isOn = true, brightness = 30)),
                    SceneAction("front_door_lock", DeviceState(isLocked = true)),
                    SceneAction("garage_door", DeviceState(isOpen = false)),
                    SceneAction("thermostat", DeviceState(temperature = 68)),
                    SceneAction("security_system", DeviceState(isArmed = true))
                )
            ),
            Scene(
                id = "movie_time",
                name = "Movie Time",
                description = "Dims lights, sets ambiance",
                icon = "movie",
                actions = listOf(
                    SceneAction("living_room_lights", DeviceState(isOn = true, brightness = 20)),
                    SceneAction("kitchen_lights", DeviceState(isOn = false))
                )
            )
        )
    }

    /**
     * Get a device by ID
     */
    fun getDevice(deviceId: String): Device? {
        return _devices.value.find { it.id == deviceId }
    }

    /**
     * Update a device state
     */
    suspend fun updateDevice(deviceId: String, newState: DeviceState): Boolean {
        // Simulate network delay
        delay(300)

        val deviceIndex = _devices.value.indexOfFirst { it.id == deviceId }
        if (deviceIndex != -1) {
            val updatedDevices = _devices.value.toMutableList()
            updatedDevices[deviceIndex] = updatedDevices[deviceIndex].copy(state = newState)
            _devices.value = updatedDevices
            return true
        }
        return false
    }

    /**
     * Toggle a device
     */
    suspend fun toggleDevice(deviceId: String): Boolean {
        val device = getDevice(deviceId) ?: return false
        device.toggle()
        return updateDevice(deviceId, device.state)
    }

    /**
     * Activate a scene
     */
    suspend fun activateScene(sceneId: String): Boolean {
        val scene = _scenes.value.find { it.id == sceneId } ?: return false

        // Apply all scene actions
        scene.actions.forEach { action ->
            updateDevice(action.deviceId, action.newState)
        }

        return true
    }

    /**
     * Adjust thermostat temperature
     */
    suspend fun adjustThermostat(increase: Boolean): Boolean {
        val thermostat = getDevice("thermostat") ?: return false
        val currentTemp = thermostat.state.temperature
        val newTemp = if (increase) {
            (currentTemp + 1).coerceAtMost(85)
        } else {
            (currentTemp - 1).coerceAtLeast(60)
        }
        return updateDevice("thermostat", thermostat.state.copy(temperature = newTemp))
    }

    /**
     * Adjust light brightness
     */
    suspend fun adjustBrightness(deviceId: String, increase: Boolean): Boolean {
        val device = getDevice(deviceId) ?: return false
        if (device.type != DeviceType.LIGHT) return false

        val currentBrightness = device.state.brightness
        val newBrightness = if (increase) {
            (currentBrightness + 10).coerceAtMost(100)
        } else {
            (currentBrightness - 10).coerceAtLeast(0)
        }

        return updateDevice(deviceId, device.state.copy(brightness = newBrightness, isOn = newBrightness > 0))
    }

    /**
     * Get devices by room
     */
    fun getDevicesByRoom(): Map<String, List<Device>> {
        return _devices.value.groupBy { it.room }
    }

    companion object {
        @Volatile
        private var instance: DeviceRepository? = null

        fun getInstance(): DeviceRepository {
            return instance ?: synchronized(this) {
                instance ?: DeviceRepository().also { instance = it }
            }
        }
    }
}
