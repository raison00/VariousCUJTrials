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
 * Screen displaying preset automation scenes
 */
class ScenesScreen(carContext: CarContext) : Screen(carContext), DefaultLifecycleObserver {

    private val repository = DeviceRepository.getInstance()

    init {
        lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        lifecycleScope.launch {
            repository.scenes.collectLatest {
                invalidate()
            }
        }
    }

    override fun onGetTemplate(): Template {
        val scenes = repository.scenes.value
        val itemListBuilder = ItemList.Builder()

        scenes.forEach { scene ->
            val row = Row.Builder()
                .setTitle(scene.name)
                .addText(scene.description)
                .setOnClickListener {
                    lifecycleScope.launch {
                        val success = repository.activateScene(scene.id)
                        if (success) {
                            // Show confirmation
                            screenManager.push(
                                SceneConfirmationScreen(carContext, scene.name)
                            )
                        }
                    }
                }
                .build()

            itemListBuilder.addItem(row)
        }

        return ListTemplate.Builder()
            .setTitle(carContext.getString(R.string.scenes_screen_title))
            .setHeaderAction(Action.BACK)
            .setSingleList(itemListBuilder.build())
            .build()
    }
}

/**
 * Confirmation screen after activating a scene
 */
class SceneConfirmationScreen(
    carContext: CarContext,
    private val sceneName: String
) : Screen(carContext) {

    override fun onGetTemplate(): Template {
        return MessageTemplate.Builder("$sceneName activated successfully!")
            .setTitle(carContext.getString(R.string.message_scene_activated))
            .setHeaderAction(Action.BACK)
            .addAction(
                Action.Builder()
                    .setTitle("Done")
                    .setOnClickListener {
                        // Pop back to scenes list
                        screenManager.pop()
                    }
                    .build()
            )
            .build()
    }
}
