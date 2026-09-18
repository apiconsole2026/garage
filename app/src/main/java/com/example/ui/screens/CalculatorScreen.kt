package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Vehicle
import com.example.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicles by viewModel.vehicles.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabTitles = listOf("Viagem", "Flex", "Custo/KM")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Calculadoras",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // 3 Tabs na parte superior: Viagem, Flex e Custo/KM
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp)),
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (vehicles.isEmpty()) {
            EmptyVehiclesCalculatorPlaceholder()
        } else {
            when (selectedTabIndex) {
                0 -> TripPlannerTab(vehicles = vehicles, viewModel = viewModel)
                1 -> FlexEquilibriumTab(vehicles = vehicles, viewModel = viewModel)
                2 -> CostPerKmTab(vehicles = vehicles, viewModel = viewModel)
            }
        }
    }
}

@Composable
private fun EmptyVehiclesCalculatorPlaceholder() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Nenhum veículo cadastrado",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cadastre um veículo na Garagem para utilizar as calculadoras com os dados automáticos de consumo, capacidade do tanque e histórico.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ==========================================
// ABA 1 — PLANEJADOR DE VIAGEM
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TripPlannerTab(
    vehicles: List<Vehicle>,
    viewModel: VehicleViewModel
) {
    var selectedVehicle by remember(vehicles) { mutableStateOf(vehicles.firstOrNull()) }
    var vehicleDropdownExpanded by remember { mutableStateOf(false) }

    val vehicleFuelType = selectedVehicle?.fuelType ?: "Flex"
    val isFlex = vehicleFuelType.equals("Flex", ignoreCase = true)
    val isDiesel = vehicleFuelType.equals("Diesel", ignoreCase = true)

    // Trip inputs
    var distanceStr by remember { mutableStateOf("") }
    var selectedFuelMode by remember(selectedVehicle) {
        mutableStateOf(if (isDiesel) "Diesel" else "Gasolina")
    }
    var fuelPriceStr by remember(selectedVehicle, selectedFuelMode) {
        val initialPrice = selectedVehicle?.fuelPrice ?: 5.89
        mutableStateOf(if (initialPrice > 0) "%.2f".format(initialPrice).replace(".", ",") else "5,89")
    }

    // Secondary fuel price for Flex comparison
    var ethanolPriceStr by remember(selectedVehicle) {
        val base = selectedVehicle?.fuelPrice ?: 5.89
        val ethEst = if (base > 0) base * 0.70 else 3.99
        mutableStateOf("%.2f".format(ethEst).replace(".", ","))
    }

    // Fetch average consumption from vehicle or real logs
    val avgConsumption = remember(selectedVehicle) {
        selectedVehicle?.let { viewModel.getVehicleConsumption(it.id) } ?: 11.5
    }

    // Tank capacity parsed
    val tankCapacity = remember(selectedVehicle) {
        val raw = selectedVehicle?.fuelTankCapacity?.filter { it.isDigit() || it == '.' } ?: ""
        raw.toDoubleOrNull() ?: 50.0
    }

    val distance = distanceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val fuelPrice = fuelPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val ethanolPrice = ethanolPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Planejador de Viagem",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Vehicle Selector Dropdown
                    ExposedDropdownMenuBox(
                        expanded = vehicleDropdownExpanded,
                        onExpandedChange = { vehicleDropdownExpanded = !vehicleDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedVehicle?.let { "${it.brand} ${it.model}" } ?: "Selecione um veículo",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Veículo Selecionado") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = vehicleDropdownExpanded,
                            onDismissRequest = { vehicleDropdownExpanded = false }
                        ) {
                            vehicles.forEach { v ->
                                DropdownMenuItem(
                                    text = { Text("${v.brand} ${v.model} (${v.fuelType})") },
                                    onClick = {
                                        selectedVehicle = v
                                        vehicleDropdownExpanded = false
                                        selectedFuelMode = if (v.fuelType.equals("Diesel", ignoreCase = true)) "Diesel" else "Gasolina"
                                    }
                                )
                            }
                        }
                    }

                    // Consumption indicator
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Média de Consumo:",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = "%.1f km/L".format(avgConsumption),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // Trip Distance
                    OutlinedTextField(
                        value = distanceStr,
                        onValueChange = { distanceStr = it },
                        label = { Text("Distância da Viagem (KM)") },
                        suffix = { Text("km") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Fuel Type Selection
                    if (isFlex) {
                        Text("Combustível Principal:", style = MaterialTheme.typography.labelMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = selectedFuelMode == "Gasolina",
                                onClick = { selectedFuelMode = "Gasolina" },
                                label = { Text("Gasolina") },
                                leadingIcon = {
                                    Icon(Icons.Default.LocalGasStation, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            )
                            FilterChip(
                                selected = selectedFuelMode == "Etanol",
                                onClick = { selectedFuelMode = "Etanol" },
                                label = { Text("Etanol") },
                                leadingIcon = {
                                    Icon(Icons.Default.Eco, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            )
                        }
                    } else if (isDiesel) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Veículo Diesel: cálculo automático com Diesel S-10",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    // Price per liter
                    OutlinedTextField(
                        value = fuelPriceStr,
                        onValueChange = { fuelPriceStr = it },
                        label = { Text("Preço do Combustível ($selectedFuelMode)") },
                        prefix = { Text("R$ ") },
                        suffix = { Text("/L") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isFlex) {
                        OutlinedTextField(
                            value = ethanolPriceStr,
                            onValueChange = { ethanolPriceStr = it },
                            label = { Text("Preço do Etanol (para comparativo)") },
                            prefix = { Text("R$ ") },
                            suffix = { Text("/L") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Calculation Results in Real Time
        if (distance > 0 && avgConsumption > 0 && fuelPrice > 0) {
            val totalLitersNeeded = distance / avgConsumption
            val completeTanks = if (tankCapacity > 0) totalLitersNeeded / tankCapacity else 0.0
            val totalTripCost = totalLitersNeeded * fuelPrice

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Resultado da Viagem",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Icon(
                                Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Total de Litros", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("%.1f Litros".format(totalLitersNeeded), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Tanques Completos", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    text = if (tankCapacity > 0) "%.1f tanque(s)".format(completeTanks) else "N/D",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Custo Estimado da Viagem:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "R$ %,.2f".format(totalTripCost),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Flex Comparative Card (Gasolina vs Etanol)
            if (isFlex && ethanolPrice > 0) {
                // Etanol consumption typically ~70% efficiency of gasoline
                val ethanolConsumption = avgConsumption * 0.70
                val litersEthanol = distance / ethanolConsumption
                val totalCostEthanol = litersEthanol * ethanolPrice

                val costGasoline = if (selectedFuelMode == "Gasolina") totalTripCost else (distance / avgConsumption) * fuelPrice
                val cheaperWithEthanol = totalCostEthanol < costGasoline
                val difference = kotlin.math.abs(costGasoline - totalCostEthanol)

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (cheaperWithEthanol) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                        ),
                        border = BorderStroke(1.dp, if (cheaperWithEthanol) Color(0xFFA5D6A7) else Color(0xFFFFCC80))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (cheaperWithEthanol) Icons.Default.Eco else Icons.Default.LocalGasStation,
                                        contentDescription = null,
                                        tint = if (cheaperWithEthanol) Color(0xFF2E7D32) else Color(0xFFE65100),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Comparativo Flex: Viagem",
                                        fontWeight = FontWeight.Bold,
                                        color = if (cheaperWithEthanol) Color(0xFF2E7D32) else Color(0xFFE65100)
                                    )
                                }
                                Surface(
                                    color = if (cheaperWithEthanol) Color(0xFF2E7D32) else Color(0xFFE65100),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (cheaperWithEthanol) "ETANOL VENCE" else "GASOLINA VENCE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Gasolina: R$ %,.2f".format(costGasoline), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text("Etanol: R$ %,.2f".format(totalCostEthanol), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                            }

                            Text(
                                text = "Economia de R$ %,.2f escolhendo %s.".format(
                                    difference,
                                    if (cheaperWithEthanol) "Etanol" else "Gasolina"
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = if (cheaperWithEthanol) Color(0xFF1B5E20) else Color(0xFFBF360C)
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ABA 2 — PONTO DE EQUILÍBRIO FLEX
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FlexEquilibriumTab(
    vehicles: List<Vehicle>,
    viewModel: VehicleViewModel
) {
    var selectedVehicle by remember(vehicles) { mutableStateOf(vehicles.firstOrNull()) }
    var vehicleDropdownExpanded by remember { mutableStateOf(false) }

    // Tank capacity parsed
    val tankCapacity = remember(selectedVehicle) {
        val raw = selectedVehicle?.fuelTankCapacity?.filter { it.isDigit() || it == '.' } ?: ""
        raw.toDoubleOrNull() ?: 50.0
    }

    // Default or prefilled consumptions
    val baseKmPerL = remember(selectedVehicle) {
        selectedVehicle?.let { viewModel.getVehicleConsumption(it.id) } ?: 11.5
    }

    var gasPriceStr by remember { mutableStateOf("5,89") }
    var ethanolPriceStr by remember { mutableStateOf("3,99") }
    var kmPerLGasStr by remember(selectedVehicle) { mutableStateOf("%.1f".format(baseKmPerL).replace(".", ",")) }
    var kmPerLEthanolStr by remember(selectedVehicle) { mutableStateOf("%.1f".format(baseKmPerL * 0.70).replace(".", ",")) }

    val gasPrice = gasPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val ethanolPrice = ethanolPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val kmPerLGas = kmPerLGasStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val kmPerLEthanol = kmPerLEthanolStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Ponto de Equilíbrio Flex Real",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Vehicle Selector
                    ExposedDropdownMenuBox(
                        expanded = vehicleDropdownExpanded,
                        onExpandedChange = { vehicleDropdownExpanded = !vehicleDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedVehicle?.let { "${it.brand} ${it.model}" } ?: "Selecione um veículo",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Veículo (para dados de consumo)") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = vehicleDropdownExpanded,
                            onDismissRequest = { vehicleDropdownExpanded = false }
                        ) {
                            vehicles.forEach { v ->
                                DropdownMenuItem(
                                    text = { Text("${v.brand} ${v.model}") },
                                    onClick = {
                                        selectedVehicle = v
                                        vehicleDropdownExpanded = false
                                        val cons = viewModel.getVehicleConsumption(v.id)
                                        kmPerLGasStr = "%.1f".format(cons).replace(".", ",")
                                        kmPerLEthanolStr = "%.1f".format(cons * 0.70).replace(".", ",")
                                    }
                                )
                            }
                        }
                    }

                    // Gasolina Inputs
                    OutlinedTextField(
                        value = gasPriceStr,
                        onValueChange = { gasPriceStr = it },
                        label = { Text("Preço da Gasolina (R$/L)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Etanol Inputs
                    OutlinedTextField(
                        value = ethanolPriceStr,
                        onValueChange = { ethanolPriceStr = it },
                        label = { Text("Preço do Etanol (R$/L)") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Specific consumptions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = kmPerLGasStr,
                            onValueChange = { kmPerLGasStr = it },
                            label = { Text("Consumo Gasolina") },
                            suffix = { Text("km/L") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = kmPerLEthanolStr,
                            onValueChange = { kmPerLEthanolStr = it },
                            label = { Text("Consumo Etanol") },
                            suffix = { Text("km/L") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Calculation: Real equilibrium point (kmPerLEthanol / kmPerLGas)
        if (gasPrice > 0 && ethanolPrice > 0 && kmPerLGas > 0 && kmPerLEthanol > 0) {
            val realEfficiencyRatio = kmPerLEthanol / kmPerLGas
            val maxEthanolPriceCompensates = gasPrice * realEfficiencyRatio
            val isEthanolBetter = ethanolPrice < maxEthanolPriceCompensates

            // Cost per KM with each fuel
            val costPerKmGas = gasPrice / kmPerLGas
            val costPerKmEthanol = ethanolPrice / kmPerLEthanol

            // Estimated savings per full tank (in KM driven on one tank of Gas vs same distance on Ethanol)
            val rangeOnGasTank = tankCapacity * kmPerLGas
            val gasFullTankCost = tankCapacity * gasPrice
            val ethanolCostForSameRange = (rangeOnGasTank / kmPerLEthanol) * ethanolPrice
            val fullTankSavings = kotlin.math.abs(gasFullTankCost - ethanolCostForSameRange)

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isEthanolBetter) Color(0xFFE8F5E9) else Color(0xFFFFF3E0)
                    ),
                    border = BorderStroke(1.5.dp, if (isEthanolBetter) Color(0xFF4CAF50) else Color(0xFFFF9800))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Recomendação Ideal",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isEthanolBetter) Color(0xFF2E7D32) else Color(0xFFE65100)
                                )
                                Text(
                                    text = if (isEthanolBetter) "Abasteça com ETANOL" else "Abasteça com GASOLINA",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Black,
                                    color = if (isEthanolBetter) Color(0xFF1B5E20) else Color(0xFFBF360C)
                                )
                            }
                            Icon(
                                imageVector = if (isEthanolBetter) Icons.Default.Eco else Icons.Default.LocalGasStation,
                                contentDescription = null,
                                tint = if (isEthanolBetter) Color(0xFF2E7D32) else Color(0xFFE65100),
                                modifier = Modifier.size(36.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Ponto de Equilíbrio Exato: %.1f%% do valor da gasolina".format(realEfficiencyRatio * 100),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Compensa etanol se estiver abaixo de R$ %.2f por litro".format(maxEthanolPriceCompensates),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isEthanolBetter) Color(0xFF2E7D32) else Color(0xFFC62828)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Custo / KM (Gasolina)", fontSize = 11.sp, color = Color.DarkGray)
                                Text("R$ %.2f / km".format(costPerKmGas), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Custo / KM (Etanol)", fontSize = 11.sp, color = Color.DarkGray)
                                Text("R$ %.2f / km".format(costPerKmEthanol), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isEthanolBetter) Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Economia estimada de R$ %.2f por tanque cheio (%.0f L)".format(fullTankSavings, tankCapacity),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// ABA 3 — CUSTO POR KM
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CostPerKmTab(
    vehicles: List<Vehicle>,
    viewModel: VehicleViewModel
) {
    val allFuelLogs by viewModel.allFuelLogs.collectAsState()
    var selectedVehicle by remember(vehicles) { mutableStateOf(vehicles.firstOrNull()) }
    var vehicleDropdownExpanded by remember { mutableStateOf(false) }

    val vehicleFuelLogs = remember(selectedVehicle, allFuelLogs) {
        selectedVehicle?.let { v -> allFuelLogs.filter { it.vehicleId == v.id }.sortedBy { it.mileage } } ?: emptyList()
    }

    val hasHistory = vehicleFuelLogs.size >= 2

    // Calculate historical numbers if available
    var calculatedKmDriven = 0.0
    var calculatedTotalSpent = 0.0
    if (hasHistory) {
        calculatedKmDriven = vehicleFuelLogs.last().mileage - vehicleFuelLogs.first().mileage
        calculatedTotalSpent = vehicleFuelLogs.sumOf { it.totalCost }
    }

    // Manual input fallbacks
    var manualKmDrivenStr by remember(selectedVehicle, hasHistory) {
        mutableStateOf(if (hasHistory) "%.0f".format(calculatedKmDriven) else "500")
    }
    var manualTotalSpentStr by remember(selectedVehicle, hasHistory) {
        mutableStateOf(if (hasHistory) "%.2f".format(calculatedTotalSpent).replace(".", ",") else "280,00")
    }

    val kmDriven = manualKmDrivenStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val totalSpent = manualTotalSpentStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    // Benchmark expected cost per km
    val expectedKmPerL = remember(selectedVehicle) {
        selectedVehicle?.let { viewModel.getVehicleConsumption(it.id) } ?: 11.5
    }
    val benchmarkFuelPrice = selectedVehicle?.fuelPrice.takeIf { it != null && it > 0 } ?: 5.89
    val expectedCostPerKm = if (expectedKmPerL > 0) benchmarkFuelPrice / expectedKmPerL else 0.50

    val dailyUsageKm = selectedVehicle?.dailyMileage ?: 30.0

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Análise de Custo por KM",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Vehicle Dropdown
                    ExposedDropdownMenuBox(
                        expanded = vehicleDropdownExpanded,
                        onExpandedChange = { vehicleDropdownExpanded = !vehicleDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedVehicle?.let { "${it.brand} ${it.model}" } ?: "Selecione um veículo",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Veículo") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = vehicleDropdownExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = vehicleDropdownExpanded,
                            onDismissRequest = { vehicleDropdownExpanded = false }
                        ) {
                            vehicles.forEach { v ->
                                DropdownMenuItem(
                                    text = { Text("${v.brand} ${v.model}") },
                                    onClick = {
                                        selectedVehicle = v
                                        vehicleDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    if (hasHistory) {
                        Surface(
                            color = Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Histórico detectado: valores pré-preenchidos automaticamente",
                                    fontSize = 11.sp,
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Inputs: KM driven
                    OutlinedTextField(
                        value = manualKmDrivenStr,
                        onValueChange = { manualKmDrivenStr = it },
                        label = { Text("KM Rodados no Período") },
                        suffix = { Text("km") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Inputs: Total Spent
                    OutlinedTextField(
                        value = manualTotalSpentStr,
                        onValueChange = { manualTotalSpentStr = it },
                        label = { Text("Total Gasto com Combustível") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Calculations & Status Cards
        if (kmDriven > 0 && totalSpent > 0) {
            val realCostPerKm = totalSpent / kmDriven
            val diffPercentage = if (expectedCostPerKm > 0) ((realCostPerKm - expectedCostPerKm) / expectedCostPerKm) * 100 else 0.0

            // Indicator Colors: Green (within expected), Yellow (<=10% above), Red (>20% above)
            val (statusColor, statusBg, statusTitle, statusDesc) = when {
                diffPercentage <= 5.0 -> Quadruple(
                    Color(0xFF2E7D32),
                    Color(0xFFE8F5E9),
                    "Dentro do Esperado",
                    "Excelente! Seu custo por KM está alinhado ou melhor que a média técnica."
                )
                diffPercentage <= 15.0 -> Quadruple(
                    Color(0xFFE65100),
                    Color(0xFFFFF3E0),
                    "Atenção: 10% Acima",
                    "O custo está levemente acima do esperado. Verifique calibragem e trânsito intenso."
                )
                else -> Quadruple(
                    Color(0xFFC62828),
                    Color(0xFFFFEBEE),
                    "Alerta: Mais de 20% Acima",
                    "Custo elevado por KM. Pode indicar bicos sujos, velas desgastadas ou filtro de ar obstruído."
                )
            }

            // Monthly Projection based on dailyMileage
            val monthlyEstimatedKm = dailyUsageKm * 30
            val monthlyProjectedCost = monthlyEstimatedKm * realCostPerKm

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = statusBg),
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Diagnóstico do Custo / KM",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                            Surface(color = statusColor, shape = RoundedCornerShape(6.dp)) {
                                Text(
                                    text = statusTitle,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Divider(color = statusColor.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Custo Real / KM", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                                Text(
                                    text = "R$ %.2f / km".format(realCostPerKm),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = statusColor
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Média Esperada", style = MaterialTheme.typography.bodySmall, color = Color.DarkGray)
                                Text(
                                    text = "R$ %.2f / km".format(expectedCostPerKm),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Text(
                            text = statusDesc,
                            style = MaterialTheme.typography.bodySmall,
                            color = statusColor
                        )

                        // Projeção Mensal
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.85f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "Projeção Mensal de Combustível",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Rodando %.0f km/mês (%.0f km/dia):".format(monthlyEstimatedKm, dailyUsageKm),
                                        fontSize = 11.sp,
                                        color = Color.DarkGray
                                    )
                                    Text(
                                        text = "R$ %,.2f".format(monthlyProjectedCost),
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = statusColor
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

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
