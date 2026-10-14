package com.example.data.local

import com.example.data.model.NailStyleGalleryItem
import com.example.data.model.NotificationItem
import com.example.data.model.ProviderItem
import com.example.data.model.ServiceItem

object DefaultSalonData {
    const val SALON_PHONE = "+254115071788"
    const val SALON_NAME = "Steph's Nail Studio"
    const val SALON_TAGLINE = "Clean • Classy • Creative • You"
    const val SALON_LOCATION = "Embu Town CBD & Surrounding Areas (Dallas, Majengo, Kangaru, Blue Valley, Manyatta)"
    const val WHATSAPP_URL = "https://wa.me/254115071788"

    // Supported Embu county service zones with travel surcharges for house calls
    val EMBU_LOCATIONS = listOf(
        EmbuLocation("Embu Town CBD (Salon / Office)", 0, "0 - 1 km"),
        EmbuLocation("Dallas", 250, "2 km"),
        EmbuLocation("Majengo", 250, "2.5 km"),
        EmbuLocation("Blue Valley", 300, "3 km"),
        EmbuLocation("Kangaru (Near Embu Univ)", 300, "4 km"),
        EmbuLocation("Kamiu", 300, "3 km"),
        EmbuLocation("Njukiri", 350, "5 km"),
        EmbuLocation("Gakwegori", 350, "5.5 km"),
        EmbuLocation("Manyatta", 500, "8 km"),
        EmbuLocation("Runyenjes", 700, "15 km"),
        EmbuLocation("Kiritiri Road", 400, "7 km")
    )

    data class EmbuLocation(
        val name: String,
        val transportFeeKes: Int,
        val approxDistance: String
    )

    val TIME_SLOTS = listOf(
        "08:30 AM",
        "10:00 AM",
        "11:30 AM",
        "01:00 PM",
        "02:30 PM",
        "04:00 PM",
        "05:30 PM",
        "07:00 PM"
    )

