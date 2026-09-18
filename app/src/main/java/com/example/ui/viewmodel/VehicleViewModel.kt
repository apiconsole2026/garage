package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.FuelLog
import com.example.data.model.MaintenanceAlert
import com.example.data.model.MaintenanceHistory
import com.example.data.model.Vehicle
import com.example.data.repository.VehicleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VehicleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: VehicleRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = VehicleRepository(database.vehicleDao())
    }

    // STATE FLOWS
    val vehicles: StateFlow<List<Vehicle>> = repository.allVehicles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFuelLogs: StateFlow<List<FuelLog>> = repository.allFuelLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allAlerts: StateFlow<List<MaintenanceAlert>> = repository.allAlerts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allHistory: StateFlow<List<MaintenanceHistory>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedVehicleId = MutableStateFlow<Int?>(null)
    val selectedVehicleId: StateFlow<Int?> = _selectedVehicleId.asStateFlow()

    // SET SELECTION
    fun selectVehicle(id: Int?) {
        _selectedVehicleId.value = id
    }

    // VEHICLE ACTIONS
    fun addVehicle(
        name: String,
        type: String,
        brand: String,
        model: String,
        year: Int,
        currentMileage: Double,
        dailyMileage: Double,
        fuelType: String,
        fuelPrice: Double,
        engineSpec: String = "",
        oilSpec: String = "",
        oilCapacity: String = "",
        tirePressure: String = "",
        sparkPlug: String = "",
        fuelTankCapacity: String = "",
        brakeFluid: String = "",
        coolantType: String = ""
    ) {
        viewModelScope.launch {
            val vehicle = Vehicle(
                name = name,
                type = type,
                brand = brand,
                model = model,
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
            try {
                val insertedId = repository.insertVehicle(vehicle).toInt()
                _selectedVehicleId.value = insertedId
                
                // Auto-generate standard smart maintenance alerts for this vehicle!
                createDefaultAlertsForVehicle(insertedId, currentMileage, type)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateVehicleSpecs(
        vehicleId: Int,
        engineSpec: String,
        oilSpec: String,
        oilCapacity: String,
        tirePressure: String,
        sparkPlug: String,
        fuelTankCapacity: String,
        brakeFluid: String,
        coolantType: String
    ) {
        viewModelScope.launch {
            val vehicle = repository.getVehicleById(vehicleId) ?: return@launch
            val updated = vehicle.copy(
                engineSpec = engineSpec,
                oilSpec = oilSpec,
                oilCapacity = oilCapacity,
                tirePressure = tirePressure,
                sparkPlug = sparkPlug,
                fuelTankCapacity = fuelTankCapacity,
                brakeFluid = brakeFluid,
                coolantType = coolantType
            )
            repository.updateVehicle(updated)
        }
    }

    fun updateVehicleMileage(vehicleId: Int, newMileage: Double) {
        viewModelScope.launch {
            val vehicle = repository.getVehicleById(vehicleId)
            if (vehicle != null && newMileage > vehicle.currentMileage) {
                repository.updateVehicle(vehicle.copy(currentMileage = newMileage))
            }
        }
    }

    fun deleteVehicle(vehicle: Vehicle) {
        viewModelScope.launch {
            repository.deleteVehicle(vehicle)
            if (_selectedVehicleId.value == vehicle.id) {
                _selectedVehicleId.value = null
            }
        }
    }

    // FUEL LOG ACTIONS
    fun addFuelLog(vehicleId: Int, mileage: Double, liters: Double, pricePerLiter: Double) {
        viewModelScope.launch {
            val totalCost = liters * pricePerLiter
            val log = FuelLog(
                vehicleId = vehicleId,
                mileage = mileage,
                liters = liters,
                pricePerLiter = pricePerLiter,
                totalCost = totalCost
            )
            repository.insertFuelLog(log)
            
            // Auto update mileage if newer
            val vehicle = repository.getVehicleById(vehicleId)
            if (vehicle != null && mileage > vehicle.currentMileage) {
                repository.updateVehicle(vehicle.copy(currentMileage = mileage))
            }
        }
    }

    fun updateFuelLog(id: Int, vehicleId: Int, mileage: Double, liters: Double, pricePerLiter: Double, date: Long) {
        viewModelScope.launch {
            val totalCost = liters * pricePerLiter
            val log = FuelLog(
                id = id,
                vehicleId = vehicleId,
                date = date,
                mileage = mileage,
                liters = liters,
                pricePerLiter = pricePerLiter,
                totalCost = totalCost
            )
            repository.updateFuelLog(log)
        }
    }

    fun deleteFuelLog(id: Int) {
        viewModelScope.launch {
            repository.deleteFuelLog(id)
        }
    }

    // MAINTENANCE ALERT ACTIONS
    fun addCustomAlert(vehicleId: Int, title: String, type: String, intervalKm: Double, currentMileage: Double) {
        viewModelScope.launch {
            val alert = MaintenanceAlert(
                vehicleId = vehicleId,
                title = title,
                type = type,
                intervalKm = intervalKm,
                lastCompletedMileage = currentMileage,
                lastCompletedDate = System.currentTimeMillis()
            )
            repository.insertAlert(alert)
        }
    }

    fun deleteAlert(alert: MaintenanceAlert) {
        viewModelScope.launch {
            repository.deleteAlert(alert)
        }
    }

    // MAINTENANCE HISTORY ACTIONS
    fun completeMaintenanceAlert(alertId: Int, mileage: Double, cost: Double, notes: String) {
        viewModelScope.launch {
            // Find active alert in the list
            val alert = allAlerts.value.find { it.id == alertId } ?: return@launch
            
            // 1. Log in history as PREVENTIVE
            val history = MaintenanceHistory(
                vehicleId = alert.vehicleId,
                title = alert.title,
                cost = cost,
                mileage = mileage,
                isPreventive = true,
                notes = notes
            )
            repository.insertHistory(history)

            // 2. Refresh/Reset alert for next cycle with the completion mileage
            val updatedAlert = alert.copy(
                lastCompletedMileage = mileage,
                lastCompletedDate = System.currentTimeMillis(),
                isCompleted = false
            )
            repository.updateAlert(updatedAlert)

            // 3. Update vehicle mileage if higher
            val vehicle = repository.getVehicleById(alert.vehicleId)
            if (vehicle != null && mileage > vehicle.currentMileage) {
                repository.updateVehicle(vehicle.copy(currentMileage = mileage))
            }
        }
    }

    fun addManualMaintenanceHistory(vehicleId: Int, title: String, cost: Double, mileage: Double, isPreventive: Boolean, notes: String) {
        viewModelScope.launch {
            val history = MaintenanceHistory(
                vehicleId = vehicleId,
                title = title,
                cost = cost,
                mileage = mileage,
                isPreventive = isPreventive,
                notes = notes
            )
            repository.insertHistory(history)

            // Update mileage if higher
            val vehicle = repository.getVehicleById(vehicleId)
            if (vehicle != null && mileage > vehicle.currentMileage) {
                repository.updateVehicle(vehicle.copy(currentMileage = mileage))
            }
        }
    }

    fun updateMaintenanceHistory(id: Int, vehicleId: Int, title: String, cost: Double, mileage: Double, isPreventive: Boolean, notes: String, date: Long) {
        viewModelScope.launch {
            val history = MaintenanceHistory(
                id = id,
                vehicleId = vehicleId,
                title = title,
                cost = cost,
                date = date,
                mileage = mileage,
                isPreventive = isPreventive,
                notes = notes
            )
            repository.updateHistory(history)
        }
    }

    fun deleteHistory(id: Int) {
        viewModelScope.launch {
            repository.deleteHistory(id)
        }
    }

    // HELPER CREATOR FOR DEFAULT RECOMMENDATIONS
    private suspend fun createDefaultAlertsForVehicle(vehicleId: Int, startMileage: Double, type: String) {
        val now = System.currentTimeMillis()
        
        // Define standard alerts depending on vehicle type
        val alerts = when (type) {
            "MOTO" -> listOf(
                MaintenanceAlert(vehicleId = vehicleId, title = "Troca de Óleo do Motor", type = "OIL", intervalKm = 3000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Ajuste e Lubrificação da Corrente", type = "OTHER", intervalKm = 1000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Calibragem e Desgaste de Pneus", type = "TIRES", intervalKm = 2000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Pastilhas de Freio", type = "BRAKES", intervalKm = 8000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Revisão Geral Preventiva", type = "GENERAL", intervalKm = 12000.0, lastCompletedMileage = startMileage, lastCompletedDate = now)
            )
            "TRUCK" -> listOf(
                MaintenanceAlert(vehicleId = vehicleId, title = "Troca de Óleo e Filtro Diesel", type = "OIL", intervalKm = 20000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Rodízio e Calibragem de Pneus", type = "TIRES", intervalKm = 15000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Pastilhas e Lonas de Freio", type = "BRAKES", intervalKm = 40000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Filtro de Ar do Motor", type = "OTHER", intervalKm = 25000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Revisão Geral e Suspensão", type = "GENERAL", intervalKm = 50000.0, lastCompletedMileage = startMileage, lastCompletedDate = now)
            )
            else -> listOf( // "CAR"
                MaintenanceAlert(vehicleId = vehicleId, title = "Troca de Óleo do Motor", type = "OIL", intervalKm = 10000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Calibragem e Alinhamento", type = "TIRES", intervalKm = 10000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Pastilhas de Freio", type = "BRAKES", intervalKm = 25000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Troca de Filtro de Combustível", type = "OTHER", intervalKm = 15000.0, lastCompletedMileage = startMileage, lastCompletedDate = now),
                MaintenanceAlert(vehicleId = vehicleId, title = "Revisão Geral Periódica", type = "GENERAL", intervalKm = 40000.0, lastCompletedMileage = startMileage, lastCompletedDate = now)
            )
        }

        for (alert in alerts) {
            repository.insertAlert(alert)
        }
    }

    // MATHEMATICAL LOGIC CALCULATIONS
    
    // Data class representing consumption calculation results
    data class ConsumptionResult(
        val kmPerLiter: Double,
        val isReal: Boolean,
        val totalDistance: Double,
        val totalLiters: Double,
        val logCount: Int
    )

    // Calculates real consumption (km/L) for a given vehicle using fuel logs
    fun getVehicleConsumptionResult(vehicleId: Int): ConsumptionResult {
        val vehicleLogs = allFuelLogs.value.filter { it.vehicleId == vehicleId }.sortedBy { it.mileage }
        val v = vehicles.value.find { it.id == vehicleId }
        val defaultFallback = when (v?.type) {
            "MOTO" -> 35.0
            "TRUCK" -> 4.5
            else -> 11.5 // CAR
        }

        if (vehicleLogs.size < 2) {
            return ConsumptionResult(
                kmPerLiter = defaultFallback,
                isReal = false,
                totalDistance = 0.0,
                totalLiters = 0.0,
                logCount = vehicleLogs.size
            )
        }

        var totalDistance = 0.0
        var totalLiters = 0.0
        for (i in 1 until vehicleLogs.size) {
            val prev = vehicleLogs[i - 1]
            val curr = vehicleLogs[i]
            val distance = curr.mileage - prev.mileage
            if (distance > 0) {
                totalDistance += distance
                totalLiters += curr.liters
            }
        }

        return if (totalLiters > 0 && totalDistance > 0) {
            ConsumptionResult(
                kmPerLiter = totalDistance / totalLiters,
                isReal = true,
                totalDistance = totalDistance,
                totalLiters = totalLiters,
                logCount = vehicleLogs.size
            )
        } else {
            ConsumptionResult(
                kmPerLiter = defaultFallback,
                isReal = false,
                totalDistance = 0.0,
                totalLiters = 0.0,
                logCount = vehicleLogs.size
            )
        }
    }

    // Calculates consumption (km/L) for a given vehicle using full-tank strategy
    fun getVehicleConsumption(vehicleId: Int): Double {
        return getVehicleConsumptionResult(vehicleId).kmPerLiter
    }

    // Calculates economy & investment stats for wallet screen
    fun getEconomyStats(): EconomyStats {
        val validVehicleIds = vehicles.value.map { it.id }.toSet()
        val historyList = allHistory.value.filter { it.vehicleId in validVehicleIds }
        val fuelList = allFuelLogs.value.filter { it.vehicleId in validVehicleIds }
        
        val totalSpentPreventive = historyList.filter { it.isPreventive }.sumOf { it.cost }
        val totalSpentCorrective = historyList.filter { !it.isPreventive }.sumOf { it.cost }
        val totalSpentFuel = fuelList.sumOf { it.totalCost }
        val totalLiters = fuelList.sumOf { it.liters }
        
        // Total invested = all maintenance (preventive + corrective) + fuel costs
        val totalInvested = totalSpentPreventive + totalSpentCorrective + totalSpentFuel
        
        // Net savings is the estimated money saved by doing preventive maintenance
        val totalSavings = historyList.sumOf { it.getEstimatedSavings() }

        // Overall consumption
        var calculatedDistance = 0.0
        var calculatedLiters = 0.0
        val groupedLogs = fuelList.groupBy { it.vehicleId }
        groupedLogs.forEach { (_, logs) ->
            val sorted = logs.sortedBy { it.mileage }
            if (sorted.size >= 2) {
                for (i in 1 until sorted.size) {
                    val dist = sorted[i].mileage - sorted[i - 1].mileage
                    if (dist > 0) {
                        calculatedDistance += dist
                        calculatedLiters += sorted[i].liters
                    }
                }
            }
        }
        val overallKmPerL = if (calculatedLiters > 0) calculatedDistance / calculatedLiters else 0.0
        
        return EconomyStats(
            totalSpentPreventive = totalSpentPreventive,
            totalSpentCorrective = totalSpentCorrective,
            totalSpentFuel = totalSpentFuel,
            totalInvested = totalInvested,
            totalLiters = totalLiters,
            averageKmPerL = overallKmPerL,
            netSavings = totalSavings
        )
    }
}

// Data class to wrap wallet summary statistics
data class EconomyStats(
    val totalSpentPreventive: Double,
    val totalSpentCorrective: Double,
    val totalSpentFuel: Double = 0.0,
    val totalInvested: Double = 0.0,
    val totalLiters: Double = 0.0,
    val averageKmPerL: Double = 0.0,
    val netSavings: Double
)

class VehicleViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VehicleViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
