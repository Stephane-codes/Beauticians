package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DefaultSalonData
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
    assertEquals("Steph's Nails", appName)
  }

  @Test
  fun `verify salon contact details and flyer prices`() {
    assertEquals("+254115071788", DefaultSalonData.SALON_PHONE)
    assertTrue(DefaultSalonData.INITIAL_SERVICES.isNotEmpty())

    val gelService = DefaultSalonData.INITIAL_SERVICES.find { it.id == "srv_gel_application" }
    assertEquals(500, gelService?.priceKes)

    val pediService = DefaultSalonData.INITIAL_SERVICES.find { it.id == "srv_pedicure_gel" }
    assertEquals(1000, pediService?.priceKes)

    val acrylics = DefaultSalonData.INITIAL_SERVICES.find { it.id == "srv_acrylics_full" }
    assertEquals(3500, acrylics?.priceKes)
  }
}
