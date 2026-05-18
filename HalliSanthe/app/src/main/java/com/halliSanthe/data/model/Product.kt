package com.halliSanthe.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    @DocumentId
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val category: String = "",
    val imageUrl: String = "",
    val artisanId: String = "",
    val artisanName: String = "",
    val artisanPhone: String = "",
    val village: String = "",
    val isAvailable: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
