package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NailStyleGalleryItem
import com.example.ui.theme.ChampagneGold
import com.example.ui.theme.PlumDark
import com.example.ui.theme.PlumPrimary
import com.example.ui.theme.RoseGold
import com.example.ui.theme.SoftBlush
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.SalonViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GalleryScreen(
    viewModel: SalonViewModel,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val galleryItems = viewModel.galleryItems
    var selectedCategory by remember { mutableStateOf("All") }
    val likedItems = remember { mutableStateMapOf<String, Boolean>() }

    val categories = listOf("All", "Sculpting & Acrylics", "Bridal & Ruracio", "Nail Art", "Gel & Natural", "Extensions")

    val filteredItems = galleryItems.filter {
        selectedCategory == "All" || it.category == selectedCategory
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("gallery_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Nail Designs & Style Gallery",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = PlumDark
                )
                Text(
                    text = "Steph's latest handcrafted sets in Embu. Tap 'Use as My Inspo & Book' to schedule any design!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Category Filter Row
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PlumPrimary,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("gallery_filter_$cat")
                    )
                }
            }
        }

        // Gallery Items
        items(filteredItems) { item ->
            val isLiked = likedItems[item.id] == true
            val currentLikes = item.likesCount + (if (isLiked) 1 else 0)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("gallery_card_${item.id}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column {
                    // Image container
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        val drawableId = when (item.drawableResName) {
                            "bridal_ruracio_glam_1791956430418" -> R.drawable.bridal_ruracio_glam_1791956430418
                            "gallery_nail_art_1_1791956446048" -> R.drawable.gallery_nail_art_1_1791956446048
                            "acrylic_sculpting_square_1791958541246" -> R.drawable.acrylic_sculpting_square_1791958541246
                            else -> R.drawable.sculpted_nails_perfect_1791958518664
                        }

                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = item.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Category chip
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(12.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PlumDark.copy(alpha = 0.85f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = item.category,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Heart like button
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(12.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.9f))
                                .clickable {
                                    likedItems[item.id] = !isLiked
                                }
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color.Red else Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Content
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = PlumDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "$currentLikes likes • Steph's Studio Embu",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = "Ksh %,d".format(item.priceKes),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = PlumPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tags
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            item.tags.forEach { tag ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SoftBlush)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "#$tag",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = PlumPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Action Button
                        Button(
                            onClick = {
                                viewModel.selectGalleryLookForBooking(item)
                                onNavigate(AppScreen.BOOKING)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("book_inspo_btn_${item.id}"),
                            colors = ButtonDefaults.buttonColors(containerColor = PlumPrimary),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ChampagneGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Use as My Inspo & Book Appointment", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}
