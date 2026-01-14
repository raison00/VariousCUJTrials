package com.homehub.auto.data

/**
 * Represents a preset automation scene
 */
data class Scene(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val actions: List<SceneAction>
)

/**
 * An action to perform when a scene is activated
 */
data class SceneAction(
    val deviceId: String,
    val newState: DeviceState
)
