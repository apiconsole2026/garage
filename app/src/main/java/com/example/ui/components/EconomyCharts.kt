package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MaintenanceHistory
import kotlin.math.max

@Composable
fun ExpensesComparisonChart(
    preventiveCost: Double,
    correctiveCost: Double,
    modifier: Modifier = Modifier
) {
    val total = preventiveCost + correctiveCost
    val animatedPreventivePercent = animateFloatAsState(
        targetValue = if (total > 0) (preventiveCost / total).toFloat() else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "preventivePercent"
    )
    val animatedCorrectivePercent = animateFloatAsState(
        targetValue = if (total > 0) (correctiveCost / total).toFloat() else 0.5f,
        animationSpec = tween(durationMillis = 1000),
        label = "correctivePercent"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Comparativo de Gastos (R$)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Text(
                text = "Demonstrativo entre Preventiva vs Corretiva",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start).padding(bottom = 24.dp)
            )

            if (preventiveCost == 0.0 && correctiveCost == 0.0) {
                // Empty state for chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum histórico financeiro registrado ainda.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Bar 1: Preventiva
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "R$ %.2f".format(preventiveCost),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .fillMaxHeight(animatedPreventivePercent.value * 0.9f)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFF4CAF50), Color(0xFF2E7D32))
                                    ),
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Preventiva",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Bar 2: Corretiva
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "R$ %.2f".format(correctiveCost),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC62828)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .fillMaxHeight(animatedCorrectivePercent.value * 0.9f)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(Color(0xFFEF5350), Color(0xFFC62828))
                                    ),
                                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Corretiva",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavingsSummaryPieChart(
    netSavings: Double,
    preventiveCost: Double,
    modifier: Modifier = Modifier
) {
    val totalImpact = netSavings + preventiveCost
    val sweepAngle = animateFloatAsState(
        targetValue = if (totalImpact > 0) (netSavings / totalImpact).toFloat() * 360f else 240f,
        animationSpec = tween(durationMillis = 1200),
        label = "savingsSweep"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = "Retorno do Investimento",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "A cada R$ 1,00 gasto em prevenção, você evita R$ 2,50 adicionais em quebras corretivas.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color(0xFF2E7D32), RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Economia Líquida",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(Color(0xFF1E88E5), RoundedCornerShape(3.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Custo de Manutenção",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    val diameter = size.minDimension
                    val radius = diameter / 2f
                    val center = Offset(size.width / 2f, size.height / 2f)

                    // Draw Background Circle (Total preventive cost portion)
                    drawCircle(
                        color = Color(0xFF1E88E5),
                        radius = radius,
                        center = center
                    )

                    // Draw Sweep Segment (Savings portion)
                    drawArc(
                        color = Color(0xFF2E7D32),
                        startAngle = -90f,
                        sweepAngle = sweepAngle.value,
                        useCenter = true,
                        size = Size(diameter, diameter),
                        topLeft = Offset(center.x - radius, center.y - radius)
                    )

                    // Draw inner hole for Donut Chart look
                    drawCircle(
                        color = Color(0xFFFFFDF9), // Match surface background color
                        radius = radius * 0.55f,
                        center = center
                    )
                }

                val percentText = if (totalImpact > 0) {
                    ((netSavings / totalImpact) * 100).toInt().toString() + "%"
                } else {
                    "71%"
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = percentText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32)
                    )
                    Text(
                        text = "Salvo",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
