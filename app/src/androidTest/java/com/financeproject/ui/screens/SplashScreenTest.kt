package com.financeproject.ui.screens

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@RunWith(AndroidJUnit4::class)
class SplashScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_isDisplayed() {
        var compositionFinished = false
        composeTestRule.setContent {
            SplashScreen(
                onSplashFinished = { compositionFinished = true },
                isTest = true
            )
        }

        // Assert the splash screen is displayed
        composeTestRule.onNodeWithTag("splashScreen").assertExists()
        
        // Assert the callback was triggered
        assert(compositionFinished) { "Splash screen callback was not triggered" }
    }

    @Test
    fun splashScreen_callbackTriggered() {
        var callbackTriggered = false
        
        composeTestRule.setContent {
            SplashScreen(
                onSplashFinished = { callbackTriggered = true },

                isTest = true
            )
        }

        // Assert the callback was triggered
        assert(callbackTriggered) { "Splash screen callback was not triggered" }
    }
} 