package com.homehub.auto.screens

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.*
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.homehub.auto.R
import com.homehub.auto.data.DeviceRepository
import com.homehub.auto.data.DeviceType
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Screen for controlling an individual device
 */
class DeviceControlScreen(
    carContext: CarContext,
    private val deviceId: String
) : Screen(carContext), DefaultLifecycleObserver {

    private val repository = DeviceRepository.getInstance()

    init {
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifecycleScope.launch {
            repository.devices.collectLatest {
                invalidate()
            }
        }
    }

    override fun onGetTemplate(): Template {
        val device = repository.getDevice(deviceId)

        if (device == null) {
            return MessageTemplate.Builder(carContext.getString(R.string.message_error))
                .setTitle("Device Not Found")
                .setHeaderAction(Action.BACK)
                .addAction(
                    Action.Builder()
                        .setTitle(carContext.getString(R.string.nav_back))
                        .setOnClickListener {
                            screenManager.pop()
                        }
                        .build()
                )
                .build()
        }

        val paneBuilder = Pane.Builder()
            .addRow(
                Row.Builder()
                    .setTitle(device.name)
                    .addText(device.room)
                    .build()
            )
            .addRow(
                Row.Builder()
                    .setTitle("Status")
                    .addText(device.getStatusText())
                    .build()
            )

        // Add control actions based on device type
        val actions = mutableListOf<Action>()

        when (device.type) {
            DeviceType.LIGHT -> {
                // Toggle on/off
                actions.add(
                    Action.Builder()
                        .setTitle(device.getPrimaryActionText())
                        .setOnClickListener {
                            lifecycleScope.launch {
                                repository.toggleDevice(deviceId)
                            }
                        }
                        .build()
                )
                // Brightness controls
                if (device.state.isOn) {
                    actions.add(
                        Action.Builder()
                            .setTitle("Brightness +")
                            .setOnClickListener {
                                lifecycleScope.launch {
                                    repository.adjustBrightness(deviceId, true)
                                }
                            }
                            .build()
                    )
                    actions.add(
                        Action.Builder()
                            .setTitle("Brightness -")
                            .setOnClickListener {
                                lifecycleScope.launch {
                                    repository.adjustBrightness(deviceId, false)
                                }
                            }
                            .build()
                    )
                }
            }

            DeviceType.THERMOSTAT -> {
                actions.add(
                    Action.Builder()
                        .setTitle("Temperature +")
                        .setOnClickListener {
                            lifecycleScope.launch {
                                repository.adjustThermostat(true)
                            }
                        }
                        .build()
                )
                actions.add(
                    Action.Builder()
                        .setTitle("Temperature -")
                        .setOnClickListener {
                            lifecycleScope.launch {
                                repository.adjustThermostat(false)
                            }
                        }
                        .build()
                )
            }

            else -> {
                // Simple toggle for other devices
                actions.add(
                    Action.Builder()
                        .setTitle(device.getPrimaryActionText())
                        .setOnClickListener {
                            lifecycleScope.launch {
                                repository.toggleDevice(deviceId)
                            }
                        }
                        .build()
                )
            }
        }

        // Build action strip
        val actionStripBuilder = ActionStrip.Builder()
        actions.forEach { actionStripBuilder.addAction(it) }

        return PaneTemplate.Builder(paneBuilder.build())
            .setTitle(device.name)
            .setHeaderAction(Action.BACK)
            .setActionStrip(actionStripBuilder.build())
            .build()
    }
}
