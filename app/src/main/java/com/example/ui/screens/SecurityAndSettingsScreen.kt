package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoftBlush
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SalonViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SecurityAndSettingsScreen(
    viewModel: SalonViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val is2FaEnabled by viewModel.isTwoFactorEnabled.collectAsState()
    val isEncrypted by viewModel.isDataEncryptionActive.collectAsState()
    val emailReminders by viewModel.automatedEmailReminders.collectAsState()
    val smsReminders by viewModel.automatedSmsReminders.collectAsState()
    val calendarGoogle by viewModel.calendarSyncedGoogle.collectAsState()
    val calendarOutlook by viewModel.calendarSyncedOutlook.collectAsState()
    val notifications by viewModel.notifications.collectAsState()

    var showOtpDialog by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }

    if (showOtpDialog) {
        AlertDialog(
            onDismissRequest = { showOtpDialog = false },
            title = { Text("Enter 2FA Verification Code", fontWeight = FontWeight.Bold, color = PlumDark) },
            text = {
                Column {
                    Text(
                        text = "A 6-digit authentication code was sent to your registered number via SMS. Enter it below to enable Two-Factor Authentication:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = otpCode,
                        onValueChange = { if (it.length <= 6) otpCode = it },
                        placeholder = { Text("e.g. 849201") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (otpCode.length >= 4) {
                            viewModel.toggleTwoFactor(true)
                            showOtpDialog = false
                            Toast.makeText(context, "Two-Factor Authentication is now ACTIVE!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Enter a valid code", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary)
                ) {
                    Text("Verify & Enable")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showOtpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("security_settings_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Security, Reminders & Sync",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PlumDark
                )
                Text(
                    text = "High-end protection for client data, automated reminders, and calendar synchronization.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Section 1: Advanced Data Encryption & 2FA
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = PlumPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Advanced Security & Data Privacy",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // AES-256 Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8F4)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(MpesaGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "AES-256 Client Data Encryption",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF104E2B)
                                )
                                Text(
                                    text = "All personal bookings, phone contacts, and home visit addresses are protected with bank-grade encryption.",
                                    fontSize = 11.sp,
                                    color = Color(0xFF267345)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MpesaGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text("ACTIVE", fontSize = 9.sp, fontWeight = FontWeight.Black, color = MpesaGreen)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2FA Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Two-Factor Authentication (2FA)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Require SMS OTP verification before modifying bookings or provider accounts.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = is2FaEnabled,
                            onCheckedChange = { enable ->
                                if (enable) {
                                    showOtpDialog = true
                                } else {
                                    viewModel.toggleTwoFactor(false)
                                    Toast.makeText(context, "2FA disabled", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = PlumPrimary, checkedTrackColor = RoseGold),
                            modifier = Modifier.testTag("2fa_switch")
                        )
                    }
                }
            }
        }

        // Section 2: Automated Reminders
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = PlumPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Automated Appointment Reminders",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Email Reminders Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Email, contentDescription = null, modifier = Modifier.size(16.dp), tint = RoseGold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Automated Email Reminders", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                            Text(
                                text = "Dispatches confirmation receipt and 24h & 2h appointment notices.",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = emailReminders,
                            onCheckedChange = { viewModel.toggleEmailReminders(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PlumPrimary, checkedTrackColor = RoseGold)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // SMS Reminders Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Sms, contentDescription = null, modifier = Modifier.size(16.dp), tint = PlumPrimary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Automated SMS Reminders", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                            Text(
                                text = "Sends Safaricom SMS reminders with technician arrival time for House Calls.",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                        Switch(
                            checked = smsReminders,
                            onCheckedChange = { viewModel.toggleSmsReminders(it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = PlumPrimary, checkedTrackColor = RoseGold)
                        )
                    }
                }
            }
        }

        // Section 3: Calendar Synchronization
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = PlumPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Calendar Synchronization",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                    }
                    Text(
                        text = "Sync seamlessly with your personal schedule to prevent double bookings.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.syncWithGoogleCalendar(context) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (calendarGoogle) "Google Synced ✓" else "Sync Google", fontSize = 12.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.syncWithOutlook(context) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (calendarOutlook) "Outlook Synced ✓" else "Sync Outlook", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // Section 4: Real-Time Scheduling Updates & Push Notifications Feed
        item {
            Text(
                text = "Urgent Scheduling Updates & Notifications",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = PlumDark
            )
        }

        items(notifications) { notif ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (notif.isUrgent) Color(0xFFFFF3E0) else MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.markNotificationRead(notif.id) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (notif.isUrgent) Color(0xFFEF6C00) else PlumPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = notif.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (notif.isUrgent) Color(0xFFB71C1C) else PlumDark
                            )
                            val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(notif.timestamp))
                            Text(text = timeStr, fontSize = 10.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = notif.message,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 15.sp
                        )
                    }
                }
            }
        }
    }
}
