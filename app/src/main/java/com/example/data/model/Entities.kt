package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class Vehicle(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String, // "CAR", "MOTO", "TRUCK"
    val brand: String,
    val model: String,
    val year: Int,
    val currentMileage: Double,
    val dailyMileage: Double, // KM driven per day
    val fuelType: String, // "Gasolina", "Etanol", "Diesel", "Flex"
    val fuelPrice: Double, // Cost per liter
    val engineSpec: String = "",
    val oilSpec: String = "",
    val oilCapacity: String = "",
    val tirePressure: String = "",
    val sparkPlug: String = "",
    val fuelTankCapacity: String = "",
    val brakeFluid: String = "",
    val coolantType: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fuel_logs")
data class FuelLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val date: Long = System.currentTimeMillis(),
    val mileage: Double, // Mileage at refueling
    val liters: Double,
    val pricePerLiter: Double,
    val totalCost: Double
)

@Entity(tableName = "maintenance_alerts")
data class MaintenanceAlert(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val title: String, // e.g. "Troca de Óleo", "Revisão Geral"
    val type: String, // "OIL", "BRAKES", "TIRES", "GENERAL", "OTHER"
    val intervalKm: Double, // e.g. 10000 km
    val lastCompletedMileage: Double, // Mileage of last done
    val lastCompletedDate: Long, // Date of last done
    val isCompleted: Boolean = false
) {
    // Calculates due mileage
    fun getDueMileage(): Double {
        return lastCompletedMileage + intervalKm
    }

    // Calculates remaining KMs until next service
    fun getRemainingKm(currentVehicleMileage: Double): Double {
        return getDueMileage() - currentVehicleMileage
    }

    // Checks if service is overdue (mileage exceeded)
    fun isOverdue(currentVehicleMileage: Double): Boolean {
        return getRemainingKm(currentVehicleMileage) <= 0.0
    }

    // Checks if service needs attention soon (within 20% of interval or <= 300 km)
    fun isWarning(currentVehicleMileage: Double): Boolean {
        val remaining = getRemainingKm(currentVehicleMileage)
        val warningThreshold = minOf(300.0, intervalKm * 0.20)
        return remaining > 0.0 && remaining <= warningThreshold
    }

    // True if alert needs action (either warning or overdue)
    fun isCritical(currentVehicleMileage: Double): Boolean {
        return isOverdue(currentVehicleMileage) || isWarning(currentVehicleMileage)
    }
}

@Entity(tableName = "maintenance_history")
data class MaintenanceHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val vehicleId: Int,
    val title: String,
    val cost: Double,
    val date: Long = System.currentTimeMillis(),
    val mileage: Double,
    val isPreventive: Boolean = true, // Preventive saves money!
    val notes: String = ""
) {
    // Estimating that corrective repair is ~3.5x more expensive than preventive maintenance.
    // Therefore, doing a preventive maintenance of R$X saves approximately R$ 2.5X.
    fun getEstimatedSavings(): Double {
        return if (isPreventive) cost * 2.5 else 0.0
    }
}
