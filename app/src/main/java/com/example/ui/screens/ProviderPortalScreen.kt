package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BookingItem
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoftBlush
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SalonViewModel

@Composable
fun ProviderPortalScreen(
    viewModel: SalonViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bookings by viewModel.bookings.collectAsState()
    val providers by viewModel.providers.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var showExportDialog by remember { mutableStateOf(false) }

    // Beautician registration fields
    var regName by remember { mutableStateOf("") }
    var regSpecialty by remember { mutableStateOf("Nail Specialist") }
    var regPhone by remember { mutableStateOf("+254") }
    var regLocation by remember { mutableStateOf("Embu Town CBD") }
    var regPlan by remember { mutableStateOf("Monthly Pro (KES 1,499)") }
    var regBio by remember { mutableStateOf("") }

    if (showExportDialog) {
        val reportText = viewModel.generateAnalyticalReportText(bookings)
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = {
                Text("Analytical Report Preview", fontWeight = FontWeight.Bold, color = PlumDark)
            },
            text = {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = reportText,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .padding(10.dp)
                            .height(280.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, reportText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Export Report Via"))
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Share / Export")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("provider_portal_screen")
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = PlumPrimary
        ) {
            Tab(
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 },
                text = { Text("Provider Dashboard", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.Assessment, contentDescription = null) }
            )
            Tab(
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 },
                text = { Text("Beautician Registration", fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.PersonAdd, contentDescription = null) }
            )
        }

        if (selectedTabIndex == 0) {
            // PROVIDER DASHBOARD VIEW
            ProviderDashboardView(
                bookings = bookings,
                onExportReport = { showExportDialog = true },
                onUpdateStatus = { id, status -> viewModel.updateBookingStatus(id, status) }
            )
        } else {
            // BEAUTICIAN REGISTRATION VIEW
            BeauticianRegistrationView(
                name = regName,
                onNameChange = { regName = it },
                specialty = regSpecialty,
                onSpecialtyChange = { regSpecialty = it },
                phone = regPhone,
                onPhoneChange = { regPhone = it },
                location = regLocation,
                onLocationChange = { regLocation = it },
                plan = regPlan,
                onPlanChange = { regPlan = it },
                bio = regBio,
                onBioChange = { regBio = it },
                onRegister = {
                    if (regName.isBlank() || regPhone.length < 9) {
                        Toast.makeText(context, "Please enter your name and phone number", Toast.LENGTH_SHORT).show()
                        return@BeauticianRegistrationView
                    }
                    viewModel.registerNewProvider(
                        name = regName,
                        specialty = regSpecialty,
                        phone = regPhone,
                        location = regLocation,
                        subscriptionTier = regPlan,
                        bio = regBio
                    ) {
                        Toast.makeText(context, "Registration successful! Welcome to Embu Beauty Hub.", Toast.LENGTH_LONG).show()
                        selectedTabIndex = 0
                    }
                }
            )
        }
    }
}

