package com.halliSanthe.data.repository

import androidx.room.*
import com.halliSanthe.data.model.Product
import kotlinx.coroutines.flow.Flow

// ──────────────────────────────────────────────────
//  DAO
// ──────────────────────────────────────────────────
@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY createdAt DESC")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE category = :category")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    @Update
    suspend fun updateProduct(product: Product)

    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("DELETE FROM products WHERE artisanId = :artisanId")
    suspend fun deleteProductsByArtisan(artisanId: String)
}

// ──────────────────────────────────────────────────
//  Database
// ──────────────────────────────────────────────────
@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class HalliSantheDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile private var INSTANCE: HalliSantheDatabase? = null

        fun getDatabase(context: android.content.Context): HalliSantheDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HalliSantheDatabase::class.java,
                    "halli_santhe_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