    // Exact services & one-person deal prices from Steph's official flyer
    val INITIAL_SERVICES = listOf(
        // Core Steph's Nail Services from official menu
        ServiceItem(
            id = "srv_pedicure_gel",
            title = "Pedicure + Gel Application",
            category = "Pedicure & Manicure",
            priceKes = 1000,
            durationMinutes = 60,
            description = "Complete pedicure foot soak, exfoliation, cuticle cleanup, and long-lasting gel polish.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Popular Deal"
        ),
        ServiceItem(
            id = "srv_manicure_gel",
            title = "Manicure + Gel Application",
            category = "Pedicure & Manicure",
            priceKes = 1000,
            durationMinutes = 50,
            description = "Russian/classic manicure shaping, cuticle perfection, hydrating massage, and durable gel application.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Classic"
        ),
        ServiceItem(
            id = "srv_gel_application",
            title = "Gel Application",
            category = "Gel & Natural",
            priceKes = 500,
            durationMinutes = 35,
            description = "Natural nail prep with chip-free, mirror-gloss UV gel color application.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true
        ),
        ServiceItem(
            id = "srv_tips_gel",
            title = "Tips Installation + Gel Application",
            category = "Extensions",
            priceKes = 850,
            durationMinutes = 60,
            description = "Lightweight tip extensions shaped to your choice, reinforced with vibrant UV gel color.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Great Value"
        ),
        ServiceItem(
            id = "srv_stickons_gel",
            title = "Stickons + Gel Application",
            category = "Extensions",
            priceKes = 700,
            durationMinutes = 45,
            description = "Quick, safe stick-on extensions overlayed with long-wear gel polish.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true
        ),
        ServiceItem(
            id = "srv_biab_overlay",
            title = "Builder Gel Application (Overlay)",
            category = "Builder & BIAB",
            priceKes = 1000,
            durationMinutes = 55,
            description = "Reinforcing structured builder gel overlay on natural nails to promote natural growth and strength.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Trending"
        ),
        ServiceItem(
            id = "srv_biab_tips",
            title = "Builder Gel On Tips",
            category = "Builder & BIAB",
            priceKes = 1500,
            durationMinutes = 75,
            description = "Tip extensions strengthened with high-durability builder gel for a sleek, flexible finish.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true
        ),
        ServiceItem(
            id = "srv_sculpting_full",
            title = "Sculpting Full Set",
            category = "Sculpting & Acrylics",
            priceKes = 2500,
            durationMinutes = 90,
            description = "Hand-sculpted bespoke extensions tailored to your natural nail bed with precision architecture.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Artisan"
        ),
        ServiceItem(
            id = "srv_acrylics_full",
            title = "Acrylics (Full Set)",
            category = "Sculpting & Acrylics",
            priceKes = 3500,
            durationMinutes = 90,
            description = "Full acrylic extensions set (Ksh 3,500 - 5,000 depending on length & custom shapes: Coffin, Stiletto, Almond, Square).",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Bestseller"
        ),
        ServiceItem(
            id = "srv_gum_gel",
            title = "Gum Gel Extensions",
            category = "Extensions",
            priceKes = 1800,
            durationMinutes = 75,
            description = "Hybrid polygel/gum gel extensions combining acrylic strength with gel featherlight flexibility.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true
        ),
        ServiceItem(
            id = "srv_gel_x",
            title = "Gel X Extensions",
            category = "Extensions",
            priceKes = 2500,
            durationMinutes = 80,
            description = "Full cover soft gel extension system offering fast, non-damaging, sleek celebrity nails.",
            isHouseCallEligible = true,
            isFriendsDealEligible = true,
            popularTag = "Premium"
        ),
        ServiceItem(
            id = "srv_nail_art_solo",
            title = "Special Nail Art (Per Nail)",
            category = "Nail Art",
            priceKes = 50,
            durationMinutes = 15,
            description = "Custom nail art per nail: French Art, Chrome Isolation, Ombre, Cat Eye, Gold Leaf or 3D gems.",
            isHouseCallEligible = true,
            isFriendsDealEligible = false
        ),
        // Group & Bridal Uniformity Packages (Event feature requested by user)
        ServiceItem(
            id = "srv_ruracio_bridal_uniformity",
            title = "Ruracio & Wedding Glam Uniformity Package",
            category = "Bridal & Ruracio",
            priceKes = 12000,
            durationMinutes = 240,
            description = "Uniform glam for the entire bridal party (Nails + Hair + Makeup + Styling). Perfect synchronization for traditional Kikuyu ruracios and church weddings.",
            isHouseCallEligible = true,
            isBridalPackage = true,
            isFriendsDealEligible = true,
            popularTag = "Bridal Event"
        )
    )

    val SPECIAL_OFFERS = listOf(
        "✂ Free removal for next application (if done by Steph) ♥",
        "🕒 I can come at your convenience (Home or Office) ♥",
        "👯 Friends Deal: 20% discount when 2 friends book together for house call or shop visit!",
        "✨ Clean & Safe Tools • Flexible Bookings • Quality & Care"
    )

    val SPECIAL_ART_STYLES = listOf(
        Pair("French Art", "Crisp classic & modern smile lines"),
        Pair("Chrome Isolation", "Mirror chrome metallic accents"),
        Pair("Ombre", "Seamless milky & gradient transitions"),
        Pair("Cat Eye", "Magnetic multi-dimensional velvet shimmer"),
        Pair("3D Flowers & Gems", "Hand-sculpted petals & crystals")
    )

    val INITIAL_PROVIDERS = listOf(
        ProviderItem(
            id = "prov_steph",
            name = "Steph",
            title = "Lead Nail Technician & Founder",
            specialty = "Acrylics, Sculpting, BIAB, Gel X, Nail Art",
            rating = 4.98f,
            reviewsCount = 210,
            phone = "+254115071788",
            location = "Embu Town & Surrounding Areas",
            subscriptionTier = "Pro Founder Tier",
            bio = "Steph's Nail Studio: Personal nail services brought to you. Specialized in clean, classy, creative nail sculpting and home visits in Embu."
        ),
        ProviderItem(
            id = "prov_wanja",
            name = "Wanja K.",
            title = "Bridal & Event Makeup Artist",
            specialty = "HD Glam Makeup, Gele & Skin Prep",
            rating = 4.90f,
            reviewsCount = 76,
            phone = "+254712345678",
            location = "Embu Town & Manyatta",
            subscriptionTier = "Pro Monthly (KES 1,499)",
            bio = "Partner beautician for bridal ruracios, white weddings, and gala glam."
        ),
        ProviderItem(
            id = "prov_muthoni",
            name = "Muthoni W.",
            title = "Hair Stylist & Braider",
            specialty = "Knotless Braids, Bridal Updos, Wigs",
            rating = 4.85f,
            reviewsCount = 92,
            phone = "+254722334455",
            location = "Dallas, Embu",
            subscriptionTier = "Weekly Starter (KES 499)",
            bio = "Specializing in pain-free scalp protection braids, bridal hair styling, and wig customization."
        ),
        ProviderItem(
            id = "prov_achieng",
            name = "Achieng O.",
            title = "Lash & Brow Specialist",
            specialty = "Volume Lashes, Brow Tint & Lamination",
            rating = 4.90f,
            reviewsCount = 63,
            phone = "+254733445566",
            location = "Embu Town, Kangaru",
            subscriptionTier = "Pro Monthly (KES 1,499)",
            bio = "Trained lash technician offering natural hybrid and dramatic Russian volume sets."
        )
    )