@Composable
fun ProviderDashboardView(
    bookings: List<BookingItem>,
    onExportReport: () -> Unit,
    onUpdateStatus: (String, String) -> Unit
) {
    val totalRevenue = bookings.sumOf { it.totalAmountKes }
    val depositsSecured = bookings.sumOf { it.depositAmountKes }
    val completedCount = bookings.count { it.status == "Completed" }
    val houseCallCount = bookings.count { it.isHouseCall }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("provider_dashboard_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Steph's Business Dashboard",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PlumDark
                    )
                    Text(
                        text = "Real-time earnings, bookings & analytics",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Button(
                    onClick = onExportReport,
                    colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("export_report_button")
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export Report", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Metrics Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SoftBlush),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Total Booked", fontSize = 11.sp, color = PlumPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ksh %,d".format(totalRevenue), fontSize = 18.sp, fontWeight = FontWeight.Black, color = PlumDark)
                        Text("${bookings.size} appointments", fontSize = 10.sp, color = Color.Gray)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F4)),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Retainers & Deposits", fontSize = 11.sp, color = MpesaGreen, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Ksh %,d".format(depositsSecured), fontSize = 18.sp, fontWeight = FontWeight.Black, color = Color(0xFF104E2B))
                        Text("M-PESA verified", fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }

        // Data Visualization Chart (Canvas Custom Bar Chart)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("analytics_chart_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Weekly Activity & Booking Volume",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SoftBlush)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text("7-Day Trend", fontSize = 10.sp, color = PlumPrimary, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Custom Canvas Bar Visualization
                    val barData = remember(bookings.size) {
                        listOf(
                            Pair("Mon", 3),
                            Pair("Tue", 5),
                            Pair("Wed", 4),
                            Pair("Thu", 7),
                            Pair("Fri", 9),
                            Pair("Sat", 12),
                            Pair("Sun", 6)
                        )
                    }

                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                    ) {
                        val maxCount = 14f
                        val barWidth = size.width / (barData.size * 2f)
                        val spacing = size.width / barData.size

                        barData.forEachIndexed { index, (day, count) ->
                            val left = index * spacing + (spacing - barWidth) / 2
                            val barHeight = (count / maxCount) * (size.height - 30.dp.toPx())
                            val top = size.height - barHeight - 20.dp.toPx()

                            // Bar
                            drawRoundRect(
                                color = if (index == 5) PlumPrimary else RoseGold,
                                topLeft = Offset(left, top),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach {
                            Text(text = it, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Quick Breakdown Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Completed", fontSize = 11.sp, color = Color.Gray)
                            Text("$completedCount", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PlumPrimary)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("House Calls", fontSize = 11.sp, color = Color.Gray)
                            Text("$houseCallCount", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = RoseGold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Salon Visits", fontSize = 11.sp, color = Color.Gray)
                            Text("${bookings.size - houseCallCount}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = PlumDark)
                        }
                    }
                }
            }
        }

        // Appointments List
        item {
            Text(
                text = "Client Appointments (${bookings.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PlumDark
            )
        }

        if (bookings.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No appointments booked yet", fontWeight = FontWeight.SemiBold, color = Color.Gray)
                        Text("New client bookings and retainer deposits will appear here in real-time.", fontSize = 11.sp, color = Color.Gray)
                    }
                }
            }
        }

        items(bookings) { booking ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = booking.clientName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = PlumDark
                            )
                            Text(
                                text = "${booking.clientPhone} • ${booking.appointmentDate} at ${booking.timeSlot}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (booking.status == "Completed") Color(0xFFE8F5E9) else SoftBlush)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = booking.status,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (booking.status == "Completed") Color(0xFF2E7D32) else PlumPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Service: ${booking.serviceName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (booking.inspoDescription.isNotBlank()) {
                        Text(
                            text = "Inspo: ${booking.nailShape} • ${booking.inspoDescription}",
                            fontSize = 11.sp,
                            color = RoseGold,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "Location: ${booking.locationArea} ${if (booking.specificAddress.isNotBlank()) "(${booking.specificAddress})" else ""}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total: Ksh ${"%,d".format(booking.totalAmountKes)} (Retainer: Ksh ${"%,d".format(booking.depositAmountKes)})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PlumPrimary
                        )

                        Row {
                            if (booking.status != "Completed") {
                                OutlinedButton(
                                    onClick = { onUpdateStatus(booking.id, "Completed") },
                                    shape = RoundedCornerShape(8.dp),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Complete", fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BeauticianRegistrationView(
    name: String,
    onNameChange: (String) -> Unit,
    specialty: String,
    onSpecialtyChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    plan: String,
    onPlanChange: (String) -> Unit,
    bio: String,
    onBioChange: (String) -> Unit,
    onRegister: () -> Unit
) {
    val plans = listOf(
        Pair("Weekly Starter (KES 499)", "Ksh 499 / week - Listed in Embu directory, up to 15 client bookings"),
        Pair("Monthly Pro (KES 1,499)", "Ksh 1,499 / month - Unlimited bookings, priority placement, analytics & M-PESA deposits")
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("beautician_registration_view"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Register as a Beauty Specialist",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PlumDark
                )
                Text(
                    text = "Join Steph's Embu beauty platform to offer your specialized nail, hair, lash or makeup services at a flexible subscription fee.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Select Subscription Tier:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    plans.forEach { (planTitle, desc) ->
                        val isSelected = plan == planTitle
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onPlanChange(planTitle) }
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) PlumPrimary else Color.LightGray,
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = if (isSelected) SoftBlush else MaterialTheme.colorScheme.surface)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { onPlanChange(planTitle) },
                                    colors = RadioButtonDefaults.colors(selectedColor = PlumPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(planTitle, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text(desc, fontSize = 10.sp, color = Color.Gray)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = onNameChange,
                        label = { Text("Your Business or Professional Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = specialty,
                        onValueChange = onSpecialtyChange,
                        label = { Text("Specialty (e.g. Nails, Bridal Makeup, Braids, Lashes)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = onPhoneChange,
                        label = { Text("M-PESA & Contact Phone (+254...)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = location,
                        onValueChange = onLocationChange,
                        label = { Text("Base Location in Embu (e.g. Dallas, Kangaru, Town)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bio,
                        onValueChange = onBioChange,
                        label = { Text("Brief Bio & Experience") },
                        placeholder = { Text("Describe your services, skills, and portfolio") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        minLines = 2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onRegister,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("activate_beautician_subscription_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Register & Activate Subscription", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
