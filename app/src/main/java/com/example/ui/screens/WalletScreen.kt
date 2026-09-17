package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FuelLog
import com.example.data.model.MaintenanceHistory
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.ExpensesComparisonChart
import com.example.ui.components.LargeNativeAdPlaceholder
import com.example.ui.components.SavingsSummaryPieChart
import com.example.ui.viewmodel.VehicleViewModel
import java.text.SimpleDateFormat
import java.util.*

enum class WalletFilter {
    ALL, MAINTENANCE, FUEL, CORRECTIVE
}

@Composable
fun WalletScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicles by viewModel.vehicles.collectAsState()
    val history by viewModel.allHistory.collectAsState()
    val fuelLogs by viewModel.allFuelLogs.collectAsState()
    val stats = viewModel.getEconomyStats()

    var selectedFilter by remember { mutableStateOf(WalletFilter.ALL) }
    var showAddCorrectiveDialog by remember { mutableStateOf(false) }
    var showAddFuelDialog by remember { mutableStateOf(false) }

    // Edit states
    var editingHistory by remember { mutableStateOf<MaintenanceHistory?>(null) }
    var editingFuelLog by remember { mutableStateOf<FuelLog?>(null) }
    var deletingHistory by remember { mutableStateOf<MaintenanceHistory?>(null) }
    var deletingFuelLog by remember { mutableStateOf<FuelLog?>(null) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header: Total Investido vs Economia (ROI Balanço Geral)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF0B0F19), // Structural Trust
                                Color(0xFF162032)
                            )
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = Color(0xFFFF521D),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "FINANCEIRO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Balanço do Veículo",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Surface(
                            color = Color.White.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Investido vs Economizado",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFCBD5E1),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Total Investido (Manutenções + Combustível)
                        Column {
                            Text(
                                text = "Total Investido",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "R$ %,.2f".format(stats.totalInvested),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        // Economia Gerada
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Economia Salva",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "+R$ %,.2f".format(stats.netSavings),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFFF521D) // Brand Primary Action
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Manutenções preventivas e consumo controlados evitam quebras e reduzem o custo por KM.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF94A3B8),
                        fontSize = 11.sp
                    )
                }
            }
        }

        // 2. Summary Mini Cards: Combustível, Preventiva, Corretiva
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Card Combustível
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.LocalGasStation, contentDescription = "Combustível", tint = Color(0xFFE65100), modifier = Modifier.size(20.dp))
                            Text("Abastecimento", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "R$ %,.2f".format(stats.totalSpentFuel),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = "%.1f L • %s".format(
                                stats.totalLiters,
                                if (stats.averageKmPerL > 0) "%.1f km/L".format(stats.averageKmPerL) else "km/L"
                            ),
                            fontSize = 11.sp,
                            color = Color(0xFF8D6E63)
                        )
                    }
                }

                // Card Preventiva
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Build, contentDescription = "Preventiva", tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                            Text("Preventiva", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2E7D32))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "R$ %,.2f".format(stats.totalSpentPreventive),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color(0xFF2E7D32)
                        )
                        Text(
                            text = "${history.count { it.isPreventive }} revisões",
                            fontSize = 11.sp,
                            color = Color(0xFF388E3C)
                        )
                    }
                }

                // Card Quebra Corretiva
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "Quebra", tint = Color(0xFFC62828), modifier = Modifier.size(20.dp))
                            Text("Quebras", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC62828))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "R$ %,.2f".format(stats.totalSpentCorrective),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = Color(0xFFC62828)
                        )
                        Text(
                            text = "${history.count { !it.isPreventive }} quebras",
                            fontSize = 11.sp,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        }

        // 3. Action Buttons: Lançar Quebra & Lançar Abastecimento
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botão Lançar Quebra Corretiva
                Button(
                    onClick = { showAddCorrectiveDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("log_corrective_button")
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Quebra", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Lançar Quebra", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Botão Lançar Abastecimento
                Button(
                    onClick = { showAddFuelDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF6C00)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("log_fuel_wallet_button")
                ) {
                    Icon(Icons.Default.LocalGasStation, contentDescription = "Abastecimento", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Abastecimento", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // 4. Chart 1: Expenses Comparison
        item {
            ExpensesComparisonChart(
                preventiveCost = stats.totalSpentPreventive,
                correctiveCost = stats.totalSpentCorrective
            )
        }

        // 5. Chart 2: Savings ROI Donut Chart
        item {
            SavingsSummaryPieChart(
                netSavings = stats.netSavings,
                preventiveCost = stats.totalSpentPreventive
            )
        }

        // 6. Ad Banner
        item {
            LargeNativeAdPlaceholder()
        }

        // 7. Relatório Detalhado de Gastos & Histórico (Com Opção de Editar / Excluir)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Relatório Detalhado",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Toque no item para editar ou excluir",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Filter Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        FilterChip(
                            selected = selectedFilter == WalletFilter.ALL,
                            onClick = { selectedFilter = WalletFilter.ALL },
                            label = { Text("Tudo (${history.size + fuelLogs.size})") }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == WalletFilter.MAINTENANCE,
                            onClick = { selectedFilter = WalletFilter.MAINTENANCE },
                            label = { Text("Revisões & Óleo (${history.count { it.isPreventive }})") },
                            leadingIcon = {
                                Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF2E7D32))
                            }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == WalletFilter.FUEL,
                            onClick = { selectedFilter = WalletFilter.FUEL },
                            label = { Text("Combustível (${fuelLogs.size})") },
                            leadingIcon = {
                                Icon(Icons.Default.LocalGasStation, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFEF6C00))
                            }
                        )
                    }
                    item {
                        FilterChip(
                            selected = selectedFilter == WalletFilter.CORRECTIVE,
                            onClick = { selectedFilter = WalletFilter.CORRECTIVE },
                            label = { Text("Quebras (${history.count { !it.isPreventive }})") },
                            leadingIcon = {
                                Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFC62828))
                            }
                        )
                    }
                }
            }
        }

        // List combined items based on filter
        val showFuel = selectedFilter == WalletFilter.ALL || selectedFilter == WalletFilter.FUEL
        val showMaintenance = selectedFilter == WalletFilter.ALL || selectedFilter == WalletFilter.MAINTENANCE
        val showCorrective = selectedFilter == WalletFilter.ALL || selectedFilter == WalletFilter.CORRECTIVE

        val filteredHistory = history.filter {
            if (it.isPreventive) showMaintenance else showCorrective
        }

        val filteredFuel = if (showFuel) fuelLogs else emptyList()

        if (filteredHistory.isEmpty() && filteredFuel.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                ) {
                    Box(modifier = Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Nenhum registro encontrado nesta categoria.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Display Maintenance History Items (Revisões, Troca de Óleo, Quebras)
        items(filteredHistory, key = { "maint_${it.id}" }) { record ->
            val vehicle = vehicles.find { it.id == record.vehicleId }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (record.isPreventive) Color(0xFFF1F8E9) else Color(0xFFFFEBEE)
                ),
                border = BorderStroke(
                    1.dp,
                    if (record.isPreventive) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (record.isPreventive) Color(0xFF2E7D32) else Color(0xFFC62828)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (record.isPreventive) Icons.Default.Build else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = record.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = "${vehicle?.name ?: "Veículo"} • %,.0f KM • %s".format(record.mileage, dateFormat.format(Date(record.date))),
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "R$ %,.2f".format(record.cost),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = if (record.isPreventive) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                            Text(
                                text = if (record.isPreventive) "Preventiva" else "Quebra Corretiva",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (record.isPreventive) Color(0xFF2E7D32) else Color(0xFFC62828)
                            )
                        }
                    }

                    if (record.notes.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = record.notes,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (record.isPreventive) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFE8F5E9))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Savings, contentDescription = null, tint = Color(0xFF1B5E20), modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Economia Estimada:", fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Medium)
                            }
                            Text("+R$ %,.2f".format(record.getEstimatedSavings()), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        }
                    }

                    // Edit & Delete Action Buttons
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { editingHistory = record },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { deletingHistory = record },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // Display Fuel Log Items
        items(filteredFuel, key = { "fuel_${it.id}" }) { fuelLog ->
            val vehicle = vehicles.find { it.id == fuelLog.vehicleId }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                border = BorderStroke(1.dp, Color(0xFFFFECB3))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF6C00)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalGasStation,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Abastecimento (${vehicle?.name ?: "Veículo"})",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = "KM %,.0f • %s".format(fuelLog.mileage, dateFormat.format(Date(fuelLog.date))),
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "R$ %,.2f".format(fuelLog.totalCost),
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 15.sp,
                                color = Color(0xFFE65100)
                            )
                            Text(
                                text = "%.2f L @ R$ %.2f/L".format(fuelLog.liters, fuelLog.pricePerLiter),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFBF360C)
                            )
                        }
                    }

                    // Edit & Delete Action Buttons
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { editingFuelLog = fuelLog },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(30.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Editar", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(
                            onClick = { deletingFuelLog = fuelLog },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }

    // 8. Dialog: Add Corrective Break
    if (showAddCorrectiveDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var title by remember { mutableStateOf("") }
        var costStr by remember { mutableStateOf("") }
        var mileageStr by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddCorrectiveDialog = false },
            title = { Text("Lançar Quebra Corretiva") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Registre quebras mecânicas para acompanhar o histórico financeiro e comparar com a prevenção.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Vehicle Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val currentVehicle = vehicles.find { it.id == vehicleId }
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentVehicle?.name ?: "Selecione o Veículo")
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            vehicles.forEach { vehicle ->
                                DropdownMenuItem(
                                    text = { Text(vehicle.name) },
                                    onClick = {
                                        vehicleId = vehicle.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("O que quebrou? (ex: Correia, Bomba d'água)") },
                        modifier = Modifier.fillMaxWidth().testTag("corrective_title_input")
                    )

                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text("Quilometragem (KM)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text("Custo do Conserto (R$)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("corrective_cost_input")
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações (Peças trocadas, Oficina)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val vId = vehicleId
                        val cost = costStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val mileage = mileageStr.toDoubleOrNull() ?: 0.0
                        if (vId != null && title.isNotEmpty() && cost > 0.0) {
                            viewModel.addManualMaintenanceHistory(
                                vehicleId = vId,
                                title = title,
                                cost = cost,
                                mileage = mileage,
                                isPreventive = false,
                                notes = notes
                            )
                        }
                        showAddCorrectiveDialog = false
                    },
                    modifier = Modifier.testTag("submit_corrective_button")
                ) {
                    Text("Lançar Quebra")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCorrectiveDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 9. Dialog: Add Fuel Log
    if (showAddFuelDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var mileageStr by remember { mutableStateOf("") }
        var litersStr by remember { mutableStateOf("") }
        var priceStr by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddFuelDialog = false },
            title = { Text("Novo Abastecimento") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Vehicle Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val currentVehicle = vehicles.find { it.id == vehicleId }
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentVehicle?.name ?: "Selecione o Veículo")
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            vehicles.forEach { vehicle ->
                                DropdownMenuItem(
                                    text = { Text(vehicle.name) },
                                    onClick = {
                                        vehicleId = vehicle.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text("Odômetro / KM Atual") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = litersStr,
                        onValueChange = { litersStr = it },
                        label = { Text("Litros Abastecidos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Preço por Litro (R$)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val vId = vehicleId
                        val mileage = mileageStr.toDoubleOrNull() ?: 0.0
                        val liters = litersStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val price = priceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        if (vId != null && liters > 0.0 && price > 0.0) {
                            viewModel.addFuelLog(
                                vehicleId = vId,
                                mileage = mileage,
                                liters = liters,
                                pricePerLiter = price
                            )
                        }
                        showAddFuelDialog = false
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddFuelDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 10. Dialog: Edit Maintenance History
    editingHistory?.let { record ->
        var title by remember { mutableStateOf(record.title) }
        var costStr by remember { mutableStateOf(record.cost.toString()) }
        var mileageStr by remember { mutableStateOf("%.0f".format(record.mileage)) }
        var isPreventive by remember { mutableStateOf(record.isPreventive) }
        var notes by remember { mutableStateOf(record.notes) }

        AlertDialog(
            onDismissRequest = { editingHistory = null },
            title = { Text("Editar Registro de Manutenção") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Descrição do Serviço / Peça") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text("Custo Total (R$)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text("Quilometragem (KM)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = isPreventive,
                            onClick = { isPreventive = true },
                            label = { Text("Preventiva") }
                        )
                        FilterChip(
                            selected = !isPreventive,
                            onClick = { isPreventive = false },
                            label = { Text("Corretiva (Quebra)") }
                        )
                    }

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cost = costStr.replace(",", ".").toDoubleOrNull() ?: record.cost
                        val mileage = mileageStr.toDoubleOrNull() ?: record.mileage
                        viewModel.updateMaintenanceHistory(
                            id = record.id,
                            vehicleId = record.vehicleId,
                            title = title,
                            cost = cost,
                            mileage = mileage,
                            isPreventive = isPreventive,
                            notes = notes,
                            date = record.date
                        )
                        editingHistory = null
                    }
                ) {
                    Text("Salvar Alterações")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingHistory = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 11. Dialog: Edit Fuel Log
    editingFuelLog?.let { fuelLog ->
        var mileageStr by remember { mutableStateOf("%.0f".format(fuelLog.mileage)) }
        var litersStr by remember { mutableStateOf(fuelLog.liters.toString()) }
        var priceStr by remember { mutableStateOf(fuelLog.pricePerLiter.toString()) }

        AlertDialog(
            onDismissRequest = { editingFuelLog = null },
            title = { Text("Editar Abastecimento") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = mileageStr,
                        onValueChange = { mileageStr = it },
                        label = { Text("Odômetro / KM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = litersStr,
                        onValueChange = { litersStr = it },
                        label = { Text("Litros Abastecidos") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Preço por Litro (R$)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mileage = mileageStr.toDoubleOrNull() ?: fuelLog.mileage
                        val liters = litersStr.replace(",", ".").toDoubleOrNull() ?: fuelLog.liters
                        val price = priceStr.replace(",", ".").toDoubleOrNull() ?: fuelLog.pricePerLiter
                        viewModel.updateFuelLog(
                            id = fuelLog.id,
                            vehicleId = fuelLog.vehicleId,
                            mileage = mileage,
                            liters = liters,
                            pricePerLiter = price,
                            date = fuelLog.date
                        )
                        editingFuelLog = null
                    }
                ) {
                    Text("Salvar Alterações")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingFuelLog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // 12. Delete Confirmation Dialogs
    deletingHistory?.let { record ->
        AlertDialog(
            onDismissRequest = { deletingHistory = null },
            title = { Text("Excluir Registro?") },
            text = { Text("Deseja realmente remover '${record.title}'? Essa ação não pode ser desfeita.") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteHistory(record.id)
                        deletingHistory = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingHistory = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    deletingFuelLog?.let { fuelLog ->
        AlertDialog(
            onDismissRequest = { deletingFuelLog = null },
            title = { Text("Excluir Abastecimento?") },
            text = { Text("Deseja realmente remover o abastecimento de %.1f L? Essa ação não pode ser desfeita.".format(fuelLog.liters)) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteFuelLog(fuelLog.id)
                        deletingFuelLog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingFuelLog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
