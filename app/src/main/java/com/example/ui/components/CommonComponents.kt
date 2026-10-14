package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.MpesaGreen
import com.example.ui.theme.PlumPrimaryLight
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoftBlush
import com.example.ui.theme.WhatsAppGreen
import com.example.ui.viewmodel.AppScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalonTopAppBar(
    currentScreen: AppScreen,
    unreadNotificationsCount: Int,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier.testTag("salon_top_app_bar"),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = PlumDark,
            titleContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        title = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Steph's Nails",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(RoseGold)
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "EMBU",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = PlumDark
                        )
                    }
                }
                Text(
                    text = "Studio & Mobile Beauty Services",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.75f)
                )
            }
        },
        actions = {
            // Provider Portal icon button
            IconButton(
                onClick = { onNavigate(AppScreen.PROVIDER_PORTAL) },
                modifier = Modifier.testTag("top_bar_provider_portal")
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Provider & Beautician Portal",
                    tint = if (currentScreen == AppScreen.PROVIDER_PORTAL) ChampagneGold else Color.White
                )
            }

            // Security & Settings icon button with badge
            IconButton(
                onClick = { onNavigate(AppScreen.SETTINGS_SECURITY) },
                modifier = Modifier.testTag("top_bar_settings_security")
            ) {
                BadgedBox(
                    badge = {
                        if (unreadNotificationsCount > 0) {
                            Badge(
                                containerColor = RoseGold,
                                contentColor = PlumDark
                            ) {
                                Text("$unreadNotificationsCount")
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (currentScreen == AppScreen.SETTINGS_SECURITY) Icons.Default.Lock else Icons.Outlined.Lock,
                        contentDescription = "Security, Encryption & Reminders",
                        tint = if (currentScreen == AppScreen.SETTINGS_SECURITY) ChampagneGold else Color.White
                    )
                }
            }
        }
    )
}

@Composable
fun SalonBottomNavigationBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.testTag("salon_bottom_navigation"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            Triple(AppScreen.HOME, "Home", Pair(Icons.Default.Home, Icons.Outlined.Home)),
            Triple(AppScreen.SERVICES, "Services", Pair(Icons.Default.Spa, Icons.Outlined.Spa)),
            Triple(AppScreen.BOOKING, "Book", Pair(Icons.Default.CalendarMonth, Icons.Outlined.CalendarMonth)),
            Triple(AppScreen.GALLERY, "Styles", Pair(Icons.Default.PhotoLibrary, Icons.Outlined.PhotoLibrary)),
            Triple(AppScreen.CONTACT, "Contact", Pair(Icons.Default.Call, Icons.Outlined.Call))
        )

        items.forEach { (screen, label, icons) ->
            val isSelected = currentScreen == screen
            NavigationBarItem(
                modifier = Modifier.testTag("nav_item_${label.lowercase()}"),
                selected = isSelected,
                onClick = { onNavigate(screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) icons.first else icons.second,
                        contentDescription = label
                    )
                },
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PlumPrimary,
                    selectedTextColor = PlumPrimary,
                    indicatorColor = SoftBlush,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
fun FriendsDealBanner(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("friends_deal_promo_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(PlumDark, PlumPrimary)
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ChampagneGold)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "PROMO OFFER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PlumDark
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "👯 Friends Deal",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Book with a friend for 20% OFF! Valid for Salon Visits & House Calls in Embu.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(RoseGold)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Claim -20%",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
