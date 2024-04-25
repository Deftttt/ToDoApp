package com.example.todocomposeapp.navigation.destinations

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.todocomposeapp.ui.screens.splash.SplashScreen
import com.example.todocomposeapp.util.Constants

fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit
) {
    composable(
        route = Constants.SPLASH_SCREEN,
        exitTransition = {
            slideOutVertically(
                animationSpec = tween(
                    durationMillis = 800,
                    easing = LinearOutSlowInEasing
                ), targetOffsetY = { -it });
        }
    ) {
        SplashScreen(navigateToListScreen = navigateToListScreen)
    }
}