package com.example.data.repository

import com.example.data.local.VehicleDao
import com.example.data.model.FuelLog
import com.example.data.model.MaintenanceAlert
import com.example.data.model.MaintenanceHistory
import com.example.data.model.Vehicle
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val vehicleDao: VehicleDao) {

    // Vehicles
    val allVehicles: Flow<List<Vehicle>> = vehicleDao.getAllVehicles()

    suspend fun getVehicleById(id: Int): Vehicle? {
        return vehicleDao.getVehicleById(id)
    }

    suspend fun insertVehicle(vehicle: Vehicle): Long {
        return vehicleDao.insertVehicle(vehicle)
    }

    suspend fun updateVehicle(vehicle: Vehicle) {
        vehicleDao.updateVehicle(vehicle)
    }

    suspend fun deleteVehicle(vehicle: Vehicle) {
        vehicleDao.deleteVehicle(vehicle)
    }

    // Fuel Logs
    fun getFuelLogsForVehicle(vehicleId: Int): Flow<List<FuelLog>> {
        return vehicleDao.getFuelLogsForVehicle(vehicleId)
    }

    val allFuelLogs: Flow<List<FuelLog>> = vehicleDao.getAllFuelLogs()

    suspend fun insertFuelLog(fuelLog: FuelLog): Long {
        return vehicleDao.insertFuelLog(fuelLog)
    }

    suspend fun updateFuelLog(fuelLog: FuelLog) {
        vehicleDao.updateFuelLog(fuelLog)
    }

    suspend fun deleteFuelLog(id: Int) {
        vehicleDao.deleteFuelLog(id)
    }

    // Maintenance Alerts
    fun getAlertsForVehicle(vehicleId: Int): Flow<List<MaintenanceAlert>> {
        return vehicleDao.getAlertsForVehicle(vehicleId)
    }

    val allAlerts: Flow<List<MaintenanceAlert>> = vehicleDao.getAllAlerts()

    suspend fun insertAlert(alert: MaintenanceAlert): Long {
        return vehicleDao.insertAlert(alert)
    }

    suspend fun updateAlert(alert: MaintenanceAlert) {
        vehicleDao.updateAlert(alert)
    }

    suspend fun deleteAlert(alert: MaintenanceAlert) {
        vehicleDao.deleteAlert(alert)
    }

    // Maintenance History
    fun getHistoryForVehicle(vehicleId: Int): Flow<List<MaintenanceHistory>> {
        return vehicleDao.getHistoryForVehicle(vehicleId)
    }

    val allHistory: Flow<List<MaintenanceHistory>> = vehicleDao.getAllHistory()

    suspend fun insertHistory(history: MaintenanceHistory): Long {
        return vehicleDao.insertHistory(history)
    }

    suspend fun updateHistory(history: MaintenanceHistory) {
        vehicleDao.updateHistory(history)
    }

    suspend fun deleteHistory(id: Int) {
        vehicleDao.deleteHistory(id)
    }
}
