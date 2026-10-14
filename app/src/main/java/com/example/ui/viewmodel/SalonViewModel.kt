package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DefaultSalonData
import com.example.data.model.BookingItem
import com.example.data.model.NailStyleGalleryItem
import com.example.data.model.NotificationItem
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceItem
import com.example.data.repository.SalonRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class AppScreen {
    HOME,
    SERVICES,
    BOOKING,
    GALLERY,
    CONTACT,
    PROVIDER_PORTAL,
    SETTINGS_SECURITY
}

enum class PaymentOption {
    RETAINER_FEE_20,       // 20% Retainer fee prior to lock slot, balance after service
    PAY_PRIOR_FULL,        // 100% Full Payment prior via M-PESA
    PAY_AFTER_SERVICE      // Pay 100% after service completion
}

enum class PaymentStep {
    NOT_STARTED,
    STK_PROMPT_WAITING,
    PIN_ENTRY,
    CONFIRMED,
    FAILED
}

data class BookingFormState(
    val selectedService: ServiceItem? = null,
    val isHouseCall: Boolean = false,
    val selectedLocation: DefaultSalonData.EmbuLocation = DefaultSalonData.EMBU_LOCATIONS[0],
    val specificAddress: String = "",
    val clientName: String = "",
    val clientPhone: String = "",
    val clientEmail: String = "",
    val selectedDate: String = "",
    val selectedTimeSlot: String = "",
    // Payment Timing & Retainer choice
    val paymentOption: PaymentOption = PaymentOption.RETAINER_FEE_20,
    // Inspo features
    val inspoDescription: String = "",
    val selectedNailShape: String = "Almond",
    val selectedNailLength: String = "Medium",
    val selectedArtStyle: String = "French Art",
    // Friends Deal promo
    val isFriendsDeal: Boolean = false,
    val friendName: String = "",
    val friendPhone: String = "",
    // Bridal & Event group packages
    val isBridalGroup: Boolean = false,
    val groupPaxCount: Int = 4,
    val includeHair: Boolean = true,
    val includeMakeup: Boolean = true,
    val includeNails: Boolean = true,
    val includeStyling: Boolean = true,
    val notes: String = ""
)

class SalonViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SalonRepository(AppDatabase.getDatabase(application))

    val services: StateFlow<List<ServiceItem>> = repository.services
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val bookings: StateFlow<List<BookingItem>> = repository.bookings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val providers: StateFlow<List<ProviderItem>> = repository.providers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifications: StateFlow<List<NotificationItem>> = repository.notifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _bookingForm = MutableStateFlow(BookingFormState())
    val bookingForm: StateFlow<BookingFormState> = _bookingForm.asStateFlow()

    private val _paymentStep = MutableStateFlow(PaymentStep.NOT_STARTED)
    val paymentStep: StateFlow<PaymentStep> = _paymentStep.asStateFlow()

    private val _lastConfirmedBooking = MutableStateFlow<BookingItem?>(null)
    val lastConfirmedBooking: StateFlow<BookingItem?> = _lastConfirmedBooking.asStateFlow()

    // Security & Settings state
    private val _isTwoFactorEnabled = MutableStateFlow(false)
    val isTwoFactorEnabled: StateFlow<Boolean> = _isTwoFactorEnabled.asStateFlow()

    private val _isDataEncryptionActive = MutableStateFlow(true) // AES-256 enabled by default
    val isDataEncryptionActive: StateFlow<Boolean> = _isDataEncryptionActive.asStateFlow()

    private val _automatedEmailReminders = MutableStateFlow(true)
    val automatedEmailReminders: StateFlow<Boolean> = _automatedEmailReminders.asStateFlow()

    private val _automatedSmsReminders = MutableStateFlow(true)
    val automatedSmsReminders: StateFlow<Boolean> = _automatedSmsReminders.asStateFlow()

    private val _calendarSyncedGoogle = MutableStateFlow(false)
    val calendarSyncedGoogle: StateFlow<Boolean> = _calendarSyncedGoogle.asStateFlow()

    private val _calendarSyncedOutlook = MutableStateFlow(false)
    val calendarSyncedOutlook: StateFlow<Boolean> = _calendarSyncedOutlook.asStateFlow()

    // Gallery data
    val galleryItems: List<NailStyleGalleryItem> = repository.getGalleryItems()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun selectServiceForBooking(service: ServiceItem, startFriendsDeal: Boolean = false) {
        _bookingForm.value = _bookingForm.value.copy(
            selectedService = service,
            isFriendsDeal = startFriendsDeal || (service.isFriendsDealEligible && startFriendsDeal)
        )
        _paymentStep.value = PaymentStep.NOT_STARTED
        _currentScreen.value = AppScreen.BOOKING
    }

    fun selectGalleryLookForBooking(item: NailStyleGalleryItem) {
        val matchingService = services.value.find { it.category == item.category }
            ?: services.value.firstOrNull()
            ?: DefaultSalonData.INITIAL_SERVICES[0]

        _bookingForm.value = _bookingForm.value.copy(
            selectedService = matchingService,
            inspoDescription = "Inspo: ${item.title} (${item.tags.joinToString(", ")})",
            selectedArtStyle = item.category
        )
        _currentScreen.value = AppScreen.BOOKING
    }

    fun updateBookingForm(transform: (BookingFormState) -> BookingFormState) {
        _bookingForm.value = transform(_bookingForm.value)
    }

    fun setPaymentOption(option: PaymentOption) {
        _bookingForm.value = _bookingForm.value.copy(paymentOption = option)
    }

    fun toggleFriendsDeal(enabled: Boolean) {
        _bookingForm.value = _bookingForm.value.copy(isFriendsDeal = enabled)
    }

    fun toggleHouseCall(isHouseCall: Boolean) {
        _bookingForm.value = _bookingForm.value.copy(isHouseCall = isHouseCall)
    }

    fun selectEmbuLocation(location: DefaultSalonData.EmbuLocation) {
        _bookingForm.value = _bookingForm.value.copy(selectedLocation = location)
    }

    fun selectDate(date: String) {
        _bookingForm.value = _bookingForm.value.copy(selectedDate = date)
    }

    fun selectTimeSlot(slot: String) {
        _bookingForm.value = _bookingForm.value.copy(selectedTimeSlot = slot)
    }

    // Cost calculation engine with Friends Deal automatic discount & Retainer fee (20%)
    fun calculatePricing(): PricingSummary {
        val form = _bookingForm.value
        val service = form.selectedService ?: return PricingSummary(0, 0, 0, 0, 0, 0, 0)

        var basePrice = service.priceKes
        if (form.isBridalGroup) {
            val pax = form.groupPaxCount
            val hairCost = if (form.includeHair) 1200 * pax else 0
            val makeupCost = if (form.includeMakeup) 1500 * pax else 0
            val nailsCost = if (form.includeNails) 1000 * pax else 0
            val stylingCost = if (form.includeStyling) 500 * pax else 0
            basePrice = hairCost + makeupCost + nailsCost + stylingCost
        } else if (form.isFriendsDeal) {
            basePrice = service.priceKes * 2
        }

        val transportFee = if (form.isHouseCall) form.selectedLocation.transportFeeKes else 0

        // Friends Deal: 20% discount on services when 2 friends book together
        val discount = if (form.isFriendsDeal) {
            (basePrice * 0.20f).toInt()
        } else {
            0
        }

        val totalAmount = (basePrice - discount) + transportFee

        // 20% retainer fee of the service cost
        val rawRetainer = (totalAmount * 0.20f).toInt()
        val retainerAmount = maxOf(100, (rawRetainer / 10) * 10)

        val amountDueNow = when (form.paymentOption) {
            PaymentOption.PAY_PRIOR_FULL -> totalAmount
            PaymentOption.RETAINER_FEE_20 -> retainerAmount
            PaymentOption.PAY_AFTER_SERVICE -> 0
        }

        val balanceDueAfter = totalAmount - amountDueNow

        return PricingSummary(
            basePriceKes = basePrice,
            discountKes = discount,
            transportFeeKes = transportFee,
            totalAmountKes = totalAmount,
            retainerFeeKes = retainerAmount,
            amountDueNowKes = amountDueNow,
            balanceDueAfterKes = balanceDueAfter
        )
    }

    data class PricingSummary(
        val basePriceKes: Int,
        val discountKes: Int,
        val transportFeeKes: Int,
        val totalAmountKes: Int,
        val retainerFeeKes: Int,
        val amountDueNowKes: Int,
        val balanceDueAfterKes: Int
    )

    // M-PESA STK Push payment trigger & simulation
    fun initiateMpesaDeposit(phone: String, onPromptReady: () -> Unit) {
        viewModelScope.launch {
            _paymentStep.value = PaymentStep.STK_PROMPT_WAITING
            delay(1200) // Simulating network handshake with Safaricom Daraja API
            _paymentStep.value = PaymentStep.PIN_ENTRY
            onPromptReady()
        }
    }

    fun completeBooking(isMpesaPaid: Boolean, pinEntered: String = "") {
        viewModelScope.launch {
            val pricing = calculatePricing()
            val form = _bookingForm.value
            val mpesaCode = if (isMpesaPaid) "SHL" + (10000000..99999999).random().toString().take(7) else "PAY_ON_SITE"

            val booking = BookingItem(
                id = UUID.randomUUID().toString(),
                clientName = form.clientName.ifBlank { "Client" },
                clientPhone = form.clientPhone.ifBlank { "+254115071788" },
                clientEmail = form.clientEmail,
                serviceId = form.selectedService?.id ?: "srv_pedicure_gel",
                serviceName = if (form.isBridalGroup) "Bridal & Ruracio Party Glam (${form.groupPaxCount} Pax)" else form.selectedService?.title ?: "Nail Service",
                appointmentDate = form.selectedDate.ifBlank {
                    SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis() + 86400000))
                },
                timeSlot = form.selectedTimeSlot.ifBlank { "10:00 AM" },
                isHouseCall = form.isHouseCall,
                locationArea = if (form.isHouseCall) form.selectedLocation.name else "Embu CBD Salon",
                specificAddress = form.specificAddress,
                isFriendsDeal = form.isFriendsDeal,
                friendName = form.friendName,
                friendPhone = form.friendPhone,
                basePriceKes = pricing.basePriceKes,
                discountKes = pricing.discountKes,
                transportFeeKes = pricing.transportFeeKes,
                totalAmountKes = pricing.totalAmountKes,
                depositAmountKes = pricing.amountDueNowKes,
                isDepositPaid = isMpesaPaid,
                paymentOption = form.paymentOption.name,
                mpesaReference = mpesaCode,
                status = "Confirmed",
                inspoDescription = form.inspoDescription,
                nailShape = form.selectedNailShape,
                nailLength = form.selectedNailLength,
                notes = form.notes
            )

            repository.saveBooking(booking)
            _lastConfirmedBooking.value = booking
            _paymentStep.value = PaymentStep.CONFIRMED

            val paymentDesc = when (form.paymentOption) {
                PaymentOption.PAY_PRIOR_FULL -> "Prepaid in Full (KES ${pricing.amountDueNowKes})"
                PaymentOption.RETAINER_FEE_20 -> "20% Retainer Paid (KES ${pricing.amountDueNowKes}, Balance: KES ${pricing.balanceDueAfterKes})"
                PaymentOption.PAY_AFTER_SERVICE -> "Pay on Completion (KES ${pricing.totalAmountKes})"
            }

            // Trigger notification
            repository.addNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "💅 Appointment Booked with Steph!",
                    message = "${booking.serviceName} for ${booking.appointmentDate} at ${booking.timeSlot}. Inspo: ${form.selectedNailShape} (${form.inspoDescription.take(30)}). $paymentDesc.",
                    timestamp = System.currentTimeMillis(),
                    isUrgent = false,
                    type = "PAYMENT"
                )
            )
        }
    }

    fun resetBookingFlow() {
        _paymentStep.value = PaymentStep.NOT_STARTED
        _bookingForm.value = BookingFormState()
    }

    // Calendar sync simulation (Google Calendar & Outlook)
    fun syncWithGoogleCalendar(context: Context) {
        _calendarSyncedGoogle.value = true
        Toast.makeText(context, "Calendar synced with Google Calendar!", Toast.LENGTH_SHORT).show()
    }

    fun syncWithOutlook(context: Context) {
        _calendarSyncedOutlook.value = true
        Toast.makeText(context, "Calendar synced with Microsoft Outlook!", Toast.LENGTH_SHORT).show()
    }

    // Direct Contact actions (+254115071788)
    fun callStephDirectly(context: Context) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${DefaultSalonData.SALON_PHONE}")
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "Dialing ${DefaultSalonData.SALON_PHONE}", Toast.LENGTH_SHORT).show()
        }
    }

    fun openWhatsAppChat(context: Context, customMessage: String? = null) {
        val msg = customMessage ?: "Hi Steph! I'd like to book an appointment at Steph's Nail Studio in Embu. My nail inspo is ready!"
        val url = "https://wa.me/254115071788?text=${Uri.encode(msg)}"
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        try {
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "WhatsApp: ${DefaultSalonData.SALON_PHONE}", Toast.LENGTH_SHORT).show()
        }
    }

    // Beautician / Provider Registration
    fun registerNewProvider(
        name: String,
        specialty: String,
        phone: String,
        location: String,
        subscriptionTier: String,
        bio: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val provider = ProviderItem(
                id = "prov_${System.currentTimeMillis()}",
                name = name,
                title = "$specialty Specialist",
                specialty = specialty,
                rating = 5.0f,
                reviewsCount = 1,
                phone = phone,
                location = location,
                subscriptionTier = subscriptionTier,
                bio = bio
            )
            repository.registerProvider(provider)
            repository.addNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "✨ Welcome $name to Embu Beauty Hub!",
                    message = "Your profile is active on the $subscriptionTier. Clients in Embu can discover your beauty services.",
                    timestamp = System.currentTimeMillis(),
                    isUrgent = false,
                    type = "PROMO"
                )
            )
            onSuccess()
        }
    }

    fun updateBookingStatus(bookingId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateBookingStatus(bookingId, newStatus)
        }
    }

    fun toggleTwoFactor(enabled: Boolean) {
        _isTwoFactorEnabled.value = enabled
    }

    fun toggleEmailReminders(enabled: Boolean) {
        _automatedEmailReminders.value = enabled
    }

    fun toggleSmsReminders(enabled: Boolean) {
        _automatedSmsReminders.value = enabled
    }

    fun markNotificationRead(id: String) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }

    // Export analytical report (CSV / Summary) for provider dashboard
    fun generateAnalyticalReportText(bookingsList: List<BookingItem>): String {
        val totalRevenue = bookingsList.sumOf { it.totalAmountKes }
        val depositsCollected = bookingsList.sumOf { it.depositAmountKes }
        val completed = bookingsList.count { it.status == "Completed" }
        val confirmed = bookingsList.count { it.status == "Confirmed" }
        val houseCalls = bookingsList.count { it.isHouseCall }
        val friendsDeals = bookingsList.count { it.isFriendsDeal }

        return buildString {
            appendLine("=========================================")
            appendLine("STEPH'S NAIL STUDIO - EMBU COUNTY")
            appendLine("ANALYTICAL REPORT & BUSINESS INSIGHTS")
            appendLine("Generated: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())}")
            appendLine("Location: Embu Town & Surrounding Areas")
            appendLine("=========================================")
            appendLine("Total Bookings: ${bookingsList.size}")
            appendLine("Confirmed Appointments: $confirmed")
            appendLine("Completed Appointments: $completed")
            appendLine("House Calls (Home/Office): $houseCalls")
            appendLine("Friends Deals Availed: $friendsDeals")
            appendLine("Total Booking Revenue: KES %,d".format(totalRevenue))
            appendLine("Deposits/Retainers Secured: KES %,d".format(depositsCollected))
            appendLine("Average Ticket Size: KES %,d".format(if (bookingsList.isNotEmpty()) totalRevenue / bookingsList.size else 0))
            appendLine("-----------------------------------------")
            appendLine("BOOKING LOGS:")
            bookingsList.forEachIndexed { i, b ->
                appendLine("${i + 1}. ${b.appointmentDate} ${b.timeSlot} | ${b.clientName} | ${b.serviceName} | Inspo: ${b.nailShape} - ${b.inspoDescription} | KES ${b.totalAmountKes} | ${b.paymentOption} | ${b.status} | MPESA: ${b.mpesaReference}")
            }
            appendLine("=========================================")
        }
    }
}
