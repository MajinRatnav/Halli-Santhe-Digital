package com.halliSanthe.ui.artisan

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.halliSanthe.data.model.Product
import com.halliSanthe.data.repository.Result
import com.halliSanthe.databinding.FragmentAddProductBinding
import com.halliSanthe.viewmodel.ProductViewModel

class AddProductFragment : Fragment() {

    private var _binding: FragmentAddProductBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProductViewModel by activityViewModels()

    private var selectedImageUri: Uri? = null
    private var editingProduct: Product? = null

    private val currentArtisanId = "demo_artisan_001"
    private val currentArtisanName = "Demo Artisan"

    private val categories = listOf("Toys", "Pottery", "Textiles", "Jewellery", "Food", "Crafts", "Other")

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).centerCrop().into(binding.ivProductPreview)
            binding.tvImageHint.isVisible = false
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCategoryDropdown()
        prefillIfEditing()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        binding.actvCategory.setAdapter(adapter)
    }

    private fun prefillIfEditing() {
        editingProduct = viewModel.selectedProduct.value
        editingProduct?.let { product ->
            binding.apply {
                etProductName.setText(product.name)
                etProductPrice.setText(product.price.toString())
                etDescription.setText(product.description)
                etVillage.setText(product.village)
                actvCategory.setText(product.category, false)
                if (product.imageUrl.isNotBlank()) {
                    Glide.with(requireContext()).load(product.imageUrl).centerCrop().into(ivProductPreview)
                    tvImageHint.isVisible = false
                }
                btnSaveProduct.text = "Update Product"
            }
        }
    }

    private fun setupClickListeners() {
        binding.cardImagePicker.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.btnSaveProduct.setOnClickListener {
            if (validateInputs()) {
                saveProduct()
            }
        }
    }

    private fun validateInputs(): Boolean {
        var valid = true
        binding.apply {
            if (etProductName.text.isNullOrBlank()) {
                tilProductName.error = "Product name is required"
                valid = false
            } else tilProductName.error = null

            if (etProductPrice.text.isNullOrBlank()) {
                tilProductPrice.error = "Price is required"
                valid = false
            } else tilProductPrice.error = null

            if (actvCategory.text.isNullOrBlank()) {
                tilCategory.error = "Please select a category"
                valid = false
            } else tilCategory.error = null

            if (etVillage.text.isNullOrBlank()) {
                tilVillage.error = "Village/Location is required"
                valid = false
            } else tilVillage.error = null
        }
        return valid
    }

    private fun saveProduct() {
        val product = Product(
            id = editingProduct?.id ?: "",
            name = binding.etProductName.text.toString().trim(),
            price = binding.etProductPrice.text.toString().toDoubleOrNull() ?: 0.0,
            description = binding.etDescription.text.toString().trim(),
            category = binding.actvCategory.text.toString().trim(),
            village = binding.etVillage.text.toString().trim(),
            artisanId = currentArtisanId,
            artisanName = currentArtisanName,
            imageUrl = editingProduct?.imageUrl ?: "",
            isAvailable = true
        )

        if (editingProduct != null) {
            viewModel.updateProduct(product, selectedImageUri)
        } else {
            viewModel.addProduct(product, selectedImageUri)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
            binding.btnSaveProduct.isEnabled = !isLoading
        }

        viewModel.operationResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success<*> -> {
                    Snackbar.make(binding.root, "✅ Product saved successfully!", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
                is Result.Error -> {
                    Snackbar.make(binding.root, "❌ Error: ${result.exception.message}", Snackbar.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
