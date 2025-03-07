package me.shiven.alarmee.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.shiven.alarmee.domain.usecase.SetOnBoardingCompletedUseCase
import me.shiven.alarmee.ui.permission.PermissionApp
import me.shiven.alarmee.ui.welcome.WelcomeApp

sealed class OnBoardingGraph {

    @Serializable
    data object WelcomeAppRoute : OnBoardingGraph()

    @Serializable
    data object PermissionAppRoute: OnBoardingGraph()

    @Serializable
    data object AlarmAppRoute : OnBoardingGraph()

}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun OnBoardingNavigation(navController: NavHostController, startDestination: OnBoardingGraph,
                         setOnBoardingCompletedUseCase: SetOnBoardingCompletedUseCase) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<OnBoardingGraph.WelcomeAppRoute>(
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(durationMillis = 500)
                )
            }
        ) {
            WelcomeApp(
                onProceedClick = {
                    navController.navigate(OnBoardingGraph.PermissionAppRoute)
                }
            )
        }

        composable<OnBoardingGraph.PermissionAppRoute>(
            enterTransition = {
                // The Permission screen slides in from the right with fade and scale effects.
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = 500)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            },
            popExitTransition = {
                // When exiting from the Permission screen, slide it out to the left.
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {
            PermissionApp {
                runBlocking { setOnBoardingCompletedUseCase(true) }
                navController.navigate(OnBoardingGraph.AlarmAppRoute) {
                    popUpTo(OnBoardingGraph.PermissionAppRoute) { inclusive = true }
                }
            }
        }

        composable<OnBoardingGraph.AlarmAppRoute>(
            enterTransition = {
                // The Alarm screen appears with a slide in from the right along with fade and scale.
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeIn(animationSpec = tween(durationMillis = 500)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(durationMillis = 500)
                )
            },
            popExitTransition = {
                // When leaving the Alarm screen, it slides out completely to the left.
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(durationMillis = 500)
                ) + fadeOut(animationSpec = tween(durationMillis = 500))
            }
        ) {
            MainScreen()
        }
    }
}
