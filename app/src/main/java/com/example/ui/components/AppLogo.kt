package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.BrandPrimaryLight
import com.example.ui.theme.BrandStructural

@Composable
fun AppBrandLogo(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    cornerRadius: Dp = (size.value * 0.28f).dp
) {
    Box(
        modifier = modifier
            .size(size)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(cornerRadius), spotColor = BrandPrimary.copy(alpha = 0.5f))
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        BrandPrimaryLight,
                        BrandPrimary
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_app_brand_logo),
            contentDescription = "RevisAuto Logo",
            modifier = Modifier.fillMaxSize(0.85f)
        )
    }
}

@Composable
fun AppBrandHeader(
    modifier: Modifier = Modifier,
    subtitle: String = "GESTOR & MANUTENÇÃO"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppBrandLogo(size = 40.dp)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = "RevisAuto",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = BrandPrimary,
                letterSpacing = 0.5.sp
            )
        }
    }
}
