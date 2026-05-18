package com.halliSanthe.data.repository

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.halliSanthe.data.model.Message
import com.halliSanthe.data.model.Product
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.quality
import id.zelory.compressor.constraint.resolution
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.util.UUID

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

class ProductRepository(private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val db = HalliSantheDatabase.getDatabase(context)
    private val productDao = db.productDao()

    // ──────────────────────────────────────────────────
    //  Firestore collection references
    // ──────────────────────────────────────────────────
    private val productsCollection = firestore.collection("products")
    private val messagesCollection = firestore.collection("messages")

    // ──────────────────────────────────────────────────
    //  Products - Read
    // ──────────────────────────────────────────────────
    fun getAllProducts(): Flow<Result<List<Product>>> = flow {
        emit(Result.Loading)
        try {
            // Emit cached data first
            val cached = productDao.getAllProducts()
            cached.collect { localList ->
                emit(Result.Success(localList))
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    suspend fun syncProductsFromFirestore(): Result<List<Product>> {
        return try {
            val snapshot = productsCollection
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get().await()
            val products = snapshot.toObjects(Product::class.java)
            productDao.insertProducts(products)
            Result.Success(products)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun searchProducts(query: String): Flow<List<Product>> = productDao.searchProducts(query)

    fun getProductsByCategory(category: String): Flow<List<Product>> =
        productDao.getProductsByCategory(category)

    suspend fun getProductById(id: String): Product? = productDao.getProductById(id)

    // ──────────────────────────────────────────────────
    //  Products - Write (Artisan)
    // ──────────────────────────────────────────────────
    suspend fun addProduct(product: Product, imageUri: Uri?): Result<Product> {
        return try {
            var imageUrl = ""
            if (imageUri != null) {
                imageUrl = uploadImage(imageUri)
            }
            val newProduct = product.copy(
                id = UUID.randomUUID().toString(),
                imageUrl = imageUrl,
                createdAt = System.currentTimeMillis()
            )
            productsCollection.document(newProduct.id).set(newProduct).await()
            productDao.insertProduct(newProduct)
            Result.Success(newProduct)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun updateProduct(product: Product, imageUri: Uri?): Result<Product> {
        return try {
            var updatedProduct = product
            if (imageUri != null) {
                val imageUrl = uploadImage(imageUri)
                updatedProduct = product.copy(imageUrl = imageUrl)
            }
            productsCollection.document(updatedProduct.id).set(updatedProduct).await()
            productDao.updateProduct(updatedProduct)
            Result.Success(updatedProduct)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteProduct(product: Product): Result<Unit> {
        return try {
            productsCollection.document(product.id).delete().await()
            productDao.deleteProduct(product)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    // ──────────────────────────────────────────────────
    //  Image Upload with Compression
    // ──────────────────────────────────────────────────
    private suspend fun uploadImage(imageUri: Uri): String {
        val file = File(imageUri.path ?: return "")
        val compressed = Compressor.compress(context, file) {
            resolution(800, 800)
            quality(75)
        }
        val ref = storage.reference.child("products/${UUID.randomUUID()}.jpg")
        ref.putFile(Uri.fromFile(compressed)).await()
        return ref.downloadUrl.await().toString()
    }

    // ──────────────────────────────────────────────────
    //  Messages (Mock Communication)
    // ──────────────────────────────────────────────────
    suspend fun sendInquiry(message: Message): Result<Message> {
        return try {
            val newMessage = message.copy(id = UUID.randomUUID().toString())
            messagesCollection.document(newMessage.id).set(newMessage).await()
            Result.Success(newMessage)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getMessagesForArtisan(artisanId: String): Flow<Result<List<Message>>> = flow {
        emit(Result.Loading)
        try {
            val snapshot = messagesCollection
                .whereEqualTo("artisanId", artisanId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await()
            val messages = snapshot.toObjects(Message::class.java)
            emit(Result.Success(messages))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
