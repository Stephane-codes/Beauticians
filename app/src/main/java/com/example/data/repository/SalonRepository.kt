package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.DefaultSalonData
import com.example.data.model.BookingItem
import com.example.data.model.NailStyleGalleryItem
import com.example.data.model.NotificationItem
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class SalonRepository(private val database: AppDatabase) {
    private val serviceDao = database.serviceDao()
    private val bookingDao = database.bookingDao()
    private val providerDao = database.providerDao()
    private val notificationDao = database.notificationDao()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            if (serviceDao.getCount() == 0) {
                serviceDao.insertServices(DefaultSalonData.INITIAL_SERVICES)
            }
            if (providerDao.getCount() == 0) {
                providerDao.insertProviders(DefaultSalonData.INITIAL_PROVIDERS)
            }
            DefaultSalonData.INITIAL_NOTIFICATIONS.forEach {
                notificationDao.insertNotification(it)
            }
        }
    }

    val services: Flow<List<ServiceItem>> = serviceDao.getAllServices()
    val bookings: Flow<List<BookingItem>> = bookingDao.getAllBookings()
    val providers: Flow<List<ProviderItem>> = providerDao.getAllProviders()
    val notifications: Flow<List<NotificationItem>> = notificationDao.getAllNotifications()

    fun getGalleryItems(): List<NailStyleGalleryItem> = DefaultSalonData.GALLERY_ITEMS

    suspend fun saveBooking(booking: BookingItem) {
        bookingDao.insertBooking(booking)
    }

    suspend fun updateBookingStatus(bookingId: String, status: String) {
        bookingDao.updateBookingStatus(bookingId, status)
    }

    suspend fun deleteBooking(booking: BookingItem) {
        bookingDao.deleteBooking(booking)
    }

    suspend fun clearAllBookings() {
        bookingDao.clearAll()
    }

    suspend fun registerProvider(provider: ProviderItem) {
        providerDao.insertProvider(provider)
    }

    suspend fun addNotification(notification: NotificationItem) {
        notificationDao.insertNotification(notification)
    }

    suspend fun markNotificationRead(id: String) {
        notificationDao.markAsRead(id)
    }
}