    val GALLERY_ITEMS = listOf(
        NailStyleGalleryItem(
            id = "gal_1",
            title = "Marble Gold Leaf Stiletto Nails",
            category = "Sculpting & Acrylics",
            priceKes = 3500,
            description = "Long stiletto acrylics with white marble veins, 24k gold leaf foil, and glossy gel finish.",
            tags = listOf("Acrylics", "Gold Leaf", "Marble", "Stiletto"),
            drawableResName = "acrylic_sculpting_square_1791958541246",
            likesCount = 284
        ),
        NailStyleGalleryItem(
            id = "gal_2",
            title = "Ruracio & Bridal Uniformity French Ombre",
            category = "Bridal & Ruracio",
            priceKes = 2500,
            description = "Matching French ombre stiletto and almond sets with rose gold glitter, as showcased for wedding celebrations.",
            tags = listOf("Bridal", "Ruracio", "Ombre", "Friends Deal"),
            drawableResName = "bridal_ruracio_glam_1791956430418",
            likesCount = 356
        ),
        NailStyleGalleryItem(
            id = "gal_3",
            title = "Blush Ombre with 3D Sculpted Petals",
            category = "Nail Art",
            priceKes = 2800,
            description = "Handcrafted 3D acrylic flower petals with golden caviar beads and Swarovski crystal clusters on nude blush base.",
            tags = listOf("3D Flowers", "Special Art", "Blush", "Almond"),
            drawableResName = "gallery_nail_art_1_1791956446048",
            likesCount = 219
        ),
        NailStyleGalleryItem(
            id = "gal_4",
            title = "Hailey Glazed Donut Chrome Isolation",
            category = "Gel & Natural",
            priceKes = 1000,
            description = "Iridescent pearlescent chrome powder buffed over milky BIAB overlay with isolated metallic lines.",
            tags = listOf("Chrome Isolation", "BIAB", "Glazed", "Trending"),
            drawableResName = "sculpted_nails_perfect_1791958518664",
            likesCount = 194
        ),
        NailStyleGalleryItem(
            id = "gal_5",
            title = "Tips Installation + Pink Floral Art",
            category = "Extensions",
            priceKes = 850,
            description = "Clean square tip installation with hand-painted blossom petals and pink gel polish.",
            tags = listOf("Tips + Gel", "Floral Art", "Classy"),
            drawableResName = "bridal_ruracio_glam_1791956430418",
            likesCount = 175
        )
    )

    val INITIAL_NOTIFICATIONS = listOf(
        NotificationItem(
            id = "notif_1",
            title = "💅 Welcome to Steph's Nail Studio!",
            message = "Clean • Classy • Creative • You. Enjoy our Ksh 1,000 Mani/Pedi Gel deals, home services in Embu, and 20% off Friends Deal!",
            timestamp = System.currentTimeMillis() - 3600000,
            isUrgent = false,
            type = "PROMO"
        ),
        NotificationItem(
            id = "notif_2",
            title = "📅 Flexible Booking Alert",
            message = "Steph is available for house calls & office visits in Embu Town, Dallas & Kangaru today. Tap to reserve your spot.",
            timestamp = System.currentTimeMillis() - 7200000,
            isUrgent = true,
            type = "SCHEDULING"
        )
    )
}
