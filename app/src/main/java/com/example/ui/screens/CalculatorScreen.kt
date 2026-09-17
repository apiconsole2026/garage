package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: VehicleViewModel,
    modifier: Modifier = Modifier
) {
    val vehicles by viewModel.vehicles.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0: Flex, 1: Custo Viagem, 2: Quanto Rende

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Calculadoras de Combustível",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tab Selector
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            SegmentedButton(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 3)
            ) {
                Text("Flex", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            SegmentedButton(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 3)
            ) {
                Text("Viagem", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            SegmentedButton(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                shape = SegmentedButtonDefaults.itemShape(index = 2, count = 3)
            ) {
                Text("Rendimento", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        when (selectedTab) {
            0 -> FlexCalculator()
            1 -> TripCalculator(vehicles, viewModel)
            2 -> RangeCalculator(vehicles, viewModel)
        }
    }
}

@Composable
fun FlexCalculator() {
    var priceEthanolStr by remember { mutableStateOf("") }
    var priceGasolineStr by remember { mutableStateOf("") }

    val priceEthanol = priceEthanolStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val priceGasoline = priceGasolineStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    val ratio = if (priceGasoline > 0) priceEthanol / priceGasoline else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalGasStation, contentDescription = "Flex", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Etanol ou Gasolina?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Descubra qual combustível é mais vantajoso. Tradicionalmente, o etanol vale a pena se custar até 70% do preço da gasolina.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = priceEthanolStr,
                    onValueChange = { priceEthanolStr = it },
                    label = { Text("Preço do Etanol (R$/L)") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = priceGasolineStr,
                    onValueChange = { priceGasolineStr = it },
                    label = { Text("Preço da Gasolina (R$/L)") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (priceEthanol > 0 && priceGasoline > 0) {
                item {
                    val isEthanolBetter = ratio <= 0.70
                    val percentage = ratio * 100

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (isEthanolBetter) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (isEthanolBetter) "Vá de ETANOL! 🌱" else "Vá de GASOLINA! ⚡",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isEthanolBetter) Color(0xFF2E7D32) else Color(0xFFE65100)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "O preço do Etanol equivale a %.1f%% do preço da Gasolina.".format(percentage),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = if (isEthanolBetter) "Economia garantida no bolso com combustível verde."
                                else "Melhor desempenho e custo-benefício por quilômetro rodado.",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TripCalculator(vehicles: List<com.example.data.model.Vehicle>, viewModel: VehicleViewModel) {
    var distanceStr by remember { mutableStateOf("") }
    var consumptionStr by remember { mutableStateOf("") }
    var fuelPriceStr by remember { mutableStateOf("") }

    var selectedVehicleIndex by remember { mutableStateOf(-1) }
    var expandedVehicles by remember { mutableStateOf(false) }

    val distance = distanceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val consumption = consumptionStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val fuelPrice = fuelPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Map, contentDescription = "Viagem", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Custo Estimado de Viagem",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Calcule quanto vai gastar de combustível em uma viagem ou trajeto específico.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Quick Auto-fill from garage
            if (vehicles.isNotEmpty()) {
                item {
                    Text("Preencher com veículo da garagem:", style = MaterialTheme.typography.labelMedium)
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        OutlinedButton(
                            onClick = { expandedVehicles = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedVehicleIndex >= 0) vehicles[selectedVehicleIndex].name else "Selecione para auto-preencher"
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                        }
                        DropdownMenu(
                            expanded = expandedVehicles,
                            onDismissRequest = { expandedVehicles = false }
                        ) {
                            vehicles.forEachIndexed { index, vehicle ->
                                DropdownMenuItem(
                                    text = { Text("${vehicle.name} (${vehicle.brand} ${vehicle.model})") },
                                    onClick = {
                                        selectedVehicleIndex = index
                                        val realCons = viewModel.getVehicleConsumption(vehicle.id)
                                        consumptionStr = "%.1f".format(realCons)
                                        fuelPriceStr = "%.2f".format(vehicle.fuelPrice)
                                        expandedVehicles = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = distanceStr,
                    onValueChange = { distanceStr = it },
                    label = { Text("Distância da Viagem (KM)") },
                    suffix = { Text("KM") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = consumptionStr,
                    onValueChange = { consumptionStr = it },
                    label = { Text("Consumo Médio (KM/L)") },
                    suffix = { Text("km/L") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = fuelPriceStr,
                    onValueChange = { fuelPriceStr = it },
                    label = { Text("Preço do Combustível (R$/L)") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (distance > 0 && consumption > 0 && fuelPrice > 0) {
                val litersNeeded = distance / consumption
                val totalCost = litersNeeded * fuelPrice

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Custo Total: R$ %.2f".format(totalCost),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Você precisará de aproximadamente %.1f Litros de combustível para completar este trajeto.".format(litersNeeded),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RangeCalculator(vehicles: List<com.example.data.model.Vehicle>, viewModel: VehicleViewModel) {
    var fuelBudgetStr by remember { mutableStateOf("") }
    var fuelPriceStr by remember { mutableStateOf("") }
    var consumptionStr by remember { mutableStateOf("") }

    var selectedVehicleIndex by remember { mutableStateOf(-1) }
    var expandedVehicles by remember { mutableStateOf(false) }

    val fuelBudget = fuelBudgetStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val fuelPrice = fuelPriceStr.replace(",", ".").toDoubleOrNull() ?: 0.0
    val consumption = consumptionStr.replace(",", ".").toDoubleOrNull() ?: 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.TrendingUp, contentDescription = "Rendimento", tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Calculadora de Autonomia",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Insira quanto quer gastar no posto e descubra quantos litros vai receber e quantos KM consegue rodar.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Quick Auto-fill from garage
            if (vehicles.isNotEmpty()) {
                item {
                    Text("Preencher com veículo da garagem:", style = MaterialTheme.typography.labelMedium)
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        OutlinedButton(
                            onClick = { expandedVehicles = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (selectedVehicleIndex >= 0) vehicles[selectedVehicleIndex].name else "Selecione para auto-preencher"
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "dropdown")
                        }
                        DropdownMenu(
                            expanded = expandedVehicles,
                            onDismissRequest = { expandedVehicles = false }
                        ) {
                            vehicles.forEachIndexed { index, vehicle ->
                                DropdownMenuItem(
                                    text = { Text("${vehicle.name} (${vehicle.brand} ${vehicle.model})") },
                                    onClick = {
                                        selectedVehicleIndex = index
                                        val realCons = viewModel.getVehicleConsumption(vehicle.id)
                                        consumptionStr = "%.1f".format(realCons)
                                        fuelPriceStr = "%.2f".format(vehicle.fuelPrice)
                                        expandedVehicles = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = fuelBudgetStr,
                    onValueChange = { fuelBudgetStr = it },
                    label = { Text("Quanto quer abastecer? (R$)") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = fuelPriceStr,
                    onValueChange = { fuelPriceStr = it },
                    label = { Text("Preço por Litro (R$/L)") },
                    prefix = { Text("R$ ") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = consumptionStr,
                    onValueChange = { consumptionStr = it },
                    label = { Text("Consumo Médio (KM/L)") },
                    suffix = { Text("km/L") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (fuelBudget > 0 && fuelPrice > 0 && consumption > 0) {
                val litersObtained = fuelBudget / fuelPrice
                val estimatedRange = litersObtained * consumption

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Autonomia: %.0f KM".format(estimatedRange),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Você vai encher o tanque com %.2f Litros de combustível.".format(litersObtained),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
