package me.shiven.alarmee

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import me.shiven.alarmee.domain.usecase.CheckOnBoardingCompletedUseCase
import me.shiven.alarmee.domain.usecase.SetOnBoardingCompletedUseCase
import me.shiven.alarmee.ui.navigation.OnBoardingGraph
import me.shiven.alarmee.ui.navigation.OnBoardingNavigation
import me.shiven.alarmee.ui.theme.AlarmeeTheme
import javax.inject.Inject

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var checkOnBoardingCompletedUseCase: CheckOnBoardingCompletedUseCase

    @Inject
    lateinit var setOnBoardingCompletedUseCase: SetOnBoardingCompletedUseCase

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOnBoardingCompleted = runBlocking { checkOnBoardingCompletedUseCase().first() }

        enableEdgeToEdge()
        setContent {
            AlarmeeTheme {
                val navController = rememberNavController()
                OnBoardingNavigation(
                    navController,
                    startDestination = if(isOnBoardingCompleted) OnBoardingGraph.AlarmAppRoute
                    else OnBoardingGraph.WelcomeAppRoute,
                    setOnBoardingCompletedUseCase
                )
            }
        }
    }
}
