package com.example.data.local

import androidx.room.*
import com.example.data.model.FuelLog
import com.example.data.model.MaintenanceAlert
import com.example.data.model.MaintenanceHistory
import com.example.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    // VEHICLES
    @Query("SELECT * FROM vehicles ORDER BY createdAt DESC")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Query("SELECT * FROM vehicles WHERE id = :id")
    suspend fun getVehicleById(id: Int): Vehicle?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVehicle(vehicle: Vehicle): Long

    @Update
    suspend fun updateVehicle(vehicle: Vehicle)

    @Delete
    suspend fun deleteVehicle(vehicle: Vehicle)

    // FUEL LOGS
    @Query("SELECT * FROM fuel_logs WHERE vehicleId = :vehicleId ORDER BY mileage DESC")
    fun getFuelLogsForVehicle(vehicleId: Int): Flow<List<FuelLog>>

    @Query("SELECT * FROM fuel_logs ORDER BY date DESC")
    fun getAllFuelLogs(): Flow<List<FuelLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFuelLog(fuelLog: FuelLog): Long

    @Update
    suspend fun updateFuelLog(fuelLog: FuelLog)

    @Query("DELETE FROM fuel_logs WHERE id = :id")
    suspend fun deleteFuelLog(id: Int)

    @Query("DELETE FROM fuel_logs WHERE vehicleId = :vehicleId")
    suspend fun deleteFuelLogsByVehicleId(vehicleId: Int)

    // MAINTENANCE ALERTS
    @Query("SELECT * FROM maintenance_alerts WHERE vehicleId = :vehicleId")
    fun getAlertsForVehicle(vehicleId: Int): Flow<List<MaintenanceAlert>>

    @Query("SELECT * FROM maintenance_alerts")
    fun getAllAlerts(): Flow<List<MaintenanceAlert>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: MaintenanceAlert): Long

    @Update
    suspend fun updateAlert(alert: MaintenanceAlert)

    @Delete
    suspend fun deleteAlert(alert: MaintenanceAlert)

    @Query("DELETE FROM maintenance_alerts WHERE vehicleId = :vehicleId")
    suspend fun deleteAlertsByVehicleId(vehicleId: Int)

    // MAINTENANCE HISTORY
    @Query("SELECT * FROM maintenance_history WHERE vehicleId = :vehicleId ORDER BY mileage DESC")
    fun getHistoryForVehicle(vehicleId: Int): Flow<List<MaintenanceHistory>>

    @Query("SELECT * FROM maintenance_history ORDER BY date DESC")
    fun getAllHistory(): Flow<List<MaintenanceHistory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: MaintenanceHistory): Long

    @Update
    suspend fun updateHistory(history: MaintenanceHistory)

    @Query("DELETE FROM maintenance_history WHERE id = :id")
    suspend fun deleteHistory(id: Int)

    @Query("DELETE FROM maintenance_history WHERE vehicleId = :vehicleId")
    suspend fun deleteHistoryByVehicleId(vehicleId: Int)
}
