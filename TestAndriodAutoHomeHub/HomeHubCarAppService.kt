package com.homehub.auto.service

import android.content.Intent
import androidx.car.app.CarAppService
import androidx.car.app.Screen
import androidx.car.app.Session
import androidx.car.app.validation.HostValidator
import com.homehub.auto.screens.HomeScreen

/**
 * Main Car App Service for HomeHub Auto
 * This service is the entry point for Android Auto
 */
class HomeHubCarAppService : CarAppService() {

    override fun createHostValidator(): HostValidator {
        // Allow all hosts for development
        // In production, use HostValidator.ALLOW_ALL_HOSTS_VALIDATOR or specific allowlist
        return HostValidator.ALLOW_ALL_HOSTS_VALIDATOR
    }

    override fun onCreateSession(): Session {
        return HomeHubSession()
    }
}

/**
 * Session manages the lifecycle of the car app
 */
class HomeHubSession : Session() {

    override fun onCreateScreen(intent: Intent): Screen {
        // Return the home screen as the initial screen
        return HomeScreen(carContext)
    }
}
