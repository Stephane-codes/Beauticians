package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BookingItem
import com.example.data.model.NotificationItem
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDao {
    @Query("SELECT * FROM services ORDER BY category ASC, priceKes ASC")
    fun getAllServices(): Flow<List<ServiceItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertServices(services: List<ServiceItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertService(service: ServiceItem)

    @Delete
    suspend fun deleteService(service: ServiceItem)

    @Query("SELECT COUNT(*) FROM services")
    suspend fun getCount(): Int
}

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdAt DESC")
    fun getAllBookings(): Flow<List<BookingItem>>

    @Query("SELECT * FROM bookings WHERE appointmentDate = :date ORDER BY timeSlot ASC")
    fun getBookingsForDate(date: String): Flow<List<BookingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: BookingItem)

    @Update
    suspend fun updateBooking(booking: BookingItem)

    @Query("UPDATE bookings SET status = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: String, status: String)

    @Delete
    suspend fun deleteBooking(booking: BookingItem)

    @Query("DELETE FROM bookings")
    suspend fun clearAll()
}

@Dao
interface ProviderDao {
    @Query("SELECT * FROM providers ORDER BY rating DESC")
    fun getAllProviders(): Flow<List<ProviderItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProviders(providers: List<ProviderItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderItem)

    @Update
    suspend fun updateProvider(provider: ProviderItem)

    @Query("SELECT COUNT(*) FROM providers")
    suspend fun getCount(): Int
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("DELETE FROM notifications")
    suspend fun clearAll()
}
