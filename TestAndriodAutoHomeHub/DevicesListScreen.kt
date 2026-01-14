package com.homehub.auto.screens

import androidx.car.app.CarContext
import androidx.car.app.Screen
import androidx.car.app.model.*
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.homehub.auto.R
import com.homehub.auto.data.DeviceRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Screen displaying all devices organized by room
 */
class DevicesListScreen(carContext: CarContext) : Screen(carContext), DefaultLifecycleObserver {

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
        val devicesByRoom = repository.getDevicesByRoom()
        val itemListBuilder = ItemList.Builder()

        // Group devices by room
        devicesByRoom.forEach { (room, devices) ->
            // Add room header
            itemListBuilder.addItem(
                Row.Builder()
                    .setTitle(room)
                    .setBrowsable(false)
                    .build()
            )

            // Add devices in this room
            devices.forEach { device ->
                val row = Row.Builder()
                    .setTitle(device.name)
                    .addText(device.getStatusText())
                    .setOnClickListener {
                        screenManager.push(DeviceControlScreen(carContext, device.id))
                    }
                    .build()

                itemListBuilder.addItem(row)
            }
        }

        return ListTemplate.Builder()
            .setTitle(carContext.getString(R.string.devices_screen_title))
            .setHeaderAction(Action.BACK)
            .setSingleList(itemListBuilder.build())
            .build()
    }
}
