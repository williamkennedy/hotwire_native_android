package com.example.hotwire_native_android

import android.app.Application
import com.example.hotwire_native_android.bridge.FormComponent
import com.example.hotwire_native_android.bridge.KeyboardComponent
import dev.hotwire.core.BuildConfig
import dev.hotwire.core.bridge.BridgeComponent
import dev.hotwire.core.bridge.BridgeComponentFactory
import dev.hotwire.core.bridge.KotlinXJsonConverter
import dev.hotwire.core.config.Hotwire
import dev.hotwire.core.turbo.config.PathConfiguration
import dev.hotwire.navigation.config.defaultFragmentDestination
import dev.hotwire.navigation.config.registerBridgeComponents
import dev.hotwire.navigation.config.registerRouteDecisionHandlers
import dev.hotwire.navigation.routing.AppNavigationRouteDecisionHandler
import dev.hotwire.navigation.routing.BrowserTabRouteDecisionHandler
import com.example.hotwire_native_android.features.WebFragment
import com.example.hotwire_native_android.features.WebModalFragment
import dev.hotwire.navigation.config.registerFragmentDestinations

class HotwireNativeApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        configureApp()
    }

    fun configureApp() {
        Hotwire.loadPathConfiguration(
            context = this,
            location = PathConfiguration.Location(
                assetFilePath = "json/configuration.json"
            )
        )

        Hotwire.defaultFragmentDestination = WebFragment::class


        Hotwire.registerFragmentDestinations(
            WebFragment::class,
            WebModalFragment::class
        )

        // Bridge
        Hotwire.registerBridgeComponents(
            BridgeComponentFactory("form", ::FormComponent),
            BridgeComponentFactory("keyboard", ::KeyboardComponent)
        )



        // Register route decision handlers
        Hotwire.registerRouteDecisionHandlers(
            AppNavigationRouteDecisionHandler(),
            BrowserTabRouteDecisionHandler()
        )

        // Set configuration options
        Hotwire.config.debugLoggingEnabled = BuildConfig.DEBUG
        Hotwire.config.webViewDebuggingEnabled = BuildConfig.DEBUG
        Hotwire.config.jsonConverter = KotlinXJsonConverter()
        Hotwire.config.userAgent = "Hotwire Demo; ${Hotwire.config.userAgentSubstring()}"
    }
}