package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VehicleBrandsDb
import com.example.ui.viewmodel.VehicleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleDialog(
    viewModel: VehicleViewModel,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("CAR") } // "CAR", "MOTO", "TRUCK"
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var yearStr by remember { mutableStateOf("2024") }
    var currentMileageStr by remember { mutableStateOf("") }
    var dailyMileageStr by remember { mutableStateOf("30") }
    var fuelType by remember { mutableStateOf("Flex") }
    var fuelPriceStr by remember { mutableStateOf("5.89") }

    // Auto-fill specs
    var engineSpec by remember { mutableStateOf("") }
    var oilSpec by remember { mutableStateOf("") }
    var oilCapacity by remember { mutableStateOf("") }
    var tirePressure by remember { mutableStateOf("") }
    var sparkPlug by remember { mutableStateOf("") }
    var fuelTankCapacity by remember { mutableStateOf("") }
    var brakeFluid by remember { mutableStateOf("") }
    var coolantType by remember { mutableStateOf("") }

    var brandDropdownExpanded by remember { mutableStateOf(false) }
    var modelDropdownExpanded by remember { mutableStateOf(false) }
    var yearDropdownExpanded by remember { mutableStateOf(false) }
    var isCustomBrand by remember { mutableStateOf(false) }
    var isCustomModel by remember { mutableStateOf(false) }

    val availableBrands = remember(type) { VehicleBrandsDb.getBrandsForType(type) }
    val availableModels = remember(type, brand) {
        if (brand.isNotEmpty()) VehicleBrandsDb.getModelsForBrand(type, brand) else emptyList()
    }
    val yearsList = remember { (2026 downTo 1900).map { it.toString() } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (type == "MOTO") Icons.Default.TwoWheeler else Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text("Cadastrar Veículo", fontWeight = FontWeight.Bold)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Tipo de Veículo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("1. Tipo de Veículo", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf(
                                Triple("CAR", "Carro", Icons.Default.DirectionsCar),
                                Triple("MOTO", "Moto", Icons.Default.TwoWheeler),
                                Triple("TRUCK", "Caminhão", Icons.Default.LocalShipping)
                            ).forEach { (vType, label, icon) ->
                                FilterChip(
                                    selected = type == vType,
                                    onClick = {
                                        type = vType
                                        brand = ""
                                        model = ""
                                        isCustomBrand = false
                                        isCustomModel = false
                                        fuelType = if (vType == "TRUCK") "Diesel" else if (vType == "MOTO") "Gasolina" else "Flex"
                                    },
                                    leadingIcon = {
                                        Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                                    },
                                    label = { Text(label) }
                                )
                            }
                        }
                    }
                }

                // 2. Painel de Escolha da Marca
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("2. Marca do Veículo", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)

                        // Quick select brand chips
                        val topBrands = when (type) {
                            "MOTO" -> listOf("Honda", "Yamaha", "BMW", "Kawasaki", "Royal Enfield", "Suzuki", "Shineray", "Bajaj")
                            "TRUCK" -> listOf("Mercedes-Benz", "Volkswagen", "Volvo", "Scania", "Iveco")
                            else -> listOf("Chevrolet", "Fiat", "Volkswagen", "Toyota", "Hyundai", "Honda", "Renault", "Ford", "Jeep", "Nissan", "Citroën", "Peugeot")
                        }
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(topBrands) { bName ->
                                val isSelected = brand.equals(bName, ignoreCase = true)
                                SuggestionChip(
                                    onClick = {
                                        brand = bName
                                        isCustomBrand = false
                                        model = ""
                                        isCustomModel = false
                                        modelDropdownExpanded = true
                                    },
                                    label = { Text(bName, fontSize = 12.sp) },
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                                    ),
                                    border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null
                                )
                            }
                        }

                        // Brand Dropdown Box
                        ExposedDropdownMenuBox(
                            expanded = brandDropdownExpanded,
                            onExpandedChange = { brandDropdownExpanded = !brandDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = if (isCustomBrand) brand else if (brand.isEmpty()) "Toque para escolher a marca..." else brand,
                                onValueChange = {
                                    if (isCustomBrand) brand = it
                                },
                                readOnly = !isCustomBrand,
                                label = { Text("Marca Selecionada") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = brandDropdownExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = brandDropdownExpanded,
                                onDismissRequest = { brandDropdownExpanded = false }
                            ) {
                                availableBrands.forEach { bName ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = bName,
                                                fontWeight = if (bName == brand) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            brand = bName
                                            isCustomBrand = false
                                            model = ""
                                            isCustomModel = false
                                            brandDropdownExpanded = false
                                            modelDropdownExpanded = true
                                        }
                                    )
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Outra Marca (Digitar Manual)", color = MaterialTheme.colorScheme.primary) },
                                    onClick = {
                                        isCustomBrand = true
                                        brand = ""
                                        brandDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // 3. Painel de Escolha do Modelo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("3. Modelo do Veículo", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)

                        ExposedDropdownMenuBox(
                            expanded = modelDropdownExpanded,
                            onExpandedChange = {
                                if (brand.isNotEmpty()) {
                                    modelDropdownExpanded = !modelDropdownExpanded
                                }
                            }
                        ) {
                            OutlinedTextField(
                                value = if (isCustomModel) model else if (model.isEmpty()) (if (brand.isEmpty()) "Escolha uma marca primeiro" else "Toque para escolher o modelo...") else model,
                                onValueChange = {
                                    if (isCustomModel) model = it
                                },
                                readOnly = !isCustomModel,
                                enabled = brand.isNotEmpty(),
                                label = { Text("Modelo Selecionado") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = modelDropdownExpanded)
                                },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = modelDropdownExpanded,
                                onDismissRequest = { modelDropdownExpanded = false }
                            ) {
                                availableModels.forEach { mName ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = mName,
                                                fontWeight = if (mName == model) FontWeight.Bold else FontWeight.Normal
                                            )
                                        },
                                        onClick = {
                                            model = mName
                                            isCustomModel = false
                                            modelDropdownExpanded = false
                                            // Auto-fill specs from catalog
                                            VehicleBrandsDb.findSpec(type, brand, mName)?.let { spec ->
                                                if (name.isEmpty()) name = "$brand $mName"
                                                engineSpec = spec.engine
                                                oilSpec = spec.oilSpec
                                                oilCapacity = spec.oilCapacity
                                                tirePressure = spec.tirePressure
                                                sparkPlug = spec.sparkPlug
                                                fuelTankCapacity = spec.fuelTank
                                                brakeFluid = spec.brakeFluid
                                                coolantType = spec.coolant
                                                fuelType = spec.fuelType
                                            }
                                        }
                                    )
                                }
                                HorizontalDivider()
                                DropdownMenuItem(
                                    text = { Text("Outro Modelo (Digitar Manual)", color = MaterialTheme.colorScheme.primary) },
                                    onClick = {
                                        isCustomModel = true
                                        model = ""
                                        modelDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Spec Loaded Notification
                    if (engineSpec.isNotEmpty() && model.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Ficha Técnica Oficial encontrada e preenchida automaticamente!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // 4. Ano de Fabricação
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("4. Ano de Fabricação", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        ExposedDropdownMenuBox(
                            expanded = yearDropdownExpanded,
                            onExpandedChange = { yearDropdownExpanded = !yearDropdownExpanded }
                        ) {
                            OutlinedTextField(
                                value = yearStr,
                                onValueChange = { yearStr = it },
                                label = { Text("Ano") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = yearDropdownExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            ExposedDropdownMenu(
                                expanded = yearDropdownExpanded,
                                onDismissRequest = { yearDropdownExpanded = false }
                            ) {
                                yearsList.take(30).forEach { y ->
                                    DropdownMenuItem(
                                        text = { Text(y) },
                                        onClick = {
                                            yearStr = y
                                            yearDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                // 5. Quilometragem Atual
                item {
                    OutlinedTextField(
                        value = currentMileageStr,
                        onValueChange = { currentMileageStr = it },
                        label = { Text("Quilometragem Atual (Odômetro)") },
                        placeholder = { Text("Ex: 45000") },
                        suffix = { Text("KM") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("vehicle_mileage_input")
                    )
                }

                // 6. Média de KM Diária estimada
                item {
                    OutlinedTextField(
                        value = dailyMileageStr,
                        onValueChange = { dailyMileageStr = it },
                        label = { Text("Uso Diário Estimado") },
                        placeholder = { Text("Ex: 30") },
                        suffix = { Text("KM/dia") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 7. Tipo de Combustível Principal
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Tipo de Combustível", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val fuels = if (type == "TRUCK") listOf("Diesel", "Arla 32") else listOf("Flex", "Gasolina", "Etanol", "Elétrico")
                            fuels.forEach { fType ->
                                FilterChip(
                                    selected = fuelType == fType,
                                    onClick = { fuelType = fType },
                                    label = { Text(fType) }
                                )
                            }
                        }
                    }
                }

                // 8. Preço do Combustível por Litro
                item {
                    OutlinedTextField(
                        value = fuelPriceStr,
                        onValueChange = { fuelPriceStr = it },
                        label = { Text("Preço do Combustível por Litro") },
                        prefix = { Text("R$ ") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // 9. Apelido Opcional
                item {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Apelido do Veículo (Opcional)") },
                        placeholder = { Text(if (model.isNotEmpty()) "$brand $model" else "Ex: Meu Carro do Dia a Dia") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("vehicle_name_input")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalBrand = brand.trim().ifEmpty { "Geral" }
                    val finalModel = model.trim().ifEmpty { if (finalBrand != "Geral") "$finalBrand Veículo" else "Modelo Padrão" }
                    val finalName = name.trim().ifEmpty { "$finalBrand $finalModel".trim() }
                    val year = yearStr.toIntOrNull() ?: 2024
                    val currentMileage = currentMileageStr.toDoubleOrNull() ?: 0.0
                    val dailyMileage = dailyMileageStr.toDoubleOrNull() ?: 30.0
                    val fuelPrice = fuelPriceStr.replace(",", ".").toDoubleOrNull() ?: 5.89

                    // Auto lookup specs if empty
                    if (engineSpec.isEmpty()) {
                        VehicleBrandsDb.findSpec(type, finalBrand, finalModel)?.let { spec ->
                            engineSpec = spec.engine
                            oilSpec = spec.oilSpec
                            oilCapacity = spec.oilCapacity
                            tirePressure = spec.tirePressure
                            sparkPlug = spec.sparkPlug
                            fuelTankCapacity = spec.fuelTank
                            brakeFluid = spec.brakeFluid
                            coolantType = spec.coolant
                        }
                    }

                    viewModel.addVehicle(
                        name = finalName,
                        type = type,
                        brand = finalBrand,
                        model = finalModel,
                        year = year,
                        currentMileage = currentMileage,
                        dailyMileage = dailyMileage,
                        fuelType = fuelType,
                        fuelPrice = fuelPrice,
                        engineSpec = engineSpec,
                        oilSpec = oilSpec,
                        oilCapacity = oilCapacity,
                        tirePressure = tirePressure,
                        sparkPlug = sparkPlug,
                        fuelTankCapacity = fuelTankCapacity,
                        brakeFluid = brakeFluid,
                        coolantType = coolantType
                    )
                    onDismiss()
                },
                modifier = Modifier.testTag("submit_vehicle_button")
            ) {
                Text("Cadastrar Veículo", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
