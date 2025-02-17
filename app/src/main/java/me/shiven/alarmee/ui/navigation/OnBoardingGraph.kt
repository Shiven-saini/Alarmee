package me.shiven.alarmee.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import me.shiven.alarmee.domain.usecase.SetOnBoardingCompletedUseCase
import me.shiven.alarmee.ui.alarm.AlarmApp
import me.shiven.alarmee.ui.welcome.WelcomeApp

sealed class OnBoardingGraph {

    @Serializable
    data object WelcomeAppRoute : OnBoardingGraph()

    @Serializable
    data object AlarmAppRoute : OnBoardingGraph()

}

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
                    targetOffsetX = { -it / 2 },
                    animationSpec = tween(750)
                ) + fadeOut(animationSpec = tween(750)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(750)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 2 },
                    animationSpec = tween(750)
                ) + fadeIn(animationSpec = tween(750)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(750)
                )
            }
        ) {
            WelcomeApp(
                onProceedClick = {
                    runBlocking { setOnBoardingCompletedUseCase(true) }
                    navController.navigate(OnBoardingGraph.AlarmAppRoute) {
                        popUpTo(OnBoardingGraph.WelcomeAppRoute) { inclusive = true }
                    }
                }
            )
        }

        composable<OnBoardingGraph.AlarmAppRoute>(
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it / 2 },
                    animationSpec = tween(750)
                ) + fadeIn(animationSpec = tween(750)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(750)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it / 2 },
                    animationSpec = tween(750)
                ) + fadeOut(animationSpec = tween(750)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(750)
                )
            }
        ) {
            MainScreen()
        }
    }
}
