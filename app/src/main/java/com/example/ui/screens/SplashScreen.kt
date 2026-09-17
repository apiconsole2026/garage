package com.example.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NotificationHelper
import com.example.ui.components.AppBrandLogo
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryDark
import com.example.ui.theme.BrandStructural
import com.example.ui.theme.BrandSurface
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onEnterApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        onEnterApp()
    }

    val enterApp = {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationHelper.isNotificationPermissionGranted(context)) {
                permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            } else {
                onEnterApp()
            }
        } else {
            onEnterApp()
        }
    }

    // Animation state
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(700),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000)
        enterApp()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        BrandStructural, // #0B0F19 Structural Trust
                        Color(0xFF141C2E),
                        BrandStructural
                    )
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .alpha(alphaAnim)
                .fillMaxWidth()
        ) {
            // App Logo with glowing aura in #FF521D
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(pulseScale)
                    .size(136.dp)
                    .clip(CircleShape)
                    .background(BrandPrimary.copy(alpha = 0.15f))
                    .padding(12.dp)
            ) {
                AppBrandLogo(
                    size = 100.dp,
                    cornerRadius = 28.dp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // App Name
            Text(
                text = "RevisAuto",
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )

            // Badge
            Surface(
                color = BrandPrimary.copy(alpha = 0.18f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.padding(top = 6.dp)
            ) {
                Text(
                    text = "GESTOR & MANUTENÇÃO VEICULAR",
                    color = BrandPrimary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle description
            Text(
                text = "Revisões preventivas, alertas de quilometragem e fichas técnicas completas para carros e motos.",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(44.dp))

            // Progress bar indicator
            CircularProgressIndicator(
                color = BrandPrimary,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Direct Entry Button
            Button(
                onClick = { enterApp() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPrimary,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 26.dp, vertical = 12.dp),
                modifier = Modifier.testTag("enter_app_button")
            ) {
                Text("Acessar Garagem", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

