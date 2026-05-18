package com.halliSanthe.data.model

data class Artisan(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val village: String = "",
    val district: String = "",
    val craft: String = "",
    val bio: String = "",
    val profileImageUrl: String = "",
    val totalProducts: Int = 0,
    val joinedAt: Long = System.currentTimeMillis()
)
