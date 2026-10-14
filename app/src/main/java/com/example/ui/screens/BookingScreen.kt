package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultSalonData
import com.example.ui.components.MpesaStkPushDialog
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoftBlush
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.PaymentOption
import com.example.ui.viewmodel.PaymentStep
import com.example.ui.viewmodel.SalonViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingScreen(
    viewModel: SalonViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val form by viewModel.bookingForm.collectAsState()
    val paymentStep by viewModel.paymentStep.collectAsState()
    val services by viewModel.services.collectAsState()

    var showMpesaDialog by remember { mutableStateOf(false) }

    val pricing = viewModel.calculatePricing()

    val calendarDays = remember {
        val list = mutableListOf<Pair<String, String>>()
        val cal = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val isoFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (i in 0..13) {
            val fullDate = isoFormat.format(cal.time)
            val display = "${dayFormat.format(cal.time)}\n${dateFormat.format(cal.time)}"
            list.add(Pair(fullDate, display))
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    val nailShapes = listOf("Almond", "Coffin", "Square", "Stiletto", "Oval", "Squoval")
    val nailLengths = listOf("Natural / Short", "Medium", "Long", "Extra Long (XXL)")
    val artInspoStyles = listOf("French Art", "Chrome Isolation", "Ombre", "Cat Eye", "3D Flowers & Gems", "Gold Leaf Marble")

    if (showMpesaDialog) {
        MpesaStkPushDialog(
            step = paymentStep,
            depositAmountKes = pricing.amountDueNowKes,
            phoneNumber = form.clientPhone.ifBlank { "+254115071788" },
            serviceTitle = form.selectedService?.title ?: "Nail Appointment",
            onDismiss = {
                showMpesaDialog = false
                if (paymentStep == PaymentStep.CONFIRMED) {
                    onNavigate(AppScreen.HOME)
                }
            },
            onConfirmPin = { pin ->
                viewModel.completeBooking(isMpesaPaid = true, pinEntered = pin)
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("booking_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Book Your Appointment",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PlumDark
                )
                Text(
                    text = "Steph's Nail Studio: Embu Town & Surrounding Areas (Home or Office)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // STEP 1: Selected Service
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "1. Selected Service",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    if (form.selectedService == null && services.isNotEmpty()) {
                        viewModel.updateBookingForm { it.copy(selectedService = services[0]) }
                    }

                    form.selectedService?.let { selected ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SoftBlush),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = selected.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = PlumDark
                                    )
                                    Text(
                                        text = "${selected.durationMinutes} mins • Ksh ${"%,d".format(selected.priceKes)}",
                                        fontSize = 12.sp,
                                        color = PlumPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                OutlinedButton(
                                    onClick = { onNavigate(AppScreen.SERVICES) },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Change", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // STEP 2: Nail Inspo Prior to Service (Shape, Length, Art & Description)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("nail_inspo_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = RoseGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "2. Nail Inspo Prior to Service",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                    }
                    Text(
                        text = "Share your inspiration with Steph so materials and charms are prepped!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nail Shape Selector
                    Text("Desired Nail Shape:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        nailShapes.forEach { shape ->
                            val isSelected = form.selectedNailShape == shape
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PlumPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.updateBookingForm { f -> f.copy(selectedNailShape = shape) } }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = shape,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Nail Length Selector
                    Text("Desired Nail Length:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        nailLengths.forEach { len ->
                            val isSelected = form.selectedNailLength == len
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PlumPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.updateBookingForm { f -> f.copy(selectedNailLength = len) } }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = len,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Special Art Inspo Style
                    Text("Special Art Style:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(artInspoStyles) { artStyle ->
                            val isSelected = form.selectedArtStyle == artStyle
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) RoseGold else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.updateBookingForm { f -> f.copy(selectedArtStyle = artStyle) } }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = artStyle,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) PlumDark else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Inspo Description Box
                    OutlinedTextField(
                        value = form.inspoDescription,
                        onValueChange = { viewModel.updateBookingForm { f -> f.copy(inspoDescription = it) } },
                        label = { Text("Describe Your Inspo (Colors, Accents, Details)") },
                        placeholder = { Text("e.g., Milky pink ombre with glazed chrome powder and gold foil on ring finger") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("inspo_description_input"),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "💡 Tip: You can also pick directly from our Photo Gallery tab!",
                        fontSize = 11.sp,
                        color = RoseGold,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable { onNavigate(AppScreen.GALLERY) }
                    )
                }
            }
        }

        // STEP 3: Location & Home Service
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("location_selection_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "3. Location & Service Mode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleHouseCall(false) }
                                .border(
                                    width = if (!form.isHouseCall) 2.dp else 1.dp,
                                    color = if (!form.isHouseCall) PlumPrimary else Color.LightGray,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!form.isHouseCall) SoftBlush else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = if (!form.isHouseCall) PlumPrimary else Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Salon Visit",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Embu Town CBD",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.toggleHouseCall(true) }
                                .border(
                                    width = if (form.isHouseCall) 2.dp else 1.dp,
                                    color = if (form.isHouseCall) PlumPrimary else Color.LightGray,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (form.isHouseCall) SoftBlush else MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = null,
                                    tint = if (form.isHouseCall) PlumPrimary else Color.Gray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "House Call / Office",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Embu Environs",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = form.isHouseCall) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            Text(
                                text = "Select your Embu location (Travel surcharge):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                DefaultSalonData.EMBU_LOCATIONS.forEach { loc ->
                                    val isSelected = form.selectedLocation.name == loc.name
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) PlumPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                            .clickable { viewModel.selectEmbuLocation(loc) }
                                            .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "${loc.name} (+Ksh ${loc.transportFeeKes})",
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = form.specificAddress,
                                onValueChange = { viewModel.updateBookingForm { f -> f.copy(specificAddress = it) } },
                                placeholder = { Text("Exact Estate / Landmark / House in Embu") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }
        }

        // STEP 4: Friends Deal & Bridal Groups
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("friends_deal_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "4. Friends Deal (-20% OFF)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (form.isFriendsDeal) Color(0xFFFFF8E7) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "👯 Friends Deal Promo",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "20% discount applied automatically when two bookings are made concurrently or linked!",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Switch(
                                checked = form.isFriendsDeal,
                                onCheckedChange = { viewModel.toggleFriendsDeal(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = PlumPrimary, checkedTrackColor = RoseGold),
                                modifier = Modifier.testTag("friends_deal_switch")
                            )
                        }
                    }

                    AnimatedVisibility(visible = form.isFriendsDeal) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            Text("Friend's Contact (Linked Concurrently):", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = form.friendName,
                                    onValueChange = { viewModel.updateBookingForm { f -> f.copy(friendName = it) } },
                                    placeholder = { Text("Friend's Name") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = form.friendPhone,
                                    onValueChange = { viewModel.updateBookingForm { f -> f.copy(friendPhone = it) } },
                                    placeholder = { Text("Friend's Phone") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 5: Interactive Calendar Date & Time Slot
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("calendar_selection_section")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "5. Appointment Schedule",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                        Text(
                            text = "Sync Calendar",
                            fontSize = 11.sp,
                            color = PlumPrimary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.syncWithGoogleCalendar(context) }
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Pick Date (starts empty by default for new scheduling):", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(calendarDays) { (fullDate, label) ->
                            val isSelected = form.selectedDate == fullDate
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) PlumPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.selectDate(fullDate) }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Select Time Slot:", style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DefaultSalonData.TIME_SLOTS.forEach { slot ->
                            val isSelected = form.selectedTimeSlot == slot
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) PlumPrimary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.selectTimeSlot(slot) }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = slot,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        // STEP 6: Client Contact Details
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "6. Client Contact Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = form.clientName,
                        onValueChange = { viewModel.updateBookingForm { f -> f.copy(clientName = it) } },
                        label = { Text("Your Full Name") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("client_name_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = form.clientPhone,
                        onValueChange = { viewModel.updateBookingForm { f -> f.copy(clientPhone = it) } },
                        label = { Text("Phone Number (+254...)") },
                        placeholder = { Text("e.g. 0712345678 or +254...") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("client_phone_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // STEP 7: Payment Options (Paying Prior, Retainer Fee 20%, or Pay After Service)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_options_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "7. Payment Options",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Text(
                        text = "Choose whether to pay prior, pay a 20% retainer fee to lock slot, or pay after service.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option A: 20% Retainer Fee
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setPaymentOption(PaymentOption.RETAINER_FEE_20) }
                            .border(
                                width = if (form.paymentOption == PaymentOption.RETAINER_FEE_20) 2.dp else 1.dp,
                                color = if (form.paymentOption == PaymentOption.RETAINER_FEE_20) PlumPrimary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (form.paymentOption == PaymentOption.RETAINER_FEE_20) SoftBlush else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = form.paymentOption == PaymentOption.RETAINER_FEE_20,
                                onClick = { viewModel.setPaymentOption(PaymentOption.RETAINER_FEE_20) },
                                colors = RadioButtonDefaults.colors(selectedColor = PlumPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "20% Retainer Fee",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ChampagneGold)
                                            .padding(horizontal = 5.dp, vertical = 1.dp)
                                    ) {
                                        Text("POPULAR", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = PlumDark)
                                    }
                                }
                                Text(
                                    text = "Pay 20% retainer (Ksh ${"%,d".format(pricing.retainerFeeKes)}) upfront via M-PESA to lock the booking; pay balance of Ksh ${"%,d".format(pricing.totalAmountKes - pricing.retainerFeeKes)} after service.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option B: Pay Prior (100% Upfront)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setPaymentOption(PaymentOption.PAY_PRIOR_FULL) }
                            .border(
                                width = if (form.paymentOption == PaymentOption.PAY_PRIOR_FULL) 2.dp else 1.dp,
                                color = if (form.paymentOption == PaymentOption.PAY_PRIOR_FULL) PlumPrimary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (form.paymentOption == PaymentOption.PAY_PRIOR_FULL) SoftBlush else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = form.paymentOption == PaymentOption.PAY_PRIOR_FULL,
                                onClick = { viewModel.setPaymentOption(PaymentOption.PAY_PRIOR_FULL) },
                                colors = RadioButtonDefaults.colors(selectedColor = PlumPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Pay Prior (100% Full Prepayment)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Pay full amount (Ksh ${"%,d".format(pricing.totalAmountKes)}) prior via M-PESA STK Push. Completely seamless, no payments needed on the day!",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Option C: Pay After Service
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.setPaymentOption(PaymentOption.PAY_AFTER_SERVICE) }
                            .border(
                                width = if (form.paymentOption == PaymentOption.PAY_AFTER_SERVICE) 2.dp else 1.dp,
                                color = if (form.paymentOption == PaymentOption.PAY_AFTER_SERVICE) PlumPrimary else Color.LightGray,
                                shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (form.paymentOption == PaymentOption.PAY_AFTER_SERVICE) SoftBlush else MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = form.paymentOption == PaymentOption.PAY_AFTER_SERVICE,
                                onClick = { viewModel.setPaymentOption(PaymentOption.PAY_AFTER_SERVICE) },
                                colors = RadioButtonDefaults.colors(selectedColor = PlumPrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Pay After Service",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Pay 100% (Ksh ${"%,d".format(pricing.totalAmountKes)}) after service completion via Cash or M-PESA at the salon or house visit.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Financial breakdown
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Service Total", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Ksh %,d".format(pricing.totalAmountKes), fontWeight = FontWeight.Bold)
                    }

                    if (pricing.discountKes > 0) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Friends Deal (-20%)", color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                            Text("-Ksh %,d".format(pricing.discountKes), color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = when (form.paymentOption) {
                                    PaymentOption.PAY_PRIOR_FULL -> "Amount Due Now (100%)"
                                    PaymentOption.RETAINER_FEE_20 -> "20% Retainer Due Now"
                                    PaymentOption.PAY_AFTER_SERVICE -> "Amount Due Now"
                                },
                                fontWeight = FontWeight.Bold,
                                color = if (pricing.amountDueNowKes > 0) Color(0xFF104E2B) else PlumDark,
                                fontSize = 14.sp
                            )
                            if (pricing.balanceDueAfterKes > 0) {
                                Text(
                                    text = "Balance after service: Ksh %,d".format(pricing.balanceDueAfterKes),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                        Text(
                            text = "Ksh %,d".format(pricing.amountDueNowKes),
                            fontWeight = FontWeight.Black,
                            fontSize = 19.sp,
                            color = if (pricing.amountDueNowKes > 0) MpesaGreen else PlumPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Action Button based on selected payment timing
                    if (pricing.amountDueNowKes > 0) {
                        Button(
                            onClick = {
                                if (form.clientPhone.isBlank()) {
                                    Toast.makeText(context, "Please enter your phone number above", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                showMpesaDialog = true
                                viewModel.initiateMpesaDeposit(form.clientPhone) {}
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("pay_deposit_mpesa_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = MpesaGreen),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (form.paymentOption == PaymentOption.RETAINER_FEE_20)
                                    "Pay 20% Retainer (Ksh ${"%,d".format(pricing.retainerFeeKes)}) via M-PESA"
                                else
                                    "Pay Ksh ${"%,d".format(pricing.amountDueNowKes)} via M-PESA STK Push",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                if (form.clientName.isBlank() || form.clientPhone.isBlank()) {
                                    Toast.makeText(context, "Please enter your name & phone number above", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                viewModel.completeBooking(isMpesaPaid = false)
                                Toast.makeText(context, "Appointment booked! Pay after service.", Toast.LENGTH_LONG).show()
                                onNavigate(AppScreen.HOME)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("confirm_pay_after_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Confirm Booking (Pay After Service)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
