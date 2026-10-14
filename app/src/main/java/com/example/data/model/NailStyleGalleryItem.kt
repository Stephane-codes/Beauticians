package com.example.data.model

data class NailStyleGalleryItem(
    val id: String,
    val title: String,
    val category: String, // "Acrylics", "BIAB & Gel", "Bridal & Ruracio", "Glazed Chrome", "3D Art & Gems"
    val priceKes: Int,
    val description: String,
    val tags: List<String>,
    val drawableResName: String,
    val likesCount: Int = 128
)
