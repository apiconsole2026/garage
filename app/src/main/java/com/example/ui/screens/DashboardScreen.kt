package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.MaintenanceAlert
import com.example.data.model.Vehicle
import com.example.ui.components.AdBannerPlaceholder
import com.example.ui.components.AddVehicleDialog
import com.example.ui.viewmodel.VehicleViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.example.ui.NotificationHelper
import androidx.compose.runtime.mutableStateListOf

@Composable
fun DashboardScreen(
    viewModel: VehicleViewModel,
    onNavigateToGarage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vehicles by viewModel.vehicles.collectAsState()
    val alerts by viewModel.allAlerts.collectAsState()
    val history by viewModel.allHistory.collectAsState()
    
    val economyStats = viewModel.getEconomyStats()

    var showCompleteMaintenanceDialog by remember { mutableStateOf<MaintenanceAlert?>(null) }
    var showAddFuelDialog by remember { mutableStateOf(false) }
    var showAddVehicleDialog by remember { mutableStateOf(false) }
    var showAddMaintenanceDialog by remember { mutableStateOf(false) }
    var showAddTowingDialog by remember { mutableStateOf(false) }
    var showAddGeneralExpenseDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Section / Welcome Banner
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                com.example.ui.components.AppBrandLogo(size = 36.dp, cornerRadius = 10.dp)
                IconButton(
                    onClick = { showAddVehicleDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Cadastrar do Catálogo",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }

        // Consolidated Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1: Veículos (Clickable to go to Garage)
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onNavigateToGarage),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = "Veículos",
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowForward,
                                contentDescription = "Ir para Garagem",
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${vehicles.size}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Veículos na Garagem",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Card 2: Economia
                var showExpenseMenu by remember { mutableStateOf(false) }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = "Economia",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                            Box {
                                IconButton(
                                    onClick = { showExpenseMenu = true },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Adicionar",
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = showExpenseMenu,
                                    onDismissRequest = { showExpenseMenu = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("⛽ Abastecimento") },
                                        onClick = {
                                            showExpenseMenu = false
                                            showAddFuelDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("🔧 Manutenção") },
                                        onClick = {
                                            showExpenseMenu = false
                                            showAddMaintenanceDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("🚐 Guincho") },
                                        onClick = {
                                            showExpenseMenu = false
                                            showAddTowingDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("💰 Despesa Geral") },
                                        onClick = {
                                            showExpenseMenu = false
                                            showAddGeneralExpenseDialog = true
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "R$ %.2f".format(economyStats.netSavings),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Economia Estimada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Section: Critical Alerts Across All Vehicles
        item {
            Text(
                text = "Alertas Importantes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Filter alerts to show Urgent or Warning
        val criticalAlerts = alerts.filter { alert ->
            val v = vehicles.find { it.id == alert.vehicleId }
            if (v != null) {
                alert.isCritical(v.currentMileage)
            } else false
        }.sortedBy { alert ->
            val v = vehicles.find { it.id == alert.vehicleId }
            alert.getRemainingKm(v?.currentMileage ?: 0.0)
        }

        if (criticalAlerts.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "OK",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Todas as revisões em dia!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Nenhum veículo precisa de manutenção imediata.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            items(criticalAlerts) { alert ->
                val vehicle = vehicles.find { it.id == alert.vehicleId }
                if (vehicle != null) {
                    val remainingKm = alert.getRemainingKm(vehicle.currentMileage)
                    val isUrgent = remainingKm <= 0

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUrgent) Color(0xFFFEEBEE) else Color(0xFFFFFDE7)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(
                                        color = if (isUrgent) Color(0xFFC62828) else Color(0xFFF9A825),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isUrgent) Icons.Default.Warning else Icons.Default.Info,
                                    contentDescription = "Alerta",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = alert.title,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "${vehicle.name} (${vehicle.brand} ${vehicle.model})",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    text = if (isUrgent) "Vencido há %.0f KM".format(-remainingKm)
                                           else "Vence em %.0f KM".format(remainingKm),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                    color = if (isUrgent) Color(0xFFC62828) else Color(0xFFE65100)
                                )
                            }

                            Button(
                                onClick = { showCompleteMaintenanceDialog = alert },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isUrgent) Color(0xFFC62828) else Color(0xFFF9A825)
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Concluir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Ad space placeholder
        item {
            AdBannerPlaceholder()
        }

        // Section: Garage Shortcuts
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Meus Veículos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                TextButton(onClick = onNavigateToGarage) {
                    Text("Ver Garagem", fontWeight = FontWeight.SemiBold)
                }
            }
        }

        if (vehicles.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToGarage),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = "Adicionar Veículo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Nenhum veículo cadastrado",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "Toque aqui para acessar a garagem e cadastrar seu carro, moto ou caminhão.",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        } else {
            items(vehicles) { vehicle ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.selectVehicle(vehicle.id)
                            onNavigateToGarage()
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Vehicle type icon
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (vehicle.type) {
                                    "MOTO" -> Icons.Default.TwoWheeler
                                    "TRUCK" -> Icons.Default.LocalShipping
                                    else -> Icons.Default.DirectionsCar
                                },
                                contentDescription = vehicle.type,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = vehicle.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = "${vehicle.brand} ${vehicle.model} • ${vehicle.year}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "%.0f KM".format(vehicle.currentMileage),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "+%.0f KM/dia".format(vehicle.dailyMileage),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

    // Complete Maintenance Dialog
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
            title = { Text(text = "Concluir ${alert.title}") },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Informe os detalhes da revisão preventiva executada para alimentar sua carteira de economia.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = currentMileageStr,
                        onValueChange = { currentMileageStr = it },
                        label = { Text("Quilometragem Atual (KM)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("maintenance_mileage_input")
                    )

                    OutlinedTextField(
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text("Custo da Manutenção (R$)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth().testTag("maintenance_cost_input")
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações (Peças trocadas, Marca)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                val context = LocalContext.current
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
                        NotificationHelper.sendNotification(
                            context = context,
                            title = "Manutenção Concluída ✅",
                            message = "${alert.title} realizada com sucesso! Próxima revisão recalculada e histórico registrado na Carteira."
                        )
                        showCompleteMaintenanceDialog = null
                    },
                    modifier = Modifier.testTag("submit_maintenance_button")
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCompleteMaintenanceDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Abastecimento
    if (showAddFuelDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var mileageStr by remember { mutableStateOf("") }
        var litersStr by remember { mutableStateOf("") }
        var priceStr by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        val liters = litersStr.replace(",", ".").toDoubleOrNull() ?: 0.0
        val price = priceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
        val totalCost = liters * price

        AlertDialog(
            onDismissRequest = { showAddFuelDialog = false },
            title = { Text("Novo Abastecimento") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                        label = { Text("KM Atual") },
                        suffix = { Text("KM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = litersStr,
                        onValueChange = { litersStr = it },
                        label = { Text("Litros Abastecidos") },
                        suffix = { Text("L") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = priceStr,
                        onValueChange = { priceStr = it },
                        label = { Text("Preço por Litro") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (totalCost > 0) {
                        Text(
                            text = "Total: R$ %.2f".format(totalCost),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val vId = vehicleId
                    val mileage = mileageStr.toDoubleOrNull() ?: 0.0
                    if (vId != null && liters > 0.0 && price > 0.0) {
                        viewModel.addFuelLog(
                            vehicleId = vId,
                            mileage = mileage,
                            liters = liters,
                            pricePerLiter = price
                        )
                    }
                    showAddFuelDialog = false
                }) {
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

    // Dialog: Manutenção
    if (showAddMaintenanceDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var expanded by remember { mutableStateOf(false) }
        var notes by remember { mutableStateOf("") }

        val maintenanceItems = remember {
            mutableStateListOf(
                Triple("Troca de Óleo", false, ""),
                Triple("Filtro de Ar", false, ""),
                Triple("Filtro de Combustível", false, ""),
                Triple("Velas de Ignição", false, ""),
                Triple("Correia Dentada", false, ""),
                Triple("Freios", false, ""),
                Triple("Pneus", false, ""),
                Triple("Alinhamento / Balanceamento", false, ""),
                Triple("Fluido de Freio", false, ""),
                Triple("Fluido de Arrefecimento", false, ""),
                Triple("Ar Condicionado", false, ""),
                Triple("Bateria", false, ""),
                Triple("Outros", false, "")
            )
        }

        val totalCost = maintenanceItems.sumOf {
            it.third.replace(",", ".").toDoubleOrNull() ?: 0.0
        }

        AlertDialog(
            onDismissRequest = { showAddMaintenanceDialog = false },
            title = { Text("Manutenção") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                    Text(
                        text = "Selecione os itens e informe o valor de cada:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    maintenanceItems.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Checkbox(
                                checked = item.second,
                                onCheckedChange = { checked ->
                                    maintenanceItems[index] = Triple(item.first, checked, item.third)
                                }
                            )
                            Text(
                                text = item.first,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall
                            )
                            if (item.second) {
                                OutlinedTextField(
                                    value = item.third,
                                    onValueChange = { value ->
                                        maintenanceItems[index] = Triple(item.first, item.second, value)
                                    },
                                    prefix = { Text("R$") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.width(100.dp),
                                    singleLine = true
                                )
                            }
                        }
                    }
                    if (totalCost > 0) {
                        Text(
                            text = "Total: R$ %.2f".format(totalCost),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Observações (Oficina, Peças)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val vId = vehicleId
                    if (vId != null) {
                        maintenanceItems.filter { it.second }.forEach { item ->
                            val cost = item.third.replace(",", ".").toDoubleOrNull() ?: 0.0
                            if (cost > 0.0) {
                                viewModel.addManualMaintenanceHistory(
                                    vehicleId = vId,
                                    title = item.first,
                                    cost = cost,
                                    mileage = 0.0,
                                    isPreventive = true,
                                    notes = notes
                                )
                            }
                        }
                    }
                    showAddMaintenanceDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMaintenanceDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Guincho
    if (showAddTowingDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var costStr by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddTowingDialog = false },
            title = { Text("🚐 Guincho") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text("Valor do Guincho") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val vId = vehicleId
                    val cost = costStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    if (vId != null && cost > 0.0) {
                        viewModel.addManualMaintenanceHistory(
                            vehicleId = vId,
                            title = "Guincho",
                            cost = cost,
                            mileage = 0.0,
                            isPreventive = false,
                            notes = ""
                        )
                    }
                    showAddTowingDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTowingDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Despesa Geral
    if (showAddGeneralExpenseDialog) {
        var vehicleId by remember { mutableStateOf<Int?>(vehicles.firstOrNull()?.id) }
        var costStr by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var expandedVehicle by remember { mutableStateOf(false) }
        var expandedCategory by remember { mutableStateOf(false) }
        var selectedCategory by remember { mutableStateOf("Lavagem") }

        val categories = listOf(
            "Lavagem", "Troca de Pneus", "IPVA", "Seguro",
            "Multa", "Estacionamento", "Outros"
        )

        AlertDialog(
            onDismissRequest = { showAddGeneralExpenseDialog = false },
            title = { Text("💰 Despesa Geral") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        val currentVehicle = vehicles.find { it.id == vehicleId }
                        OutlinedButton(
                            onClick = { expandedVehicle = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentVehicle?.name ?: "Selecione o Veículo")
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                        }
                        DropdownMenu(
                            expanded = expandedVehicle,
                            onDismissRequest = { expandedVehicle = false }
                        ) {
                            vehicles.forEach { vehicle ->
                                DropdownMenuItem(
                                    text = { Text(vehicle.name) },
                                    onClick = {
                                        vehicleId = vehicle.id
                                        expandedVehicle = false
                                    }
                                )
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { expandedCategory = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(selectedCategory)
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "categoria")
                        }
                        DropdownMenu(
                            expanded = expandedCategory,
                            onDismissRequest = { expandedCategory = false }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(category) },
                                    onClick = {
                                        selectedCategory = category
                                        expandedCategory = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Descrição") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = costStr,
                        onValueChange = { costStr = it },
                        label = { Text("Valor") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    val vId = vehicleId
                    val cost = costStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    if (vId != null && cost > 0.0) {
                        viewModel.addManualMaintenanceHistory(
                            vehicleId = vId,
                            title = "$selectedCategory: $description",
                            cost = cost,
                            mileage = 0.0,
                            isPreventive = false,
                            notes = ""
                        )
                    }
                    showAddGeneralExpenseDialog = false
                }) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGeneralExpenseDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showAddVehicleDialog) {
        AddVehicleDialog(
            viewModel = viewModel,
            onDismiss = { showAddVehicleDialog = false }
        )
    }
}
