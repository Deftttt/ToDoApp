package com.example.todocomposeapp.ui.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.todocomposeapp.R
import com.example.todocomposeapp.ui.theme.splashScreenBackground
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    navigateToListScreen: () -> Unit
) {

    var animation by remember { mutableStateOf(false) }
    val positionState by animateDpAsState(
        targetValue = if (animation) 0.dp else 300.dp,
        animationSpec = tween(1200)
    )
    val alphaState by animateFloatAsState(
        targetValue = if (animation) 1f else 0f,
        animationSpec = tween(1200)
    )

    LaunchedEffect(key1 = true) {
        animation = true
        delay(1800)
        navigateToListScreen()
    }

    Splash(positionState, alphaState)

}


@Composable
fun Splash(positionState: Dp, alphaState: Float) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "splash screen logo",
                modifier = Modifier
                    .size(150.dp)
                    .offset(y = positionState)
                    .alpha(alphaState),
                colorFilter = ColorFilter.tint(Color.White)
            )
            Text(
                text = "ToDo App",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.alpha(alphaState)
            )
        }

    }
}


