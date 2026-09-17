package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FuelLog
import com.example.data.model.MaintenanceAlert
import com.example.data.model.Vehicle
import com.example.data.model.VehicleBrandsDb
import com.example.data.model.VehicleSpec
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.AddVehicleDialog
import com.example.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GarageScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicles by viewModel.vehicles.collectAsState()
    val allAlerts by viewModel.allAlerts.collectAsState()
    val allFuelLogs by viewModel.allFuelLogs.collectAsState()
    val allHistory by viewModel.allHistory.collectAsState()
    
    val selectedVehicleId by viewModel.selectedVehicleId.collectAsState()
    val selectedVehicle = vehicles.find { it.id == selectedVehicleId }

    var showAddVehicleDialog by remember { mutableStateOf(false) }
    var showLogFuelDialog by remember { mutableStateOf(false) }
    var showCompleteMaintenanceDialog by remember { mutableStateOf<MaintenanceAlert?>(null) }
    var showAddAlertDialog by remember { mutableStateOf(false) }
    var showUpdateMileageDialog by remember { mutableStateOf(false) }
    var showManualChecklistMilestone by remember { mutableStateOf<Int?>(null) }
    var showEditSpecsDialog by remember { mutableStateOf(false) }
    var showCatalogPickerModal by remember { mutableStateOf(false) }

    // Auto-select first vehicle if none selected
    LaunchedEffect(vehicles, selectedVehicleId) {
        if (selectedVehicleId == null && vehicles.isNotEmpty()) {
            viewModel.selectVehicle(vehicles.first().id)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Top Toolbar inside Screen
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Minha Garagem",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = "Fichas técnicas, manutenções e consumo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (vehicles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(0.92f),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(28.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Garage,
                                contentDescription = "Vazio",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sua garagem está vazia",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Cadastre seu carro ou moto com a ficha técnica oficial pronta para receber alertas inteligentes.",
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { showAddVehicleDialog = true },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cadastrar do Catálogo", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // Horizontal row of vehicles (LazyRow instead of ScrollableTabRow for zero crash & smooth flinging)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vehicles, key = { it.id }) { vehicle ->
                    val isSelected = vehicle.id == selectedVehicleId
                    Surface(
                        onClick = { viewModel.selectVehicle(vehicle.id) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        tonalElevation = if (isSelected) 4.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (vehicle.type) {
                                    "MOTO" -> Icons.Default.TwoWheeler
                                    "TRUCK" -> Icons.Default.LocalShipping
                                    else -> Icons.Default.DirectionsCar
                                },
                                contentDescription = vehicle.type,
                                tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = vehicle.name,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            selectedVehicle?.let { vehicle ->
                val vehicleAlerts = allAlerts.filter { it.vehicleId == vehicle.id }
                val vehicleFuelLogs = allFuelLogs.filter { it.vehicleId == vehicle.id }.sortedByDescending { it.mileage }
                val realConsumption = viewModel.getVehicleConsumption(vehicle.id)

                // Main Content List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Vehicle Info Header Card
                    item(key = "vehicle_header_${vehicle.id}") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "${vehicle.brand} ${vehicle.model}",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Ano: ${vehicle.year} • Combustível: ${vehicle.fuelType} • ${if (vehicle.type == "MOTO") "Moto" else "Carro"}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    IconButton(
                                        onClick = { viewModel.deleteVehicle(vehicle) },
                                        modifier = Modifier.testTag("delete_vehicle_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Excluir veículo",
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }

                                Divider(modifier = Modifier.padding(vertical = 12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "KM Atual: %,.0f".format(vehicle.currentMileage),
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "Rodagem média: %.0f KM/dia".format(vehicle.dailyMileage),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Button(
                                        onClick = { showUpdateMileageDialog = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.height(36.dp)
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Atualizar KM", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Section: Technical Specifications Card ("Ficha Técnica Completa")
                    item(key = "vehicle_specs_${vehicle.id}") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.FactCheck,
                                            contentDescription = "Ficha Técnica",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Ficha Técnica do Veículo",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    TextButton(
                                        onClick = { showEditSpecsDialog = true },
                                        contentPadding = PaddingValues(horizontal = 8.dp)
                                    ) {
                                        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Ajustar Ficha", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Grid of technical items
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Row 1: Motor & Óleo
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        SpecItemBox(
                                            title = "Motor & Cilindrada",
                                            value = vehicle.engineSpec.ifEmpty { "Motor Padrão" },
                                            icon = Icons.Default.PrecisionManufacturing,
                                            modifier = Modifier.weight(1f)
                                        )
                                        SpecItemBox(
                                            title = "Óleo Recomendado",
                                            value = vehicle.oilSpec.ifEmpty { "5W30 Sintético" },
                                            icon = Icons.Default.Opacity,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Row 2: Capacidade de Óleo & Calibragem de Pneus
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        SpecItemBox(
                                            title = "Capacidade do Cárter",
                                            value = vehicle.oilCapacity.ifEmpty { "3.5 Litros" },
                                            icon = Icons.Default.Speed,
                                            modifier = Modifier.weight(1f)
                                        )
                                        SpecItemBox(
                                            title = "Calibragem dos Pneus",
                                            value = vehicle.tirePressure.ifEmpty { "32 PSI Diant. / 30 PSI Tras." },
                                            icon = Icons.Default.TireRepair,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Row 3: Tanque & Velas
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        SpecItemBox(
                                            title = "Tanque Combustível",
                                            value = vehicle.fuelTankCapacity.ifEmpty { "45 Litros" },
                                            icon = Icons.Default.LocalGasStation,
                                            modifier = Modifier.weight(1f)
                                        )
                                        SpecItemBox(
                                            title = "Velas de Ignição",
                                            value = vehicle.sparkPlug.ifEmpty { "NGK Laser Iridium" },
                                            icon = Icons.Default.Bolt,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    // Row 4: Fluido de Freio & Arrefecimento
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        SpecItemBox(
                                            title = "Fluido de Freio",
                                            value = vehicle.brakeFluid.ifEmpty { "DOT 4 Sintético" },
                                            icon = Icons.Default.Warning,
                                            modifier = Modifier.weight(1f)
                                        )
                                        SpecItemBox(
                                            title = "Arrefecimento",
                                            value = vehicle.coolantType.ifEmpty { "Orgânico Long Life 50/50" },
                                            icon = Icons.Default.Thermostat,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section: Consumption statistics
                    item(key = "vehicle_consumption_${vehicle.id}") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.LocalGasStation,
                                            contentDescription = "Combustível",
                                            tint = Color(0xFFEF6C00)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Consumo Médio Real",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    Button(
                                        onClick = { showLogFuelDialog = true },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFEF6C00)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.height(32.dp).testTag("log_fuel_button")
                                    ) {
                                        Icon(Icons.Default.LocalGasStation, contentDescription = "Abastecer", modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Abasteci", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = "%.1f km/L".format(realConsumption),
                                            style = MaterialTheme.typography.titleLarge,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFFEF6C00)
                                        )
                                        Text(
                                            text = if (vehicleFuelLogs.size >= 2) "Média baseada nos últimos abastecimentos"
                                                   else "Estimativa padrão (abasteça 2 vezes para calibrar)",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text(
                                            text = "R$ %.2f / L".format(vehicle.fuelPrice),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "Preço Pago",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section: Manual Checklist Concierge
                    item(key = "manual_concierge_${vehicle.id}") {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "Manual",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Concierge: Revisão de Manual",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Toque na quilometragem planejada para inspecionar itens do manual, lançar gastos e redefinir alertas em lote:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                
                                // KM Milestone chips using fluid LazyRow (safe, no negative index, smooth scrolling!)
                                val milestones = if (vehicle.type == "MOTO") {
                                    listOf(3000, 6000, 9000, 12000, 15000, 18000, 24000, 30000)
                                } else {
                                    listOf(10000, 20000, 30000, 40000, 50000, 60000, 80000, 100000)
                                }

                                LazyRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(milestones, key = { it }) { km ->
                                        Surface(
                                            onClick = { showManualChecklistMilestone = km },
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        ) {
                                            Text(
                                                text = "%,.0f KM".format(km.toDouble()),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Section: Alerts Header
                    item(key = "alerts_header_${vehicle.id}") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Lembretes de Revisão",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(onClick = { showAddAlertDialog = true }) {
                                Icon(
                                    imageVector = Icons.Default.AddCircle,
                                    contentDescription = "Adicionar lembrete customizado",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    if (vehicleAlerts.isEmpty()) {
                        item(key = "no_alerts_${vehicle.id}") {
                            Text(
                                text = "Nenhum lembrete configurado para este veículo.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(vehicleAlerts, key = { "alert_${it.id}" }) { alert ->
                            val remaining = alert.getRemainingKm(vehicle.currentMileage)
                            val isOverdue = alert.isOverdue(vehicle.currentMileage)
                            val isWarning = alert.isWarning(vehicle.currentMileage)
                            val statusColor = when {
                                isOverdue -> Color(0xFFC62828) // Urgent
                                isWarning -> Color(0xFFF57F17) // Attention
                                else -> Color(0xFF2E7D32) // Ok
                            }
                            val statusBg = when {
                                isOverdue -> Color(0xFFFEEBEE)
                                isWarning -> Color(0xFFFFFDE7)
                                else -> Color(0xFFE8F5E9)
                            }
                            val statusText = when {
                                isOverdue -> "Urgente! Passou %,.0f KM".format(-remaining)
                                isWarning -> "Atenção: Vence em %,.0f KM".format(remaining)
                                else -> "Em dia: Faltam %,.0f KM".format(remaining)
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = alert.title,
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Row(
                                            modifier = Modifier.padding(top = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(statusBg)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = statusText,
                                                    color = statusColor,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Intervalo: %,.0f KM".format(alert.intervalKm),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { showCompleteMaintenanceDialog = alert }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Marcar como feito",
                                            tint = Color(0xFF2E7D32)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Sponsored block between modules
                    item(key = "sponsored_banner") {
                        AdBannerPlaceholder(adText = "Precisa trocar óleo? Visite a Rede MaxLube e ganhe filtro de cortesia apresentando o app!")
                    }

                    // Section: Fuel Log List (Abastecimentos)
                    item(key = "fuel_history_header") {
                        Text(
                            text = "Histórico de Abastecimento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (vehicleFuelLogs.isEmpty()) {
                        item(key = "no_fuel_logs") {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f))
                            ) {
                                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Nenhum abastecimento registrado ainda.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        items(vehicleFuelLogs, key = { "fuel_${it.id}" }) { log ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.LocalGasStation,
                                        contentDescription = "Abastecido",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "%.1f Litros • KM %,.0f".format(log.liters, log.mileage),
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "R$ %.2f /L".format(log.pricePerLiter),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "R$ %.2f".format(log.totalCost),
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    IconButton(onClick = { viewModel.deleteFuelLog(log.id) }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Excluir abastecimento",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal / Dialog for Catalog Picker
    if (showCatalogPickerModal) {
        var catalogSearchQuery by remember { mutableStateOf("") }
        var typeFilter by remember { mutableStateOf<String?>("CAR") } // "CAR", "MOTO"

        AlertDialog(
            onDismissRequest = { showCatalogPickerModal = false },
            title = {
                Column {
                    Text("Catálogo Oficial de Veículos", fontWeight = FontWeight.Bold)
                    Text("Selecione para preencher toda a ficha técnica automaticamente:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth().heightIn(max = 480.dp)) {
                    // Type filter chips
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = typeFilter == "CAR",
                            onClick = { typeFilter = "CAR" },
                            label = { Text("Carros") },
                            leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                        FilterChip(
                            selected = typeFilter == "MOTO",
                            onClick = { typeFilter = "MOTO" },
                            label = { Text("Motos") },
                            leadingIcon = { Icon(Icons.Default.TwoWheeler, contentDescription = null, modifier = Modifier.size(16.dp)) }
                        )
                    }

                    OutlinedTextField(
                        value = catalogSearchQuery,
                        onValueChange = { catalogSearchQuery = it },
                        placeholder = { Text("Buscar modelo (ex: Onix, Civic, CG 160, Fazer...)") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        trailingIcon = {
                            if (catalogSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { catalogSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = null)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    val filteredSpecs = remember(catalogSearchQuery, typeFilter) {
                        VehicleBrandsDb.searchSpecs(catalogSearchQuery, typeFilter)
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().weight(1f, fill = false),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (filteredSpecs.isEmpty()) {
                            item {
                                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                                    Text("Nenhum modelo encontrado com esse nome.", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
                                }
                            }
                        } else {
                            items(filteredSpecs, key = { "${it.brand}_${it.model}" }) { spec ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            // Auto-fill into vehicle registration
                                            viewModel.addVehicle(
                                                name = spec.model,
                                                type = spec.type,
                                                brand = spec.brand,
                                                model = spec.model,
                                                year = 2022,
                                                currentMileage = 0.0,
                                                dailyMileage = 30.0,
                                                fuelType = spec.fuelType,
                                                fuelPrice = 5.89,
                                                engineSpec = spec.engine,
                                                oilSpec = spec.oilSpec,
                                                oilCapacity = spec.oilCapacity,
                                                tirePressure = spec.tirePressure,
                                                sparkPlug = spec.sparkPlug,
                                                fuelTankCapacity = spec.fuelTank,
                                                brakeFluid = spec.brakeFluid,
                                                coolantType = spec.coolant
                                            )
                                            showCatalogPickerModal = false
                                            showAddVehicleDialog = false
                                        },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "${spec.brand} ${spec.model}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                            )
                                            Text(
                                                text = spec.engine,
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.primary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "🛢️ ${spec.oilSpec} (${spec.oilCapacity}) • 💨 ${spec.tirePressure}",
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showCatalogPickerModal = false }) {
                    Text("Fechar")
                }
            }
        )
    }

    // Edit Vehicle Technical Specs Dialog
    if (showEditSpecsDialog) {
        selectedVehicle?.let { vehicle ->
            var engineSpec by remember { mutableStateOf(vehicle.engineSpec) }
            var oilSpec by remember { mutableStateOf(vehicle.oilSpec) }
            var oilCapacity by remember { mutableStateOf(vehicle.oilCapacity) }
            var tirePressure by remember { mutableStateOf(vehicle.tirePressure) }
            var sparkPlug by remember { mutableStateOf(vehicle.sparkPlug) }
            var fuelTankCapacity by remember { mutableStateOf(vehicle.fuelTankCapacity) }
            var brakeFluid by remember { mutableStateOf(vehicle.brakeFluid) }
            var coolantType by remember { mutableStateOf(vehicle.coolantType) }

            AlertDialog(
                onDismissRequest = { showEditSpecsDialog = false },
                title = { Text("Ajustar Ficha Técnica") },
                text = {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item {
                            Text(
                                text = "Personalize as especificações recomendadas para o seu veículo:",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = engineSpec,
                                onValueChange = { engineSpec = it },
                                label = { Text("Motor / Cilindrada (ex: 1.0 12V Flex / 160cc)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = oilSpec,
                                onValueChange = { oilSpec = it },
                                label = { Text("Viscosidade do Óleo (ex: 0W20 Sintético)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = oilCapacity,
                                onValueChange = { oilCapacity = it },
                                label = { Text("Capacidade de Óleo (ex: 3.5 Litros)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = tirePressure,
                                onValueChange = { tirePressure = it },
                                label = { Text("Calibragem dos Pneus (ex: 32 PSI / 30 PSI)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = sparkPlug,
                                onValueChange = { sparkPlug = it },
                                label = { Text("Velas de Ignição (ex: NGK Laser Iridium)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = fuelTankCapacity,
                                onValueChange = { fuelTankCapacity = it },
                                label = { Text("Capacidade do Tanque (ex: 50 Litros)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = brakeFluid,
                                onValueChange = { brakeFluid = it },
                                label = { Text("Fluido de Freio (ex: DOT 4)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            OutlinedTextField(
                                value = coolantType,
                                onValueChange = { coolantType = it },
                                label = { Text("Líquido de Arrefecimento (ex: Orgânico Rosa)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.updateVehicleSpecs(
                                vehicleId = vehicle.id,
                                engineSpec = engineSpec,
                                oilSpec = oilSpec,
                                oilCapacity = oilCapacity,
                                tirePressure = tirePressure,
                                sparkPlug = sparkPlug,
                                fuelTankCapacity = fuelTankCapacity,
                                brakeFluid = brakeFluid,
                                coolantType = coolantType
                            )
                            showEditSpecsDialog = false
                        }
                    ) {
                        Text("Salvar Ficha")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditSpecsDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // Update Mileage Dialog
    if (showUpdateMileageDialog) {
        selectedVehicle?.let { vehicle ->
            var mileageStr by remember { mutableStateOf("%.0f".format(vehicle.currentMileage)) }

            AlertDialog(
                onDismissRequest = { showUpdateMileageDialog = false },
                title = { Text("Atualizar Quilometragem") },
                text = {
                    Column {
                        Text(
                            text = "Insira o KM atual exibido no painel de seu veículo:",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        OutlinedTextField(
                            value = mileageStr,
                            onValueChange = { mileageStr = it },
                            label = { Text("Quilometragem (KM)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("update_mileage_input")
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val newMileage = mileageStr.toDoubleOrNull() ?: 0.0
                            viewModel.updateVehicleMileage(vehicle.id, newMileage)
                            showUpdateMileageDialog = false
                        },
                        modifier = Modifier.testTag("submit_mileage_button")
                    ) {
                        Text("Atualizar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showUpdateMileageDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // Add Vehicle Dialog
    if (showAddVehicleDialog) {
        AddVehicleDialog(
            viewModel = viewModel,
            onDismiss = { showAddVehicleDialog = false }
        )
    }

    // Log Fuel Dialog
    if (showLogFuelDialog) {
        selectedVehicle?.let { vehicle ->
            var currentMileageStr by remember { mutableStateOf("%.0f".format(vehicle.currentMileage)) }
            var litersStr by remember { mutableStateOf("") }
            var priceStr by remember { mutableStateOf("%.2f".format(vehicle.fuelPrice)) }

            AlertDialog(
                onDismissRequest = { showLogFuelDialog = false },
                title = { Text("Registrar Abastecimento") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Abasteça sempre de tanque cheio para calibrar o consumo preciso em km/L.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        OutlinedTextField(
                            value = currentMileageStr,
                            onValueChange = { currentMileageStr = it },
                            label = { Text("Quilometragem Atual (KM)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("fuel_mileage_input")
                        )

                        OutlinedTextField(
                            value = litersStr,
                            onValueChange = { litersStr = it },
                            label = { Text("Quantidade de Litros (L)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth().testTag("fuel_liters_input")
                        )

                        OutlinedTextField(
                            value = priceStr,
                            onValueChange = { priceStr = it },
                            label = { Text("Valor por Litro (R$)") },
                            prefix = { Text("R$ ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val mileage = currentMileageStr.toDoubleOrNull() ?: vehicle.currentMileage
                            val liters = litersStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                            val price = priceStr.replace(",", ".").toDoubleOrNull() ?: vehicle.fuelPrice

                            if (liters > 0.0) {
                                viewModel.addFuelLog(
                                    vehicleId = vehicle.id,
                                    mileage = mileage,
                                    liters = liters,
                                    pricePerLiter = price
                                )
                            }
                            showLogFuelDialog = false
                        },
                        modifier = Modifier.testTag("submit_fuel_button")
                    ) {
                        Text("Salvar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogFuelDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // Complete Maintenance Alert Dialog (when completed directly from details)
    showCompleteMaintenanceDialog?.let { alert ->
        var costStr by remember { mutableStateOf("") }
        var currentMileageStr by remember { mutableStateOf("") }
        var notes by remember { mutableStateOf("") }
        
        val currentVehicle = vehicles.find { it.id == alert.vehicleId }
        LaunchedEffect(alert) {
            currentVehicle?.let {
                currentMileageStr = "%.0f".format(it.currentMileage)
            }
        }

        AlertDialog(
            onDismissRequest = { showCompleteMaintenanceDialog = null },
            title = { Text("Concluir ${alert.title}") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Informe o custo de manutenção preventiva realizado para computarmos seus ganhos na Carteira.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = currentMileageStr,
                        onValueChange = { currentMileageStr = it },
                        label = { Text("Quilometragem (KM)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notas (Ex: Óleo Selênia 5W30)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val mileage = currentMileageStr.toDoubleOrNull() ?: currentVehicle?.currentMileage ?: 0.0
                        val cost = costStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        viewModel.completeMaintenanceAlert(
                            alertId = alert.id,
                            mileage = mileage,
                            cost = cost,
                            notes = notes
                        )
                        showCompleteMaintenanceDialog = null
                    }
                ) {
                    Text("Concluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteMaintenanceDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Add Custom Alert Dialog
    if (showAddAlertDialog) {
        selectedVehicle?.let { vehicle ->
            var title by remember { mutableStateOf("") }
            var type by remember { mutableStateOf("OTHER") }
            var intervalKmStr by remember { mutableStateOf("") }

            AlertDialog(
                onDismissRequest = { showAddAlertDialog = false },
                title = { Text("Novo Lembrete Personalizado") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Nome da Manutenção (ex: Correia Dentada)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text("Categoria:", style = MaterialTheme.typography.labelSmall)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("OIL" to "Óleo", "BRAKES" to "Freio", "TIRES" to "Pneu", "OTHER" to "Outro").forEach { (vType, label) ->
                                FilterChip(
                                    selected = type == vType,
                                    onClick = { type = vType },
                                    label = { Text(label) }
                                )
                            }
                        }

                        OutlinedTextField(
                            value = intervalKmStr,
                            onValueChange = { intervalKmStr = it },
                            label = { Text("Frequência de Troca (em KM)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val interval = intervalKmStr.toDoubleOrNull() ?: 10000.0
                            if (title.isNotEmpty()) {
                                viewModel.addCustomAlert(
                                    vehicleId = vehicle.id,
                                    title = title,
                                    type = type,
                                    intervalKm = interval,
                                    currentMileage = vehicle.currentMileage
                                )
                            }
                            showAddAlertDialog = false
                        }
                    ) {
                        Text("Adicionar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddAlertDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }

    // Manual Checklist Milestone Dialog
    showManualChecklistMilestone?.let { km ->
        val originalItems = remember(km) {
            when (km) {
                3000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo / Peneira (Limpeza/Troca)", "Lubrificação da Corrente", "Calibragem dos Pneus")
                6000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo", "Vela de Ignição (Inspeção)", "Filtro de Ar", "Pastilhas de Freio")
                9000 -> listOf("Óleo de Motor (Troca)", "Filtro de Combustível", "Folga de Válvulas", "Líquido de Freio")
                10000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Combustível (Troca)", "Alinhamento e Balanceamento (Serviço)", "Pastilhas de Freio (Inspeção)")
                20000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Cabine/Ar Condicionado (Troca)", "Filtro de Ar do Motor (Troca)", "Rodízio de Pneus (Serviço)")
                30000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Combustível (Troca)", "Fluido de Freio (Troca)", "Correia de Acessórios (Inspeção)")
                40000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Velas de Ignição (Troca)", "Filtro de Ar do Motor (Troca)", "Líquido de Arrefecimento (Troca)", "Inspeção Geral de Suspensão")
                50000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Combustível (Troca)", "Alinhamento e Balanceamento (Serviço)", "Palhetas do Para-brisa (Troca)")
                60000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Ar do Motor (Troca)", "Filtro de Cabine (Troca)", "Correia Dentada (Troca)", "Fluido de Direção Hidráulica (Troca)")
                80000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Combustível (Troca)", "Velas de Ignição (Troca)", "Filtro de Ar do Motor (Troca)", "Pastilhas e Discos de Freio (Troca)", "Amortecedores (Inspeção)")
                100000 -> listOf("Óleo de Motor (Troca)", "Filtro de Óleo (Troca)", "Filtro de Cabine (Troca)", "Líquido de Arrefecimento (Troca)", "Cabo de Velas (Troca)", "Inspeção Geral do Motor e Câmbio")
                else -> listOf("Troca de Óleo e Filtro", "Alinhamento de Rodas", "Filtro de Ar", "Pastilhas de Freio")
            }
        }

        val checkedStates = remember(km) {
            mutableStateMapOf<String, Boolean>().apply {
                originalItems.forEach { item -> this[item] = true }
            }
        }

        var checklistCostStr by remember { mutableStateOf("") }
        var checklistNotes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showManualChecklistMilestone = null },
            title = { Text("Manual: Checklist de %,.0f KM".format(km.toDouble())) },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "Selecione o que você realmente trocou ou realizou para registrar na carteira e resetar os alertas preventivos relacionados:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(originalItems) { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { checkedStates[item] = !(checkedStates[item] ?: true) }
                                .padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = checkedStates[item] ?: true,
                                onCheckedChange = { checkedStates[item] = it }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = item, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    item {
                        OutlinedTextField(
                            value = checklistCostStr,
                            onValueChange = { checklistCostStr = it },
                            label = { Text("Custo Total (R$)") },
                            prefix = { Text("R$ ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = checklistNotes,
                            onValueChange = { checklistNotes = it },
                            label = { Text("Notas do Mecânico / Oficina (Opcional)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cost = checklistCostStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                        val checkedList = originalItems.filter { checkedStates[it] ?: true }
                        if (checkedList.isNotEmpty() && selectedVehicle != null) {
                            val activeAlerts = allAlerts.filter { it.vehicleId == selectedVehicle.id }
                            
                            // 1. Reset related alerts
                            checkedList.forEach { item ->
                                val matchedAlert = when {
                                    item.contains("Óleo", ignoreCase = true) -> activeAlerts.find { it.type == "OIL" }
                                    item.contains("Pneu", ignoreCase = true) || item.contains("Alinhamento", ignoreCase = true) || item.contains("Rodízio", ignoreCase = true) -> activeAlerts.find { it.type == "TIRES" }
                                    item.contains("Freio", ignoreCase = true) || item.contains("Pastilha", ignoreCase = true) || item.contains("Disco", ignoreCase = true) -> activeAlerts.find { it.type == "BRAKES" }
                                    else -> null
                                }
                                matchedAlert?.let { alert ->
                                    viewModel.completeMaintenanceAlert(
                                        alertId = alert.id,
                                        mileage = selectedVehicle.currentMileage,
                                        cost = 0.0, // avoid double cost
                                        notes = "Resetado via Revisão dos %,.0f KM".format(km.toDouble())
                                    )
                                }
                            }

                            // 2. Register complete history
                            val itemsJoined = checkedList.joinToString(", ")
                            val notesToSave = if (checklistNotes.isNotEmpty()) {
                                "Checklist: $itemsJoined. Obs: $checklistNotes"
                            } else {
                                "Checklist: $itemsJoined."
                            }

                            viewModel.addManualMaintenanceHistory(
                                vehicleId = selectedVehicle.id,
                                title = "Revisão Manual %,.0f KM".format(km.toDouble()),
                                cost = cost,
                                mileage = selectedVehicle.currentMileage,
                                isPreventive = true,
                                notes = notesToSave
                            )
                        }
                        showManualChecklistMilestone = null
                    }
                ) {
                    Text("Registrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showManualChecklistMilestone = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun SpecItemBox(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = value,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
            }
        }
    }
}
