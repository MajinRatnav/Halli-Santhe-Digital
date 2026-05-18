package com.halliSanthe.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.halliSanthe.data.model.Message
import com.halliSanthe.data.model.Product
import com.halliSanthe.data.repository.ProductRepository
import com.halliSanthe.data.repository.Result
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(application)

    // ──────────────────────────────────────────────────
    //  Products List
    // ──────────────────────────────────────────────────
    private val _products = MutableLiveData<List<Product>>(emptyList())
    val products: LiveData<List<Product>> = _products

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _operationResult = MutableLiveData<Result<*>>()
    val operationResult: LiveData<Result<*>> = _operationResult

    // ──────────────────────────────────────────────────
    //  Selected Product
    // ──────────────────────────────────────────────────
    private val _selectedProduct = MutableLiveData<Product?>()
    val selectedProduct: LiveData<Product?> = _selectedProduct

    // ──────────────────────────────────────────────────
    //  Search
    // ──────────────────────────────────────────────────
    private var searchJob: Job? = null
    private val _searchQuery = MutableLiveData("")
    val searchQuery: LiveData<String> = _searchQuery

    // ──────────────────────────────────────────────────
    //  Messages
    // ──────────────────────────────────────────────────
    private val _messages = MutableLiveData<List<Message>>(emptyList())
    val messages: LiveData<List<Message>> = _messages

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllProducts().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _products.value = result.data
                        _isLoading.value = false
                    }
                    is Result.Error -> {
                        _error.value = result.exception.message
                        _isLoading.value = false
                    }
                    Result.Loading -> _isLoading.value = true
                }
            }
        }
        // Sync from Firestore in background
        viewModelScope.launch {
            repository.syncProductsFromFirestore()
        }
    }

    fun searchProducts(query: String) {
        _searchQuery.value = query
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300) // debounce
            if (query.isBlank()) {
                loadProducts()
            } else {
                repository.searchProducts(query).collectLatest { list ->
                    _products.value = list
                }
            }
        }
    }

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            if (category == "All") {
                loadProducts()
            } else {
                repository.getProductsByCategory(category).collectLatest { list ->
                    _products.value = list
                }
            }
        }
    }

    fun selectProduct(product: Product) {
        _selectedProduct.value = product
    }

    // ──────────────────────────────────────────────────
    //  Artisan Operations
    // ──────────────────────────────────────────────────
    fun addProduct(product: Product, imageUri: Uri?) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.addProduct(product, imageUri)
            _operationResult.value = result
            _isLoading.value = false
            if (result is Result.Error) _error.value = result.exception.message
        }
    }

    fun updateProduct(product: Product, imageUri: Uri?) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.updateProduct(product, imageUri)
            _operationResult.value = result
            _isLoading.value = false
            if (result is Result.Error) _error.value = result.exception.message
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.deleteProduct(product)
            _operationResult.value = result
            _isLoading.value = false
        }
    }

    // ──────────────────────────────────────────────────
    //  Buyer - Send Inquiry
    // ──────────────────────────────────────────────────
    fun sendInquiry(message: Message) {
        viewModelScope.launch {
            val result = repository.sendInquiry(message)
            _operationResult.value = result
        }
    }

    fun loadMessagesForArtisan(artisanId: String) {
        viewModelScope.launch {
            repository.getMessagesForArtisan(artisanId).collectLatest { result ->
                if (result is Result.Success) _messages.value = result.data
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

class ProductViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
