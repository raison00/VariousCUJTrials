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
import com.homehub.auto.utils.CarIcons
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Home screen displaying quick action buttons for common devices
 */
class HomeScreen(carContext: CarContext) : Screen(carContext), DefaultLifecycleObserver {

    private val repository = DeviceRepository.getInstance()

    init {
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        // Observe device changes and invalidate screen
        lifecycleScope.launch {
            repository.devices.collectLatest {
                invalidate()
            }
        }
    }

    override fun onGetTemplate(): Template {
        val devices = repository.devices.value

        // Create grid items for quick actions
        val gridItems = mutableListOf<GridItem>()

        // Add quick action buttons for key devices
        devices.take(6).forEach { device ->
            val gridItem = GridItem.Builder()
                .setTitle(device.name)
                .setText(device.getStatusText())
                .setImage(
                    CarIcons.createDeviceIcon(
                        device.type,
                        when (device.type) {
                            DeviceType.LIGHT -> device.state.isOn
                            DeviceType.GARAGE_DOOR -> device.state.isOpen
                            DeviceType.DOOR_LOCK -> device.state.isLocked
                            DeviceType.THERMOSTAT -> true
                            DeviceType.SECURITY -> device.state.isArmed
                        }
                    ),
                    GridItem.IMAGE_TYPE_ICON
                )
                .setOnClickListener {
                    lifecycleScope.launch {
                        repository.toggleDevice(device.id)
                    }
                }
                .build()

            gridItems.add(gridItem)
        }

        // Build the grid template
        val gridTemplate = GridTemplate.Builder()
            .setTitle(carContext.getString(R.string.home_screen_title))
            .setHeaderAction(Action.APP_ICON)
            .setSingleList(
                ItemList.Builder()
                    .addItems(gridItems)
                    .build()
            )
            .setActionStrip(
                ActionStrip.Builder()
                    .addAction(
                        Action.Builder()
                            .setTitle(carContext.getString(R.string.nav_all_devices))
                            .setOnClickListener {
                                screenManager.push(DevicesListScreen(carContext))
                            }
                            .build()
                    )
                    .addAction(
                        Action.Builder()
                            .setTitle(carContext.getString(R.string.nav_scenes))
                            .setOnClickListener {
                                screenManager.push(ScenesScreen(carContext))
                            }
                            .build()
                    )
                    .build()
            )
            .build()

        return gridTemplate
    }
}
