package me.shiven.alarmee.ui.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import me.shiven.alarmee.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutApp(onGitHubClick: (String) -> Unit = {}) {

    val githubURL = "https://github.com/Shiven-saini/"
    val twitterURL = "https://x.com/rip_syntax"
    val sourceURL = "https://github.com/Shiven-saini/Alarmee"

    val context = LocalContext.current

    // Create an implicit intent with ACTION_VIEW
    val githubIntent = Intent(Intent.ACTION_VIEW, Uri.parse(githubURL))
    val twitterIntent = Intent(Intent.ACTION_VIEW, Uri.parse(twitterURL))
    val sourceIntent = Intent(Intent.ACTION_VIEW, Uri.parse(sourceURL))

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                    text = "About",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 10.dp)
                    ) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.podcast),
                    contentDescription = "Chill guy"
                )
                Column(modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopStart)) {
                    Text(
                        text = "Alarmee App",
                        style = MaterialTheme.typography.titleMedium,
                        textDecoration = TextDecoration.Underline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Wake up smarter with this challenge-based alarm! " +
                                "Scan NFC, QR code, or solve grid puzzles to dismiss the alarm. No " +
                                "more snoozing and oversleeping. Sleep if you can!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                        .clickable {
                            context.startActivity(sourceIntent)
                        }
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.github_mark),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = "Source code",
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            // Developer Section
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "Developed with 🚀",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Image(
                                painter = painterResource(R.drawable.shiven_logo),
                                contentDescription = "logo"
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.clickable {
                                    // TODO: Implement the try catch when no browser is installed.
                                    context.startActivity(githubIntent)
                                }
                            ){

                                Icon(
                                    painter = painterResource(R.drawable.github_mark),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )

                                Text(
                                    text = "@shiven-saini",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp),
                                modifier = Modifier.clickable {
                                    // TODO: Implement the try catch when no browser is installed.
                                    context.startActivity(twitterIntent)
                                }
                            ){

                                Icon(
                                    painter = painterResource(R.drawable.logo_black),
                                    contentDescription = null,
                                    modifier = Modifier.size(22.dp)
                                )

                                Text(
                                    text = "@rip_syntax",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }

                    }
                }
            }
        }
    }
}

