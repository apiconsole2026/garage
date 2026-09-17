package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.MaintenanceHistory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("RevisAuto", appName)
  }

  @Test
  fun `verify preventive savings formula`() {
    // Preventive maintenance of $100 estimates a 2.5x savings of $250.0
    val history = MaintenanceHistory(
        id = 1,
        vehicleId = 1,
        title = "Troca de Óleo",
        cost = 100.0,
        mileage = 15000.0,
        isPreventive = true,
        notes = "Filtro e óleo trocados"
    )
    
    val savings = history.getEstimatedSavings()
    assertEquals(250.0, savings, 0.001)
  }

  @Test
  fun `verify corrective savings is zero`() {
    // Corrective maintenance does not save money, as the failure has already occurred
    val history = MaintenanceHistory(
        id = 2,
        vehicleId = 1,
        title = "Motor Fundido",
        cost = 4500.0,
        mileage = 22000.0,
        isPreventive = false,
        notes = "Retífica completa"
    )
    
    val savings = history.getEstimatedSavings()
    assertEquals(0.0, savings, 0.001)
  }
}
