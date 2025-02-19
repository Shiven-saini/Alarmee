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
import me.shiven.alarmee.ui.welcome.WelcomeApp

sealed class OnBoardingGraph {

    @Serializable
    data object WelcomeAppRoute : OnBoardingGraph()

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
                fadeOut(animationSpec = tween(500)) + scaleOut(
                    targetScale = 0.8f,
                    animationSpec = tween(500)
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
                fadeIn(animationSpec = tween(500)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(500)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it / 2 },
                    animationSpec = tween(750)
                ) + fadeOut(animationSpec = tween(750))
            }
        ) {
            MainScreen()
        }
    }
}
