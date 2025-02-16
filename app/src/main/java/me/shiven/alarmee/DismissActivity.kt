package me.shiven.alarmee

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.shiven.alarmee.domain.model.ChallengeList
import me.shiven.alarmee.domain.usecase.GetChallengeSelectedUseCase
import me.shiven.alarmee.domain.usecase.PlayAlarmToneUseCase
import me.shiven.alarmee.domain.usecase.StopAlarmToneUseCase
import me.shiven.alarmee.ui.dismiss.DismissApp
import me.shiven.alarmee.ui.gridchallenge.GridApp
import me.shiven.alarmee.ui.navigation.DismissGraph
import me.shiven.alarmee.ui.nfcchallenge.NFCApp
import me.shiven.alarmee.ui.qrchallenge.QRApp
import me.shiven.alarmee.ui.theme.AlarmeeTheme
import javax.inject.Inject

@AndroidEntryPoint
class DismissActivity : ComponentActivity() {

    @Inject
    lateinit var playAlarmToneUseCase: PlayAlarmToneUseCase

    @Inject
    lateinit var stopAlarmToneUseCase: StopAlarmToneUseCase

    @Inject
    lateinit var getChallengeSelectedUseCase: GetChallengeSelectedUseCase

    private lateinit var startDestination: DismissGraph

    private lateinit var challengeToShow: ChallengeList

    private lateinit var navigateTo: DismissGraph

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toneReceived = intent?.getStringExtra("toneUri")
        val showChallenge = intent?.getBooleanExtra("toShowChallenge", false)
        val startDestinationString = intent?.getStringExtra("startDestinationID")

        startDestination = when(startDestinationString) {
            "gridGame" -> {
                DismissGraph.GridAppRoute
            }

            "qrCode" -> {
                DismissGraph.QRAppRoute
            }

            "nfcTag" -> {
                DismissGraph.NFCAppRoute
            }

            else -> {
                DismissGraph.DismissAppRoute
            }
        }

        challengeToShow = runBlocking { getChallengeSelectedUseCase().first() }
        println("Challenge to show received from preferences $challengeToShow")
        println("startDestination string as received from challenge box demo $startDestinationString")
        println("To show challenge or not condition $showChallenge")

        lifecycleScope.launch {
            playAlarmToneUseCase(toneReceived!!)
        }

        navigateTo = when(challengeToShow) {
            ChallengeList.GRID_GAME -> {
                DismissGraph.GridAppRoute
            }

            ChallengeList.NFC_TAG -> {
                DismissGraph.NFCAppRoute
            }

            ChallengeList.QR_CODE -> {
                DismissGraph.QRAppRoute
            }
        }

        setContent {
            val navController = rememberNavController()
            AlarmeeTheme {
                NavHost(
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable<DismissGraph.DismissAppRoute> {
                        DismissApp{
                            if(showChallenge == true) {
                                navController.navigate(navigateTo) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                stopAlarmToneUseCase()
                                this@DismissActivity.finish()
                            }
                        }
                    }
                    composable<DismissGraph.QRAppRoute> {
                        QRApp()
                    }
                    composable<DismissGraph.NFCAppRoute> {
                        NFCApp()
                    }
                    composable<DismissGraph.GridAppRoute> {
                        GridApp {
                            stopAlarmToneUseCase()
                            this@DismissActivity.finish()
                        }
                    }
                }
            }
        }
    }
}

